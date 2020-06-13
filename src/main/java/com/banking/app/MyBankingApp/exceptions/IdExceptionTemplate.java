package com.banking.app.MyBankingApp.exceptions;

public class IdExceptionTemplate {

	private String paymentId;
	
	//we are returning a json object as a key value pair we need
	public IdExceptionTemplate(String id) {
		this.paymentId = id;
	}

	public String getProjectId() {
		return paymentId;
	}

	public void setProjectId(String projectId) {
		this.paymentId = projectId;
	}
}
