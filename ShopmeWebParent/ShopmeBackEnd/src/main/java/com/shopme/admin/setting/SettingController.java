package com.shopme.admin.setting;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.util.Strings;
import org.aspectj.lang.annotation.Pointcut;
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
import org.supercsv.cellprocessor.ParseInt;

import com.shopme.admin.AmazonS3Util;
import com.shopme.admin.FileUploadUtil;
import com.shopme.common.Constants;
import com.shopme.common.entity.Currency;
import com.shopme.common.entity.setting.Setting;

@Controller
public class SettingController {
	@Autowired SettingService service;
	@Autowired CurrencyRepository currencyRepo;
	
	@GetMapping("/settings")
	public String listAll(Model model) {
		List<Setting> listSettings = service.listAllSettings();
		List<Currency> listCurrencies = currencyRepo.findAllByOrderByNameAsc();
		model.addAttribute("listCurrencies", listCurrencies);
		for (Setting setting :listSettings) {
			model.addAttribute(setting.getKey(), setting.getValue());
		}
		model.addAttribute("S3_BASE_URI", Constants.S3_BUCKET_URI);
		return "settings/settings";
		
	}
	
	@PostMapping("/settings/save_general")
	public String sevaGeneralSettings(@RequestParam("fileImage") MultipartFile multipartFile,
			HttpServletRequest request,	RedirectAttributes ra) throws IOException {
			GeneralSettingBag settingBag = service.getGeneralSetting();
					
			saveSiteLogo(multipartFile, settingBag);
			saveCurrencySymbol(request,settingBag);
			updateSettingValuesFromForm(request, settingBag.list());
			
			ra.addFlashAttribute("message", "General settings has been saved.");
		return "redirect:/settings#genaral";
	}

	private void saveSiteLogo(MultipartFile multipartFile, GeneralSettingBag settingBag) throws IOException {
		if(!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			String value = "/site-logo/" + fileName;
			settingBag.updateSiteLogo(value);
			
			String uploadDir = "site-logo";
			AmazonS3Util.removeFolder(uploadDir);
			AmazonS3Util.uploadFolder(uploadDir, fileName, multipartFile.getInputStream());
			
			//FileUploadUtil.cleanDir(uploadDir);
			//FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		}
	}
	public void saveCurrencySymbol(HttpServletRequest request,GeneralSettingBag settingBag) {
		Integer currencyId = Integer.parseInt(request.getParameter("CURRENCY_ID"));
		Optional<Currency> findByIdResult = currencyRepo.findById(currencyId);
		if(findByIdResult.isPresent()) {
			Currency currency = findByIdResult.get();
			settingBag.updateCurrencySymbol(currency.getSymbol());
			
		}
	}
	public void updateSettingValuesFromForm(HttpServletRequest request,List<Setting> listSettings ) {
		for(Setting setting :listSettings) {
			String value = request.getParameter(setting.getKey());
			if(value !=null) {
				setting.setValue(value);
			}
		}
		service.saveAll(listSettings);
	}
	
	@PostMapping("/settings/save_mail_server")
	public String saveSettingsMailServer(HttpServletRequest request,	RedirectAttributes ra) {
		List<Setting> listMailServerSettings = service.getSettingMailServer();
		updateSettingValuesFromForm(request, listMailServerSettings);
		ra.addFlashAttribute("message", "Mail server settings has been saved");
		return "redirect:/settings#mailServer";
	}
	
	@PostMapping("/settings/save_mail_templates")
	public String saveSettingsMailTemplates(HttpServletRequest request,	RedirectAttributes ra) {
		List<Setting> listMailTemplatesSettings = service.getSettingMailTemplates();
		updateSettingValuesFromForm(request, listMailTemplatesSettings);
		ra.addFlashAttribute("message", "Mail templates settings has been saved");
		return "redirect:/settings#mailtemplates";
	}
	
	@PostMapping("/settings/save_payment")
	public String saveSettingsPayment(HttpServletRequest request,	RedirectAttributes ra) {
		List<Setting> listPaymentSettings = service.getPaymentSettings();
		updateSettingValuesFromForm(request, listPaymentSettings);
		ra.addFlashAttribute("message", "Payment settings has been saved");
		return "redirect:/settings#payment";
	}
}
