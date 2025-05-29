package com.NadraWraper.Model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NadraVerifyCitizenRequest {
    private String sessionId;
    private String franchiseeId;
    private String transactionId;
    private String citizenContactNumber;
    private String citizenNumber;
    private String issueDate;           // "dd-MM-yyyy"
    private String yearOfBirth;         // nullable
    private String areaName;
    private String clientBranchId;
    private String clientMachineIdentifier;
    private String clientSessionId;
    private String clientTimeStamp;     // "dd/MM/yyyy"
    private OperatorBiometric operatorBiometric;

    @Data
    @Builder
    public static class OperatorBiometric {
        private String citizenNumber;
        private String citizenContactNumber;
        private String fingerIndex;
        private String templateType;
        private String fingerTemplate;    // Base64
    }
}
