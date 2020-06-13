package com.banking.app.MyBankingApp.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import static com.banking.app.MyBankingApp.security.SecurityConstants.EXPIRATION_TIME;
import static com.banking.app.MyBankingApp.security.SecurityConstants.SECRET;
import com.banking.app.MyBankingApp.domain.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenProvider {
	public String generateToken(Authentication authentication) {
		
		User user = (User)authentication.getPrincipal();
		Date now = new Date(System.currentTimeMillis());
		
		Date tokenExpirationTime = new Date(now.getTime()+ EXPIRATION_TIME);
		
		String userId = Long.toString(user.getId());
		
		Map<String, Object> claims = new HashMap<>();
		
		claims.put("id", (Long.toString(user.getId())));
		claims.put("username", user.getUsername());
		claims.put("fullname", user.getFullName());
		
		return Jwts.builder()
				.setSubject(userId)
				.setClaims(claims)
				.setIssuedAt(now)
				.setExpiration(tokenExpirationTime)
				.signWith(SignatureAlgorithm.HS512, SECRET)
				.compact();
	}
	
	//here we validate the token that was generated when the user loggged in
	//we need to test for 4 things:  invalid token signature, invalid token itself, expired token, unsupported token, empty token
	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
			return true;
		}
		catch(SignatureException e) {
			System.out.println("incorrect token signature");
		}
		catch(MalformedJwtException ex) {
			System.out.println("Invalid token");
		}
		catch(ExpiredJwtException ex) {
			System.out.println("Token session has expired");
		}
		catch(UnsupportedJwtException ex) {
			System.out.println("unsupported token");
		}
		catch(IllegalArgumentException ex) {
			System.out.println("empty token received");
		}
		return false;
	}
	
	//now that we validated the token , we need to extract the user details from the token, we will return the userID
	public long getUserIdFromJWT(String token) {
		Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
		//might have to change to getId
		String id = (String)claims.get("id");
		
		return Long.parseLong(id);
	}

}
