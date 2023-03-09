package com.shopme.admin.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerRestController {
	@Autowired CustomerService service;
	
	@PostMapping("customers/check_unique")
	public String checkEmailUnique( Integer id,String email ) {
		return service.checkEmailUnique(id, email)?"OK": "Duplicated";
	}
}
