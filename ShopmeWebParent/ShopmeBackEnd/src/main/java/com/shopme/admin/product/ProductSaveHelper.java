package com.shopme.admin.product;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.shopme.admin.AmazonS3Util;
import com.shopme.admin.FileUploadUtil;
import com.shopme.common.entity.product.Product;
import com.shopme.common.entity.product.ProductImage;

public class ProductSaveHelper {
	
private static final Logger LOGGER= LoggerFactory.getLogger(ProductSaveHelper.class);

static void deleteExtraImagesWeredRemovedOnform(Product product) {
		
		String extraImageDir = "product-images/" + product.getId() +"/extras" ;
		List<String> listObjectKey = AmazonS3Util.listFolder(extraImageDir);
		for(String objectKey : listObjectKey) {
			int lastIndexSlash = objectKey.lastIndexOf("/");
			String fileName = objectKey.substring(lastIndexSlash+1, objectKey.length());
			if(!product.containsImageName(fileName)) {
				AmazonS3Util.deleteFile(objectKey);
				System.out.println("Delete extra image " + objectKey);
			}
		}
		
/*Path dirPath = Paths.get(extraImageDir);
		try {
			Files.list(dirPath).forEach(file ->{
				String fileName = file.toFile().getName();
				if(!product.containsImageName(fileName)) {
					try {
						Files.delete(file);
						LOGGER.info("Deleted extra image: " + fileName);
					}catch(IOException e){
						LOGGER.error("Could not delete extra image: " + fileName);
					}
				}
			});
			
		} catch (Exception e) {
			LOGGER.error("Could not list directory:" + dirPath);
		} */
	}

static void setExistingExtraImageNames(String[] imageIDs, String[] imageNames, Product product) {
		if(imageIDs==null ||imageIDs.length==0) return;
		Set<ProductImage> images = new HashSet<>();
		for(int count = 0; count  < imageIDs.length ; count++) {
			Integer id = Integer.parseInt(imageIDs[count]);
			String name = imageNames[count];
			images.add(new ProductImage(id,name,product));
		}
		product.setImages(images);
	}

static void setProductDetails(String[] detailIDs, String[] detailNames, String[] detailValues, Product product) {
		if(detailNames == null||detailNames.length == 0) return;
		for(int count = 0; count < detailNames.length;count++) {
			String name = detailNames[count];
			String value =  detailValues[count];
			Integer id = Integer.parseInt(detailIDs[count]);
			
			if(id !=0) {
				product.addDetail(id,name, value);
			}else if(!name.isEmpty()&& !value.isEmpty()) {
				product.addDetail(name, value);
			}
			
		}
		
	}

static void saveUploadImages(MultipartFile mainImageMultipart, 
			MultipartFile[] extraImageMultipart,Product saveProduct) throws IOException {
		if(!mainImageMultipart.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
			String uploadDir = "product-images/" + saveProduct.getId() ;
			List<String> listObjectKey = AmazonS3Util.listFolder(uploadDir +"/");
			for(String objectKey : listObjectKey) {
				if(!objectKey.contains("/extras/")) {
					AmazonS3Util.deleteFile(objectKey);
				}
			}
			AmazonS3Util.uploadFolder(uploadDir, fileName, mainImageMultipart.getInputStream());
			
			//FileUploadUtil.cleanDir(uploadDir);
			//FileUploadUtil.saveFile(uploadDir, fileName, mainImageMultipart);
		}
		
		if(extraImageMultipart.length>0) {
			String uploadDir = "product-images/" + saveProduct.getId() +"/extras";
			
			for(MultipartFile extraMulti : extraImageMultipart) {
				if(extraMulti.isEmpty()) continue;
				
				String fileName = StringUtils.cleanPath(extraMulti.getOriginalFilename());
				AmazonS3Util.uploadFolder(uploadDir, fileName, extraMulti.getInputStream());
				//FileUploadUtil.saveFile(uploadDir, fileName, extraMulti);
				}
			}
	}

static void setMainImageNames(MultipartFile mainImageMultipart, Product product) {
		if(!mainImageMultipart.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
			product.setMainImage(fileName);
		}
	}

static void setNewExtraImageNames(MultipartFile[] extraImageMultipart, Product product) {
		if(extraImageMultipart.length > 0) {
			for(MultipartFile extraMulti : extraImageMultipart) {
				if(!extraMulti.isEmpty()) {
					String fileName = StringUtils.cleanPath(extraMulti.getOriginalFilename());
					if(!product.containsImageName(fileName))
					
					product.addExtraImage(fileName);
				}
			}
		}
		
	}

}
