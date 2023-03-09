package com.shopme.admin.brand;



import java.io.IOException;
import java.util.List;


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
import com.shopme.admin.category.CategoryService;
import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.exception.BrandNotFoundException;

@Controller
public class BrandController {
	@Autowired private BrandServices service;
	@Autowired private CategoryService cateService;
	@GetMapping("/brands")
	public String listFirsPage() {
		
		return "redirect:/brands/page/1?sortField=name&sortDir=asc";
	}
	
	@GetMapping("/brands/page/{pageNum}")
		
public String listByPage(
		@PagingAndSortingParam(moduleURL ="brands", listName="listBrands") PagingAndSortingHelper helper,
		@PathVariable(name="pageNum") Integer pageNum) {
		service.listByPage(pageNum, helper);	
		
		return "brands/brands";
	}
	@GetMapping("/brands/new")
	
	public String createNew(Model model) {
		Brand brand = new Brand();
		List<Brand> listBrands = service.listBrands();
		List<Category> listCategories = cateService.listCategoriesInForm("asc");
		model.addAttribute("brand",brand);
		model.addAttribute("listBrands",listBrands);
		model.addAttribute("listCategories",listCategories);
		model.addAttribute("pageTitle", "Create New Brand");
		return "brands/brand_form";
	}
	
	@PostMapping("/brands/save")
	public String save(Brand brand,@RequestParam(name="fileImage") MultipartFile multipartFile,
			RedirectAttributes ra) throws IOException {
		if(!multipartFile.isEmpty()){
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			brand.setLogo(fileName);
			Brand saveBrand = service.save(brand);
			String uploadDir = "brand-logos/" + saveBrand.getId();
			
			AmazonS3Util.removeFolder(uploadDir);
			AmazonS3Util.uploadFolder(uploadDir, fileName, multipartFile.getInputStream());
			//FileUploadUtil.cleanDir(uploadDir);
			//FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			
		}else {
			if(brand.getLogo().isEmpty()) brand.setLogo(null);
			service.save(brand);
		}
		ra.addFlashAttribute("message", "Brand has been saved successfully");
		return "redirect:/brands";
	}
	@GetMapping("/brands/edit/{id}")
	public String editBrand(@PathVariable(name="id") Integer id,Model model,RedirectAttributes ra) {
		try {
			Brand brand = service.getId(id);
			List<Category> listCategories = cateService.listCategoriesInForm("asc");
			model.addAttribute("brand", brand);
			model.addAttribute("listCategories",listCategories);
			return "brands/brand_form";
		} catch (BrandNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			return "redirect:/brands";
		}
		
	}
	@GetMapping("/brands/delete/{id}")
	public String delete(@PathVariable(name="id") Integer id,RedirectAttributes ra) throws IOException {
		try { 
			service.delete(id);
			String uploadDir = "brand-logos/" +id;
			AmazonS3Util.removeFolder(uploadDir);
			
			//FileUploadUtil.removeDir(uploadDir);
			ra.addFlashAttribute("message", "The brand with id " +id + " has been deleted successfully.");
		} catch (BrandNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/brands";
	}
}
