package com.shopme.order;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.Utility;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.customer.CustomerService;

@RestController
public class OrderRestController {
	@Autowired OrderService orderService;
	@Autowired CustomerService customerService;
	
	@PostMapping("/orders/return")
	
	public ResponseEntity<?> handleOrderReturnRequest(@RequestBody OrderReturnRequest returnRequest,
			HttpServletRequest request) {
		Customer customer = null;
		try {
			 customer = getAuthenticatedCustomer(request);
			
		} catch (CustomerNotFoundException ex) {
			return new  ResponseEntity<>("Authencation required", HttpStatus.BAD_REQUEST); //trả về một phản hồi " Authencation required  " với mã trạng thái http là bad_required.
		}
		try {
			orderService.setOrderReturnRequested(returnRequest, customer);
		} catch (orderNotFoundException ex) {
			return new  ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
		}
		
		return  new ResponseEntity<>(returnRequest.getOrderId(),HttpStatus.OK);
	}
	
	private Customer getAuthenticatedCustomer( HttpServletRequest request) throws CustomerNotFoundException {
		String email =  Utility.getEmailOfAuthenticatedCustomer(request);
		if(email == null) {
			throw new CustomerNotFoundException("Not authencated customer") ;
		}
		return customerService.getCustomerByEmail(email);
	}
}
