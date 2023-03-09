package com.shopme.admin.product;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.common.entity.product.Product;
import com.shopme.common.exception.ProductNotFoundException;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
@Service
@Transactional
public class ProductService {
	public static final Integer PRODUCTS_PER_PAGE =5;
	@Autowired private ProductRepository repo;
	
	public List<Product> listAll(){
		return (List<Product>) repo.findAll();
	}
	
	public Product save(Product product) {
		if(product.getId() == null) {
			product.setCreatedTime(new Date());
		}
		if(product.getAlias()==null || product.getAlias().isEmpty()) {
			product.setAlias(product.getName().replaceAll(" ", "_"));
		}else {
			product.setAlias(product.getAlias().replaceAll(" ", "_"));
		}
		product.setUpdatedTime(new Date());
		
		return repo.save(product);
	}
	
	public Product saveProductPrice(Product productInForm) {
		Product productInDB = repo.findById(productInForm.getId()).get();
		productInDB.setCost(productInForm.getCost());
		productInDB.setPrice(productInForm.getPrice());
		productInDB.setDiscountPercent(productInForm.getDiscountPercent());
		return repo.save(productInDB);
	}
	
	public String checkUnique(Integer id,String name,String alias) {
		boolean isCreateNew = (id==null||id==0);
		Product productName =repo.findByName(name);
		Product productAlias = repo.findByAlias(alias);
		if(productName==null && productAlias==null) return "OK";
		
		if(isCreateNew) {
			if(productName == null) {
				if(productAlias!=null) {
					return "Duplicated Alias";
				}
			}else if(productName !=null) return "Duplicated Name";
			
			
		}else {
			if(productName == null ) {
				if(productAlias.getId() !=id ) return "Duplicated Alias";
			}else {
				if(productAlias !=null &&productAlias.getId() !=id )  return "Duplicated Alias";
				if(productName.getId() !=id ) return "Duplicated Name";
			}
			
		}
		return "OK";
	}
	
	
	public void updateEnabledStatus(Integer id, boolean enabled) {
		repo.updateEnabledStatus(id, enabled);
	}
	
	public void delete(Integer id) throws ProductNotFoundException {
		Long count = repo.countById(id);
		if(count==null ||count==0) {
			throw new ProductNotFoundException("Could not find Product with Id " +id);
			}
		repo.deleteById(id);
		}
	
	public Product getId(Integer id) throws ProductNotFoundException {
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new ProductNotFoundException("Could not find any Product with  Id (" +id +")");
		}
	}
	
	public Page<Product> listByPage(Integer pageNum, String sortDir,String sortField,
			String keyword, Integer categoryId){
		Sort sort = Sort.by(sortField);
		sort= sortDir.equals("asc")? sort.ascending():sort.descending();
		Pageable pageable = PageRequest.of(pageNum-1,PRODUCTS_PER_PAGE,sort);
		if(keyword !=null && !keyword.isEmpty()) {
			if(categoryId !=null &&categoryId >0) {
				String categoryIdMatch = String.valueOf(categoryId) +"-";
				return repo.findAllInCategory(categoryId, categoryIdMatch, keyword, pageable);
					
				}	
			return repo.findByKeyword(keyword, pageable);
		}
		if(categoryId !=null &&categoryId>0) {
			String categoryIdMatch = String.valueOf(categoryId) +"-";
			return repo.findAllInCategory(categoryId, categoryIdMatch, pageable);
		}
		
		 return repo.findAll(pageable);	
		}	
	
	public void searchProducts(Integer pageNum, PagingAndSortingHelper helper){
		Pageable pageable = helper.createPageable(pageNum, PRODUCTS_PER_PAGE);
		
		String keyword = helper.getKeyword();
		
		Page<Product> page = repo.searchProductByName(keyword, pageable);
		helper.updateModelAttributes(pageNum, page);
		}
	}
