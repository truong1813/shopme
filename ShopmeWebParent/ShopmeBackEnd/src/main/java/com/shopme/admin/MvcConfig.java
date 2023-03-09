package com.shopme.admin;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.shopme.admin.paging.PagingAndSortingArgumentResolver;
@Configuration
public class MvcConfig implements WebMvcConfigurer {

	/*@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String dirName = "user-photo";
		Path userPhotoDir = Paths.get(dirName);
		String userPhotoPath = userPhotoDir.toFile().getAbsolutePath();
		
		registry.addResourceHandler("/" + dirName + "/**")
		.addResourceLocations("file:" + userPhotoPath +"/");
		
		String cateDirName = "../category-images";
		Path cateImageDir = Paths.get(cateDirName);
		String cateImagePath = cateImageDir.toFile().getAbsolutePath();
		
		registry.addResourceHandler("/category-images/**")
		.addResourceLocations("file:" +  cateImagePath +"/");
		
		String brandDirName = "../brand-logos";
		Path brandImageDir = Paths.get(brandDirName);
		String brandImagePath = brandImageDir.toFile().getAbsolutePath();
		
		registry.addResourceHandler("/brand-logos/**")
		.addResourceLocations("file:" +  brandImagePath +"/");
		
		String productDirName = "../product-images";
		Path productImageDir = Paths.get(productDirName);
		String productImagePath = productImageDir.toFile().getAbsolutePath();
		
		registry.addResourceHandler("/product-images/**")
		.addResourceLocations("file:" +  productImagePath +"/");
		
		String siteLogoDirName = "../site-logo";
		Path siteLogoDir = Paths.get(siteLogoDirName);
		String siteLogoPath = siteLogoDir.toFile().getAbsolutePath();
		
		registry.addResourceHandler("/site-logo/**")
		.addResourceLocations("file:" +  siteLogoPath +"/");
	}
*/
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		
		resolvers.add(new PagingAndSortingArgumentResolver());
	}

}
