package com.shopme.admin.customer;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.setting.country.CountryRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;

@Controller
public class CustomerController {

	@Autowired CustomerService service;
	@Autowired  CountryRepository countryRepo;
	@GetMapping("/customers")
	public  String listFirstPage(Model model) {
		return listByPage(1, "firstName", "asc", null, model);
	}
	
	@GetMapping("/customers/page/{pageNum}")
	public String listByPage(@PathVariable(name="pageNum") Integer pageNum, @Param("sortField") String sortField,
			@Param("sortDir") String sortDir, @Param("keyword") String keyword, Model model) {
		Page<Customer> page = service.listByPage(pageNum, sortField, sortDir, keyword);
		List<Customer> listCustomers = page.getContent();
		
		long startCount = (pageNum-1)*service.CUSTOMER_PER_PAGE +1;
		long endCount = startCount +service.CUSTOMER_PER_PAGE -1;
		if(endCount>page.getTotalElements()) {
			endCount=page.getTotalElements();
		}
		
		String reverseSortDir = sortDir.equals("asc")? "desc":"asc";
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("listCustomers", listCustomers);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("modelURL", "customers");
		model.addAttribute("pageTitle", "Customer Manager");
		
		return "customers/customers";
	}
	@GetMapping("customers/{id}/enabled/{status}")
	
	public String updateCustomerEnabledStatus(@PathVariable(name ="id") Integer id, @PathVariable(name="status") boolean enabled,
			RedirectAttributes ra) {
		
		service.updateStatusEnabled(id, enabled);
		String status = enabled?"enabled":"disabled";
		ra.addFlashAttribute("message", "The Customer with Id "+ id + " has been " + status);
		
		return "redirect:/customers";
	}
	
	@GetMapping("/customers/delete/{id}")
	public String delete(@PathVariable(name="id") Integer id, RedirectAttributes ra) throws CustomerNotFoundException {
		try {
			service.delete(id);
			ra.addFlashAttribute("message", "The customer with id " +id + " has been deleted successfully.");	
		} catch (CustomerNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
		}
				
		return "redirect:/customers";
	}
	
	@PostMapping("/customers/save")
	public String saveCustomer(Customer customer, RedirectAttributes ra) {
		service.updateCustomer(customer);
		ra.addFlashAttribute("message", String.format("The Customer (ID:%d) has been updated successfully", customer.getId()));
		return "redirect:/customers";
	}
	
	@GetMapping("/customers/edit/{id}")
	public String editCustomer(@PathVariable(name="id")Integer id, Model model, RedirectAttributes ra) {
		try {
			Customer  customer = service.getId(id); 
			List<Country> listCountries = countryRepo.findAllByOrderByNameAsc();
			
			model.addAttribute("customer", customer);
			model.addAttribute("listCountries", listCountries);
			model.addAttribute("pageTitle", String.format("Edit customer (ID:%d)", id));
			
			return "customers/customer_form";
		} catch (Exception e) {
			ra.addFlashAttribute("message", e.getMessage());
			return "redirect:/customers";
		}
		
	}
	
	@GetMapping("/customers/detail/{id}")
	public String viewDetailsCustomer(@PathVariable(name="id") Integer id, Model model, RedirectAttributes ra) {
		try {
			Customer customer = service.getId(id);
			model.addAttribute("customer", customer);
			return "customers/customer_details_modal";
		} catch (CustomerNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			return "redirect:/customers";
		}
		
		
	}
}
