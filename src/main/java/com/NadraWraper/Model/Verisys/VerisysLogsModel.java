package com.NadraWraper.Model.Verisys;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "verisy_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerisysLogsModel {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "verisy_logs_seq"
    )
    @SequenceGenerator(
            name = "verisy_logs_seq",
            sequenceName = "accreg.verisy_logs_seq",
            allocationSize = 1
    )
    @Column(name = "log_id")
    private Long logId;
    @Column(name = "transaction_id")
    private Long transactionId;


    @Column(name = "cnic", nullable = false, length = 20)
    private String cnic;

    @Lob
    @Column(name = "jp_request", nullable = false)
    private String jpRequest;

    @Lob
    @Column(name = "nadra_verify_request", nullable = false)
    private String nadraVerifyRequest;

    @Column(name = "API_ENDPOINT", nullable = false)
    private String apiEndpoint;

    @Column(name = "SESSION_ID")
    private String sessionId;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Lob
    @Column(name = "nadra_verify_response")
    private String nadraVerifyResponse;

    @Column(name = "request_timestamp", nullable = false)
    private LocalDateTime requestTimestamp;

    @Column(name = "response_timestamp")
    private LocalDateTime responseTimestamp;

    @PrePersist
    void onInsert() {
        this.requestTimestamp = LocalDateTime.now();
    }
}
