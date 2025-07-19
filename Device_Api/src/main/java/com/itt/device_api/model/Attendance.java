package com.itt.device_api.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "attendance_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "machine_id", nullable = false)
    private String machineID;

    @Column(name = "punch_id", nullable = false)
    private String punchID;

    @Column(name = "punch_time", nullable = false)
    private String punchTime;

    @Column(name = "punch_status")
    private Integer punchStatus;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "longitude")
    private String longitude;

    @Column(name = "speed")
    private String speed;

    @Column(name = "din1")
    private Integer DIN1;

    @Column(name = "ain1")
    private Integer AIN1;
}
