package com.itt.device_api.repository;

import com.itt.device_api.model.GpsLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GpsLogRepository extends JpaRepository<GpsLog, Long> {
    Optional<GpsLog> findTopByOrderByTimestampDesc();
}
