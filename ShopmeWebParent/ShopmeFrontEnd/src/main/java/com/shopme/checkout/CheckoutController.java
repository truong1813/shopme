package com.shopme.checkout;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.shopme.Utility;
import com.shopme.address.AddressService;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.entity.order.Order;
import com.shopme.common.entity.order.PaymentMethod;
import com.shopme.customer.CustomerService;
import com.shopme.order.OrderService;
import com.shopme.setting.CurrencySettingBag;
import com.shopme.setting.EmailSettingBag;
import com.shopme.setting.SettingService;
import com.shopme.shipping.ShippingRateService;
import com.shopme.shoppingcart.ShoppingCartService;

@Controller
public class CheckoutController {
	@Autowired private checkoutService checkoutService;
	@Autowired private CustomerService customerService;
	@Autowired private AddressService addressService;
	@Autowired private ShippingRateService shippingService;
	@Autowired private ShoppingCartService shoppingCartService;
	@Autowired private OrderService orderService;
	@Autowired private SettingService settingService;
	@GetMapping("/checkout")
	public String showCheckoutPage(Model model, HttpServletRequest request) {
		
		Customer customer = getAuthenticatedCustomer(request);
		ShippingRate shippingrRate =null;
		Address defaultAddress = addressService.getDefaultAddress(customer);
		
		if(defaultAddress !=null) {
			model.addAttribute("shippingAddress", defaultAddress.toString());
			shippingrRate = shippingService.getShippingRateForAddress(defaultAddress);
		}else {
			model.addAttribute("shippingAddress", customer.getAddress());
			shippingrRate = shippingService.getShippingRateForCustomer(customer);
		}
		if(shippingrRate ==null) {
			return "redirect:/cart";
		}
		
		List<CartItem> cartItems = shoppingCartService.listCartItems(customer);
		CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems, shippingrRate);
		model.addAttribute("checkoutInfo", checkoutInfo);
		model.addAttribute("cartItems", cartItems);
		
		return "checkout/checkout";
	}
	
	private  Customer getAuthenticatedCustomer( HttpServletRequest request) {
		String email = Utility.getEmailOfAuthenticatedCustomer(request);
		return customerService.getCustomerByEmail(email);
	}
	
	@PostMapping("place_order")
	
	public String placeOder(HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
		String paymentType = request.getParameter("paymentMethod");
		PaymentMethod paymentMethod = PaymentMethod.valueOf(paymentType);
		
		Customer customer = getAuthenticatedCustomer(request);
		ShippingRate shippingrRate =null;
		Address defaultAddress = addressService.getDefaultAddress(customer);
		
		if(defaultAddress !=null) {
			shippingrRate = shippingService.getShippingRateForAddress(defaultAddress);
		}else {
			shippingrRate = shippingService.getShippingRateForCustomer(customer);
		}
				
		List<CartItem> cartItems = shoppingCartService.listCartItems(customer);
		CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems, shippingrRate);
		
		Order createdOrder = orderService.createOrder(customer, defaultAddress, paymentMethod, checkoutInfo, cartItems);
		shoppingCartService.deleteByCustomer(customer);
		
		sendOrderConfirmationEmail(request,createdOrder);
		
		return "checkout/order_completed";
		
	}

	private void sendOrderConfirmationEmail(HttpServletRequest request, Order order) throws MessagingException, UnsupportedEncodingException {
		EmailSettingBag emailSettings = settingService.getEmailSetting();
		JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);
		mailSender.setDefaultEncoding("utf-8");
		String toAddress = order.getCustomer().getEmail();
		String subject = emailSettings.getOrderConfirmationSubject();
		String content = emailSettings.getOrderConfirmationContent();
		
		subject = subject.replace("[[orderId]]", String.valueOf(order.getId()));
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
		helper.setTo(toAddress);
		helper.setSubject(subject);
		
		DateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss E, dd MMM yyyy");
		String orderTime = dateFormatter.format(order.getOrderTime());
		
		CurrencySettingBag currencySetting = settingService.getCurrencySetting();
		String totalAmount = Utility.formatCurrency(order.getTotal(), currencySetting);
		
		content = content.replace("[[name]]",order.getCustomer().getFullName());
		content = content.replace("[[orderId]]",String.valueOf(order.getId()));
		content = content.replace("[[orderTime]]",orderTime);
		content = content.replace("[[shippingAddress]]",order.getShippingAddress());
		content = content.replace("[[total]]",totalAmount);
		content = content.replace("[[paymentMethod]]",order.getPaymentMethod().name());
		
		helper.setText(content,true);
		
		mailSender.send(message);
		
	}

}
