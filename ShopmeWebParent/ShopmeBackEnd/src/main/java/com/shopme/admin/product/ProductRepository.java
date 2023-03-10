package com.shopme.admin.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shopme.common.entity.product.Product;
@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {
	@Query("SELECT p FROM Product p WHERE p.name= :name")
	public Product findByName(@Param("name")String name);
	
	@Query("SELECT p FROM Product p WHERE p.alias= :alias")
	public Product findByAlias(@Param("alias")String alias);
	
	@Query(" UPDATE Product p SET p.enabled= ?2 WHERE p.id= ?1")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean enabled);
	
	
	public Long countById(Integer id);
	@Query("SELECT p FROM Product p WHERE p.name LIKE %?1%"
			+ "OR p.shortDescription LIKE %?1%"
			+ "OR p.fullDescription LIKE %?1%"
			+ "OR p.brand.name LIKE %?1%"
			+ "OR p.category.name LIKE %?1%")
	public Page<Product> findByKeyword(String keyword, Pageable pageable);
	@Query("SELECT p FROM Product p WHERE p.category.id = ?1 "
			+ "OR p.category.allParentIDs LIKE %?2%")
	public Page<Product> findAllInCategory(Integer CategoryId,String categoryIdMatch,Pageable pageable);
	
	@Query("SELECT p FROM Product p WHERE (p.category.id = ?1 "
			+ "OR p.category.allParentIDs LIKE %?2%)"
			+ "AND (p.name LIKE %?3%"
			+ "OR p.shortDescription LIKE %?3%"
			+ "OR p.fullDescription LIKE %?3%"
			+ "OR p.brand.name LIKE %?3%"
			+ "OR p.category.name LIKE %?3%)")
	public Page<Product> findAllInCategory(Integer CategoryId,String categoryIdMatch,String keyword,Pageable pageable);
	
	@Query("SELECT p FROM Product p WHERE p.name LIKE %?1%")
	public Page<Product> searchProductByName(String keyword, Pageable pageable);
}
