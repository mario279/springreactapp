package com.banking.app.MyBankingApp.web;

import java.security.Principal;
import java.util.Iterator;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.app.MyBankingApp.domain.PaymentInstallment;
import com.banking.app.MyBankingApp.services.PaymentInstallmentService;
import com.banking.app.MyBankingApp.services.ValidationCleaning;

//the @Crossorigin, allows us to receive calls from the react app

@RestController
@RequestMapping("/api/paymentlog")
@CrossOrigin
public class PaymentLogController {
	//we need all the actions from paymentinstallment service
	//because we will be working with payment installments
	//as the log is a list of installments
	@Autowired
	private PaymentInstallmentService paymentInstallmentService;
	
	//we also need validations, very important
	@Autowired
	private ValidationCleaning validationCleaning;
	
	//paymentlog id is the same as the payment id
	//they need to match, we need to make sure the payment log exists
	//before we save the installment
	//we need to take a @Valid @requestbody
	//we add binding result so that we can extract any errors we might have
	@PostMapping("/{pamentlog_id}")
	public ResponseEntity<?> addPIToPaymentLog(@Valid @RequestBody PaymentInstallment paymentInstallment,
			BindingResult result, @PathVariable String pamentlog_id, Principal principal ){
		
		//make sure before you try to persist we have a valid object
		ResponseEntity<?> error = validationCleaning.ErrorCleanUp(result);
		//check if there where errors
		if(error!=null) {
			return error;
		}
		//paymentInstallment1, HttpStatus.CREATED
		PaymentInstallment paymentInstallment1 = paymentInstallmentService.addPaymentInstallment(pamentlog_id , paymentInstallment, principal.getName());
		
		
		return new ResponseEntity<PaymentInstallment>(paymentInstallment1, HttpStatus.CREATED);
		
	}
	
	//lets find the payment log by with the id
	//this method should return a paymentinstallment list for the particular
	//project and payment log
	@GetMapping("/{paymentlog_id}")
	public Iterable<PaymentInstallment> getPaymentLog(@PathVariable String paymentlog_id, Principal principal){
		
//		System.out.println("dude you are calling getPaymentLog, this is what you passed: "+paymentlog_id);
//		Iterable<PaymentInstallment> response = paymentInstallmentService.findByPaymentIdentifierOrderByPriority(paymentlog_id);
//	    
//		Iterator<PaymentInstallment> itr = response.iterator();
//		
//		while(itr.hasNext()) {
//			System.out.println("I am an item inside response");
//		}
//	  System.out.println("this is the size of the response: "+ response.toString());
		return paymentInstallmentService.findPaymentInstallmentById(paymentlog_id, principal.getName());
	}
	//we are going to get the installment through the backlog and through the id
	@GetMapping("/{paymentlog_id}/{pi_id}")
	public ResponseEntity<?> getPaymentInstallment(@PathVariable String paymentlog_id, @PathVariable String pi_id, Principal principal){
		
		System.out.println(" you are passing me the following values : "+paymentlog_id +" "+pi_id);
		
		PaymentInstallment paymentInstallment = paymentInstallmentService.findPIByPaymentSequence(paymentlog_id, pi_id,principal.getName());
		
		return new ResponseEntity<PaymentInstallment>(paymentInstallment,HttpStatus.OK);
	}
	
	//response entity reprents the entire http response and its generic so you can send back whatever
	//@Request deserializes the jason into a java type
	//@Valid loads hybernates validator to make sure the parameter is valid
	//with the @PathVariable we are telling spring that we are sending ina variable within the path
	@PatchMapping("/{paymentlog_id}/{pi_id}")
	public ResponseEntity<?> updatePaymentInstallment(@Valid @RequestBody PaymentInstallment paymentInstallment, BindingResult result,
			@PathVariable String paymentlog_id, @PathVariable String pi_id, Principal principal){
	    
		System.out.println(" updatepayment in payment log controller being called! this is the paymentlog_ id: "+paymentlog_id +" and pi_id: "+pi_id);
		System.out.println(" updatepayment in payment log controller being called! this is the paymentInstallment.priority: "+paymentInstallment.getPriority());
		//make sure before you try to persist we have a valid object
		ResponseEntity<?> error = validationCleaning.ErrorCleanUp(result);
				//check if there where errors
		if(error!=null) {
					return error;
		}
		
		//we will reuse code we already wrote to make sure that we can perform validations and
		//not rewrite them
		System.out.println("inside the controller paymentlog, this is paymentInstallment.getidetnifier: "+paymentInstallment.getPaymentIdentifier());
		
		PaymentInstallment updatedInstallment = paymentInstallmentService.updateByPaymentSequence(paymentInstallment, paymentlog_id, pi_id, principal.getName());
			System.out.println("inside the controller paymentlog, this is updatedInstallment.getDescription: "+ updatedInstallment.getDescription());
		return new ResponseEntity<PaymentInstallment>(updatedInstallment,HttpStatus.OK);
	}
	
	@DeleteMapping("/{paymentlog_id}/{pi_id}")
	public ResponseEntity<?> deletePaymentInstallment(@PathVariable String paymentlog_id, @PathVariable String pi_id, Principal principal){
		paymentInstallmentService.DeletePaymentInstallmentByPISequence(paymentlog_id, pi_id, principal.getName());
		return new ResponseEntity<String>("Installment " + pi_id +" was deleted succesfully", HttpStatus.OK);
	}
	
}
