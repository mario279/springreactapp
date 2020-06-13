package com.banking.app.MyBankingApp.web;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;


import javax.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.app.MyBankingApp.domain.Payment;
import com.banking.app.MyBankingApp.services.PaymentService;
import com.banking.app.MyBankingApp.services.ValidationCleaning;


@RestController
@RequestMapping("/api/payment")
@CrossOrigin
public class PaymentController {
	
	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private ValidationCleaning validationCleaning;
	
	//route to post a new project
	//ResponseEntity allows us to have more control on our JSON responses
	//this is the happy path, 
	//generics wild card
	@PostMapping("")
	public ResponseEntity<?> CreateNewPayment(@Valid @RequestBody Payment p, BindingResult r, Principal principal){
		
		ResponseEntity<?> cleanError = validationCleaning.ErrorCleanUp(r);
		
		if(cleanError!=null) return cleanError;
		
		//persist the project
		Payment p1 = paymentService.saveOrUpdateProject(p, principal.getName());
		return new ResponseEntity<Payment>(p1, HttpStatus.CREATED);
	}
	
	@PutMapping("")
	public ResponseEntity<?> UpdatePayment(@Valid @RequestBody Payment p, BindingResult r, Principal principal){
		
		
		ResponseEntity<?> cleanError = validationCleaning.ErrorCleanUp(r);
		if(cleanError!=null) return cleanError;
		
		Payment p1 = paymentService.saveOrUpdateProject(p, principal.getName());
		p1.addUpdate();
		return new ResponseEntity<Payment>(p1, HttpStatus.ACCEPTED);
	}
	
//	@PutMapping("")
//	public ResponseEntity<?> UpdateExistingPayment(@Valid @RequestBody Payment p, BindingResult r){
//		ResponseEntity<?> cleanError = validationCleaning.ErrorCleanUp(r);
//		if(cleanError!=null) return cleanError;
//		
//		Payment p1 = paymentService.saveOrUpdateProject(p);
//		return new ResponseEntity<Payment>(p1, HttpStatus.ACCEPTED);
//	}
//	
	@GetMapping("/{paymentIdentifier}")
	public ResponseEntity<?> getPaymentByIdentif(@PathVariable String paymentIdentifier, Principal principal){
		
		System.out.println("this is the id you passed" + paymentIdentifier);
		
		Payment payment = paymentService.findPaymentByIdentifier(paymentIdentifier, principal.getName());
		
		return new ResponseEntity<Payment>(payment, HttpStatus.OK);
	}
	
//	@GetMapping("/all")
//	public ResponseEntity<?> getPaymentList(){
//		
//		List<Payment> allPayments =paymentService.findAll();;
//		
//		return new ResponseEntity<List<Payment> >(allPayments,HttpStatus.OK);
//				
//	}
	
	
	@GetMapping("/all")
	public Iterable<Payment> getPaymentList(Principal principal){
		
//		List<Payment> allPayments =paymentService.findAll();;
		
		return paymentService.findAll(principal.getName());
				
	}
	
	
	
	@DeleteMapping("/{paymentIdentifier}")
	public ResponseEntity<?> deletePaymentByIdentifier(@PathVariable String paymentIdentifier, Principal principal){
		
		paymentService.deletePaymentByIdentifier(paymentIdentifier, principal.getName());
		return new ResponseEntity<String>("payment was deleted", HttpStatus.OK);
	}
	
	
	
	
	
}

