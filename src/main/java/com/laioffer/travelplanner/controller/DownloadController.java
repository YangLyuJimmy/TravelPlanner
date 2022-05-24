package com.laioffer.travelplanner.controller;

import com.laioffer.travelplanner.service.CategoryService;
import com.laioffer.travelplanner.service.PointService;
import com.laioffer.travelplanner.service.RapidAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;


@RestController
public class DownloadController {

    private PointService pointService;
    private CategoryService categoryService;

    @Autowired
    public DownloadController(PointService pointService, CategoryService categoryService) {
        this.pointService = pointService;
        this.categoryService = categoryService;
    }

//        LA map coordinates:
    //    WeiDu:
//        top_lat = 34.354388;
//        mid_lat = 34.0249365;
//        bottom_lat = 33.695485;
    //    JingDu:
//        left_lng = -118.902627;
//        mid_lng = -118.483685;
//        right_lng = -118.064743;
//

    // run http://localhost:8080/downloadtest to download all points (without category set yet).
    // You can rename the API value to "download" if you'd like. :)  It might make more sense
    @RequestMapping(value = "/downloadtest", method = RequestMethod.POST)
    public void downloadPointswithP(@RequestParam(value = "tr_lng", required = true) String tr_lng,
                                    @RequestParam(value = "tr_lat", required = true) String tr_lat,
                                    @RequestParam(value = "bl_lng", required = true) String bl_lng,
                                    @RequestParam(value = "bl_lat", required = true) String bl_lat) throws RapidAPIException {

        try {// please refer to pointService for method details
            pointService.downloadPoints(Double.parseDouble(tr_lng), Double.parseDouble(tr_lat), Double.parseDouble(bl_lng), Double.parseDouble(bl_lat));
        } catch (RapidAPIException e) {
            e.printStackTrace();
            throw new RapidAPIException("Failed to downloadPoints");
        }
    }

    // run http://localhost:8080/downloadCate to download all category sets for each point
    @RequestMapping(value = "/downloadCate", method = RequestMethod.POST)
    public void downloadCate(@RequestParam(value = "tr_lng", required = true) String tr_lng,
                             @RequestParam(value = "tr_lat", required = true) String tr_lat,
                             @RequestParam(value = "bl_lng", required = true) String bl_lng,
                             @RequestParam(value = "bl_lat", required = true) String bl_lat) throws RapidAPIException {
        try {
            categoryService.downloadCategory(pointService.searchRapidAPI(pointService.buildRapidAPIURL(
                    "https://travel-advisor.p.rapidapi.com/attractions/list-in-boundary",
                    Double.parseDouble(tr_lng),
                    Double.parseDouble(tr_lat),
                    Double.parseDouble(bl_lng),
                    Double.parseDouble(bl_lat)
            )));

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}

