package com.shopme.admin.setting.country;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopme.admin.setting.state.StatesRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.States;

@SpringBootTest
@AutoConfigureMockMvc
public class CountryRestControllerTest {
	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper objectMapper;
	@Autowired CountryRepository repo;
	@Autowired StatesRepository stateRepo;
	@Test
	@WithMockUser(username="nam@codejava.net",password="nam2020", roles="ADMIN")
	public void testListCountries() throws Exception {
		String url= "/countries/list";
		
		MvcResult result= mockMvc.perform(get(url)).andExpect(status().isOk())
		.andDo(print())
		.andReturn();
		
		String responseJson= result.getResponse().getContentAsString();
		System.out.println(responseJson);
		Country[] countries = objectMapper.readValue(responseJson, Country[].class);
			for (Country country :countries) {
				System.out.println(country);
			}
	}
	@Test
	@WithMockUser(username="nam@codejava.net",password="nam2020", roles="ADMIN")
	public void testSaveCountries() throws Exception {
		String url= "/countries/save";
		Country country = new Country("Germany", "DE");
		MvcResult result= mockMvc.perform(post(url).contentType("application/json")
				.content(objectMapper.writeValueAsString(country))
				.with(csrf()))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		String response = result.getResponse().getContentAsString();
		Integer CountryId = Integer.parseInt(response);
		Country saveCountry = repo.findById(CountryId).get();
		
		assertThat(saveCountry.getName()).isEqualTo("Germany");
		
	}
	
	@Test
	@WithMockUser(username="nam@codejava.net",password="nam2020", roles="ADMIN")
	public void createState() throws IOException, Exception {
		String url="/states/save";
		Country country = repo.findById(1).get();
		States state =  new States("thanh hoa", country);
		
		
		MvcResult result= mockMvc.perform(post(url).contentType("application/json")
				.content(objectMapper.writeValueAsString(state))
				.with(csrf()))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		
		String response = result.getResponse().getContentAsString();
		Integer stateId = Integer.parseInt(response);
		States saveState = stateRepo.findById(stateId).get();
		
		assertThat(saveState.getName()).isEqualTo("thanh hoa");
		
	}
	
}
