package com.shopme.order;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shopme.Utility;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.order.Order;
import com.shopme.common.entity.product.Product;
import com.shopme.customer.CustomerService;

@Controller
public class OrderController {
	@Autowired  private OrderService orderService;
	@Autowired private CustomerService customerService;
	@GetMapping("/orders")
	public String listFirstPage(Model model,HttpServletRequest request) {
		return listOrderByPage(model, request, 1, "orderTime", "desc", null);
	}
	@GetMapping("/orders/page/{pageNum}")
	public String listOrderByPage(Model model, HttpServletRequest request, 
			@PathVariable(name="pageNum") int pageNum, String sortField, String sortDir, String orderKeyword) {
		Customer customer = getAuthenticatedCustomer(request);
		Page<Order>  page = orderService.listForCustomerByPage(sortField, sortDir, orderKeyword,pageNum, customer);
		
		List<Order> listOrders= page.getContent();
		String reverseSortDir=sortDir.equals("asc")? "desc":"asc";
		long startCount = (pageNum-1)*orderService.ORDERS_PER_PAGE +1;
		long endCount = startCount +orderService.ORDERS_PER_PAGE-1;
		if(endCount > page .getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("listOrders", listOrders);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("sortField", sortField);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("orderKeyword", orderKeyword);
		model.addAttribute("modelURL", "orders");
		
		return "orders/orders_customer";
	}
	
	@GetMapping("/orders/detail/{id}")
	public String viewOrderDetails(Model model,
			@PathVariable(name="id") Integer id, HttpServletRequest request) {
		Customer customer = getAuthenticatedCustomer(request);
		Order  order= orderService.getOrderByIdAndCustomer(id, customer);
		model.addAttribute("order", order);
		
		return "orders/order_details_modal";
	}
	private  Customer getAuthenticatedCustomer( HttpServletRequest request) {
		String email = Utility.getEmailOfAuthenticatedCustomer(request);
		return customerService.getCustomerByEmail(email);
	}
}
