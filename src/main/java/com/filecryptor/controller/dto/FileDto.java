package com.filecryptor.controller.dto;

import lombok.Data;
import org.bson.types.Binary;

import javax.validation.constraints.NotEmpty;

@Data
public class FileDto {

    private String id;
    private String title;
    private String dateReview;
    private String username;
    private String secretKey;
    private String type;
}
