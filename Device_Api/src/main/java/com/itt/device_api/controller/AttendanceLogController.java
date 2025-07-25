package com.itt.device_api.controller;

import com.itt.device_api.dto.BulkAttendanceRequest;
import com.itt.device_api.dto.DailyMetricsResponse;
import com.itt.device_api.dto.DeviceResponse;
import com.itt.device_api.dto.EmployeeResponse;
import com.itt.device_api.model.AttendanceLog;
import com.itt.device_api.repository.EmployeeRepository;
import com.itt.device_api.service.AttendanceLogService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequiredArgsConstructor
@RequestMapping("/gpsinsert")
public class AttendanceLogController {

    private static final Logger logger = LoggerFactory.getLogger(AttendanceLogController.class);

    @Autowired
    AttendanceLogService attendanceLogService;

    @Autowired
    EmployeeRepository employeeRepository;

    @PostMapping
    public ResponseEntity<DeviceResponse> receiveAttendance(@RequestBody BulkAttendanceRequest attendanceRequest) {
        logger.info("Received Attendance Data: MachineID: {}", attendanceRequest.getMachineID());
        attendanceLogService.saveBulkAttendance(attendanceRequest);
        DeviceResponse response = new DeviceResponse(0);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AttendanceLog>> getAllAttendanceLogs() {
        logger.info("getAllAttendanceLogs");
        return ResponseEntity.ok(attendanceLogService.getAllLogs());
    }

    @GetMapping("/metrics/{employeeId}/{date}")
    public ResponseEntity<DailyMetricsResponse> getDailyMetrics(
            @PathVariable String employeeId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        logger.info("API hit on daily metrics with employeeId string: {}", employeeId);

        UUID employeeUUID;
        try {
            employeeUUID = UUID.fromString(employeeId);
        } catch (Exception ex) {
            employeeUUID = UUID.nameUUIDFromBytes(employeeId.getBytes());
        }

        return ResponseEntity.ok(attendanceLogService.getDailyMetrics(employeeUUID, date));
    }

    @GetMapping("/latest")
    public ResponseEntity<List<AttendanceLog>> getLatestLogs() {
        logger.info("Fetching latest attendance logs");
        return ResponseEntity.ok(attendanceLogService.getLatestLogs());
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<Map<String, String>> getEmployeeDetails(@PathVariable UUID id) {
        return employeeRepository.findById(id)
                .map(emp -> {
                    Map<String, String> map = new HashMap<>();
                    map.put("name", emp.getName());
                    map.put("employeeCode", emp.getEmployeeCode());
                    return ResponseEntity.ok(map);
                })
                .orElse(ResponseEntity.notFound().build());
    }


}
