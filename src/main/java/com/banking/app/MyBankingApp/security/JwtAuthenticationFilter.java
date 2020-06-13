package com.banking.app.MyBankingApp.security;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.banking.app.MyBankingApp.domain.User;
import com.banking.app.MyBankingApp.services.CustomUserDetailService;
 import static com.banking.app.MyBankingApp.security.SecurityConstants.TOKEN_PREFIX;
 import static com.banking.app.MyBankingApp.security.SecurityConstants.HEADER_STRING;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@Autowired
	private CustomUserDetailService customUserDetailsService;
	
	
	
	
	
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		try {
			String jwt = getJWTFromRequest(request);
			
			if(StringUtils.hasText(jwt)&& tokenProvider.validateToken(jwt)) {
				Long userId = tokenProvider.getUserIdFromJWT(jwt);
				User userDetails = customUserDetailsService.loadUserById(userId);
				
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, Collections.emptyList());
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}catch(Exception e){
			logger.error("could not set user authentication in security context", e);
			
		}
		
		filterChain.doFilter(request, response);
		
		
	}

	
	


private String getJWTFromRequest(HttpServletRequest r) {
	String bearerToken = r.getHeader(HEADER_STRING);
	
	if(StringUtils.hasText(bearerToken)&&bearerToken.startsWith(TOKEN_PREFIX)){
		return bearerToken.substring(7, bearerToken.length());
	}
	
	return null;
}


}