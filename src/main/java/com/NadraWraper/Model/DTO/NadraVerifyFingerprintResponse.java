package com.NadraWraper.Model.DTO;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class NadraVerifyFingerprintResponse {
//    private CitizenData citizenData;
//    private String sessionId;
//    private String citizenNumber;
//    private Object fingerIndex;
//    private ResponseStatus responseStatus;
//
//    @Data
//    public static class CitizenData {
//    }
//
//    @Data
//    public static class ResponseStatus {
//        private String code;
//        private String message;
//    }
    private Map<String, Object> citizenData;
    private String sessionId;
    private String citizenNumber;
    private List<String> fingerIndex;
    private ResponseStatus responseStatus;

    @Data
    public static class ResponseStatus {
        private String code;
        private String message;
    }
}