package com.shopme.category;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Category;
import com.shopme.common.exception.CategoryNotFoundException;

@Service
@Transactional
public class CategoryService {
	@Autowired private CategoryRepository repo;
	public List<Category> listNoChildrenCategories(){
		List<Category> listNoChildrenCategories = new ArrayList<>();
		List<Category> listEnabledCategories = repo.findAllEnabled();
		listEnabledCategories.forEach(category -> {
			if(category.getChildren()==null || category.getChildren().size()==0) {
				listNoChildrenCategories.add(category);
			}
		} );
		return listNoChildrenCategories;
	}
	
	public Category getCategory(String alias) throws CategoryNotFoundException {
		Category category = repo.findByAilasEnabled(alias);
		if(category==null) {
			throw new CategoryNotFoundException("Could not find any category with alias" +alias);
		}
		return category;
	}
	
	public List<Category> ListCategoryParents(Category children){
		List<Category> listCategoryParents = new ArrayList<>();
		Category parent = children.getParent();
		while(parent !=null) {
			listCategoryParents.add(0, parent);
			parent= parent.getParent();
		}
		listCategoryParents.add(children);
		return listCategoryParents;
	}
}
