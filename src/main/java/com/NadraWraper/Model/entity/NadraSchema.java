package com.NadraWraper.Model.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "verisys_management")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NadraSchema {

    @Id
    // Use sequence or identity based on Oracle setup (see earlier advice)
    private Long id;

    @Column(name = "SUB_ACCOUNT_ID")
    private Long subAccountId;

    @Column(name = "ACCOUNT_TYPE")
    private String accountType;

    @Column(name = "CNIC")
    private String cnic;

    @Column(name = "CNIC_ISSUE_DATE")
    private LocalDate cnicIssueDate;

    @Column(name = "EMAIL")
    private String email;
    @Column(name = "MOBILE")
    private String mobile;

    @Column(name = "RECORD_TYPE")
    private String recordType;


    @Column(name = "STATUS")
    private String status;

    @Column(name = "OTP")
    private String otp;

    @Column(name = "NTN")
    private String ntn;

    @Column(name = "PIN")
    private String pin;

    @Transient
    private List<NadraSchema> subRecords;
}
