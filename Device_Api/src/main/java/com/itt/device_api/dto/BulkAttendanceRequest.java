package com.itt.device_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BulkAttendanceRequest {
    @JsonProperty("MachineID")
    private String machineID;
    @JsonProperty("Attendance")
    private List<AttendanceRequest> attendance;
}