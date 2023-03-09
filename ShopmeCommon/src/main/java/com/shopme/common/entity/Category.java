package com.shopme.common.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.shopme.common.Constants;

@Entity
@Table(name="categories")
public class Category extends idBaseEntity{
	
	@Column(length=128, nullable = false, unique = true)
	private String name;
	@Column(length=64, nullable = false, unique = true)
	private String alias;
	@Column(length=128, nullable = false)
	private String image;
	@Column(name="all_parent_ids", nullable = true, length = 255)
	private String allParentIDs;
	private boolean enabled;
	@OneToOne
	@JoinColumn(name="parent_id")
	private Category parent;
	@OneToMany(mappedBy="parent")
	@OrderBy("name asc")
	private Set<Category> children = new HashSet<>();
	
	
	public Category() {
		
	}
	
	public Category(Integer id) {
	
		this.id = id;
	}

	public Category(String name) {
		this.name =  name;
	
	}
	public Category(Integer id, String name) {
		this.id = id;
		this.name = name;
		this.alias = name;
		this.image= "default.png";
	}
	
	public Category(String name, Category parent) {
		
		this.name = name;
		this.parent = parent;
		this.alias = name;
		this.image= "default.png";
	}

	public Category(Integer id, String name, String alias, String image, boolean enabled, Category parent) {
		
		this.id = id;
		this.name = name;
		this.alias = alias;
		this.image = image;
		this.enabled = enabled;
		this.parent = parent;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public Category getParent() {
		return parent;
	}
	public void setParent(Category parent) {
		this.parent = parent;
	}
	public Set<Category> getChildren() {
		return children;
	}
	public void setChildren(Set<Category> children) {
		this.children = children;
	}
	
	
	public String getAllParentIDs() {
		return allParentIDs;
	}


	public void setAllParentIDs(String allParentIDs) {
		this.allParentIDs = allParentIDs;
	}


	@Transient
	private boolean hasChildren;
		
	public boolean isHasChildren() {
		return hasChildren;
	}


	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}


	@Override
	public String toString() {
		return this.name;
	}


	@Transient
	public String getImagePath() {
		if(id==null|| image==null) return "/images/default-user.png";
		return  Constants.S3_BUCKET_URI + "/category-images/" + this.id +"/" + this.image;
	}
	
	public static Category copyIdName(Category category) {
		Category copyCategory = new Category();
		copyCategory.setId(category.getId());
		copyCategory.setName(category.getName());
		return copyCategory;
	}
	public static Category copyIdName( Integer id,String name) {
		Category copyCategory = new Category();
		copyCategory.setId(id);
		copyCategory.setName(name);
		return copyCategory;
	}
	
	
	public static Category copyAll(Category category) {
		Category copyCategory  =new Category();
		copyCategory.setId(category.getId());
		copyCategory.setName(category.getName());
		copyCategory.setAlias(category.getAlias());
		copyCategory.setEnabled(category.isEnabled());
		copyCategory.setImage(category.getImage());
		copyCategory.setHasChildren(category.getChildren().size()>0);
		return copyCategory;
	}
	public static Category copyAll(Category category ,String name) {
		Category copyCategory  = new Category();
		copyCategory.setId(category.getId());
		copyCategory.setName(name);
		copyCategory.setAlias(category.getAlias());
		copyCategory.setEnabled(category.isEnabled());
		copyCategory.setImage(category.getImage());
		copyCategory.setParent(category.getParent());
		copyCategory.setChildren(category.getChildren());
		copyCategory.setHasChildren(category.getChildren().size()>0);
		return copyCategory;
	}
	
}
