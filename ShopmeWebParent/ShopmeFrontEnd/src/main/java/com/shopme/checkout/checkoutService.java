package com.shopme.checkout;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.entity.product.Product;

@Service
public class checkoutService {
	
	private static final int DIM_DIVISOR = 5000;
	public CheckoutInfo prepareCheckout(List<CartItem> cartItems, ShippingRate shippingRate) {
		CheckoutInfo checkInfo = new CheckoutInfo();
		
		float productCost = calculateProductCost(cartItems);
		float productTotal = calculateProductTotal(cartItems);
		float shippingCostTotal = calculateShippingCostTotal(cartItems, shippingRate);
		float paymentTotal = productTotal + shippingCostTotal;
		
		checkInfo.setProductCost(productCost);
		checkInfo.setProductTotal(productTotal);
		checkInfo.setDeliverDays(shippingRate.getDays());
		checkInfo.setCodSupported(shippingRate.isCodSupported());
		checkInfo.setShippingCostTotal(shippingCostTotal);
		checkInfo.setPaymentTotal(paymentTotal);
		
		return checkInfo;
	}

	private float calculateShippingCostTotal(List<CartItem> cartItems, ShippingRate shippingRate) {
		float shippingCostTotal = 0.0f;
		for (CartItem item : cartItems) {	
			Product product  = item.getProduct();
			float dimWeight = (product.getLength()*product.getWidth()*product.getHeight())/DIM_DIVISOR;
			float finalWeight = product.getWeight()>dimWeight? product.getWeight():dimWeight;
			float shippingCost = finalWeight*item.getQuantity()*shippingRate.getRate();
			item.setShippingCost(shippingCost);
			shippingCostTotal +=shippingCost;
		}
		
		return shippingCostTotal;
	}

	private float calculateProductTotal(List<CartItem> cartItems) {
		
		float total = 0.0f;
		
		for (CartItem item : cartItems) {
			total += item.getSubTotal();
		}
		return total;
	}

	private float calculateProductCost(List<CartItem> cartItems) {
		float cost = 0.0f;
		
		for (CartItem item : cartItems) {
			cost += item.getQuantity()*item.getProduct().getCost();
		}
		return cost;
	}
}
