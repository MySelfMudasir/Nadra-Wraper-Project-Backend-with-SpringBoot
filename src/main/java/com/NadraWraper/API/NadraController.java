package com.NadraWraper.API;

import com.NadraWraper.Model.DTO.NadraVerifyFingerprintResponse;
import com.NadraWraper.Model.JpVerifyFingerprint;

import com.NadraWraper.Service.FingerprintService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class NadraController {
    private final FingerprintService fingerprintService;
//    @Value("${jp.userName}")
//    private String userName;
//    @Value("${jp.password}")
//    private String password;
//
//    @GetMapping("/authenticate")
//    public JpLoginResponse authenticate(@RequestBody JPLogin jpLogin){
//        if(jpLogin.getUsername().equals(userName) && jpLogin.getPassword().equals(password)){
//            JpLoginResponse response = new JpLoginResponse();
//            response.setStatusCode("00");
//            response.setMessage("Login successful");
//            return response;
//        } else {
//            JpLoginResponse response = new JpLoginResponse();
//            response.setStatusCode("01");
//            response.setMessage("Invalid credentials");
//            return response;
//        }
//    }

    @PostMapping(
            value    = "/VerifyFingerPrint",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> verifyFingerprint(
            @RequestPart("FINGER_TEMPLATE") MultipartFile fingerTemplate,
            @RequestPart("CITIZEN_NUMBER") String citizenNumber,
            @RequestPart("FINGER_INDEX") String fingerIndex,
            @RequestPart("AREA_NAME") String areaName,
            @RequestPart("CONTACT_NUMBER") String contactNumber,
            @RequestPart("ISSUE_DATE") String issueDate
    ) throws Exception {
        JpVerifyFingerprint request = new JpVerifyFingerprint();
        String fingerTemplateBase64 = Base64.getEncoder()
                .encodeToString(IOUtils.toByteArray(fingerTemplate.getInputStream()));
        request.setFingerTemplate(fingerTemplateBase64);
        request.setCitizenNumber(citizenNumber);
        request.setFingerIndex(fingerIndex);
        request.setAreaName(areaName);
        request.setContactNumber(contactNumber);
        request.setIssueDate(issueDate);
        Object nadraResponse = fingerprintService.processVerification(request);
        return ResponseEntity.ok(nadraResponse);
//        return fingerprintService.processVerification(request);
    }
}
