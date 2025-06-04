package com.NadraWraper.Model;

import lombok.*;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
public class ApiResponse {

    private String referenceId;
    private String responseCode;
    private String responseMessage;

    @Override
    public String toString() {
        return "ApiResponse [referenceId=" + referenceId + ", responseCode=" + responseCode + ", responseMessage="
                + responseMessage + "]";
    }

}

