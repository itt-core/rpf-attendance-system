package com.itt.device_api.controller;

import com.itt.device_api.model.GpsLog;
import com.itt.device_api.repository.GpsLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/gps")
@CrossOrigin(origins = "http://localhost:5173")
public class GpsController {

    @Autowired
    private GpsLogRepository gpsLogRepository;

    // Get all GPS logs
    @GetMapping("/all")
    public List<GpsLog> getAllGpsLogs() {
        return gpsLogRepository.findAll();
    }

    // Get latest GPS log
    @GetMapping("/latest")
    public GpsLog getLatestGpsLog() {
        return gpsLogRepository
                .findTopByOrderByTimestampDesc()
                .orElse(null);
    }
}