package com.shopme.admin.brand;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.common.entity.Brand;
import com.shopme.common.exception.BrandNotFoundException;

@Service
@Transactional
public class BrandServices {
	@Autowired private BrandRepository repo;
	public static final Integer BRANDS_PER_PAGE =5;
	public List<Brand> listBrands(){
		return (List<Brand>) repo.findAll();
	}
	
	public Brand save(Brand brand) {
		return repo.save(brand);
	}
	public boolean checkNameUnique(Integer id, String name) {
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
	
	
	public Brand getId(Integer id) throws BrandNotFoundException {
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new BrandNotFoundException("The Could not find any brand with id:" +id);
		}
	}
	
	public void delete(Integer id) throws BrandNotFoundException {
		Long count = repo.countById(id);
		if(count==0 || id==null) {
			throw new BrandNotFoundException("The Could not find any brand with id:" +id);
		}
		repo.deleteById(id);
	}
	
	public void listByPage(Integer pageNum, PagingAndSortingHelper helper){
		helper.listEntities(pageNum, BRANDS_PER_PAGE, repo);
	}
}
