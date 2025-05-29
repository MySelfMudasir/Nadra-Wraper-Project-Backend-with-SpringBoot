package com.NadraWraper.Model.entity;


import lombok.*;
import java.sql.*;
import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_registration")
public class UserRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "my_sequence")
    @SequenceGenerator(name = "my_sequence", sequenceName = "USER_REGISTRATION_SEQ", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "cnic")
    private String cnic;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "email")
    private String email;

    @Column(name = "face_id")
    private String faceId;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "otp")
    private String otp;

    @Column(name = "otp_validity_time")
    private Timestamp otpValidityTime;

    @Column(name = "password")
    private String password;

    @Column(name = "PASSWORD_SCHEME_CODE")
    private int passwordSchemeCode;

    @Column(name = "post")
    private boolean post;

    @Column(name = "registration_date")
    private Date registrationDate;

    @Column(name = "ROLE_CODE")
    private String roleCode;

    @Column(name = "status_code")
    private String statusCode;

    @Column(name = "touch_id")
    private String touchId;

    @Column(name = "customer_pin")

    private String pin;

    @Override
    public String toString() {
        return "UserRegistration{" +
                "id=" + id +
                ", cnic='" + cnic + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", email='" + email + '\'' +
                ", faceId='" + faceId + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", otp='" + otp + '\'' +
                ", otpValidityTime=" + otpValidityTime +
                ", password='" + password + '\'' +
                ", passwordSchemeCode=" + passwordSchemeCode +
                ", post=" + post +
                ", registrationDate=" + registrationDate +
                ", roleCode='" + roleCode + '\'' +
                ", statusCode='" + statusCode + '\'' +
                ", touchId='" + touchId + '\'' +
                ", userName='" + userId + '\'' +
                ", userType='" + userType + '\'' +
                '}';
    }

    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_type")
    private String userType; // need to add reference with user_type table
}

