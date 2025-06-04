package com.NadraWraper.Service;

public class ResponseCode {
    public static final String SUCCESS = "SUCCESS";

    public static final String FAILURE = "FAILURE";
    public static final String CDC_FAILURE = "99";
    public static final String CDC_SUCCESS = "00";

    public static final String NO_DATA_FOUND = "NO_DATA_FOUND";

    public static final String already_registered = "ALREADY_REGISTERED";
    public static final String already_saved = "ALREADY_SAVED";
    public static final String record_not_exist = "RECORD_NOT_EXIST";

    //entered data is incorrect or value missing
    public static final String invalid_details = "INVALID_DETAILS";
    public static final String blocked_account = "BLOCKED_ACCOUNT";
    public static final String invalid_user_id_pwd = "INVALID_USER_ID_PWD";
    public static final String device_not_registered = "DEVICE_NOT_REGISTERED";

    public static final String invalid_payment_instruction = "INVALID_PAYMENT_INSTRUCTION";
    public static final String investment_not_allowed = "INVESTMENT_NOT_ALLOWED";
    public static final String AUTHENTICATION_FAILED = "AUTHENTICATION_FAILED";
    public static final String invalid_otp = "INVALID_OTP";

    //currently system for specific fund is not ready to accept transaction
    //please try again after few minutes
    public static final String SYSTEM_NOT_READY_FOR_TRANSACTION = "SYSTEM_NOT_READY_FOR_TRANSACTION";
    public static final String ACCOUNT_BLOCKED_FOR_CONVERSION = "ACCOUNT_BLOCKED_FOR_CONVERSION";
    public static final String ACCOUNT_BLOCKED_FOR_INVESTMENT = "ACCOUNT_BLOCKED_FOR_INVESTMENT";
    public static final String ACCOUNT_BLOCKED_FOR_REDEMPTION = "ACCOUNT_BLOCKED_FOR_REDEMPTION";
    public static final String ACCOUNT_BLOCKED = "ACCOUNT_BLOCKED";
    public static final String UNAUTHORIZED_PERSON = "UNAUTHORIZED_PERSON";

    public static final String INVALID_SESSION_TOKEN = "INVALID_SESSION_TOKEN";
}
