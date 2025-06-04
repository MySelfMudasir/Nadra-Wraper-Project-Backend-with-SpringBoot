package com.NadraWraper.Service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ConsumerSessionService {

    @Value("${jp.userName}")
    private String jpUserId;

    private final static Logger log = Logger.getLogger(ConsumerSessionService.class);



    public UserDetails loadUserByUsername(String username) {
        // For demonstration purposes, creating a mock user
        if (username.equals(jpUserId)) {
            return new org.springframework.security.core.userdetails.User(
                    username,
                    "",
                    new ArrayList<>()
            );
        }
        return null;
    }


}
