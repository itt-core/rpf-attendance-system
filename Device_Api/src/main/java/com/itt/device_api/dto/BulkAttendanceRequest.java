package com.itt.device_api.dto;

import lombok.Data;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class BulkAttendanceRequest {
    @JsonProperty("MachineID")
    private String machineID;

    @JsonProperty("Attendance")
    private List<AttendanceRequest> attendance;
}