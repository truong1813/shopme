package com.shopme.admin;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.product.ProductRepository;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.product.Product;
@DataJpaTest
@AutoConfigureTestDatabase(replace= Replace.NONE)
@Rollback(false)
public class ProductRepositoryTest {
	@Autowired private ProductRepository repo;
	@Autowired private EntityManager entityManager;
	
	@Test
	public void CreateProductTest() {
		Brand brand = entityManager.find(Brand.class, 14);
		Category category = entityManager.find(Category.class, 15);
		Product product = new Product();
		product.setName("Iphone15");
		product.setAlias("iphone_15");
		product.setShortDescription("asasa");
		product.setFullDescription("sdsds");
		product.setBrand(brand);
		product.setCategory(category);
		product.setCost(12);
		product.setPrice(12);
		product.setCreatedTime(new Date());
		product.setUpdatedTime(new Date());
		product.setEnabled(false);
		product.setInStock(false);
		
		Product save = repo.save(product);
		assertThat(save.getId()).isGreaterThan(0);
	}
	
	public void findByName() {
		Product save = repo.findByName("Acer 4775");
		assertThat(save.getId()).isGreaterThan(0);
	}
}
