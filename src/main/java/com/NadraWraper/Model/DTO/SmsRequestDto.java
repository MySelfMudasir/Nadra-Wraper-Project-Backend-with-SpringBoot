package com.NadraWraper.Model.DTO;

import lombok.Data;

@Data
public class SmsRequestDto {
    private String userId;
    private String password;
    private String mobileNo;
    private String msgId;
    private String sms;
    private String msgHeader;
    private String smsType;
    private String handsetPort;
    private String smsChannel;
    private String telco;
}