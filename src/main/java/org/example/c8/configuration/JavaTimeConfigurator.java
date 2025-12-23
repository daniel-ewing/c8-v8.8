package org.example.c8.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class JavaTimeConfigurator {
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    public static String getDateTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT));
    }

    @Bean
    public ObjectMapper configureObjectMapper() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(dateFormat);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // To store and retrieve a Date variable as a Gregorian date String according to the format specified in DEFAULT_DATE_FORMAT
        // In this case, the date variable is readable as formatted Gregorian date String in Operate
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
//        // To store and retrieve a Date variable as a Long - a Unix timestamp based on milliseconds
//        // In this case, the date variable is readable as Long representing a timestamp in Operate
//        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);

        objectMapper.registerModule(new JavaTimeModule());

        return objectMapper;
    }

}
