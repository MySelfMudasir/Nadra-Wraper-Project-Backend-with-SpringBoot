package com.NadraWraper.Model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class JpVerifyFingerprint {
    private String fingerTemplate;
    private String citizenNumber;
    private String fingerIndex;
    private String areaName;
    private String contactNumber;
    private String issueDate;
}
