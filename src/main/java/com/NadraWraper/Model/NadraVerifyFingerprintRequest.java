package com.NadraWraper.Model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NadraVerifyFingerprintRequest {
    private String sessionId;
    private String franchiseeId;
    private String transactionId;
    private String citizenContactNumber;
    private String citizenNumber;
    private String fingerIndex;
    private String templateType;
    private String fingerTemplate; // base64 string
    private String areaName;
    private String clientBranchId;
    private String clientMachineIdentifier;
    private String clientSessionId;
    private String clientTimeStamp;
}
