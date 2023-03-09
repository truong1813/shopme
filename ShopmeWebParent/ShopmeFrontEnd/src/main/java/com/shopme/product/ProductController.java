package com.shopme.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shopme.category.CategoryService;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.product.Product;
import com.shopme.common.exception.CategoryNotFoundException;
import com.shopme.common.exception.ProductNotFoundException;

@Controller
public class ProductController {
	@Autowired CategoryService categoryService;
	@Autowired ProductServices productService;
	
	@GetMapping("/c/{category_alias}")
	public  String viewCategoryFirstPage(@PathVariable(name="category_alias") String alias, Model model) throws CategoryNotFoundException {
		return viewCategoryPage(alias, model, 1);
	}
	
	@GetMapping("/c/{category_alias}/page/{pageNum}")
	public String viewCategoryPage(@PathVariable(name="category_alias") String alias, Model model,
			@PathVariable(name="pageNum") Integer pageNum) throws CategoryNotFoundException {
		Category category = categoryService.getCategory(alias);
		if(category == null) {
			return "/error/404";
		}
		List<Category> listCategoryParents = categoryService.ListCategoryParents(category);
		Page<Product> pageProducts = productService.listByCategory(category.getId(), pageNum);
		List<Product> listProducts = pageProducts.getContent();
		
		long startCount = (pageNum-1)*productService.PRODUCTS_PER_PAGE +1;
		long endCount = startCount +productService.PRODUCTS_PER_PAGE -1;
		if(endCount > pageProducts .getTotalElements()) {
			endCount = pageProducts .getTotalElements();
		}
		
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", pageProducts.getTotalElements());
		model.addAttribute("totalPages", pageProducts.getTotalPages());
		model.addAttribute("listProducts", listProducts);
		model.addAttribute("category", category);		
		model.addAttribute("pageTitle", category.getName());
		model.addAttribute("listCategoryParents", listCategoryParents);
		return "product/products_by_category";
	}
	@GetMapping("/p/{product_alias}")
	public String viewProductDetails(@PathVariable(name="product_alias") String alias, Model model) {
		try {
			Product product = productService.getProductByAlias(alias);
			List<Category> listCategoryParents = categoryService.ListCategoryParents(product.getCategory());
			model.addAttribute("product", product);
			model.addAttribute("listCategoryParents", listCategoryParents);
			model.addAttribute("pageTitle", product.getShortName());
			return "product/product_detail";
		} catch (ProductNotFoundException e) {
			return "error/404";
		}
		
	}
	@GetMapping("/search")
	public String searchFirstPage(@Param("keword") String keyword, Model model) {
		return searchByPage(1, keyword, model);
	}
	
	@GetMapping("/search/page/pageNum")
	public String searchByPage(@PathVariable(name="pageNum") Integer pageNum,@Param("keword") String keyword, Model model )	{
		Page<Product> pageProducts =productService.searchProduct(pageNum, keyword);
		List<Product> listResult = pageProducts.getContent();
		long startCount = (pageNum-1)*productService.PRODUCTS_PER_PAGE +1;
		long endCount = startCount +productService.PRODUCTS_PER_PAGE -1;
		if(endCount > pageProducts .getTotalElements()) {
			endCount = pageProducts .getTotalElements();
		}
		
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", pageProducts.getTotalElements());
		model.addAttribute("totalPages", pageProducts.getTotalPages());
		
		model.addAttribute("pageTitle", keyword + " Search Result");
		model.addAttribute("listResult", listResult);
		model.addAttribute("keyword", keyword);
 		
		return "product/search_result";
	}

}
