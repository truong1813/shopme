package com.shopme.admin.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.admin.user.UserService;

@RestController
public class UserRestController {
	@Autowired private UserService service;
	@PostMapping("/users/check_email")
	public String checkEmailUnique(Integer id,String email) {
		return service.checkEmailUnique(id,email) ?"OK":"Duplicated";
	}
}
