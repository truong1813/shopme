package com.shopme.setting;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.States;
@Repository
public interface StatesRepository extends CrudRepository<States, Integer> {
	public List<States> findByCountryOrderByNameAsc(Country country);
}
