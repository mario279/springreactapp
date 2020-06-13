 package com.banking.app.MyBankingApp.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Payment {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private long id;
	
	//setup attributes and rules for validation
	
	//we don't want a blank project name
	@NotBlank(message="you either need to make a change or enter a value")
	private String paymentName;
	
	@NotBlank(message="you either need to make a change or enter a value")
	@Size(min=4, max=5, message="please use any 4-5 characters")
	@Column(updatable=false, unique=true)
	private String identifier;
	
	@NotBlank(message="you either need to make a change or enter a value")
	private String description;
	
	@JsonFormat(pattern="mm-dd-yy")
	private Date startDate;
	@JsonFormat(pattern="mm-dd-yy")
	private Date endDate;
	@JsonFormat(pattern="mm-dd-yy")
	@Column(updatable=false)
	private Date createdOn;
	@JsonFormat(pattern="mm-dd-yy")
	private Date updatedOn;
	@JsonFormat(pattern="10")
	private int Updates;
	
	@Lob
	private byte[] image;
//	
////	byte[] byteImage = image.getBytes();
	
	public byte[] getImage() {
		return image;
	}


	public void setImage(byte[] image) {
		this.image = image;
	}

	//adding a payment log
	//mappedBy needs to have the same name as the name you will give the project
	//object of the paymentLog
	//@JsonIgnore excludes this member from being serialized into json
	@OneToOne(fetch= FetchType.EAGER, cascade = CascadeType.ALL, mappedBy="payment")
	@JsonIgnore
	private PaymentLog paymentLog;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private User user;
	
	private String personInChargeOfPayment;
	
	
	public String getPersonInChargeOfPayment() {
		return personInChargeOfPayment;
	}

	
	public void setPersonInChargeOfPayment(String personInChargeOfPayment) {
		this.personInChargeOfPayment = personInChargeOfPayment;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public PaymentLog getPaymentLog() {
		return paymentLog;
	}

	public void setPaymentLog(PaymentLog paymentLog) {
		this.paymentLog = paymentLog;
	}

	public void setUpdates(int updates) {
		Updates = updates;
	}
	
	public int getUpdates() {
		return Updates;
	}
	public void addUpdate() {
		Updates++;
		System.out.println(Updates);
		
	}
	//every time we create an object, it will store the date
//	@PrePersist
	protected void uponCreation() {
		
		this.createdOn = new Date();
	}
	
	//everytime we update the object it will store the date
//	@PreUpdate
	protected void uponUpdate() {
		this.updatedOn = new Date();
	}
	
	public Payment() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPaymentName() {
		return paymentName;
	}

	public void setPaymentName(String projectName) {
		this.paymentName = projectName;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String paymentIdentifier) {
		this.identifier = paymentIdentifier;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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
	
	
	
	
	
	
}
