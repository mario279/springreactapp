package com.banking.app.MyBankingApp.services;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.banking.app.MyBankingApp.domain.Payment;
import com.banking.app.MyBankingApp.domain.PaymentLog;
import com.banking.app.MyBankingApp.domain.User;
import com.banking.app.MyBankingApp.exceptions.IdException;
import com.banking.app.MyBankingApp.exceptions.PaymentNotFoundException;
import com.banking.app.MyBankingApp.repos.PaymentLogRepo;
import com.banking.app.MyBankingApp.repos.PaymentRepo;
import com.banking.app.MyBankingApp.repos.UserRepo;
import com.google.common.collect.Lists;


@Service
public class PaymentService {

	@Autowired
	private PaymentRepo paymentRepository;
	
	//autowire the backlong repository onto the service, inject it as a dependency
	//obviously because we depende on it
	@Autowired
	private PaymentLogRepo paymentlogrepo;
	
	//bean injection
//	@Bean and @Autowired do two very different things. The other answers here explain in a little more detail, but at a simpler level:
//
//		@Bean tells Spring 'here is an instance of this class, please keep hold of it and give it back to me when I ask'.
//
//		@Autowired says 'please give me an instance of this class, for example, one that I created with an @Bean annotation earlier'.
//
//		Does that make sense? In your first example, you're asking Spring to give you an instance of BookingService, but you're never creating one, so Spring has nothing to give you. In your second example, you're creating a new instance of BookingService, telling Spring about it, and then, in the main() method, asking for it back.
	
	@Autowired
	private UserRepo userRepo;
	
	public Payment saveOrUpdateProject(Payment p, String username) {
		
//		if(paymentRepository.findPaymentByIdentifier(p.getIdentifier())!=null) {
//			Payment paymentToUpdate = paymentRepository.findPaymentByIdentifier(p.getIdentifier());
//			paymentToUpdate.setDescription(p.getDescription());
//			paymentToUpdate.setPaymentName(p.getPaymentName());
//			return paymentRepository.save(paymentToUpdate);
//		} else
			
		if(p.getId()!= 0) {
			Payment currentPayment = paymentRepository.findPaymentByIdentifier(p.getIdentifier());
			
               System.out.println("this is the id dude, inside save or update: "+ p.getId()+" : "+p.getDescription());
			if(currentPayment!=null && (!currentPayment.getPersonInChargeOfPayment().equals(username) )){
				throw new PaymentNotFoundException("This payment is not in your account");
			}
			
			else if(currentPayment==null) {
				throw new PaymentNotFoundException("payment with id: "+ p.getIdentifier() +" cannot be updated");
			}
			
		
		}
		
		//breakpoint
			try {
				User user = userRepo.findByUsername(username);
				System.out.println("this is the username passed: "+ user.getFullName());
				
				p.setUser(user);
				System.out.println("it did set the userr");
				
				p.setPersonInChargeOfPayment(user.getUsername());
				System.out.println("it did setperson in charge");
				
			    p.setIdentifier(p.getIdentifier().toUpperCase());
			    
			    
//			    if(p.getId()==null) {
//			    	PaymentLog paymentlog = new PaymentLog();
//			    	p.setPaymentlog(paymentlog);
//			    	paymentlog.setPayment(p);
//			    	paymentlog.setPaymentIdentifier(p.getIdentifier().toUpperCase());
//			    }
			//lets check if its the first time we are creating a payment. 
			if(p.getId()== 0) {
				System.out.println("first time you created a project");
				//create new payment log as the id is null because there is non
				PaymentLog paymentlog = new PaymentLog();
				
				//create the relationship with the paymentlog
				p.setPaymentLog(paymentlog);
				 
				//now set relationship in payment
				paymentlog.setPayment(p);
				paymentlog.setPaymentIdentifier(p.getIdentifier().toUpperCase());
			}
			if(p.getId()!=0) {
				System.out.println("im inside the function to set the payment log");
				p.setPaymentLog(paymentlogrepo.findBypaymentIdentifier(p.getIdentifier().toUpperCase()));
			}
			
			return paymentRepository.save(p);
		}
		catch(Exception e) {
			throw new IdException("this project id is already in use "+p.getIdentifier().toUpperCase());
		}
	}
	
	public Payment findPaymentByIdentifier(String Identifier, String username) {
		Payment payment = paymentRepository.findPaymentByIdentifier(Identifier.toUpperCase()); 
		
		if(payment==null) {
			throw new IdException("There is no payment with such Idenfier ");
		}
		
		if(!payment.getPersonInChargeOfPayment().equals(username)) {
			throw new PaymentNotFoundException("This payment is not in your account");
		}
		return payment;
	}
	//example of method overloading
	public Payment findPaymentByIdentifier(String Identifier) {
		Payment payment = paymentRepository.findPaymentByIdentifier(Identifier.toUpperCase()); 
		
		if(payment==null) {
			throw new IdException("There is no payment with such Idenfier ");
		}
		
		
		return payment;
	}
	
	public void deletePaymentByIdentifier(String Identifier, String username) {
		//first find the payment
//		Payment paymentToDelete = paymentRepository.findPaymentByIdentifier(Identifier.toUpperCase());
//		if(paymentToDelete==null) {
//			throw new IdException("can't delete payment which dosen't exist");
//		}
//		else {
//			paymentRepository.delete(paymentToDelete);
//		}
		
		paymentRepository.delete(findPaymentByIdentifier(Identifier, username));
	}
	
	public Iterable<Payment> findAll(String username){
		
//		List<Payment> allPayments;
		
		
		return paymentRepository.findAllByPersonInChargeOfPayment(username);
		
				
	}
	
//public List<Payment> findAll(){
//		
//		List<Payment> allPayments;
//		
//		
//		allPayments = Lists.newArrayList(paymentRepository.findAll());
//		return allPayments;
//				
//	}	
	
}
