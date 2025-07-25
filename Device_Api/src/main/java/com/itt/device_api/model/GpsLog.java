package com.itt.device_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "gps_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GpsLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imei;

    private LocalDateTime timestamp;

    private double latitude;
    private double longitude;

//    private int speed;
}

