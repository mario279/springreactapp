package com.banking.app.MyBankingApp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PaymentNotFoundException extends RuntimeException{

	public PaymentNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	
}