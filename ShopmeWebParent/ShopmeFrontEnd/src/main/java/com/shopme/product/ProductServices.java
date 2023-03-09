package com.shopme.product;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.product.Product;
import com.shopme.common.exception.ProductNotFoundException;

@Service
public class ProductServices {
	public static final Integer PRODUCTS_PER_PAGE= 10;
	@Autowired  private ProductRepository repo;
	
	public Page<Product> listByCategory(Integer categoryId, Integer pageNum){
		String categoryIdMatch = "-" + String.valueOf(categoryId) + "-" ;
		Pageable pageable = PageRequest.of(pageNum-1, PRODUCTS_PER_PAGE);
		return repo.listByCategory(categoryId, categoryIdMatch, pageable);
	}
	
	public Product getProductByAlias(String alias) throws ProductNotFoundException {
		Product product = repo.findByAlias(alias);
		if(product ==null) {
			throw new ProductNotFoundException("Could not find any product with alias" + alias);
		}
		return product;
	}
	
	public Page<Product> searchProduct(Integer pageNum, String keyword){
		Pageable pageable = PageRequest.of(pageNum-1, PRODUCTS_PER_PAGE);
		return repo.search(keyword, pageable);
	}
}
