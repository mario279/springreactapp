package com.banking.app.MyBankingApp.repos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.banking.app.MyBankingApp.domain.PaymentInstallment;

@Repository
public interface PaymentInstallmentRepo extends CrudRepository<PaymentInstallment, Long>{

//	public PaymentInstallment findPaymentInstallmentbyId(String Id);
	//OrderByPriority is some functionality out of the box
//    List<PaymentInstallment> findByPaymentIdentifierOrderByPriority(String id);
	PaymentInstallment findByPaymentSequence(String psequence);
	Iterable<PaymentInstallment> findByPaymentIdentifierOrderByPriority(String paymentlog_id);
	
}
