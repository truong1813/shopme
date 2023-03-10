package com.shopme.common.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="customers")
public class Customer extends AbstractAddress {
	
	@Column(nullable = false, length = 45)
	private String email;
	@Column(nullable = false,unique = true, length = 64)
	private String password;
		
	@Column(name="verification_code" , length = 64)
	private String verificationCode;
	
	private boolean enabled;
	
	@Column(name="created_time")
	private Date createdTime;
		
	@ManyToOne
	@JoinColumn(name="country_id")
	private Country country ;
	@Enumerated(EnumType.STRING)
	@Column(name="authentication_type", length =10)
	private AuthenticationType authenticationType;
	@Column(name="reset_password_token", length = 30)
	private String resetPasswordToken;

	public Customer() {
		
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Date getCreatedTime() {
		return createdTime;
	}


	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}


	public Country getCountry() {
		return country;
	}


	public void setCountry(Country country) {
		this.country = country;
	}


	public AuthenticationType getAuthenticationType() {
		return authenticationType;
	}


	public void setAuthenticationType(AuthenticationType authenticationType) {
		this.authenticationType = authenticationType;
	}
	


	public String getResetPasswordToken() {
		return resetPasswordToken;
	}


	public void setResetPasswordToken(String resetPasswordToken) {
		this.resetPasswordToken = resetPasswordToken;
	}


	@Override
	public String toString() {
		return "Customer [id=" + id + ", email=" + email + ", firstName=" + firstName + ", lastName=" + lastName + "]";
	}
	
	public String getFullName() {
		return firstName + " " + lastName;
	}
	@Transient
	public String getAddress() {
		String address= firstName;
		if(lastName !=null && !lastName.isEmpty()) address += " " + lastName;
		if(!addressLine1.isEmpty()) address += ", " + addressLine1;
		if(addressLine2 !=null && !addressLine2.isEmpty()) address += " " + addressLine2;
		if(!city.isEmpty()) address += city.equals(state)? "": ", " + city;
		if(state !=null && !state.isEmpty()) address += ", " + state;
		
		address += ", " + country.getName();
		if(postalCode !=null && !postalCode.isEmpty()) address += ". Postal Code: " + postalCode;
		if(!phoneNumber.isEmpty()) address += " . Phone Number: " + phoneNumber;
		return address;
	}

}
