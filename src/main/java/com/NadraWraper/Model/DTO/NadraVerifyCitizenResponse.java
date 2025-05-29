package com.NadraWraper.Model.DTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class NadraVerifyCitizenResponse {
    private Map<String, Object> citizenData;
    private String sessionId;
    private String citizenNumber;
    private List<String> operatorFingerIndex;
    private ResponseStatus responseStatus;

    @Data
    public static class ResponseStatus {
        private String code;
        private String message;
    }
}