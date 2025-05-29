package com.NadraWraper.security;

import com.NadraWraper.Model.DTO.JpLoginResponse;
import com.NadraWraper.Model.JPLogin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class SecurityAuthController {
	@Autowired
	private SecureTokenUtil jwtTokenUtil;

	@Value("${jp.userName}")
	private String userName;
	@Value("${jp.password}")
	private String password;

	private static final Logger logger = LoggerFactory.getLogger(SecurityAuthController.class);

	@RequestMapping(value = "/Authenticate", method = RequestMethod.POST, consumes = "application/json")
	public JpLoginResponse createConsumerAuthenticationToken(@RequestBody JPLogin jpLogin) throws Exception {
		JpLoginResponse response = new JpLoginResponse();
		String token = null;
		try {
			System.out.println(jpLogin.getPassword()+jpLogin.getUserName());

			if (jpLogin != null && password.equals(jpLogin.getPassword()) && userName.equals(jpLogin.getUserName())) {
				final UserDetails userDetails = new User(jpLogin.getUserName(), jpLogin.getPassword(),
						new ArrayList<>());
				token = jwtTokenUtil.generateToken(userDetails);
				System.out.println(token);
				response.setStatusCode("00");
				response.setMessage("Login successful");
				response.setToken(token);
				response.setTokenExpiry( jwtTokenUtil.getExpirationDateFromToken(token).toString());
			} else {
				response.setStatusCode("01");
				response.setMessage("Invalid credentials");
			}

		} catch (Exception e) {
			token = null;
			logger.warn(e.toString());
		}
		return response;
	}
}