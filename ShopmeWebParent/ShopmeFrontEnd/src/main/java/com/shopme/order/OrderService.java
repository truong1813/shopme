package com.shopme.order;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.checkout.CheckoutInfo;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.order.Order;
import com.shopme.common.entity.order.OrderDetail;
import com.shopme.common.entity.order.OrderStatus;
import com.shopme.common.entity.order.OrderTrack;
import com.shopme.common.entity.order.PaymentMethod;
import com.shopme.common.entity.product.Product;

@Service
public class OrderService {
	public static final Integer ORDERS_PER_PAGE= 10;
	@Autowired  private OrderRepository  repo;
	
	public Page<Order> listForCustomerByPage(String sortField, String sortDir, String keyword,Integer pageNum,Customer customer){
		Sort sort = Sort.by(sortField);
		sort=sortDir.equals("asc")?sort.ascending():sort.descending();
		Pageable pageable = PageRequest.of(pageNum-1, ORDERS_PER_PAGE, sort);
		if(keyword==null) {
			return repo.findAll(customer.getId(), pageable);
		}
		return repo.findAll(keyword, customer.getId(), pageable);
	}
	public Order getOrderByIdAndCustomer(Integer id,Customer customer) {
		return repo.findOrderByIdAndCustomer(id, customer);
	}
	public Order createOrder(Customer customer, Address address, PaymentMethod paymentMethod, CheckoutInfo checkoutInfo,
			List<CartItem> cartItems) {
		
		Order newOrder = new Order();
		newOrder.setOrderTime(new Date());
		newOrder.setStatus(OrderStatus.NEW);
		newOrder.setCustomer(customer);
		newOrder.setPaymentMethod(paymentMethod);
		newOrder.setProductCost(checkoutInfo.getProductCost());
		newOrder.setShippingCost(checkoutInfo.getShippingCostTotal());
		newOrder.setSubTotal(checkoutInfo.getProductTotal());
		newOrder.setTax(0.0f);
		newOrder.setTotal(checkoutInfo.getPaymentTotal());
		newOrder.setDeliverDate(checkoutInfo.getDeliverDate());
		newOrder.setDeliverDays(checkoutInfo.getDeliverDays());
		
		if(address == null) {
			newOrder.copyAddressFromCustomer();
		}else {
			newOrder.copyShippingAddress(address);
		}
		
		Set<OrderDetail> orderDetails = newOrder.getOrderDetail();
		
		for(CartItem cartItem : cartItems) {
			Product product = cartItem.getProduct();
			OrderDetail orderDetail = new OrderDetail();
			
			orderDetail.setOrder(newOrder);
			orderDetail.setProduct(product);
			orderDetail.setQuantity(cartItem.getQuantity());
			orderDetail.setUnitPrice(product.getDiscountPrice());
			orderDetail.setProductCost(product.getCost()*cartItem.getQuantity());
			orderDetail.setSubTotal(cartItem.getSubTotal());
			orderDetail.setShippingCost(cartItem.getShippingCost());
			 
			orderDetails.add(orderDetail);
		}
		OrderTrack track = new OrderTrack();
		track.setOrder(newOrder);
		track.setStatus(OrderStatus.NEW);
		track.setNotes(OrderStatus.NEW.defaultDescription());
		track.setUpdateTime(new Date());
		
		newOrder.getOrderTracks().add(track);
		
		return repo.save(newOrder);
	}
	public  void setOrderReturnRequested(OrderReturnRequest request, Customer customer) throws orderNotFoundException {
		Order order= repo.findOrderByIdAndCustomer(request.getOrderId(), customer);
		if(order == null) {
			throw new orderNotFoundException("Order Id" + request.getOrderId() + "not found");
		}
		if(order.isReturnRequested()) return;
		OrderTrack track = new OrderTrack();
		track.setOrder(order);
		track.setUpdateTime(new Date());
		track.setStatus(OrderStatus.RESTURN_REQUESTED);
		
		String notes =  "Reason" + request.getReason();
		if(!"".equals(request.getNote())) {
			notes += ". " + request.getNote();  
		}
		
		track.setNotes(notes);
		order.getOrderTracks().add(track);
		order.setStatus(OrderStatus.RESTURN_REQUESTED);
		repo.save(order);
	}
}
