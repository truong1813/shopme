package com.shopme.admin.order;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.admin.setting.SettingService;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.order.Order;
import com.shopme.common.entity.order.OrderDetail;
import com.shopme.common.entity.order.OrderStatus;
import com.shopme.common.entity.order.OrderTrack;
import com.shopme.common.entity.product.Product;
import com.shopme.common.entity.setting.Setting;

@Controller
public class OrderController {

 private String defaultRedirectURL = "redirect:/orders/page/1?sortField=orderTime&sortDir=desc";
 @Autowired OrderService orderService;
 @Autowired SettingService settingService;
 
 @GetMapping("/orders")
 public String listFirstPage(Model model,HttpServletRequest request) {
	 return defaultRedirectURL;
 }
 
 @GetMapping("/orders/page/{pageNum}")
 public String listByPage(Model model,@PathVariable(name="pageNum") Integer pageNum, @Param("sortDir") String sortDir,
			@Param("sortField")String sortField, @Param("keyword") String keyword, HttpServletRequest request,
			@AuthenticationPrincipal ShopmeUserDetails loggedUser) {
	
	 Page<Order> page = orderService.listByPage(pageNum, sortDir, sortField, keyword);
	 List<Order> listOrders = page.getContent();
	 long startCount = (pageNum-1)*orderService.ORDER_BY_PAGE  + 1;
	 long endCount = startCount + orderService.ORDER_BY_PAGE - 1;
		if(endCount>page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		String reverseSortDir = sortDir.equals("asc")? "desc":"asc";
		
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("totalPages", page.getTotalPages());
		
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		
		model.addAttribute("keyword", keyword);
		model.addAttribute("modelURL", "orders");
				
		model.addAttribute("listOrders", listOrders);
		model.addAttribute("reverseSortDir", reverseSortDir);
		loadCurrencySetting(request);
		
		if(!loggedUser.hasRole("Admin")&&!loggedUser.hasRole("Salesperson")&&loggedUser.hasRole("Shipper")){
			return "orders/orders_shipper";
		}
		
	 
	 return "orders/orders";
 }
 
 @GetMapping("/orders/detail/{id}")
 public String viewOrderDetails(HttpServletRequest request,Model model, @PathVariable(name="id") Integer id,
		 RedirectAttributes ra,@AuthenticationPrincipal ShopmeUserDetails loggedUser) {
	try {
		boolean isVisibleForAdminOrSalesperson = false;
		
		if(loggedUser.hasRole("Admin")|| loggedUser.hasRole("Salesperson")) {
			isVisibleForAdminOrSalesperson = true;
		}
		
		Order order = orderService.get(id);
		loadCurrencySetting(request);
		model.addAttribute("isVisibleForAdminOrSalesperson", isVisibleForAdminOrSalesperson);
		model.addAttribute("order", order);
		return "orders/orders_details_modal";
	} catch (Exception e) {
		ra.addFlashAttribute("message", e.getMessage());
		return defaultRedirectURL;
	}
	
 }
 
 @GetMapping("/orders/delete/{id}")
 
 public String delete(@PathVariable(name="id") Integer id,RedirectAttributes ra) {
	 try {
		orderService.delete(id);
		ra.addFlashAttribute("message", "The order has been deleted successfully");
		
	} catch (orderNotFoundException e) {
		ra.addFlashAttribute("message", e.getMessage());
	}
	 return defaultRedirectURL;
 }
 
 
 @GetMapping("/orders/edit/{id}")
 public String editOrder(@PathVariable(name="id") Integer id, Model model, RedirectAttributes ra) {
	 try {
		Order order = orderService.get(id);
		
		List<Country> listCountries = orderService.listCountries(); 
		List<Setting> settings = settingService.getCurrencySettings();
		settings.forEach(setting ->{
			
			model.addAttribute(setting.getKey(), setting.getValue());
		});
		model.addAttribute("listCountries", listCountries);
		model.addAttribute("order", order);
		model.addAttribute("pageTitle", "Edit order with Id: " + id);
		return "orders/order_form"; 
	} catch (orderNotFoundException e) {
		ra.addFlashAttribute("message", e.getMessage());
		return defaultRedirectURL;
	}
 }
 
 @PostMapping("/orders/save")
 public String saveOrder(Order order, HttpServletRequest request, RedirectAttributes ra) {
	 String countryName = request.getParameter("countryName");
	 order.setCountry(countryName);
	 
	 updateProductDetails(order,request);
	 updateOrderTracks(order,request);
	 
	 orderService.save(order);
	 ra.addFlashAttribute("message", "The order has been updated successfully");
	 return defaultRedirectURL;
 }
 private void updateOrderTracks(Order order, HttpServletRequest request) {
	 String [] trackIds = request.getParameterValues("trackId");
	 String [] trackStatuses= request.getParameterValues("trackStatus");
	 String [] trackNotes = request.getParameterValues("trackNotes");
	 String [] trackDates = request.getParameterValues("trackDate");
	
	List<OrderTrack> orderTracks = order.getOrderTracks();
	DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
	for(int i=0; i<trackIds.length; i++) {
		OrderTrack orderTrack = new OrderTrack();
		Integer trackId = Integer.parseInt(trackIds[i]);
		 
		 if(trackId > 0) {
			 orderTrack.setId(trackId);
		 }
		 orderTrack.setOrder(order);
		 orderTrack.setNotes(trackNotes[i]);
		 orderTrack.setStatus(OrderStatus.valueOf(trackStatuses[i]));
		 
		 try {
			 orderTrack.setUpdateTime(dateFormatter.parse(trackDates[i]));
		} catch (ParseException e) {
			e.printStackTrace();
		}
				 
		 orderTracks.add(orderTrack);
		 
	}
}

private void updateProductDetails(Order order, HttpServletRequest request) {
	String [] detailIds = request.getParameterValues("detailId");
	String [] productIds = request.getParameterValues("productId");
	String [] productDetailCosts = request.getParameterValues("productDetailCost");
	String [] quantities = request.getParameterValues("quantity");
	String [] productPrices = request.getParameterValues("productPrice");
	String [] productSubtotals = request.getParameterValues("productSubtotal");
	String [] productShipCosts = request.getParameterValues("productShipCost");
	
	Set<OrderDetail> orderDetails = order.getOrderDetail();
	
	for(int i=0;i<detailIds.length;i++) {
		
		OrderDetail orderDetail = new OrderDetail();
		Integer detailId = Integer.parseInt(detailIds[i]);
		if(detailId > 0) {
			orderDetail.setId(detailId);
		}
		orderDetail.setOrder(order);
		orderDetail.setProduct(new Product(Integer.parseInt(productIds[i])));
		orderDetail.setProductCost(Float.parseFloat(productDetailCosts[i]));
		
		orderDetail.setQuantity(Integer.parseInt(quantities[i]));
		orderDetail.setShippingCost(Float.parseFloat(productShipCosts[i]));
		
		orderDetail.setUnitPrice(Float.parseFloat(productPrices[i]));
		orderDetail.setSubTotal(Float.parseFloat(productSubtotals[i]));
		
		orderDetails.add(orderDetail);
		
	}
}

private void loadCurrencySetting(HttpServletRequest request) {
	 List<Setting> listCurrencySettings = settingService.getCurrencySettings();
	 for(Setting setting : listCurrencySettings) {
		 request.setAttribute(setting.getKey(), setting.getValue());
	 }
	 
 }
 
 
}
