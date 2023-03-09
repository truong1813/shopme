package com.shopme.admin.user.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.admin.user.UserService;
import com.shopme.common.entity.User;

@Controller
public class UserAccountController {
	
	@Autowired UserService service;
	
	@GetMapping("/account")
	public String userDetails( @AuthenticationPrincipal ShopmeUserDetails logged, Model model) {
		String email = logged.getUsername();
		User user = service.getByEmail(email);
		model.addAttribute("user", user);
		return "users/user_account";
	}
	@PostMapping("/account/update")
	public String save(User user,@RequestParam(name="image") MultipartFile multipartFile,
			@AuthenticationPrincipal ShopmeUserDetails loggedUser,
			RedirectAttributes ra ) throws IOException {
		if(!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setPhoto(fileName);
			User saveUser = service.userUpdateDetails(user);
			String uploadDir="user-photo/" + saveUser.getId();
			FileUploadUtil.cleanDir(uploadDir);    
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		}else {
			if(user.getPhoto().isEmpty())user.setPhoto(null);
			service.userUpdateDetails(user);
		}
		loggedUser.setFirstName(user.getFirstName());
		loggedUser.setLastName(user.getLastName());
	
		ra.addFlashAttribute("message", "Your user details has been updated");
		return "redirect:/account";
}
}
