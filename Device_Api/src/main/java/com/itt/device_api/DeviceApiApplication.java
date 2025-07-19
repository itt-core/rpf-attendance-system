//package com.itt.device_api;
//
//import com.itt.device_api.dto.AttendanceRequest;
//import com.itt.device_api.model.Attendance;
//import org.modelmapper.ModelMapper;
//import org.modelmapper.TypeMap;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.Bean;
//
//@SpringBootApplication
//public class DeviceApiApplication {
//
//    public static void main(String[] args) {
//        SpringApplication.run(DeviceApiApplication.class, args);
//    }
//
//    @Bean
//    public ModelMapper modelMapper() {
//        ModelMapper mapper = new ModelMapper();
//
//        mapper.getConfiguration()
//                .setAmbiguityIgnored(true)
//                .setMatchingStrategy(org.modelmapper.convention.MatchingStrategies.STRICT);
//
//        mapper.createTypeMap(AttendanceRequest.class, Attendance.class)
//                .addMappings(m -> m.skip(Attendance::setAttendanceId));
//
//        return mapper;
//    }
//}


// src/main/java/com/example/attendanceapi/AttendanceApiApplication.java
package com.itt.device_api;

import com.itt.device_api.dto.AttendanceRequest;
import com.itt.device_api.model.Attendance;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DeviceApiApplication {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        mapper.typeMap(AttendanceRequest.class, Attendance.class)
                .addMappings(m -> m.skip(Attendance::setId));

        return  mapper;
    }

    public static void main(String[] args) {
        SpringApplication.run(DeviceApiApplication.class, args);
    }
}