package com.shopme.admin.shippingrate;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.setting.SettingService;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.entity.setting.Setting;

@Controller
public class ShippingRateController {

	@Autowired  private ShippingRateService shippingService;
	@Autowired private SettingService settingService;
	@GetMapping("/shipping_rates")
	public String listFirstPage() {
		return "redirect:/shipping_rates/page/1?sortField=country&sortDir=asc";
	}
	
	@GetMapping("/shipping_rates/page/{pageNum}")
	public String listByPage(
			@PagingAndSortingParam(moduleURL="shipping_rates",listName="shippingRates")PagingAndSortingHelper helper,
			@PathVariable(name="pageNum") Integer pageNum, HttpServletRequest request) {
			shippingService.listByPage(pageNum, helper);
			loadCurrencySetting(request);
		
		return "shipping_rates/shipping_rates";
		
	}
	
	@GetMapping("/shipping_rates/new")
	public String newRate(Model model) {
		List<Country> listCountries = shippingService.listCounties();
		model.addAttribute("rate", new ShippingRate());
		model.addAttribute("listCountries", listCountries);
		model.addAttribute("pageTitle", "New Shipping Rate");
		return "shipping_rates/shipping_rate_form";
	}
	
	@PostMapping ("/shipping_rates/save")
	public String save(ShippingRate rate,RedirectAttributes ra){
		try {
			shippingService.save(rate);
			ra.addAttribute("message", "The shipping rate has been saved successfully.");
			
		} catch (ShippingRateAlraedyExistsException e) {
			ra.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/shipping_rates/page/1?sortField=country&sortDir=asc";
	}
	
	@GetMapping("/shipping_rates/edit/{id}")
	public String edit(@PathVariable(name="id") Integer id, RedirectAttributes ra, Model model) {
		try {
			ShippingRate rate = shippingService.get(id);
			List<Country> listCountries = shippingService.listCounties();
			model.addAttribute("rate", rate);
			model.addAttribute("listCountries", listCountries);
			model.addAttribute("pageTitle", "Edit Rate (ID:" + id +")");
			return "shipping_rates/shipping_rate_form";
		} catch (ShippingRateNotFoundExcepion e) {
			ra.addFlashAttribute("message", e.getMessage());
			return "redirect:/shipping_rates/page/1?sortField=country&sortDir=asc";
		}
		
	}
	
	@GetMapping("/shipping_rates/cod/{id}/enabled/{status}")
	public String updateStatusEnabled(@PathVariable(name="id") Integer id, @PathVariable(name="status") boolean enabled,
			RedirectAttributes ra, Model model) {
		try {
			shippingService.updateCODSupported(id, enabled);
			String status = enabled? "enabled":"disabled";
			
			ra.addFlashAttribute("message", "COD support for shipping rate ID:" + id + "has been" +status);
		} catch (ShippingRateNotFoundExcepion e) {
			ra.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/shipping_rates/page/1?sortField=country&sortDir=asc";
	}
	
	@GetMapping("/shipping_rates/delete/{id}")
	public String delete(@PathVariable(name="id") Integer id,RedirectAttributes ra) {
		try {
			shippingService.delete(id);
			ra.addFlashAttribute("message", "The shipping rate ID:" + id + "has been deleted");
		} catch (ShippingRateNotFoundExcepion e) {
			ra.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/shipping_rates/page/1?sortField=country&sortDir=asc";
	}
	
	private void loadCurrencySetting(HttpServletRequest request) {
		 List<Setting> listCurrencySettings = settingService.getCurrencySettings();
		 for(Setting setting : listCurrencySettings) {
			 request.setAttribute(setting.getKey(), setting.getValue());
		 }
		 
	 }
}
