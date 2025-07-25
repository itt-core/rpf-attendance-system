package com.itt.device_api.service;

import com.itt.device_api.dto.BulkAttendanceRequest;
import com.itt.device_api.dto.DailyMetricsResponse;
import com.itt.device_api.model.AttendanceLog;
import com.itt.device_api.model.Employee;
import com.itt.device_api.repository.AttendanceLogRepository;
import com.itt.device_api.repository.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AttendanceLogService {

    private static final Logger logger = LoggerFactory.getLogger(AttendanceLogService.class);
    private final AttendanceLogRepository attendanceLogRepository;
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    public AttendanceLogService(AttendanceLogRepository attendanceLogRepository,
                                EmployeeRepository employeeRepository,
                                ModelMapper modelMapper) {
        this.attendanceLogRepository = attendanceLogRepository;
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }

    private UUID resolveDeviceId(String machineID) {
        try {
            return UUID.fromString(machineID);
        } catch (Exception e) {
            return UUID.nameUUIDFromBytes(machineID.getBytes());
        }
    }

    private Employee resolveEmployee(String punchId) {
        return employeeRepository.findByPunchId(punchId)
                .orElseThrow(() -> new RuntimeException("Employee not found for punch ID: " + punchId));
    }

    private Timestamp parsePunchTime(String punchTime) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
            Date date = sdf.parse(punchTime);
            return new Timestamp(date.getTime());
        } catch (ParseException ex) {
            logger.error("Failed to parse punchTime '{}': {}", punchTime, ex.getMessage());
            return new Timestamp(System.currentTimeMillis());
        }
    }

    public void saveBulkAttendance(BulkAttendanceRequest request) {
        UUID deviceId = resolveDeviceId(request.getMachineID());

        List<AttendanceLog> logs = new ArrayList<>();

        for (var entry : request.getAttendance()) {
            try {
                Employee emp = resolveEmployee(entry.getPunchID());

                AttendanceLog log = AttendanceLog.builder()
                        .logId(UUID.randomUUID())
                        .employeeId(emp.getEmployeeId())
                        .deviceId(deviceId)
                        .locationId(null)
                        .timestamp(parsePunchTime(entry.getPunchTime()))
                        .action(entry.getPunchStatus() == 0 ? "entry" : "exit")
                        .gpsLatitude(entry.getLatitude())
                        .gpsLongitude(entry.getLongitude())
                        .build();

                // Transient field for frontend display
                log.setEmployeeName(emp.getName());

                logs.add(log);

            } catch (RuntimeException e) {
                logger.warn("Skipping punch ID {}: {}", entry.getPunchID(), e.getMessage());
            }
        }

        attendanceLogRepository.saveAll(logs);
    }

    public List<AttendanceLog> getAllLogs() {
        List<AttendanceLog> logs = attendanceLogRepository.findAll(Sort.by(Sort.Direction.DESC, "timestamp"));

        // Enrich with employee names for frontend
        logs.forEach(log -> {
            employeeRepository.findById(log.getEmployeeId()).ifPresent(emp -> {
                log.setEmployeeName(emp.getName());
            });
        });

        return logs;
    }

    public DailyMetricsResponse getDailyMetrics(UUID employeeId, LocalDate date) {
        logger.info("Calculating metrics for employee {} on {}", employeeId, date);

        Employee emp = employeeRepository.findById(employeeId).orElse(null);
        if (emp == null) {
            throw new IllegalArgumentException("Employee with ID " + employeeId + " not found.");
        }

        List<AttendanceLog> logs = attendanceLogRepository.findByEmployeeIdAndDate(employeeId, date);

        if (logs.isEmpty()) {
            return DailyMetricsResponse.builder()
                    .employeeId(employeeId)
                    .date(date)
                    .absent(true)
                    .build();
        }

        logs.sort(Comparator.comparing(AttendanceLog::getTimestamp));

        List<Timestamp> entryTimes = logs.stream()
                .filter(log -> "entry".equals(log.getAction()))
                .map(AttendanceLog::getTimestamp)
                .toList();

        List<Timestamp> exitTimes = logs.stream()
                .filter(log -> "exit".equals(log.getAction()))
                .map(AttendanceLog::getTimestamp)
                .toList();

        LocalTime firstEntry = entryTimes.isEmpty() ? null : entryTimes.get(0).toLocalDateTime().toLocalTime();
        LocalTime lastExit = exitTimes.isEmpty() ? null : exitTimes.get(exitTimes.size() - 1).toLocalDateTime().toLocalTime();

        Duration totalTime = Duration.ZERO;
        for (int i = 0; i < Math.min(entryTimes.size(), exitTimes.size()); i++) {
            totalTime = totalTime.plus(Duration.between(
                    entryTimes.get(i).toLocalDateTime(),
                    exitTimes.get(i).toLocalDateTime()));
        }

        String punctuality = "Unknown";
        if (firstEntry != null) {
            punctuality = firstEntry.isAfter(LocalTime.of(9, 0)) ? "Late" : "On Time";
        }

        boolean earlyDeparture = lastExit != null && lastExit.isBefore(LocalTime.of(18, 0));

        DailyMetricsResponse response = modelMapper.map(emp, DailyMetricsResponse.class);
        response.setEmployeeId(employeeId);
        response.setDate(date);
        response.setFirstEntry(firstEntry);
        response.setLastExit(lastExit);
        response.setNumberOfEntries(entryTimes.size());
        response.setNumberOfExits(exitTimes.size());
        response.setTotalTimeOnPremises(totalTime);
        response.setPunctualityStatus(punctuality);
        response.setEarlyDeparture(earlyDeparture);
        response.setAbsent(false);

        return response;
    }

    public List<AttendanceLog> getLatestLogs() {
        List<AttendanceLog> logs = attendanceLogRepository.findTop50ByOrderByTimestampDesc();

        for (AttendanceLog log : logs) {
            employeeRepository.findById(log.getEmployeeId()).ifPresent(emp -> {
                log.setEmployeeName(emp.getName());
            });
        }

        return logs;
    }


}
