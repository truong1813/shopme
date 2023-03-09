package com.shopme.common.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.transaction.Transactional;

import com.shopme.common.Constants;

@Entity
@Table(name="brands")
public class Brand extends idBaseEntity {
	
	@Column(length = 64, nullable = false, unique = true)
	private String name;
	@Column(length = 128, nullable = false)
	private String logo;
	@ManyToMany
	@JoinTable(name="brands_categories",
				joinColumns= @JoinColumn(name="brand_id"),
				inverseJoinColumns= @JoinColumn(name="category_id")
			)
	
	private Set<Category> categories = new HashSet<>();
		
	public Brand() {
		
	}
	
	public Brand(String name) {
		
		this.name = name;
	}
	
	
	public Brand(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public Set<Category> getCategories() {
		return categories;
	}
	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	@Transactional
	public String getLogoImagePath( ) {
		
			if(id==null|| logo==null) return "/images/image-thumbnail.png";
			return  Constants.S3_BUCKET_URI + "/brand-logos/" + this.id +"/" + this.logo;
		}
	}

