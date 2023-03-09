package com.shopme.admin.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.common.entity.product.Product;
import com.shopme.common.exception.ProductNotFoundException;

@RestController
public class ProductRestController {
	@Autowired  private ProductService service;
	
	@PostMapping("/products/check_unique")
	public String checkUnique(Integer id,String name, String alias  ) {
		return service.checkUnique(id, name,alias);
	}
	@GetMapping("/products/get/{id}")
	
	public ProductDTO getProductInfo(@PathVariable(name="id") Integer id) throws ProductNotFoundException {
		Product product = service.getId(id);
		return new ProductDTO(product.getName(),product.getMainImagePath(),
				product.getDiscountPrice(), product.getCost());
	}
}
