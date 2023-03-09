package com.shopme.admin.product;

import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.AmazonS3Util;
import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.brand.BrandRepository;
import com.shopme.admin.category.CategoryService;
import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.product.Product;
import com.shopme.common.exception.ProductNotFoundException;


@Controller
public class ProductController {

@Autowired private ProductService service;
@Autowired private BrandRepository brandRepo;
@Autowired private CategoryService cateService;
	@GetMapping("/products")
	public String listFirstPage(Model model) {
		
		return listByPage(1, "asc", "name", null,0, model);
	}
	
	@GetMapping("/products/page/{pageNum}")
		
	public String  listByPage(@PathVariable(name="pageNum") Integer pageNum, @Param("sortDir") String sortDir,
			@Param("sortField")String sortField,@Param("keyword")String keyword, 
			@Param("categoryId") Integer categoryId, Model model ) {
		
		Page<Product> page = service.listByPage(pageNum, sortDir, sortField, keyword,categoryId );
		List<Product> listProducts = page.getContent();
		List<Category> listCategories= cateService.listCategoriesInForm("asc");
		long startCount = (pageNum-1)*service.PRODUCTS_PER_PAGE +1;
		long endCount = startCount +service.PRODUCTS_PER_PAGE -1;
		if(endCount>page.getTotalElements()) {
			endCount=page.getTotalElements();
		}
		if(categoryId !=null) model.addAttribute("categoryId", categoryId);
		String reverseSortDir = sortDir.equals("asc")? "desc":"asc";
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("listProducts", listProducts);
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("modelURL", "products");
		
		
		return "products/products";
	}
	
	
	@GetMapping("/products/new")
	public String newProduct(Model model) {
		Product product = new Product();
		product.setInStock(true);
		product.setEnabled(true);
		Integer numberOfExistingExtraImages = 0;
		List<Brand> listBrands  = brandRepo.listAll();
		model.addAttribute("product", product);
		model.addAttribute("listBrands", listBrands);
		model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);
		model.addAttribute("pageTitle", "Create New Product");
		return "products/product_form";
	}
	
	@PostMapping("/products/save")
		
	public String save(Product product, RedirectAttributes ra,
						@RequestParam(value="fileImage",required = false) MultipartFile mainImageMultipart,
						@RequestParam(value="extraImage",required = false) MultipartFile[] extraImageMultipart,
						@RequestParam(name="detailIDs", required = false)String[] detailIDs,
						@RequestParam(name="detailNames", required = false)String[] detailNames,
						@RequestParam(name="detailValues", required = false)String[] detailValues,
						@RequestParam(name="imageIDs", required = false)String[] imageIDs,
						@RequestParam(name="imageNames", required = false)String[]imageNames,
						@AuthenticationPrincipal ShopmeUserDetails loggedUser) throws IOException {
		
		if(!loggedUser.hasRole("Admin") &&!loggedUser.hasRole("Editor")) {
			if(loggedUser.hasRole("Salesperson")) {
				service.saveProductPrice(product);
				ra.addFlashAttribute("message", "The product has been saved successfully");
				return "redirect:/products";
			}
		}
		ProductSaveHelper.setMainImageNames(mainImageMultipart,product);
		ProductSaveHelper.setExistingExtraImageNames(imageIDs,imageNames,product);
		ProductSaveHelper.setNewExtraImageNames(extraImageMultipart,product);
		ProductSaveHelper.setProductDetails(detailIDs,detailNames,detailValues,product);
		
		Product saveProduct = service.save(product);
		ProductSaveHelper.saveUploadImages(mainImageMultipart,extraImageMultipart,saveProduct);
		ProductSaveHelper.deleteExtraImagesWeredRemovedOnform(product);
		
		ra.addFlashAttribute("message", "The product has been saved successfully");
		return "redirect:/products";
	}
		
	@GetMapping("products/{id}/enabled/{status}")
	public String updateEnabledStatus(@PathVariable(name="id") Integer id, @PathVariable(name="status")
	boolean enabled, RedirectAttributes ra) {
		service.updateEnabledStatus(id, enabled);
		String status = enabled? "enabled":"disabled";
		ra.addFlashAttribute("message", "The product Id "+ id + " has been " +status);
		return "redirect:/products";
	}
	@GetMapping("products/delete/{id}")
	
	public String delete(@PathVariable(name="id")Integer id, RedirectAttributes ra) throws IOException {
		try {
			service.delete(id);
			
			String productExtraImageDir = "product-images/"+ id + "/extras";
			String productMainImageDir = "product-images/"+ id;
			
			AmazonS3Util.removeFolder(productExtraImageDir);
			//FileUploadUtil.removeDir(productExtraImageDir);
			
			AmazonS3Util.removeFolder(productMainImageDir)	;
			
			//FileUploadUtil.removeDir(productMainImageDir);
			ra.addFlashAttribute("message", "The product with id " +id + " has been deleted successfully.");
		} catch (ProductNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/products";
	}
	
	@GetMapping("/products/edit/{id}")
	public String editProduct(@PathVariable(name="id") Integer id,Model model, RedirectAttributes ra,
			@AuthenticationPrincipal ShopmeUserDetails loggedUser) {
		try {
			Product product = service.getId(id);
			List<Brand> listBrands  = brandRepo.listAll();
			Integer numberOfExistingExtraImages = product.getImages().size();
			
			boolean isReadOnlyForSalesperson = false;
			if(!loggedUser.hasRole("Admin") &&!loggedUser.hasRole("Editor")) {
				if(loggedUser.hasRole("Salesperson")) {
					isReadOnlyForSalesperson= true;
				}
			}
			model.addAttribute("isReadOnlyForSalesperson", isReadOnlyForSalesperson);
			model.addAttribute("product", product);
			model.addAttribute("listBrands", listBrands);
			model.addAttribute("pageTitle", "Edit Product with (ID:" +id +")");
			model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);
			return "products/product_form";
			
		} catch (ProductNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			return "redirect:/products";
		}
	}
	
	@GetMapping("/products/detail/{id}")
	public String viewDetailProduct(@PathVariable(name="id") Integer id,Model model, RedirectAttributes ra ) {
		try {
			Product product = service.getId(id);
			model.addAttribute("product", product);
			return "products/product_details_modal";
			
		} catch (ProductNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			return "redirect:/products";
		}
	}
}
