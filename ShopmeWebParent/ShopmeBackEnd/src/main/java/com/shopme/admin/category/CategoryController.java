package com.shopme.admin.category;

import java.io.IOException;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.repository.query.Param;
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
import com.shopme.common.entity.Category;
import com.shopme.common.entity.User;
import com.shopme.common.exception.CategoryNotFoundException;



@Controller
public class CategoryController {
	@Autowired CategoryService service;
	@GetMapping("/categories")
	public String listFirstPage(Model model) {
		return listBytPage(model, 1, "asc", "name", null);
	}
	@GetMapping("/categories/page/{pageNum}")
	public String listBytPage(Model model,@PathVariable(name="pageNum") Integer pageNum, @Param("sortDir") String sortDir,
			@Param("sortField")String sortField, @Param("keyword") String keyword) {
		
		if(sortDir==null||sortDir.isEmpty()) {
			sortDir="asc";
		}
		CategoryPageInfo pageInfo = new CategoryPageInfo();
		List<Category> listCategories = service.listByPage( pageInfo,pageNum, sortField, sortDir, keyword);
		long startCount = (pageNum-1)*service.CATEGORY_PER_PAGE +1;
		long endCount = startCount +service.CATEGORY_PER_PAGE -1;
		if(endCount>pageInfo.getTotalElements()) {
			endCount=pageInfo.getTotalElements();
		}
		
		String reverseSortDir = sortDir.equals("asc")? "desc":"asc";
		
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", pageInfo.getTotalElements());
		model.addAttribute("totalPages", pageInfo.getTotalPage());
		
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		
		model.addAttribute("keyword", keyword);
		model.addAttribute("modelURL", "categories");
				
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("reverseSortDir", reverseSortDir);
		return "categories/categories";
	}
	@GetMapping("/categories/new")
	public String newCategory(Model model) {
		String sortDir="asc";
		List<Category> listCategories = service.listCategoriesInForm(sortDir);
		model.addAttribute("category", new Category());
		model.addAttribute("pageTitle", "Create New Category");
		model.addAttribute("listCategories", listCategories);
		return "categories/category_form";
	}
	
	@PostMapping("/categories/save")
	public String save(Category category, 
			@RequestParam("fileImage") MultipartFile  multipartFile, RedirectAttributes ra ) throws IOException {
		if(!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			category.setImage(fileName);
			Category saveCategory = service.save(category);
			String uploadDir = "category-images/" + saveCategory.getId();
			AmazonS3Util.removeFolder(uploadDir);
			AmazonS3Util.uploadFolder(uploadDir, fileName, multipartFile.getInputStream());
			
			//FileUploadUtil.cleanDir(uploadDir);
			//FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		}else {
			if(category.getImage().isEmpty()) category.setImage(null);
			service.save(category);
			
		}
		
		ra.addFlashAttribute("message", "The category has been saved successfully");
		return "redirect:/categories";
	}
	@GetMapping("/categories/edit/{id}")
	public String editCategory(@PathVariable("id") Integer id,RedirectAttributes ra, Model model)throws CategoryNotFoundException{
		String sortDir ="asc";
		try {
			Category category = service.get(id);
			List<Category> listCategories = service.listCategoriesInForm(sortDir);
			model.addAttribute("category", category);
			model.addAttribute("listCategories", listCategories);
			return "categories/category_form";
			
		} catch (CategoryNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			return "redirect:/catergories";
		}
		
	}
	@GetMapping("/categories/delete/{id}")
	public String deleteCategory(@PathVariable(name="id") Integer id, RedirectAttributes ra, Model model) throws IOException {
		try {
			service.delete(id);
			AmazonS3Util.removeFolder("category-images/" + id);
			ra.addFlashAttribute("message", "The category with id " +id + " has been deleted successfully.");
			
		} catch (CategoryNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/categories";
	}
 @GetMapping("/categories/{id}/enabled/{status}")
 
 public String updateEnabledStatus(@PathVariable(name="id") Integer id, 
		 @PathVariable(name="status") boolean enabled, RedirectAttributes ra) {
	 String status = enabled? "enabled":"disabled";
	 service.updateEnabledStatus(id, enabled);
	 ra.addFlashAttribute("message", "The category Id "+ id + " has been " +status);
	 return "redirect:/categories";
 }
	
}
