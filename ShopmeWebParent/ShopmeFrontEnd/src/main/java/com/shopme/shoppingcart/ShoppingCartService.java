package com.shopme.shoppingcart;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.product.Product;
import com.shopme.product.ProductRepository;

@Service
@Transactional
public class ShoppingCartService {
	@Autowired private CartItemRepository repo;
	@Autowired private ProductRepository productRepository;
	
	public Integer addCartItem(Integer productId,Integer quantity,Customer customer) throws ShoppingCartException {
		Product product = new Product(productId);
		Integer updateQuantity = quantity;
		CartItem cartItem = repo.findByCustomerAndProduct(customer, product);
		
		if(cartItem !=null) {
			updateQuantity = cartItem.getQuantity() + quantity;
			if(updateQuantity >5) {
				throw new ShoppingCartException("Could not add more " + quantity+" items"
						+ " because there's already " + cartItem.getQuantity() +" items in your"
								+ " shopping cart. Maximun allowed quantity is 5");
			}
		}else {
			cartItem = new CartItem();
			cartItem.setCustomer(customer);
			cartItem.setProduct(product);
			
		}
		cartItem.setQuantity(updateQuantity);
		repo.save(cartItem);
		
		return updateQuantity;
	}
	
	public List<CartItem> listCartItems(Customer customer){
		return repo.findByCustomer(customer);
	}
	
	public float updateQuantity(Integer productId, Integer quantity, Customer customer) {
		Product product = productRepository.findById(productId).get();
		repo.updateQuantity(quantity, customer.getId(), productId);
		float subTotal = product.getDiscountPrice()*quantity;
		return subTotal;
	}
	public void revomeProduct(Customer customer, Integer productId) {
		repo.deleteByCustomerAndProduct(customer.getId(), productId);
	}
	public void deleteByCustomer(Customer customer) {
		repo.deleteByCustomer(customer.getId());
	}
}
