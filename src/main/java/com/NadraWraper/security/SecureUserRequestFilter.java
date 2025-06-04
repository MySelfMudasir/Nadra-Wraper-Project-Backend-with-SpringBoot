package com.NadraWraper.security;

import com.NadraWraper.Service.ConsumerSessionService;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SecureUserRequestFilter extends OncePerRequestFilter {

	private static final Logger logger = Logger.getLogger(SecureUserRequestFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		String url = request.getRequestURI();
		logger.info("Request URL: " + url);
		url = url.substring(url.lastIndexOf('/'));

		// Apply filter only for /echo or /test endpoint
		if (url.contentEquals("/echo")
				|| url.contentEquals("/VerifyFingerPrint")
				|| url.contentEquals("/VerifyCitizen") || url.contentEquals("/generate") || url.contentEquals("/get")
				|| url.contentEquals("/addAccount") || url.contentEquals("/getAccountsDetails")
				|| url.contentEquals("/updateAccount") || url.contentEquals("/resetStatus")
				|| url.contentEquals("/getNadraResponse") || url.contentEquals("/verifyotp")|| url.contentEquals("/generateOtp")

		) {
			System.out.println(request);
			String sessionToken = tokenUtil.getToken(request);

			if (sessionToken == null || sessionToken.isEmpty()) {
				logger.warn("No token found in request.");
				response.sendError(HttpServletResponse.SC_FORBIDDEN, "No token found in request.");
				return;
			}

			String username = null;

			try {
				logger.info("Verifying token: " + sessionToken);
				username = tokenUtil.getUsernameFromToken(sessionToken);
			} catch (IllegalArgumentException e) {
				logger.warn("Unable to get Token", e);
			} catch (ExpiredJwtException e) {
				logger.warn("Token has expired", e);
			}

			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null
//					&& userSession.isValidConsumerToken(sessionToken)
			) {
				UserDetails userDetails = userSession.loadUserByUsername(username);

				if (userDetails != null && tokenUtil.validateToken(sessionToken, userDetails)) {
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
				} else {
					logger.warn("Token validation failed or user details are null.");
				}
			} else {
				logger.warn("Username is null or authentication is already set.");
			}
		}

		chain.doFilter(request, response);
	}

	@Autowired
	private SecureTokenUtil tokenUtil;

	@Autowired
	private ConsumerSessionService userSession;
}
