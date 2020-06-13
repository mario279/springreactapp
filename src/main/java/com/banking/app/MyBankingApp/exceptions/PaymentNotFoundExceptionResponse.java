package com.banking.app.MyBankingApp.exceptions;

public class PaymentNotFoundExceptionResponse {

	private String PaymentNotFound;
	
	public PaymentNotFoundExceptionResponse(String paymentNotFound) {
		PaymentNotFound = paymentNotFound;
	}

	public String getPaymentNotFound() {
		return PaymentNotFound;
	}

	public void setPaymentNotFound(String paymentNotFound) {
		PaymentNotFound = paymentNotFound;
	}
	
	
}
