package com.shopme.setting;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import com.shopme.common.entity.Country;
import com.shopme.common.entity.StateDTO;
import com.shopme.common.entity.States;
import com.shopme.country.CountryRepository;

@RestController
public class StateRestController {
	@Autowired private StatesRepository repo;
	@Autowired private CountryRepository countryRepo;
	
	@GetMapping("states/list_by_country/{id}")
	public List<StateDTO> listAll(@PathVariable(name="id") Integer countryId){
		Country country = countryRepo.findById(countryId).get();
		List<StateDTO> result = new ArrayList<>();
		List<States> listStates = repo.findByCountryOrderByNameAsc(country);
		for(States state : listStates) {
		StateDTO stateDTO = new StateDTO(state.getId(),state.getName());
			result.add(stateDTO);
		}
		return result;
	}
	
	
	
}
