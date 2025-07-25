package com.itt.device_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyMetricsResponse {
    private UUID employeeId;
    private String employeeName;
    private LocalDate date;
    private LocalTime firstEntry;
    private LocalTime lastExit;
    private Duration totalTimeOnPremises;
    private int numberOfEntries;
    private int numberOfExits;
    private String punctualityStatus;
    private boolean earlyDeparture;
    private boolean absent;
}
