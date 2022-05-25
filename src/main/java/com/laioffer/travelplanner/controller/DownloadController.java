package com.laioffer.travelplanner.controller;

import com.laioffer.travelplanner.service.DownloadService;
import com.laioffer.travelplanner.exception.DownloadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class DownloadController {

    private DownloadService downloadService;

    @Autowired
    public DownloadController(DownloadService downloadService) {
        this.downloadService = downloadService;
    }

//        LA map coordinates:
//        Latitude:
//        top_lat = 34.354388;
//        mid_lat = 34.0249365;
//        bottom_lat = 33.695485;
//
//        Longitude:
//        left_lng = -118.902627;
//        mid_lng = -118.483685;
//        right_lng = -118.064743;
//

//     run http://localhost:8080/download to download all the points.
    @PostMapping(value = "/download")
    public void download(@RequestParam(value = "tr_lng", required = true) String tr_lng,
                                    @RequestParam(value = "tr_lat", required = true) String tr_lat,
                                    @RequestParam(value = "bl_lng", required = true) String bl_lng,
                                    @RequestParam(value = "bl_lat", required = true) String bl_lat) {

        Double trLng = Double.parseDouble(tr_lng);
        Double trLat = Double.parseDouble(tr_lat);
        Double blLng = Double.parseDouble(bl_lng);
        Double blLat = Double.parseDouble(bl_lat);

        Double midLng = (trLng + blLng) / 2;
        Double midLat = (trLat + blLat) / 2;

        try {// please refer to downloadService for method details
            downloadService.download(trLng, trLat, midLng, midLat);
            downloadService.download(midLng, trLat, blLng, midLat);
            downloadService.download(midLng, midLat, blLng, blLat);
            downloadService.download(trLng, midLat, midLng, blLat);
        } catch (DownloadException e) {
            e.printStackTrace();
            throw new DownloadException("Failed to download points");
        }
    }

}

