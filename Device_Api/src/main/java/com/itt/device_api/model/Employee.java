package com.itt.device_api.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;
import java.sql.Timestamp;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {
    @Id
    @Column(name = "employee_id", columnDefinition = "UUID")
    private UUID employeeId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "employee_code", nullable = false, unique = true)
    private String employeeCode;

    @Column(name = "email")
    private String email;

    @Column(name = "department")
    private String department;

    @Column(name = "location_id", columnDefinition = "UUID")
    private UUID locationId;

    @Column(name = "status")
    private String status; // Active, Inactive, On Leave

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "punch_id")
    private String punchId;
}