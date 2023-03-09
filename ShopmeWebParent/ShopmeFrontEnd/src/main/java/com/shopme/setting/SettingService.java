package com.shopme.setting;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.setting.Setting;
import com.shopme.common.entity.setting.SettingCategory;

@Service
public class SettingService {
	@Autowired SettingRepository repo;
	
	
	public List<Setting> getGeneralSetting() {
			
		return repo.findTwoCategory(SettingCategory.GENERAL, SettingCategory.CRURRENCY);
	}
	
	public EmailSettingBag getEmailSetting() {
		List<Setting> settings =repo.findByCategory(SettingCategory.MAIL_SERVER);
		settings.addAll(repo.findByCategory(SettingCategory.MAIL_TEMPLATE));
		return new EmailSettingBag(settings);
	}
	
	public CurrencySettingBag getCurrencySetting() {
		List<Setting> settings = repo.findByCategory(SettingCategory.CRURRENCY);
		return new CurrencySettingBag(settings);
	}
}
