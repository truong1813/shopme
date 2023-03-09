package com.shopme.admin;

import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.brand.BrandRepository;

import com.shopme.common.entity.Brand;

@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@Rollback(false)
public class BrandRepositoryTest {
	@Autowired private  BrandRepository repo;
	
	@Autowired EntityManager entity;
	@Test
	public void createBrandTest() {
		Brand brand = repo.findByName("Dell");
		String result = checkNameUnique(4, "Dell")?"OK":"Duplicated";
		System.out.println(result);
		assertThat(brand.getId()).isGreaterThan(0);
	}
	
	private boolean checkNameUnique(Integer id, String name) {
		Brand brand = repo.findByName(name);
		if (brand ==null)return true;
		boolean isCreateNew= (id==null);
		if(isCreateNew) {
			if(brand != null) return false;
		}else {
			if(brand.getId() != id) return false;
		}
		return true;
	}
}
