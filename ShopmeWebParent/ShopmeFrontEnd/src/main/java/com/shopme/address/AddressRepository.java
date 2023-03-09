package com.shopme.address;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Customer;
@Repository
public interface AddressRepository extends CrudRepository<Address, Integer> {
	
	public List<Address> findByCustomer(Customer customer);
	
	@Query("SELECT a FROM Address a WHERE a.id= ?1 AND a.customer = ?2")
	public Address findByIdAndCustomer(Integer id, Customer customer);
	
	@Query("DELETE FROM Address a WHERE a.id= ?1 AND a.customer = ?2")
	@Modifying
	public void deleteByIdAndCustomer(Integer id, Customer customer);
	
	@Query("UPDATE Address a SET a.defaultForShipping = true WHERE a.id = ?1 ")
	@Modifying
	public void setDefaultAddress(Integer id);
	
	@Query("UPDATE Address a SET a.defaultForShipping = false WHERE a.id != ?1 AND a.customer.id = ?2")
	@Modifying
	public  void setNonDefaultForOthers(Integer addressID, Integer customerId);
	
	@Query("SELECT a FROM Address a WHERE a.customer.id = ?1 AND a.defaultForShipping = true")
	public Address findDefaultByCustomer(Integer customerId);
}
