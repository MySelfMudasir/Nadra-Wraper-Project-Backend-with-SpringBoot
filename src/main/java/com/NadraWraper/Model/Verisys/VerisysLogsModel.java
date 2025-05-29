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

    @Column(name = "cnic", nullable = false, length = 20)
    private String cnic;

    @Lob
    @Column(name = "jp_request", nullable = false)
    private String jpRequest;

    @Lob
    @Column(name = "nadra_request", nullable = false)
    private String nadraRequest;

    @Lob
    @Column(name = "nadra_response")
    private String nadraResponse;

    @Lob
    @Column(name = "nadra_verify_citizen_request", nullable = false)
    private String nadraVerifyCitizenRequest;

    @Lob
    @Column(name = "nadra_verify_citizen_response")
    private String nadraVerifyCitizenResponse;

    @Column(name = "request_timestamp", nullable = false)
    private LocalDateTime requestTimestamp;

    @Column(name = "response_timestamp")
    private LocalDateTime responseTimestamp;

    @PrePersist
    void onInsert() {
        this.requestTimestamp = LocalDateTime.now();
    }
}
