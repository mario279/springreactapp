package com.banking.app.MyBankingApp.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banking.app.MyBankingApp.domain.Payment;
import com.banking.app.MyBankingApp.domain.PaymentInstallment;
import com.banking.app.MyBankingApp.domain.PaymentLog;
import com.banking.app.MyBankingApp.exceptions.IdException;
import com.banking.app.MyBankingApp.exceptions.PaymentNotFoundException;
import com.banking.app.MyBankingApp.repos.PaymentInstallmentRepo;
import com.banking.app.MyBankingApp.repos.PaymentLogRepo;
import com.banking.app.MyBankingApp.repos.PaymentRepo;

@Service
public class PaymentInstallmentService {

	//we need to inject two dependencies
	
	//this one is to use the persistence implementations of the paymentLog
	@Autowired
	private PaymentLogRepo paymentLogRepo; 
	
	@Autowired
	private PaymentInstallmentRepo paymentInstallmentRepo;
	
	//we need the exception for when we don't find a payment
	@Autowired
	private PaymentRepo paymentRepo;
	//this method takes 2 arguments
	//the payment to which this installment belongs to
	//the actual payment installment object
	@Autowired
	private PaymentService paymentService;

	
	public PaymentInstallment addPaymentInstallment(String paymentIdentifier, PaymentInstallment paymentInstallment, String username) {
		
//		try {
			//happy path
			// we will assume that the project installment and the project exists
			//find the payment log that belongs to the specific payment being passed
			PaymentLog paymentlog = paymentService.findPaymentByIdentifier(paymentIdentifier, username).getPaymentLog();
			
			//now lets set the payment log for the pair installment
			paymentInstallment.setPaymentlog(paymentlog);
			
			//lets get the current sequence for the payment log being found
			Integer PaymentSequence = paymentlog.getInstallmentSequence();
			
			//at this point if its the first one, lets set the sequnce to 1
			//or if there are payment installments already, then add one
			PaymentSequence++;
			
			//now that you updated the paymentsequence, you need to set the paymentlog sequence to this new number
			paymentlog.setIMSequence(PaymentSequence);
			
			//the logic is to increase the payment sequence  and set it, and repeat
			
			//this is not the ideal, because when you delete one, the counter should reset
			//so maybe this service should get called upon deletion
			paymentInstallment.setPaymentSequence(paymentlog.getPaymentIdentifier()+"-"+PaymentSequence);
			paymentInstallment.setPaymentIdentifier(paymentIdentifier);
			
			//lets define a priority
			//if the user doesn't set a priority, by default it will be one
			
			if(paymentInstallment.getPriority()==null||paymentInstallment.getPriority()==0) {
				paymentInstallment.setPriority(1);
			}
			
			//now lets set the default when the status is not set by the user
			if(paymentInstallment.getStatus()==null||paymentInstallment.getStatus()=="") {
				paymentInstallment.setStatus("Pending");
			}
			
			//lets use spring's CRUD methods
			return paymentInstallmentRepo.save(paymentInstallment);
//		}
//		catch(Exception e) {
//			throw new PaymentNotFoundException("PaymentNotFoundProblem");
//		}
	
	}

	public Iterable<PaymentInstallment> findPaymentInstallmentById(String paymentlog_id, String username) {
		
//		Payment payment = paymentRepo.findPaymentByIdentifier(paymentlog_id);
		
		//possible problems due to name changing
		Payment payment = paymentService.findPaymentByIdentifier(paymentlog_id, username);
		
		if(payment==null) {
			throw new PaymentNotFoundException("Payment with this id is not found"+paymentlog_id);
			}
		// TODO Auto-generated method stub
	
		return paymentInstallmentRepo.findByPaymentIdentifierOrderByPriority(paymentlog_id);
	}
	
	public PaymentInstallment findPIByPaymentSequence(String paymentlog_id, String pi_id, String username) {
		//users shouldnt be able to look in payment logs that don't exist
//		PaymentLog paymentLog = paymentLogRepo.findBypaymentIdentifier(paymentlog_id);
//	    
//		if(paymentLog==null) {
//			throw new PaymentNotFoundException("payment with ID: "+paymentlog_id+" not found");
//		}
		System.out.println("Inside finPIBypaymentSequence, this is paymentlog_id " +paymentlog_id);
		System.out.println("Inside finPIBypaymentSequence, this is pi_id " +pi_id);
		System.out.println("Inside finPIBypaymentSequence, this is username " +username);
		paymentService.findPaymentByIdentifier(paymentlog_id, username);
		//users shouldn't find payment installments that do not exist
		PaymentInstallment paymentInstallment = paymentInstallmentRepo.findByPaymentSequence(pi_id);
		
		System.out.println("Inside finPIBypaymentSequence, paymentInstallmentRepo called, this ispaymentInstallment.getPaymentIdentif: " +paymentInstallment.getPaymentIdentifier());
		if(paymentInstallment==null) {
			throw new PaymentNotFoundException("installment with id: "+ paymentlog_id+" and id: "+pi_id+ " does not exist");
		}
		
		// users shouldn't be able to find payment installments in wrong payment logs
//		if(!paymentInstallment.getPaymentSequence().equals(paymentlog_id)) {
//			throw new PaymentNotFoundException("wrong payment log");
//		}
		
		return paymentInstallment;
	}
	
	public PaymentInstallment updateByPaymentSequence(PaymentInstallment updatedInstallment, String paymentlog_id, String pi_id, String username) {
		System.out.println("inside updateByPayment inthe service layer, this is the priority passed: ");
		
		PaymentInstallment paymentinstallment2 = findPIByPaymentSequence(paymentlog_id, pi_id, username);
		//validation is needed here
//		paymentinstallment2 = updatedInstallment;
		
	     paymentinstallment2.setPaymentIdentifier(paymentlog_id);
	     paymentinstallment2.setPaymentSequence(pi_id);
	     paymentinstallment2.setDescription(updatedInstallment.getDescription());
	     paymentinstallment2.setName(updatedInstallment.getName());
	     paymentinstallment2.setPaymentForm(updatedInstallment.getPaymentForm());
	     paymentinstallment2.setPriority(updatedInstallment.getPriority());
	     paymentinstallment2.setStatus(updatedInstallment.getStatus());
	     
	
	     System.out.println("printing out the paymentlog_id inside installmentService: "+updatedInstallment.getPaymentIdentifier());
	    
	     
		return paymentInstallmentRepo.save(paymentinstallment2);
	}
	
	public void DeletePaymentInstallmentByPISequence(String paymentlog_id, String pi_id, String username) {
		
		PaymentInstallment paymentInstallment = findPIByPaymentSequence(paymentlog_id,pi_id, username);
		paymentInstallmentRepo.delete(paymentInstallment);
	}
	
}
