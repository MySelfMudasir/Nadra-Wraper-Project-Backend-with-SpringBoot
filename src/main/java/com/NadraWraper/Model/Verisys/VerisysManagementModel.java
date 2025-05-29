package com.NadraWraper.Model.Verisys;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(schema = "accreg", name = "verisys_management")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerisysManagementModel {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "verisys_mgmt_seq"
    )
    @SequenceGenerator(
            name = "verisys_mgmt_seq",
            sequenceName = "accreg.verisys_mgmt_seq",
            allocationSize = 1
    )
    @Column(name = "management_id")
    private Long managementId;

    @Column(name = "cnic", nullable = false, length = 20)
    private String cnic;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "status", nullable = false, length = 1)
    private String status;    // "P","A","F"

    @Column(name = "session_id", length = 50)
    private String sessionId;

    @Column(name = "retry_count", nullable = false)
    private Integer retryCount;

    @Column(name = "contact_number", length = 20)
    private String contactNumber;

    @Column(name = "issue_date")
    private LocalDate issueDate;

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) this.status = "P";
        if (this.retryCount == null) this.retryCount = 0;
    }
}