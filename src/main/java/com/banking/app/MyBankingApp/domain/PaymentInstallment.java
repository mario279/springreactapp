package com.banking.app.MyBankingApp.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.ManyToAny;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class PaymentInstallment {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "please include an installment name")
	private String name;
	@Column(updatable = false, unique= true)
	private String paymentSequence;
	private String paymentForm;
	private String description;
	private Integer priority;
	private String status;
	@JsonFormat(pattern = "yyyy-mm-dd")
	private Date dueDate;
	@JsonFormat(pattern = "yyyy-mm-dd")
	private Date createdOn;
	@JsonFormat(pattern = "yyyy-mm-dd")
	private Date updatedOn;
    //MANY TO ONE WITH BACKLOG
	// we use refresh here, because we are telling the db to update itself
	// because we can't delete the parent of this, which is payment log in the
	//case we delete the installment
	@ManyToOne(fetch =FetchType.EAGER)
	@JoinColumn(name="paymentlog_Id",updatable=false, nullable=false)
	@JsonIgnore
	private PaymentLog paymentlog;
	//a payment installment has a many to one relationship with payment log
	//meaning there can be many installments for only one log, but only one log
	@Column(updatable=false)
	private String paymentIdentifier;
	
	//before we save it to the db we need to initialize the createdon date
	//its protected because we only want the same family to access it on the same package
//if you want to access it outside the package, you need a child
	@PrePersist
	protected void onCreate() {
		this.createdOn = new Date();
	}
	
	@PreUpdate
	protected void onUpdate() {
		this.updatedOn = new Date();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPaymentSequence() {
		return paymentSequence;
	}

	public void setPaymentSequence(String paymentSequence) {
		this.paymentSequence = paymentSequence;
	}

	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPaymentForm() {
		return paymentForm;
	}

	public void setPaymentForm(String paymentForm) {
		this.paymentForm = paymentForm;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public PaymentLog getPaymentlog() {
		return paymentlog;
	}

	public void setPaymentlog(PaymentLog paymentlog) {
		this.paymentlog = paymentlog;
	}

	public String getPaymentIdentifier() {
		return paymentIdentifier;
	}

	public void setPaymentIdentifier(String paymentIdentifier) {
		this.paymentIdentifier = paymentIdentifier;
	}
	
	
}
