package com.shopme.admin;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.setting.SettingRepository;
import com.shopme.common.entity.setting.Setting;
import com.shopme.common.entity.setting.SettingCategory;

@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@Rollback(false)
public class SettingRepositoryTest {
	@Autowired private SettingRepository repo;
	
	@Test
	public void createSettingTest() {
		//Setting siteName = new Setting("SITE_NAME", "Shopme",SettingCategory.GENERAL);
		Setting siteLogo = new Setting("SITE_LOGO","Shopme.png",SettingCategory.GENERAL);
		Setting currencyId = new Setting("CURRENCY_ID","1",SettingCategory.CRURRENCY);
		Setting copyright = new Setting("COPYRIGHT","Copyright (C) 2022 Shopme Ltd.",SettingCategory.GENERAL);
		Setting symbol = new Setting("CURRENCY_SYMBOL","$",SettingCategory.CRURRENCY);
		Setting symbolPosition = new Setting("CURRENCY_SYMBOL_POSITION","before",SettingCategory.CRURRENCY);
		Setting decimalPointType = new Setting("DECIMAL_POINT_TYPE","Point",SettingCategory.CRURRENCY);
		Setting decimalDigits = new Setting("DECIMAL_DIGITS","2",SettingCategory.CRURRENCY);
		Setting thousandsPointType = new Setting("THOUSANDS_POINT_TYPE","COMA",SettingCategory.CRURRENCY);
		
		repo.saveAll(List.of(siteLogo,copyright,currencyId,symbol,symbolPosition,decimalPointType,decimalDigits,thousandsPointType));
	}

}
