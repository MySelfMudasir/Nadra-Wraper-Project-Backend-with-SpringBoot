package com.NadraWraper.Service;

import com.NadraWraper.Model.DTO.NadraVerifyCitizenResponse;
import com.NadraWraper.Model.JpVerifyFingerprint;
import com.NadraWraper.Model.NadraVerifyCitizenRequest;
import com.NadraWraper.Model.Verisys.VerisysLogsModel;
import com.NadraWraper.NadraClient;
import com.NadraWraper.Repository.VerisysLogsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class VerifyCitizenService {
    @Value("${nadra.client.franchisee.id}")
    private String franchiseeId;

    @Value("${nadra.client.branch.id}")
    private String branchId;

    @Value("${nadra.client.machine.identifier}")
    private String machineIdentifier;

    @Value("${nadra.session.validity.days}")
    private int validityDays;

    private final NadraClient nadraClient;
    @Autowired
    private VerisysLogsRepository verisysLogsRepository;

    public Object processCitizenVerification(JpVerifyFingerprint request) throws Exception {
        VerisysLogsModel verisysLogsModel =verisysLogsRepository.findTopByCnicAndApiEndpointOrderByLogIdDesc(request.getCitizenNumber(),"/api/verifyCitizen");
//        String txnCit = String.valueOf(System.currentTimeMillis());
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
                .sessionId(
                        verisysLogsModel.getSessionId() != null &&
                                verisysLogsModel.getCreatedAt() != null &&
                                verisysLogsModel.getCreatedAt().isAfter(LocalDateTime.now().minusDays(validityDays))
                                ? verisysLogsModel.getSessionId() : ""
                )
                .franchiseeId(franchiseeId)
                .transactionId(franchiseeId+
//                        txnCit
                                "814020617511271"
                )
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

//        if (!"100".equals(citResp.getResponseStatus().getCode())) {
            return citResp;
//        }
    }

}
