package com.shopme.admin.setting;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.setting.Setting;
import com.shopme.common.entity.setting.SettingCategory;

@Service
public class SettingService {
	@Autowired SettingRepository repo;
	public List<Setting> listAllSettings(){
		return (List<Setting>) repo.findAll();
	}
	
	public GeneralSettingBag getGeneralSetting() {
		List<Setting> settings = new ArrayList<>();
		
		List<Setting> generalSettings = repo.findByCategory(SettingCategory.GENERAL);
		List<Setting> currencySettings = repo.findByCategory(SettingCategory.CRURRENCY);
		
		settings.addAll(generalSettings);
		settings.addAll(currencySettings);
		
		return new GeneralSettingBag(settings);
	}
	
	public void saveAll(Iterable<Setting> setting) {
		repo.saveAll(setting);
	}
	
	public List<Setting> getSettingMailServer(){
		return repo.findByCategory(SettingCategory.MAIL_SERVER);
	}
	
	public List<Setting> getSettingMailTemplates(){
		return repo.findByCategory(SettingCategory.MAIL_TEMPLATE);
	}
	
	public List<Setting> getCurrencySettings(){
		return repo.findByCategory(SettingCategory.CRURRENCY);
	}
	public List<Setting> getPaymentSettings() {
		return repo.findByCategory(SettingCategory.PAYMENT);
	}
}
