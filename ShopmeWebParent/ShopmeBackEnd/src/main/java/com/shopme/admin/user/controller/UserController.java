package com.shopme.admin.user.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.AmazonS3Util;
import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.user.UserNotFoundException;
import com.shopme.admin.user.UserService;
import com.shopme.admin.user.exporter.UserCsvExporter;
import com.shopme.admin.user.exporter.UserExcelExporter;
import com.shopme.admin.user.exporter.UserPdfExporter;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Controller
public class UserController {
	@Autowired private UserService service;


	@GetMapping("/users")
	public String listFirstPage() {
		return "redirect:/users/page/1?sortField=firstName&sortDir=asc";
	}
	@GetMapping("/users/page/{pageNum}")
	public String listByPage(
			@PagingAndSortingParam(moduleURL="users",listName="listUsers")PagingAndSortingHelper helper,
			@PathVariable(name="pageNum") Integer pageNum) {
			 service.listByPage(pageNum, helper);
		
		return "users/users";
		
	}
	@GetMapping("/users/new")
	public String newUser(Model model) {
		List<Role> listRoles = service.listRoles();
		User user = new User();
		user.setEnabled(true);
		model.addAttribute("user", user);
		model.addAttribute("listRoles", listRoles);
		model.addAttribute("pageTitle", "Create New User");
		return "users/user_form";
	}
	@PostMapping("/users/save")
		public String save(User user,@RequestParam(name="image") MultipartFile multipartFile,RedirectAttributes ra ) throws IOException, UserNotFoundException {
			if(!multipartFile.isEmpty()) {
				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				user.setPhoto(fileName);
				User saveUser = service.save(user);
				String uploadDir="user-photo/" + saveUser.getId();
				AmazonS3Util.removeFolder(uploadDir);
				AmazonS3Util.uploadFolder(uploadDir, fileName, multipartFile.getInputStream());
				
				//FileUploadUtil.cleanDir(uploadDir);    
				//FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			}else {
				if(user.getPhoto().isEmpty()) user.setPhoto(null);
				service.save(user);
			}
		
			ra.addFlashAttribute("message", "The new user has been saved successfully");
			return getRedirectURLToEffectedUser(user.getId());
	}
	private String getRedirectURLToEffectedUser(Integer id) throws UserNotFoundException {
		String firstPartOfEmail = service.get(id).getEmail().split("@")[0];
		return "redirect:/users/page/1?sortField=firstName&sortDir=asc&keyword=" +firstPartOfEmail;
	}
	@GetMapping("/users/{id}/enabled/{status}")
	public String updateEnabledStatus(@PathVariable(name="id") Integer id,@PathVariable(name="status") boolean enabled,
			RedirectAttributes ra) throws UserNotFoundException {
		service.updateEnabledStatus(id, enabled);
		String status =enabled? "enabled":"disabled";
		ra.addFlashAttribute("message", "The user Id "+ id + " has been " +status);
		return getRedirectURLToEffectedUser(id);
	}
	@GetMapping("/users/edit/{id}")
	public String editUser(@PathVariable(name="id") Integer id,RedirectAttributes ra,Model model) throws UserNotFoundException {
		try {
			User user=service.get(id);
			List<Role> listRoles = service.listRoles();	
			model.addAttribute("user", user);
			model.addAttribute("listRoles", listRoles);
			model.addAttribute("pageTitle", "Edit User (ID:" +id +")");
			return "users/user_form";
		} catch (UserNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			return getRedirectURLToEffectedUser(id);
		}
		
		
		
	}
	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable(name="id") Integer id,RedirectAttributes ra,Model model) throws IOException, UserNotFoundException {
		try {
			service.deleteUser(id);
			AmazonS3Util.removeFolder("user-photo/" +id);
			//FileUploadUtil.removeDir("user-photo/" +id);
			ra.addFlashAttribute("message", "The user with id " +id + " has been deleted successfully.");
		} catch (UserNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/users";
	}
@GetMapping("/users/export/csv")
	public void exporteToCSV(HttpServletResponse response) throws IOException {
		UserCsvExporter exporter = new UserCsvExporter();
		List<User> listUsers = service.listAll();
		exporter.export(listUsers, response);
}

@GetMapping("/users/export/excel")
public void exporteToExcel(HttpServletResponse response) throws IOException {
	UserExcelExporter exporter = new UserExcelExporter();
	List<User> listUsers = service.listAll();
	exporter.export(listUsers, response);
}

@GetMapping("/users/export/pdf")
public void exporteToPDF(HttpServletResponse response) throws IOException {
	UserPdfExporter exporter = new UserPdfExporter();
	List<User> listUsers = service.listAll();
	exporter.export(listUsers, response);
}
}
