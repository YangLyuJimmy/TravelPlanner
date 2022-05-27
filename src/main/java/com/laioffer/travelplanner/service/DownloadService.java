package com.laioffer.travelplanner.service;

import com.laioffer.travelplanner.exception.DownloadException;
import com.laioffer.travelplanner.model.Category;
import com.laioffer.travelplanner.model.Point;
import com.laioffer.travelplanner.repository.CategoryRepository;
import com.laioffer.travelplanner.repository.PointRepository;
import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;

@Service
public class DownloadService {

    private PointRepository pointRepository;
    private CategoryRepository categoryRepository;

    @Autowired
    public DownloadService(PointRepository pointRepository, CategoryRepository categoryRepository){
        this.pointRepository = pointRepository;
        this.categoryRepository = categoryRepository;
    }

    private static final String APIKey = "3fae250c5fmsh22b6591d898cc59p1f2400jsn475895513a13";
    private static final String RapidAPI_URL = "https://travel-advisor.p.rapidapi.com/attractions/list-in-boundary";

    //this method is to build rapidAPI url with coordinates input
    private String buildRapidAPIURL(String url, double tr_lng, double tr_lat, double bl_lng, double bl_lat) throws URISyntaxException {
        //encode parameters into URL string
        String trLng = new String();
        String trLat = new String();
        String blLng = new String();
        String blLat = new String();

        try {
            trLng = URLEncoder.encode(String.valueOf(tr_lng), "UTF-8");
            trLat = URLEncoder.encode(String.valueOf(tr_lat), "UTF-8");
            blLng = URLEncoder.encode(String.valueOf(bl_lng), "UTF-8");
            blLat = URLEncoder.encode(String.valueOf(bl_lat), "UTF-8");

        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }

        URIBuilder b = new URIBuilder(url);
        b.addParameter("tr_longitude",trLng);
        b.addParameter("tr_latitude", trLat);
        b.addParameter("bl_longitude",blLng);
        b.addParameter("bl_latitude",blLat);

        return b.build().toString();
    }

    //this method is to deserialize the JSON response we get from rapid API (only the "data" portion with all points info) to a String
    private String searchRapidAPI(String url) throws DownloadException {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        ResponseHandler<String> responseHandler = response -> {
            int responseCode = response.getStatusLine().getStatusCode();

            if(responseCode != 200) {
                System.out.printf("Response status: " + response.getStatusLine().getReasonPhrase());
                throw new DownloadException("Failed to get result from Rapid API");
            }

            HttpEntity entity = response.getEntity();

            if(entity == null){
                throw new DownloadException("Failed to get result from Rapid API");
            }
            JSONObject obj = new JSONObject(EntityUtils.toString(entity));
            return obj.getJSONArray("data").toString();
        };

        try{
            HttpGet request = new HttpGet(url);
            request.setHeader("X-RapidAPI-Host","travel-advisor.p.rapidapi.com");
            request.setHeader("X-RapidAPI-Key",APIKey);

            return httpClient.execute(request, responseHandler);

        } catch (IOException e){
            e.printStackTrace();;
            throw new DownloadException("Failed to get result from Rapid API");
        } finally {
            try{
                httpClient.close();
            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    //this method is to parse the String (the deserialized rapidAPI response), and save the points into DB
    private void parseRapidAPI(String data, String location) throws DownloadException {
        //we want to read through the 33 items under data JSON array (from 0~32)  and skip 6, 15, 24;
        JSONArray API_dataArray = new JSONArray(data);
        try {
            for (int i = 0; i < 33; i++) {
                if (i != 6 && i != 15 && i != 24) {//skip ads at 6, 15, 24
                    JSONObject dataItem = API_dataArray.getJSONObject(i);
                    if (!pointRepository.existsById(Long.parseLong(dataItem.getString("location_id")))) {
                        //if this point has already existed in DB, skip, else save the point
                        Point newPoint = new Point.Builder()
                                .setId(Long.parseLong(dataItem.getString("location_id")))
                                .setName(dataItem.getString("name"))
                                .setLatitude(Float.parseFloat(dataItem.getString("latitude")))
                                .setLongitude(Float.parseFloat(dataItem.getString("longitude")))
                                .setDescription(dataItem.getString("description").length() > 255 ? (dataItem.getString("description").substring(0,250) + "...") : dataItem.getString("description"))
                                .setRating(Float.parseFloat(dataItem.getString("rating")))
                                .setImageUrl(dataItem.getJSONObject("photo").getJSONObject("images").getJSONObject("original").getString("url"))
                                .setLocation(location)
                                .build();

                        Point savedPoint = pointRepository.save(newPoint);

                        //add categorySet by looping through subcategory JSONArray
                        JSONArray categoryArray = dataItem.getJSONArray("subcategory");
                        if (categoryArray != null && categoryArray.length() != 0) {
                            for (int cateIndex = 0; cateIndex < categoryArray.length(); cateIndex++) {
                                String subCategoryName = categoryArray.getJSONObject(cateIndex).getString("name");

                                if (!categoryRepository.existsByTypeAndLocation(subCategoryName, location)) {//if category does not exist in repository
                                    Category newCate = new Category.Builder()
                                            .setType(subCategoryName)
                                            .setLocation(location)
                                            .build();
                                    savedPoint.getCategorySet().add(newCate);
                                    pointRepository.save(savedPoint);
                                } else {//if subcategory has already existed in categoryRepository
                                    Category existingCate = categoryRepository.getByTypeAndLocation(subCategoryName, location);
                                    savedPoint.getCategorySet().add(existingCate);
                                    pointRepository.save(savedPoint);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DownloadException("Failed to parse data from Rapid API");
        }
    }

    //this method is to download all the points in the given map area
    //(need top right & bottom left coordinates) utilizing all methods above
    public void download(double tr_lng, double tr_lat, double bl_lng, double bl_lat, String location) throws DownloadException {
        try {
            parseRapidAPI(searchRapidAPI(buildRapidAPIURL(RapidAPI_URL, tr_lng, tr_lat, bl_lng, bl_lat)), location);
        } catch (DownloadException | URISyntaxException e){
            e.printStackTrace();
            throw new DownloadException("Failed to download points and categories");
        }
    }

}
