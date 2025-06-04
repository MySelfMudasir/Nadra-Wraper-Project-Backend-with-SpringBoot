
package com.NadraWraper.Service;

import com.NadraWraper.Model.DTO.NadraVerifyCitizenResponse;
import com.NadraWraper.Model.DTO.NadraVerifyFingerprintResponse;
import com.NadraWraper.Model.JpVerifyFingerprint;
import com.NadraWraper.Model.NadraVerifyCitizenRequest;
import com.NadraWraper.Model.NadraVerifyFingerprintRequest;
import com.NadraWraper.Model.Verisys.VerisysLogsModel;
import com.NadraWraper.NadraClient;
import com.NadraWraper.Repository.VerisysLogsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class FingerprintService {

    @Value("${nadra.client.franchisee.id}")
    private String franchiseeId;

    @Value("${nadra.client.branch.id}")
    private String branchId;

    @Value("${nadra.session.validity.days}")
    private int validityDays;

    @Value("${nadra.client.machine.identifier}")
    private String machineIdentifier;
    @Autowired
    private VerisysLogsRepository verisysLogsRepository;

    private final NadraClient nadraClient;

    public Object processVerification(JpVerifyFingerprint request) throws Exception {
        VerisysLogsModel verisysLogsModel =
                verisysLogsRepository.findTopByCnicAndApiEndpointOrderByLogIdDesc(
                        request.getCitizenNumber(),
                        "/api/verifyFingerprint");
        String nowDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String base64Template = request.getFingerTemplate();

//        String txnFp = String.valueOf(System.currentTimeMillis());
        NadraVerifyFingerprintRequest fpReq = NadraVerifyFingerprintRequest.builder()
                .sessionId(
                        verisysLogsModel.getSessionId() != null &&
                        verisysLogsModel.getCreatedAt() != null &&
                        verisysLogsModel.getCreatedAt().isAfter(LocalDateTime.now().minusDays(validityDays))
                        ? verisysLogsModel.getSessionId() : ""
                )
                .franchiseeId(franchiseeId)
                .transactionId(franchiseeId+
//                        txnFp
                        "814020617512245"
                )
                .citizenContactNumber(request.getContactNumber())
                .citizenNumber(request.getCitizenNumber())
                .fingerIndex(request.getFingerIndex())
                .templateType("WSQ")
                .fingerTemplate(base64Template)
                .areaName(request.getAreaName())
                .clientBranchId(branchId)
                .clientMachineIdentifier(machineIdentifier)
                .clientSessionId("")
                .clientTimeStamp(nowDate)
                .build();

        return nadraClient.verifyFingerprint(fpReq, request);
    }
}
