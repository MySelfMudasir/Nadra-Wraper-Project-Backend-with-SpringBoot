package com.NadraWraper.Service;


import org.slf4j.*;
import client.sender;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Async;
import org.springframework.beans.factory.annotation.Value;

@Component
public class SmsSenderService {

    @Async
    public void sendSms(String mobileNumber, String body, String id) throws Exception {

        String cntct = "03132278205";

        if (mobileNumber != null && !mobileNumber.isEmpty()) {
            cntct = mobileNumber;
        }

        try {
            cntct = cntct.charAt(0) == '0' ? "92" + cntct.substring(1, cntct.length()) : cntct;
            sender.send(cntct, body, id, proxyAddress==null || proxyAddress.isEmpty()?"":proxyAddress, proxyPort==null || proxyPort.isEmpty()?"":proxyPort);
        } catch (Exception e) {
            logger.error("Error orrucred in sms sending. " + e.toString());
            throw e;
        }
    }

    @Async
    public void SendSmsAnonymous(String mobile, String body, String id) {

        String cntct = mobile;

        try {
            cntct = cntct.charAt(0) == '0' ? "92" + cntct.substring(1, cntct.length()) : cntct;
            sender.send(cntct, body, id, proxyAddress == null || proxyAddress.isEmpty() ? "" : proxyAddress, proxyPort == null || proxyPort.isEmpty() ? "" : proxyPort);
        } catch (Exception e) {
            logger.error("Error orrucred in sms sending. " + e.toString());
        }
    }

    @Value("${mobile.sms.proxy}")
    private String proxyAddress;

    @Value("${mobile.sms.proxy.port}")
    private String proxyPort;

    private static final Logger logger = LoggerFactory.getLogger(SmsSenderService.class);
}
