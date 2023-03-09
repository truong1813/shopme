package com.shopme.address;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.Utility;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.country.CountryRepository;
import com.shopme.customer.CustomerService;
import com.shopme.shipping.ShippingRateService;

@Controller

public class AddressController {
	@Autowired private AddressService addressService;
	@Autowired private CustomerService customerService;
	@Autowired private CountryRepository countryRepo;
	
	@GetMapping("/address_book")
	public String showAddressBook(Model model, HttpServletRequest request) throws CustomerNotFoundException {
		Customer customer = getAuthenticatedCustomer(request);
		List<Address> listAddresses = addressService.listByCustomer(customer);
		
		boolean usePrimaryAddressAsDefault = true;
		
		for( Address address : listAddresses) {
						
			if(address.isDefaultForShipping()) {
				usePrimaryAddressAsDefault = false;
			
				break;
			}
		}
		
		model.addAttribute("listAddresses", listAddresses);
		model.addAttribute("customer", customer);
		model.addAttribute("usePrimaryAddressAsDefault", usePrimaryAddressAsDefault);
		return "address_book/address_book";
	}
	
	@GetMapping("/address_book/new")
	public String newAddress( Model model) {
		
		List<Country> listCountries = countryRepo.findAllByOrderByNameAsc();
		model.addAttribute("address", new Address());
		model.addAttribute("listCountries", listCountries);
		model.addAttribute("pageTitle", "New Your Address");
		return "address_book/address_book_form";
	}
	
	@PostMapping("/address_book/save")
	public String save(Address address, HttpServletRequest request, RedirectAttributes ra, Model model) throws CustomerNotFoundException {
		Customer customer = getAuthenticatedCustomer(request);
		address.setCustomer(customer);
		addressService.save(address);
		ra.addFlashAttribute("message", "The address has been saved successfully");
		

		String redirectOption = request.getParameter("redirect");
		String redirectURL = "redirect:/address_book";
				
		
		if("checkout".equals(redirectOption)) {
			redirectURL += "?redirect=checkout";
		}
		
		
		return redirectURL;
	}
	@GetMapping("/address_book/edit/{id}")
	
	public String edit(@PathVariable(name= "id") Integer id, HttpServletRequest request,RedirectAttributes ra,Model model) throws CustomerNotFoundException {
		
		List<Country> listCountries = countryRepo.findAllByOrderByNameAsc();
		Customer customer = getAuthenticatedCustomer(request);
		Address address = addressService.get(id, customer);
		model.addAttribute("address", address);
		model.addAttribute("listCountries", listCountries);
		model.addAttribute("pageTitle", "Edit Address with ID: " + id);
		
		return "address_book/address_book_form";
	}
	
	
	@GetMapping("/address_book/delete/{id}")
		public String delete(@PathVariable(name= "id") Integer id, HttpServletRequest request,RedirectAttributes ra) throws CustomerNotFoundException {
		Customer customer = getAuthenticatedCustomer(request);
		addressService.delete(id, customer);
		ra.addFlashAttribute("message", "The address has been deleted successfully");
		return "redirect:/address_book";
	}
	
	
	
	private  Customer getAuthenticatedCustomer( HttpServletRequest request) throws CustomerNotFoundException {
		String email = Utility.getEmailOfAuthenticatedCustomer(request);
		if(email == null) {
			throw new CustomerNotFoundException("No authenticated customer");
		}
		return customerService.getCustomerByEmail(email);
	}
	
	@GetMapping("/address_book/default/{id}")
	
	public String defaultAddress(@PathVariable(name= "id") Integer id, HttpServletRequest request,RedirectAttributes ra) throws CustomerNotFoundException {
		Customer customer = getAuthenticatedCustomer(request);
		addressService.setDefaultAddress(id,customer);
		
		String redirectOption = request.getParameter("redirect");
		String redirectURL = "redirect:/address_book";
		
		if("cart".equals(redirectOption)) {
			redirectURL = "redirect:/cart";
		}else if("checkout".equals(redirectOption)) {
			redirectURL = "redirect:/checkout";
			
		}
		
		return redirectURL;
		
		}
	
}
