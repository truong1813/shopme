package com.shopme.admin.customer;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.admin.user.UserNotFoundException;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;


@Service
@Transactional
public class CustomerService {
	public static final int CUSTOMER_PER_PAGE =10;
	@Autowired private CustomerRepository repo;
	@Autowired private PasswordEncoder passwordEncoder;
	public List<Customer> lisAll(){
		return (List<Customer>) repo.findAll();
	}
	public Page<Customer> listByPage(Integer pageNum,String sortField, String sortDir, String keyword){
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc")? sort.ascending(): sort.descending();
		Pageable pageable = PageRequest.of(pageNum-1, CUSTOMER_PER_PAGE, sort);
		
		if(keyword !=null) {
			return repo.findAllByKeyword(keyword, pageable);
		}else {
			return repo.findAll(pageable);
		}
	}
	
	public void updateStatusEnabled(Integer id, boolean enabled) {
		repo.updateStatusEnabled(id, enabled);
	}
	
	public Customer getId(Integer id) throws CustomerNotFoundException {
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new CustomerNotFoundException("Could not find customer with id:" + id);
		}
	}
	
	public boolean checkEmailUnique(Integer id, String email) {
		Customer customer = repo.findByEmail(email);
		if(customer !=null && customer.getId()!=id) return false;
		
		return true;
	}
	
	public void delete(Integer id) throws CustomerNotFoundException {
		Long count = repo.countById(id);
		if(count ==null ||count==0) {
			throw new CustomerNotFoundException("Could not find customer with Id " +id);
		}
		repo.deleteById(id);
	}
	
	private void encodePassword(Customer customer) {
		String password = passwordEncoder.encode(customer.getPassword());
		customer.setPassword(password);
	}
	
	public void updateCustomer(Customer customerInForm) {
		Customer customerDB = repo.findById(customerInForm.getId()).get();
		if(!customerInForm.getPassword().isEmpty()) {
			encodePassword(customerInForm);
			
		}else {
			
			customerInForm.setPassword(customerDB.getPassword());
		}
		customerInForm.setEnabled(customerDB.isEnabled());
		customerInForm.setCreatedTime(customerDB.getCreatedTime());
		customerInForm.setVerificationCode(customerDB.getVerificationCode());
		customerInForm.setAuthenticationType(customerDB.getAuthenticationType());
		customerInForm.setResetPasswordToken(customerDB.getResetPasswordToken());
		repo.save(customerInForm);
	}
}
