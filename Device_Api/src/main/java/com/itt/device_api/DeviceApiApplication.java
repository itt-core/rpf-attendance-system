package com.itt.device_api;

import com.itt.device_api.dto.AttendanceRequest;
import com.itt.device_api.model.AttendanceLog;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DeviceApiApplication {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.typeMap(AttendanceRequest.class, AttendanceLog.class)
                .addMappings(m -> m.skip(AttendanceLog::setLogId));
        return mapper;
    }

    public static void main(String[] args) {
        SpringApplication.run(DeviceApiApplication.class, args);
    }
}