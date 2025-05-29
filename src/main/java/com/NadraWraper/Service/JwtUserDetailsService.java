package com.NadraWraper.Service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // For simplicity, we're just returning a hardcoded user
        // In a real application, you would fetch the user from the database
        if ("user".equals(username)) {
            return new org.springframework.security.core.userdetails.User(
                    "user",
                    "$2a$10$Tzv5ZQBxQeiDLkdfJp3n3.ZlZKfi.GczzAweJKMnz9oSbn4Onh6cS", // password: password
                    new ArrayList<>()
            );
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}
