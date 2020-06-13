package com.banking.app.MyBankingApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.banking.app.MyBankingApp.domain.User;
import com.banking.app.MyBankingApp.exceptions.UsernameAlreadyExistsException;
import com.banking.app.MyBankingApp.repos.UserRepo;

@Service
public class UserServices {
	
	@Autowired
	private UserRepo userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public User saveUser(User newUser) {
		
		
		try {
			
			newUser.setUsername(newUser.getUsername());
//			newUser.setFullName(newUser.getFullName());
			newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
			newUser.setConfirmPassword("");
			return userRepository.save(newUser);
		}catch(Exception e) {
			throw new UsernameAlreadyExistsException("Username "+newUser.getUsername()+" already exists");
		}
		
	}
	
}
