package com.shopme.admin.category;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopme.common.entity.Category;
import com.shopme.common.exception.CategoryNotFoundException;
@Service
@Transactional
public class CategoryService {
	public static final Integer CATEGORY_PER_PAGE =5;
@Autowired CategoryRespository repo;
	
	public Category save(Category category) {
		Category parent = category.getParent();
		if(parent !=null) {
			String allParentIds = parent.getAllParentIDs()==null?"-" :parent.getAllParentIDs();
			allParentIds +=String.valueOf(parent.getId()) + "-";
			category.setAllParentIDs(allParentIds);
		}
		
		 return repo.save(category);
	}
	public List<Category> listCategoriesInForm(String sortDir){
		Sort sort = Sort.by("name");
		if(sortDir.equals("asc")) {
			sort=sort.ascending();
			
		}else {
			sort=sort.descending();
		}
		Iterable<Category> rootParent = repo.findRootCategories(sort);
		List<Category> CategoryInUserForm = new ArrayList<>();
		for(Category parent : rootParent) {
			CategoryInUserForm.add(Category.copyAll(parent));
			Set<Category> children = sortSubCategories(parent.getChildren());
			for(Category subChildren : children) {
				String name = "--" + subChildren.getName();
				CategoryInUserForm.add(Category.copyAll(subChildren, name));
				listChildren(CategoryInUserForm, subChildren, 1);
			}
		}
		return CategoryInUserForm;
	}
	private void listChildren(List<Category> CategoryInUserForm,Category parent,Integer subLevel) {
		Integer newLevel = subLevel+1;
		Set<Category> children = sortSubCategories(parent.getChildren());
		for(Category subChildren : children) {
			String name ="";
			for(int i=0; i < newLevel; i++) {
				name += "--" ;
			}
		 name += subChildren.getName();
		 CategoryInUserForm.add(Category.copyAll(subChildren, name));
		 listChildren(CategoryInUserForm, subChildren, newLevel);
		}
		
	}
	
	public Category get(Integer id) throws CategoryNotFoundException {
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new  CategoryNotFoundException("The Could not find category with id:" +id);
		}
	}
	
	public void delete(Integer id) throws CategoryNotFoundException {
		Long count =repo.countById(id);
		if(count == null || count == 0) {
			 throw new CategoryNotFoundException("The Could not find category with id:" +id);
		}
		repo.deleteById(id);
	}
	
	public String checkUniqueNameAndAlias(Integer id, String name,
			String alias) {
		boolean isCreateNew = (id==null||id==0);
		Category category = repo.findByName(name);
		Category categoryByAlias = repo.findByAlias(alias);
		if(isCreateNew) {
			if(category!=null) {
				return "DuplicatedName";
			}else {
				
				if(categoryByAlias!=null) return "DuplicatedAlias";
			}
		}else {
			if(category !=null && category.getId()!=id) return "DuplicatedName";
			if(categoryByAlias !=null && categoryByAlias.getId() !=id) return "DuplicatedAlias";
		}
		
		return "OK";
	}
	
	public void updateEnabledStatus(Integer id, boolean status) {
		repo.updateEnabledStatus(id, status);
	}
	
	private Set<Category> sortSubCategories(Set<Category> children){
		return sortSubCategories(children, "asc");
	}
	private Set<Category> sortSubCategories(Set<Category> children, String sortDir){
		Set<Category> sortedChildren = new TreeSet<>(new Comparator<Category>(){

			@Override
			public int compare(Category cat1, Category cat2) {
				if(sortDir.equals("asc")) {
					return cat1.getName().compareToIgnoreCase(cat2.getName());
				}else {
					return cat2.getName().compareTo(cat1.getName());
					}
				}
			});
		sortedChildren.addAll(children);
		return sortedChildren;
	}
	
	public List<Category> listHierarchicalCategories(List<Category> rootCategories,String sortDir){
				
		List<Category> hierarchicalCategories = new ArrayList<>();
		for(Category parent : rootCategories) {
			hierarchicalCategories.add(Category.copyAll(parent));
				Set<Category> children = sortSubCategories(parent.getChildren(),sortDir);
				for(Category subChildren : children) {
					String name = "--" + subChildren.getName();
					hierarchicalCategories.add(Category.copyAll(subChildren, name));
					listChildren(hierarchicalCategories, subChildren, 1);
		
				}
		}
		return hierarchicalCategories;
	}
	public List<Category> listByPage(CategoryPageInfo pageInfo,Integer pageNum, String sortField, String sortDir, String keyword) {
		sortField = "name";
		Sort sort =Sort.by(sortField);
		if(sortDir.equals("asc"))  {
			sort= sort.ascending();
		}else {
			sort = sort.descending();
		}
		
		Pageable pageable = PageRequest.of(pageNum-1, CATEGORY_PER_PAGE, sort);
		Page<Category> rootPage =null;	
		if(keyword !=null &&!keyword.isEmpty()) {
			rootPage= repo.findAll(keyword,pageable);
			}else {
				rootPage =repo.findRootCategories(pageable);
			}
			
			List<Category> rootCategories = rootPage.getContent();	
			pageInfo.setTotalElements(rootPage.getTotalElements());
			pageInfo.setTotalPage(rootPage.getTotalPages());
			if(keyword !=null &&!keyword.isEmpty()) {
				List<Category> listSearch = rootPage.getContent();
				for(Category category :listSearch) {
					category.setHasChildren(category.getChildren().size()>0);
				}
				return listSearch;
			}else {
				return listHierarchicalCategories(rootCategories, sortDir);
			}
			
	}
	
	
	
}
