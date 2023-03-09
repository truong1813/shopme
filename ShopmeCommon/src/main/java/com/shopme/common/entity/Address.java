package com.shopme.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="addresses")
public class Address extends AbstractAddress {
	
	
	@Column(name="default_address")
	private boolean defaultForShipping;
	@ManyToOne
	@JoinColumn(name="country_id")
	private Country country;
	@ManyToOne
	@JoinColumn(name="customer_id")
	private Customer customer;
			
	public boolean isDefaultForShipping() {
		return defaultForShipping;
	}
	public void setDefaultForShipping(boolean defaultForShipping) {
		this.defaultForShipping = defaultForShipping;
	}
	public Country getCountry() {
		return country;
	}
	public void setCountry(Country country) {
		this.country = country;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
		
	@Override
	public String toString() {
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
