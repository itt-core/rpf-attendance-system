package com.itt.device_api.repository;

import com.itt.device_api.model.AttendanceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AttendanceLogRepository extends JpaRepository<AttendanceLog, UUID> {
    @Query("SELECT l FROM AttendanceLog l WHERE l.employeeId = :employeeId AND DATE(l.timestamp) = :date")
    List<AttendanceLog> findByEmployeeIdAndDate(@Param("employeeId") UUID employeeId, @Param("date") LocalDate date);

    @Query("SELECT a FROM AttendanceLog a ORDER BY a.timestamp DESC")
    List<AttendanceLog> findTop50ByOrderByTimestampDesc();

}