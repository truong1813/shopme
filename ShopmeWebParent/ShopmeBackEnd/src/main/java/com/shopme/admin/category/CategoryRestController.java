package com.shopme.admin.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryRestController {
	@Autowired  private CategoryService service;
	@PostMapping("/categories/checkUnique")
	
	public String checkUnique(Integer id,String name, String alias) {
		return service.checkUniqueNameAndAlias(id, name, alias);
	}
}
