package com.banking.app.MyBankingApp.repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.banking.app.MyBankingApp.domain.Payment;

@Repository
public interface PaymentRepo extends CrudRepository<Payment, Long>{

	public Payment findPaymentByIdentifier(String Identifier);

   
    @Override
    Iterable<Payment> findAll();
    
    Iterable<Payment> findAllByPersonInChargeOfPayment(String username);
    

	
}
