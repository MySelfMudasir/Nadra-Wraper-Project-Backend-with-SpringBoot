package com.NadraWraper.Utility;

import java.util.Random;

public class UserRegistrationUtil {
    public String getPinCode() {
        Random rand = new Random();
        String otp = String.format("%04d", rand.nextInt(10000));
        return otp;// "123456";
    }
}
