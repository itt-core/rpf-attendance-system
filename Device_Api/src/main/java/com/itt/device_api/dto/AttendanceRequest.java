package com.itt.device_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceRequest {
    @JsonProperty("PunchID")
    private String punchID;
    @JsonProperty("PunchTime")
    private String punchTime;
    @JsonProperty("PunchStatus")
    private Integer punchStatus;
    @JsonProperty("Latitude")
    private String latitude;
    @JsonProperty("Longitude")
    private String longitude;
    @JsonProperty("Speed")
    private String speed;
    @JsonProperty("DIN1")
    private Integer DIN1;
    @JsonProperty("AIN1")
    private Integer AIN1;
}