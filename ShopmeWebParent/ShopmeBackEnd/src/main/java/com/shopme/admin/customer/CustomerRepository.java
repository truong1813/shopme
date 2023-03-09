package com.shopme.admin.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.shopme.common.entity.Customer;

public interface CustomerRepository extends PagingAndSortingRepository<Customer, Integer> {
	@Query("SELECT c FROM Customer c WHERE c.email = ?1")
	public Customer findByEmail(String email);
	
	@Query("SELECT c FROM Customer c WHERE CONCAT(c.id, ' ', c.firstName, ' ', c.lastName, '', c.email, ' ',"
			+ "c.addressLine1,' ', c.addressLine2,' ', c.city,' ', c.state,' ', c.country) LIKE %?1%")
	public Page<Customer> findAllByKeyword(String keyword, Pageable pageable);
	
	public long countById(Integer id);
	
	@Query("UPDATE Customer c SET c.enabled = ?2 WHERE c.id = ?1")
	@Modifying
	public void updateStatusEnabled(Integer id, boolean enabled);
}
