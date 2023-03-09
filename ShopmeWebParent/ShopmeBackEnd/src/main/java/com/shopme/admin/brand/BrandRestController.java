package com.shopme.admin.brand;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.exception.BrandNotFoundException;

@RestController
public class BrandRestController {
	@Autowired private BrandServices service;
	@PostMapping("/brands/check_name")
	public String checkNameUnique( Integer id,String name) {
		return service.checkNameUnique(id, name) ?"OK":"Duplicated";
	}
	
	@GetMapping("/brands/{id}/categories")
	public List<CategoryDTO> listCategories(@PathVariable(name="id") Integer id) throws BrandNotFoundRestException{
		List<CategoryDTO> listCategories = new ArrayList<>();
		try {
			Brand brand = service.getId(id);
			Set<Category> categories = brand.getCategories();
			for(Category cate:categories) {
				CategoryDTO dto = new CategoryDTO(cate.getId(), cate.getName());
				listCategories.add(dto);
			}
			return listCategories;
		} catch (BrandNotFoundException e) {
			throw new BrandNotFoundRestException();
		}
	
	}
}
