package com.itt.device_api.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;
import java.sql.Timestamp;

@Entity
@Table(name = "attendance_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceLog {
    @Id
    @Column(name = "log_id", columnDefinition = "UUID")
    private UUID logId;

    @Column(name = "employee_id", columnDefinition = "UUID", nullable = false)
    private UUID employeeId;

    @Transient
    private String employeeName;

    @Column(name = "device_id", columnDefinition = "UUID", nullable = false)
    private UUID deviceId;

    @Column(name = "location_id", columnDefinition = "UUID")
    private UUID locationId;

    @Column(name = "timestamp", nullable = false)
    private Timestamp timestamp;

    @Column(name = "action")
    private String action; // derived from PunchStatus: "entry"/"exit"

    @Column(name = "gps_latitude")
    private String gpsLatitude;

    @Column(name = "gps_longitude")
    private String gpsLongitude;
}