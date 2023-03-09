package com.shopme.admin.brand;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shopme.admin.paging.SearchRepository;
import com.shopme.common.entity.Brand;
@Repository
public interface BrandRepository extends SearchRepository<Brand, Integer> {
	@Query("SELECT b FROM Brand b WHERE b.name= :name")
	public Brand findByName(@Param("name") String name);
	
	@Query("SELECT b FROM Brand b WHERE CONCAT(b.id, '' , b.name) LIKE %?1% ")
	public Page<Brand> findAll(String keyword,Pageable pageable);
	
	@Query("SElECT NEW Brand(b.id,b.name) FROM Brand b ORDER BY b.name ASC")
	public List<Brand> listAll();
	
	public Long countById(Integer id);
}
