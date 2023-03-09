package com.shopme.address;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Customer;

@Service
@Transactional
public class AddressService {
	@Autowired private AddressRepository repo;
	public List<Address> listByCustomer(Customer customer){
		return repo.findByCustomer(customer);
	}
	
	public void save(Address address) {
		repo.save(address);
	}
	
	public Address get(Integer id,Customer customer) {
		return repo.findByIdAndCustomer(id, customer);
	}
	
	public void delete(Integer id, Customer customer) {
		repo.deleteByIdAndCustomer(id, customer);
	}
	
	public void setDefaultAddress(Integer addressId, Customer customer) {
		if(addressId >0) {
			repo.setDefaultAddress(addressId);
		}
		
		repo.setNonDefaultForOthers(addressId, customer.getId());
	}
	
	public Address getDefaultAddress(Customer customer) {
		return repo.findDefaultByCustomer(customer.getId());
	}
	
}
