package com.NadraWraper;

import com.NadraWraper.Model.DTO.NadraVerifyCitizenResponse;
import com.NadraWraper.Model.DTO.NadraVerifyFingerprintResponse;
import com.NadraWraper.Model.NadraVerifyCitizenRequest;
import com.NadraWraper.Model.NadraVerifyFingerprintRequest;
import com.NadraWraper.Model.JpVerifyFingerprint;
import com.NadraWraper.Model.Verisys.VerisysLogsModel;
import com.NadraWraper.Repository.VerisysLogsRepository;
import com.NadraWraper.Repository.VerisysStatusRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
@Transactional
public class NadraClient {

    private static final Logger log = Logger.getLogger(NadraClient.class.getName());
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    @Autowired
    private VerisysLogsRepository verisysLogsRepository;
    private String tokenUrl;
    private String citizenUrl;
    private String verifyUrl;
    private String clientId;
    private String clientSecret;
    private String franchiseeId;
    private VerisysStatusRepository verisysStatusRepository;

    @Autowired
    public NadraClient(
            RestTemplateBuilder builder,
            ObjectMapper objectMapper, VerisysLogsRepository verisysLogsRepository,
            @Value("${nadra.token.url}")
            String tokenUrl,
            @Value("${nadra.verify.fingerprint.url}")
            String verifyUrl,
            @Value("${nadra.verify.citizen.url}")
            String citizenUrl,
            @Value("${nadra.client.id}")
            String clientId,
            @Value("${nadra.client.secret}")
            String clientSecret,
            @Value("${nadra.client.franchisee.id}")
            String franchiseeId
    ) {
        this.restTemplate = builder
                .setConnectTimeout(Duration.ofSeconds(5))
                .setReadTimeout(Duration.ofSeconds(10))
                .build();
        this.objectMapper = objectMapper;
        this.tokenUrl = tokenUrl;
        this.verifyUrl = verifyUrl;
        this.clientId = clientId;
        this.citizenUrl=citizenUrl;
        this.clientSecret = clientSecret;
        this.franchiseeId = franchiseeId;
    }

    public NadraVerifyCitizenResponse verifyCitizen(NadraVerifyCitizenRequest request, JpVerifyFingerprint jpRequest) {
        HttpHeaders headers = buildHeaders();
        HttpEntity<NadraVerifyCitizenRequest> entity = new HttpEntity<>(request, headers);

//        try{
//
//        }catch (Exception e){}
        log.info(() -> "Calling verifyCitizen → URL: " + citizenUrl + "  payload: " + serialize(request));
        ResponseEntity<NadraVerifyCitizenResponse> resp =
                restTemplate.postForEntity(citizenUrl,
                        entity,
                        NadraVerifyCitizenResponse.class);

        log.info(() -> "verifyCitizen response status: " + resp.getStatusCode());
        log.info(() -> "verifyCitizen response body: " + serialize(resp.getBody()));

        VerisysLogsModel logEntry = VerisysLogsModel.builder()
                .cnic(request.getCitizenNumber())
                .jpRequest(serialize(jpRequest))
                .transactionId(Long.valueOf(request.getTransactionId()))
                .apiEndpoint("/api/verifyCitizen")
                .nadraVerifyRequest(serialize(request))
                .nadraVerifyResponse(serialize(resp.getBody()))
                .sessionId(Objects.requireNonNull(resp.getBody()).getSessionId())
                .createdAt(LocalDateTime.now())
                .requestTimestamp(LocalDateTime.now())
                .responseTimestamp(LocalDateTime.now())
                .build();
        verisysLogsRepository.save(logEntry);

        return resp.getBody();
    }

    public NadraVerifyFingerprintResponse verifyFingerprint(
            NadraVerifyFingerprintRequest request,
            JpVerifyFingerprint jpRequest
    ) throws Exception {
        String token = fetchToken();
        log.info("Using OAuth token: " + token);

        HttpHeaders headers = buildHeaders();
        HttpEntity<NadraVerifyFingerprintRequest> entity = new HttpEntity<>(request, headers);

        log.info(() -> "Calling verifyFingerprint → URL: " + verifyUrl + "  payload: " + serialize(request));
        ResponseEntity<NadraVerifyFingerprintResponse> resp =
                restTemplate.postForEntity(verifyUrl, entity, NadraVerifyFingerprintResponse.class);

        // audit log
        VerisysLogsModel logEntry = VerisysLogsModel.builder()
                .cnic(jpRequest.getCitizenNumber())
                .jpRequest(serialize(jpRequest))
                .transactionId(Long.valueOf(request.getTransactionId()))
                .nadraVerifyRequest(serialize(request))
                .nadraVerifyResponse(serialize(resp.getBody()))
                .sessionId(Objects.requireNonNull(resp.getBody()).getSessionId())
                .createdAt(LocalDateTime.now())
                .apiEndpoint("/api/verifyFingerprint")
                .requestTimestamp(LocalDateTime.now())
                .responseTimestamp(LocalDateTime.now())
                .build();
        verisysLogsRepository.save(logEntry);

        if("100".equals(resp.getStatusCode())){
            verisysStatusRepository.updateStatusByCnic(request.getCitizenNumber(), "A");
            log.info("Verification successful for transaction ID: " + request.getTransactionId());
        }

        log.info(() -> "verifyFingerprint response status: " + resp.getStatusCode());
        log.info(() -> "verifyFingerprint response body: " + serialize(resp.getBody()));
        return resp.getBody();
    }

    private String fetchToken() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(clientId, clientSecret);

        String body = "grant_type=client_credentials"
                + "&scope=NADRAAPI"
                + "&franchisee_id=" + franchiseeId;

        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        ResponseEntity<Map> resp = restTemplate.postForEntity(tokenUrl, entity, Map.class);
        return resp.getBody().get("access_token").toString();
    }

    private HttpHeaders buildHeaders() {
        String token = null;
        try { token = fetchToken(); }
        catch (Exception e) { throw new IllegalStateException("Unable to fetch token", e); }

        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON);
        h.setBearerAuth(token);
        h.set("X-IBM-Client-Id", clientId);
        return h;
    }

    private String serialize(Object o) {
        try { return objectMapper.writeValueAsString(o); }
        catch (Exception e) {
            log.warning("Serialization failed: " + e.getMessage());
            return "{}";
        }
    }
}
