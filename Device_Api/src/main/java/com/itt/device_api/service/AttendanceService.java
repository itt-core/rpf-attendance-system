package com.itt.device_api.service;

import com.itt.device_api.dto.BulkAttendanceRequest;
import com.itt.device_api.model.Attendance;
import com.itt.device_api.repository.AttendanceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AttendanceRepository repository;

    public void saveBulkAttendance(BulkAttendanceRequest request) {
        List<Attendance> validRecords = request.getAttendance().stream()
                .filter(entry -> entry.getPunchID() != null && !entry.getPunchID().equals("0"))
                .map(entry -> {
                    Attendance entity = modelMapper.map(entry, Attendance.class);
                    entity.setId(null);
                    entity.setMachineID(request.getMachineID());
                    return entity;
                })
                .collect(Collectors.toList());

        if (!validRecords.isEmpty()) {
            repository.saveAll(validRecords);
        }
    }


    public List<Attendance> getAllRecords() {
        return repository.findAll();
    }
}
