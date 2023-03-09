package com.shopme.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="states")
public class States extends idBaseEntity{
	
	@Column(nullable = false, length = 45)
	private String name;
	@ManyToOne
	@JoinColumn(name="country_id")
	private Country country;
		
	public States() {
		
	}
	
	
	public States(String name, Country country) {
		
		this.name = name;
		this.country = country;
	}


	public States(Integer id, String name, Country country) {
	
		this.id = id;
		this.name = name;
		this.country = country;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Country getCountry() {
		return country;
	}
	public void setCountry(Country country) {
		this.country = country;
	}
	
	
}
