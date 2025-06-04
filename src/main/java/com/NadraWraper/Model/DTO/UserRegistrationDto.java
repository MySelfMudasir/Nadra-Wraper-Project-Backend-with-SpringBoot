package com.NadraWraper.Model.DTO;

import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDto implements Serializable {
    private String cnic;
    private String dateOfBirth;
    private String email;
    private String mobileNumber;
    private String otp;
    private String password;
    private String userName;
    private String userId;
    private String touchId;
    private String faceId;
    private String deviceId;
    private String deviceName;
    private String pin;
}
