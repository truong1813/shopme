package com.shopme.common.entity.order;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.shopme.common.entity.AbstractAddress;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.Customer;

@Entity
@Table(name="orders")
public class Order  extends AbstractAddress{

	private String country;
	
	private Date orderTime;
	
	private float  shippingCost;
	private float  productCost;
	private float  subTotal;
	private float  tax;
	private float  total;
	
	private int deliverDays;
	private Date deliverDate;
	
	@Enumerated(EnumType.STRING)
	private PaymentMethod paymentMethod;
	
	@Enumerated(EnumType.STRING)
	private OrderStatus status;
	
	@ManyToOne
	@JoinColumn(name="customer_id")
	private Customer customer;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL,orphanRemoval = true)
	private Set<OrderDetail> orderDetail = new HashSet<>();
	
	@OneToMany(mappedBy = "order",cascade = CascadeType.ALL,orphanRemoval = true)
	@OrderBy("updateTime ASC")
	private List<OrderTrack> orderTracks = new ArrayList<>();

	
	public List<OrderTrack> getOrderTracks() {
		return orderTracks;
	}

	public void setOrderTracks(List<OrderTrack> orderTracks) {
		this.orderTracks = orderTracks;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public float getShippingCost() {
		return shippingCost;
	}

	public void setShippingCost(float shippingCost) {
		this.shippingCost = shippingCost;
	}

	public float getProductCost() {
		return productCost;
	}

	public void setProductCost(float productCost) {
		this.productCost = productCost;
	}

	public float getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(float subTotal) {
		this.subTotal = subTotal;
	}

	public float getTax() {
		return tax;
	}

	public void setTax(float tax) {
		this.tax = tax;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	public int getDeliverDays() {
		return deliverDays;
	}

	public void setDeliverDays(int deliverDays) {
		this.deliverDays = deliverDays;
	}

	public Date getDeliverDate() {
		return deliverDate;
	}

	public void setDeliverDate(Date deliverDate) {
		this.deliverDate = deliverDate;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Set<OrderDetail> getOrderDetail() {
		return orderDetail;
	}

	public void setOrderDetail(Set<OrderDetail> orderDetail) {
		this.orderDetail = orderDetail;
	}
	
	public void copyAddressFromCustomer() {
		setFirstName(customer.getFirstName());
		setLastName(customer.getLastName());
		setPhoneNumber(customer.getPhoneNumber());
		setAddressLine1(customer.getAddressLine1());
		setAddressLine2(customer.getAddressLine2());
		setCity(customer.getCity());
		setState(customer.getState());
		setCountry(customer.getCountry().getName());
		setPostalCode(customer.getPostalCode());
		
	}
	
	public void copyShippingAddress(Address address) {
		setFirstName(address.getFirstName());
		setLastName(address.getLastName());
		setPhoneNumber(address.getPhoneNumber());
		setAddressLine1(address.getAddressLine1());
		setAddressLine2(address.getAddressLine2());
		setCity(address.getCity());
		setState(address.getState());
		setCountry(address.getCountry().getName());
		setPostalCode(address.getPostalCode());
		
	}
	
	
	
	@Transient
	public String getDestination() {
		String destination = city + ", ";
		
		if(state != null && !state.isEmpty()) destination += state + ", ";
		destination += country;
		
		return destination;
		}
	
	@Transient
	public String getShippingAddress() {
		String address= firstName;
		if(lastName !=null && !lastName.isEmpty()) address += " " + lastName;
		if(!addressLine1.isEmpty()) address += ", " + addressLine1;
		if(addressLine2 !=null && !addressLine2.isEmpty()) address += " " + addressLine2;
		if(!city.isEmpty()) address += city.equals(state)? "": ", " + city;
		if(state !=null && !state.isEmpty()) address += ", " + state;
		
		address += ", " + country;
		if(postalCode !=null && !postalCode.isEmpty()) address += ". Postal Code: " + postalCode;
		if(!phoneNumber.isEmpty()) address += " . Phone Number: " + phoneNumber;
		return address;
	}
	
	@Transient
	public String getDeliverDateOnForm() {
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormatter.format(this.deliverDate);
			
	}
	
	public void setDeliverDateOnForm(String dateString) {
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			this.deliverDate = dateFormatter.parse(dateString); 
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}
	
	@Transient
	public String getRecipientName() {
		String name = firstName;
		if(lastName != null &!lastName.isEmpty()) name += " " + lastName;
		return name;
	}

	@Transient
	public String getRecipientAddress() {
		String address= addressLine1;
		
		if(addressLine2 !=null && !addressLine2.isEmpty()) address += ", " + addressLine2;
		if(!city.isEmpty()) address += ", " + city;
		if(state !=null && !state.isEmpty()) address += ", " + state;
		
		address += ", " + country;
		if(!postalCode.isEmpty()) address += ". " + postalCode;
		
		return address;
	}
	@Transient
	public boolean isCOD() {
		return paymentMethod.equals(PaymentMethod.COD);
	}
	@Transient
	public boolean isProcessing() {
		return hasStatus(OrderStatus.PROCESSING);
	}
	
	@Transient
	
	public boolean isPicked() {
		return hasStatus(OrderStatus.PICKED);
	}
	@Transient
	
	public boolean isShipping() {
		return hasStatus(OrderStatus.SHIPPING);
	}
	@Transient

	public boolean isDelivered() {
	return hasStatus(OrderStatus.DELIVERED);
	}
	@Transient
	public boolean isReturnRequested() {
		return hasStatus(OrderStatus.RESTURN_REQUESTED);
	}
	
	@Transient
	
	public boolean isReturned() {
		return hasStatus(OrderStatus.RETURNED);
	}
	

	
	public boolean hasStatus(OrderStatus status) {
		
		for(OrderTrack aTrack : orderTracks) {
			if(aTrack.getStatus().equals(status)) {
				return true;
			}
		}
		return false;
	}
	
	@Transient
	public String getProductNames() {
		String productName = "";
		
		productName = "<ul>";
		for(OrderDetail detail: orderDetail) {
			productName += "<li>" + detail.getProduct().getShortName() + "<li>" ;
		}
		productName += "</ul>";
		
		return productName;
	}
	
}
