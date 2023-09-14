package com.filecryptor.service;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
public class ServiceUtility {

    private static final String PATTERN_FORMAT = "dd.MM.yyyy" + "-" +"hh:mm";

    public static String dataFormat(Instant time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_FORMAT)
                .withZone(ZoneId.systemDefault());

        Instant instant = Instant.parse(time.toString());

        return formatter.format(instant);
    }
}
