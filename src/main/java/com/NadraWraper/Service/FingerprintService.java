
package com.NadraWraper.Service;

import com.NadraWraper.Model.DTO.NadraVerifyCitizenResponse;
import com.NadraWraper.Model.DTO.NadraVerifyFingerprintResponse;
import com.NadraWraper.Model.JpVerifyFingerprint;
import com.NadraWraper.Model.NadraVerifyCitizenRequest;
import com.NadraWraper.Model.NadraVerifyFingerprintRequest;
import com.NadraWraper.NadraClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class FingerprintService {

    @Value("${nadra.client.franchisee.id}")
    private String franchiseeId;

    @Value("${nadra.client.branch.id}")
    private String branchId;

    @Value("${nadra.client.machine.identifier}")
    private String machineIdentifier;

    private final NadraClient nadraClient;

    public Object processVerification(JpVerifyFingerprint request) throws Exception {
        String txnCit = String.valueOf(System.currentTimeMillis());
        String nowDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String base64Template = request.getFingerTemplate();
        NadraVerifyCitizenRequest.OperatorBiometric opBio =
                NadraVerifyCitizenRequest.OperatorBiometric.builder()
                        .citizenNumber("4212190074775")
                        .citizenContactNumber("03085126899")
                        .fingerIndex(request.getFingerIndex())
                        .templateType("WSQ")
                        .fingerTemplate(base64Template)
                        .build();

        NadraVerifyCitizenRequest citReq = NadraVerifyCitizenRequest.builder()
                .sessionId("")
                .franchiseeId(franchiseeId)
                .transactionId(franchiseeId+"814020617511132")
                .citizenContactNumber(request.getContactNumber())
                .citizenNumber(request.getCitizenNumber())
                .issueDate(request.getIssueDate())
                .yearOfBirth(null)
                .areaName(request.getAreaName())
                .clientBranchId(branchId)
                .clientMachineIdentifier(machineIdentifier)
                .clientSessionId("7777777777")
                .clientTimeStamp(nowDate)
                .operatorBiometric(opBio)
                .build();

        NadraVerifyCitizenResponse citResp = nadraClient.verifyCitizen(citReq, request);

        if (!"100".equals(citResp.getResponseStatus().getCode())) {
            return citResp;
        }

        String txnFp = String.valueOf(System.currentTimeMillis());
        NadraVerifyFingerprintRequest fpReq = NadraVerifyFingerprintRequest.builder()
                .sessionId(citResp.getSessionId())
                .franchiseeId(franchiseeId)
                .transactionId(franchiseeId+"814020617511130")
                .citizenContactNumber(request.getContactNumber())
                .citizenNumber(request.getCitizenNumber())
                .fingerIndex(request.getFingerIndex())
                .templateType("WSQ")
                .fingerTemplate(base64Template)
                .areaName(request.getAreaName())
                .clientBranchId(branchId)
                .clientMachineIdentifier(machineIdentifier)
                .clientSessionId(txnFp)
                .clientTimeStamp(nowDate)
                .build();

        return nadraClient.verifyFingerprint(fpReq, request);
    }

    private String toBase64(MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to encode fingerprint template", e);
        }
    }
}
