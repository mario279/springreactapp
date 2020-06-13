package com.banking.app.MyBankingApp.services;

import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Service
public class ValidationCleaning {
	
	//method to cleanup the error result for human readibility
	public ResponseEntity<?> ErrorCleanUp(BindingResult r){
		if(r.hasErrors()) {
			
			
			//build the type of error message we want, by extracting the specific fields and creating an
			//object using the map
			
			HashMap<String, String> errorMap = new HashMap<>();
			
			//traverse through errors and use the getters to push the values onto our hasmap
			for(FieldError e: r.getFieldErrors()) {
				errorMap.put(e.getField(),  e.getDefaultMessage());
			}
			return new ResponseEntity < HashMap<String, String> > (errorMap, HttpStatus.BAD_REQUEST);
		}
		return null;
	}
}
