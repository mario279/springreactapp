package com.banking.app.MyBankingApp.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.app.MyBankingApp.domain.User;
import com.banking.app.MyBankingApp.payload.JWTLoginSuccessResponse;
import com.banking.app.MyBankingApp.payload.LoginRequest;
import com.banking.app.MyBankingApp.security.JwtTokenProvider;
import com.banking.app.MyBankingApp.services.UserServices;
import com.banking.app.MyBankingApp.services.ValidationCleaning;
import com.banking.app.MyBankingApp.validator.UserValidator;
import static com.banking.app.MyBankingApp.security.SecurityConstants.TOKEN_PREFIX;

@RestController
@RequestMapping("api/users")
public class UserController {
	
	@Autowired
	private ValidationCleaning validationCleaningService;
	
	@Autowired
	private UserServices userService;
	
	@Autowired
	private UserValidator userValidator;
	
	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private ValidationCleaning validationCleaning;
	
	@CrossOrigin(origins ="http://localhost:3000")
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult r){
		System.out.println("this is the user in registerUSer: " +user.getUsername() + " pass "+user.getPassword());
		userValidator.validate(user, r);
		ResponseEntity<?> errorMap = validationCleaningService.ErrorCleanUp(r);
		if(errorMap!=null) return errorMap;
		
		User newUser = userService.saveUser(user);
		return new ResponseEntity<User>(newUser, HttpStatus.CREATED);
		
	}
	
	@CrossOrigin(origins ="http://localhost:3000")
	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult r){
		ResponseEntity<?> errorMap = validationCleaning.ErrorCleanUp(r);
		if(errorMap !=null) return errorMap;
		
		Authentication authenticationOfUser = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginRequest.getUsername(),
						loginRequest.getPassword()
						)
				
				);
		
		SecurityContextHolder.getContext().setAuthentication(authenticationOfUser);
		String jwt = TOKEN_PREFIX + tokenProvider.generateToken(authenticationOfUser);
		
		return ResponseEntity.ok(new JWTLoginSuccessResponse(true, jwt));
				
		
		
		
	}
}
