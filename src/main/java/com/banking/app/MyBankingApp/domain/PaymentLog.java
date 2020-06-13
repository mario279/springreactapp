package com.banking.app.MyBankingApp.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

//you add this so that jpa knows it will be persisted
@Entity
public class PaymentLog {
	//@Id this is the primary key, Generation type IDENTITY MEANS IT uses autoincrement
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	// you will generate a sequence that makes sense IM = installment
	// for the different installments
	// in essense the payment log will be a ledger with all the payments
	private Integer IMSequence = 0;
	
	//you need the payment identifier
	private String paymentIdentifier;
	
	//we need to add the relationship with the project
	//needs to be the same name you gave to in in paymentLog
	//one to one with payment
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="payment_id", nullable=false)
	@JsonIgnore
	private Payment payment;
  
	//lets get the list of payments here
	//one to many relationship
	 //initialize the data structure here to avoid problems
	@OneToMany(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER, mappedBy="paymentlog", orphanRemoval = true )
	private List<PaymentInstallment> paymentInstallments = new ArrayList<>();

	
	
	public List<PaymentInstallment> getPaymentInstallments() {
		return paymentInstallments;
	}

	public void setPaymentInstallments(List<PaymentInstallment> paymentInstallments) {
		this.paymentInstallments = paymentInstallments;
	}

	public Integer getIMSequence() {
		return IMSequence;
	}

	public void setIMSequence(Integer iMSequence) {
		IMSequence = iMSequence;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	public PaymentLog() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Integer getInstallmentSequence() {
		return IMSequence;
	}

	public void setInstallmentSequence(Integer IMSequence) {
		IMSequence = this.IMSequence;
	}

	public String getPaymentIdentifier() {
		return paymentIdentifier;
	}

	public void setPaymentIdentifier(String paymentIdentifier) {
		this.paymentIdentifier = paymentIdentifier;
	}
	
	
	
	
}
