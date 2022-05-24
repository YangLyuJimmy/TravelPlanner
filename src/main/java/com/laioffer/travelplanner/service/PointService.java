package com.laioffer.travelplanner.service;

import com.laioffer.travelplanner.repository.CategoryRepository;
import com.laioffer.travelplanner.repository.PointRepository;
import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.http.client.utils.URIBuilder;

import com.laioffer.travelplanner.model.Point;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.*;


@Service
public class PointService {

    private PointRepository pointRepository;
    private CategoryRepository categoryRepository;

    @Autowired
    public PointService(PointRepository pointRepository, CategoryRepository categoryRepository){
        this.pointRepository = pointRepository;
        this.categoryRepository = categoryRepository;
    }

    private static final String APIKey = "3fae250c5fmsh22b6591d898cc59p1f2400jsn475895513a13";
    private static final String RapidAPI_URL = "https://travel-advisor.p.rapidapi.com/attractions/list-in-boundary";

    //this method is to build rapidAPI url with coordinates input
    public String buildRapidAPIURL(String url, double tr_lng, double tr_lat, double bl_lng, double bl_lat) throws URISyntaxException {
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
    public String searchRapidAPI(String url) throws RapidAPIException {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        ResponseHandler<String> responseHandler = response -> {
            int responseCode = response.getStatusLine().getStatusCode();

            if(responseCode != 200) {
                System.out.printf("Response status: " + response.getStatusLine().getReasonPhrase());
                throw new RapidAPIException("Failed to get result from Rapid API");
            }

            HttpEntity entity = response.getEntity();

            if(entity == null){
                throw new RapidAPIException("Failed to get result from Rapid API");
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
            throw new RapidAPIException("Failed to get result from Rapid API");
        } finally {
            try{
                httpClient.close();
            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    //this method is to parse the String (the deserialized rapidAPI response), get the points details, and save to point table
    private void parseRapidAPI(String data) throws RapidAPIException {
        //we want to read through the 33 items under data JSON array (from 0~32)  and skip 6, 15, 24;
        JSONArray API_dataArray = new JSONArray(data);
        try {
            for (int i = 0; i < 33; i++) {
                if (i != 6 && i != 15 && i != 24) {//skip ads at 6, 15, 24
                    JSONObject dataItem = API_dataArray.getJSONObject(i);
                    if (!pointRepository.existsById(Long.parseLong(dataItem.getString("location_id")))) {
                        //if this item already exists in DB, do nothing
                        //} else {//save point in repository
                        Point.Builder builder = new Point.Builder();
                        builder.setId(Long.parseLong(dataItem.getString("location_id")))
                                .setName(dataItem.getString("name"))
                                .setLatitude(Float.parseFloat(dataItem.getString("latitude")))
                                .setLongitude(Float.parseFloat(dataItem.getString("longitude")))
                                .setDescription(dataItem.getString("description").length() > 255 ? dataItem.getString("description").substring(0,250) : dataItem.getString("description"))
                                .setRating(Float.parseFloat(dataItem.getString("rating")))
                                .setImageUrl(dataItem.getJSONObject("photo").getJSONObject("images").getJSONObject("original").getString("url"));

                        Point newPoint = builder.build();
                        try {
                            pointRepository.save(newPoint);
                        } catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RapidAPIException("Failed to parse point data from Rapid API");
        }
    }

    //this method is for DownloadController. It is to download all points in the given map area (need top right & bottom left coordinates) utilizing all methods above
    public void downloadPoints(double tr_lng, double tr_lat, double bl_lng, double bl_lat) throws RapidAPIException{
        try {//top left section
            parseRapidAPI(searchRapidAPI(buildRapidAPIURL(RapidAPI_URL, tr_lng, tr_lat, bl_lng, bl_lat)));
        } catch (RapidAPIException | URISyntaxException e){
            e.printStackTrace();
            throw new RapidAPIException("Failed to get response");
        }
    }

    //this method is for SearchController. It is to return all points to search request without parameters.
    public List<Point> getAllPoints() throws RapidAPIException, URISyntaxException {
        return pointRepository.findAll();
    }

    //this method is for SearchController. It is to return points by category to search request with parameter "category"
    public List<Point> getPointsbyCate(String categoryName){

        Set<Point> resultSet = categoryRepository.findById(categoryName).get().getPointSet();

        List<Point> resultList = new ArrayList<>(resultSet);

        return resultList;
    }

}
