package com.banking.app.MyBankingApp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


//we give it the @ResponsStatus annotation to make sure that everytime we throw an error
//we have it the httpstatus.badrequest code
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IdException extends RuntimeException {

	public IdException(String message) {
		super(message);
	}
}
