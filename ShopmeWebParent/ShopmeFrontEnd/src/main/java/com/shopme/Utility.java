package com.shopme;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import com.shopme.security.oauth.CustomerOAuth2User;
import com.shopme.setting.CurrencySettingBag;
import com.shopme.setting.EmailSettingBag;

public class Utility {
	public static  String getSiteUrl(HttpServletRequest request) {
		String siteURL =request.getRequestURL().toString();
		return siteURL.replaceAll(request.getServletPath(), "");
	}
	public static JavaMailSenderImpl prepareMailSender(EmailSettingBag settings) {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(settings.getHost());
		mailSender.setPort(settings.getPort());
		mailSender.setUsername(settings.getUsername());
		mailSender.setPassword(settings.getPassword());
		
		Properties mailProperties = new Properties();
		mailProperties.setProperty("mail.smtp.auth", settings.getAUTH());
		mailProperties.setProperty("mail.smtp.starttls.enable", settings.getSmtpSecured());
		
		mailSender.setJavaMailProperties(mailProperties);
		
		return mailSender;
	}
	
	public static String getEmailOfAuthenticatedCustomer(HttpServletRequest request) {
		String customerEmail = null;
		Object principal  = request.getUserPrincipal();
		if(principal ==null) return null;
		
		if(principal instanceof UsernamePasswordAuthenticationToken ||
				principal instanceof RememberMeAuthenticationToken) {
			customerEmail = request.getUserPrincipal().getName();
		}else if(principal instanceof OAuth2AuthenticationToken) {
			
			OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) principal;
			CustomerOAuth2User oauth2User = (CustomerOAuth2User) oauth2Token.getPrincipal();
			customerEmail = oauth2User.getEmail();
		}
		
		return customerEmail;
		
	}
	
	public static String formatCurrency(float amount, CurrencySettingBag setting) {
		String symbol = setting.getSymbol();
		String symbolPossition = setting.getSymbolPossition();
		String decimalPointType = setting.getDecimalPointType();
		int decimalDigits = setting.getDecimalDigits();
		String thousandsPointType = setting.getThousandsPointType();
		
		String pattern = symbolPossition.equals("Before price")? symbol:"" ;
		pattern += "###,##";
		if(decimalDigits > 0) {
			pattern +=".";
			for(int count=1;count<decimalDigits; count++) pattern +="#";
 		}
		
		pattern += symbolPossition.equals("After price")?  symbol:"";
		
		char thousandsSeparator = thousandsPointType.equals("POINT")? '.':',';
		char decimalSeparator = decimalPointType.equals("POINT")? '.':',';
		
		DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance();
		decimalFormatSymbols.setDecimalSeparator(thousandsSeparator);
		decimalFormatSymbols.setGroupingSeparator(decimalSeparator);
		
		DecimalFormat formatter = new DecimalFormat(pattern, decimalFormatSymbols);
		
		return formatter.format(amount);
	}
}
