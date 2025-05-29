package com.NadraWraper.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class SecureTokenUtil implements Serializable {

    private static final long serialVersionUID = -2550185165626007488L;

    public boolean isValidConsumer(String userid, String pwd, String channelId) {
        return (userid.equals(jpUserid) && pwd.equals(jpPassword) && channelId.equals(channelId)) ? true : false;
    }

    public boolean isConsumerRequest(String userid) {
        return (userid.equals(jpUserid)) ? true : false;
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + USER_TOKEN_VALIDITY * 60 * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
//        if(getExpirationDateFromToken(token).before(new Date())){
//            Jwts.builder().setExpiration(new Date(System.currentTimeMillis() + USER_TOKEN_VALIDITY *30*1000));
//        }
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

//    public Boolean validateToken(String token, UserDetails userDetails, HttpSession session) {
//        final String username = getUsernameFromToken(token);
//        System.out.println("validating token");
//        if (shouldRenewToken(token)) {
//            String newToken = renewToken(token);
//            System.out.println("New Token generated :----    " + newToken);
//            System.out.println("Updating session");
//            sessionService.updateSession(newToken);
//            session.setAttribute("token", newToken);
//        }
//        System.out.println("Token renewed");
//        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
//    }

//    private boolean shouldRenewToken(String token) {
//        System.out.println("Token time check");
//        final Date expiration = getExpirationDateFromToken(token);
//        Date now = new Date();
//        long diff = expiration.getTime() - now.getTime();
//        return diff <=  60 * 1000; // 5 minutes in milliseconds
//    }
//
//    private String renewToken(String token) {
//        System.out.println("Renewing token");
//        Claims claims = getAllClaimsFromToken(token);
//        return Jwts.builder().setClaims(claims)
//                .setSubject(claims.getSubject())
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + USER_TOKEN_VALIDITY * 60 * 1000))
//                .signWith(SignatureAlgorithm.HS512, secret).compact();
//    }

    public String getToken(HttpServletRequest request) {
        String url = request.getRequestURI().toString();
        url = url.substring(url.lastIndexOf('/'));
        final String requestTokenHeader = request.getHeader("Authorization");
        String sessionToken = null;
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            sessionToken = requestTokenHeader.substring(7);
        }
        return sessionToken;
    }

//	public String getTokeUserId(String tokeAuthUserId, HttpServletRequest request) {
//		if((tokeAuthUserId==null || tokeAuthUserId.isEmpty())) {
//			StringBuffer jb = new StringBuffer();
//			  String line = null;
//			  try {
//				BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
//			    while ((line = reader.readLine()) != null) {
//			      jb.append(line);
//			      line = line.trim();
//			    if(line.contains("userId") || line.contains("userid")) {
//			    	tokeAuthUserId = line.substring(line.indexOf('"', line.indexOf("userid")) , line.lastIndexOf('"')).replace("\"", "")
//			    			.replace(":", "");			    	
//			    	break;
//			    	}
//			    }
//			    reader.close();
//			  } catch (Exception e) {}
//		}
//		return tokeAuthUserId;
//	}

    @Value("${jp.userName}")
    private String jpUserid;

//    @Value("${mobile.consumer.channelId}")
//    private String consumerChannelId;

    @Value("${jp.password}")
    private String jpPassword;

    @Value("${user.session.validity.minutes}")
    private long USER_TOKEN_VALIDITY;

    @Value("${user.session.token.secret}")
    private String secret;
}