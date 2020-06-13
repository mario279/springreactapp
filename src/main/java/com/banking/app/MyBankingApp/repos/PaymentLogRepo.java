package com.banking.app.MyBankingApp.repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.banking.app.MyBankingApp.domain.PaymentLog;


@Repository
public interface PaymentLogRepo extends CrudRepository<PaymentLog, Long>{

	//we need to find the payment log by the identifier, so that it returns
	//the appropriate paymentlog upon calling a specific payment
	public PaymentLog findBypaymentIdentifier(String Identifier);
	
}
