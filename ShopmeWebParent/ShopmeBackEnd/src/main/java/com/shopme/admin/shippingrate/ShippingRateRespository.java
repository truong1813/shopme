package com.shopme.admin.shippingrate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.shopme.admin.paging.SearchRepository;
import com.shopme.common.entity.ShippingRate;
@Repository
public interface ShippingRateRespository extends SearchRepository<ShippingRate, Integer> {
	@Query("SELECT s FROM ShippingRate s WHERE s.country.id = ?1 AND s.state = ?2")
	public ShippingRate  findByCoutryAndState(Integer countryId, String state);
	
	@Query("SELECT s FROM ShippingRate s WHERE CONCAT (s.country.name,' ' , s.state) LIKE %?1%")
	public Page<ShippingRate> findAll(String keyword, Pageable pageable);
	@Query("UPDATE ShippingRate s SET s.codSupported = ?2 WHERE s.id = ?1 ")
	@Modifying
	public void updateCODSupported(Integer id, boolean status);
	
	public Long countById( Integer id);
}
