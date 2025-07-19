package com.itt.device_api.controller;

import com.itt.device_api.dto.AttendanceRequest;
import com.itt.device_api.dto.BulkAttendanceRequest;
import com.itt.device_api.dto.DeviceResponse;
import com.itt.device_api.model.Attendance;
import com.itt.device_api.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequiredArgsConstructor
@RequestMapping("/gpsinsert")
public class AttendanceController {

    private static final Logger logger = LoggerFactory.getLogger(AttendanceController.class);

    @Autowired
    AttendanceService attendanceService;

    @PostMapping
    public ResponseEntity<DeviceResponse> receiveAttendance(@RequestBody BulkAttendanceRequest attendanceRequest) {
        System.out.println("--------------------------------------------------");
        System.out.println("Received Attendance Data:");
        System.out.println("MachineID: " + attendanceRequest.getMachineID());

        if (attendanceRequest.getAttendance() != null && !attendanceRequest.getAttendance().isEmpty()) {
            System.out.println("Attendance Entries:");
            for (AttendanceRequest entry : attendanceRequest.getAttendance()) {
                System.out.println("  - PunchID: " + entry.getPunchID() +
                        ", PunchTime: " + entry.getPunchTime() +
                        ", PunchStatus: " + entry.getPunchStatus() +
                        ", Latitude: " + entry.getLatitude() +
                        ", Longitude: " + entry.getLongitude() +
                        ", Speed: " + entry.getSpeed() +
                        ", DIN1: " + entry.getDIN1() +
                        ", AIN1: " + entry.getAIN1());
            }
            attendanceService.saveBulkAttendance(attendanceRequest);
        } else {
            System.out.println("No attendance entries found.");
        }
        System.out.println("--------------------------------------------------");
        DeviceResponse response = new DeviceResponse(0);
        return ResponseEntity.ok(response);
    }

        @GetMapping("/all")
    public ResponseEntity<List<Attendance>> getAllAttendanceRecords() {
        logger.info("getAllAttendanceRecords");
        return ResponseEntity.ok(attendanceService.getAllRecords());
    }
}