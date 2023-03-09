package com.shopme.admin;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

public class AmazonS3UtilTest {
	@Test
	public void testListFolder() {
		String folderName = "user-photo/10";
		AmazonS3Util.listFolder(folderName);
	}
	
	@Test
	public void testUploadFile() throws FileNotFoundException {
		String folderName = "test_upload";
		String fileName = "tri-tue-nhan-tao.jpeg";
		String filePath = "/Volumes/D/" +fileName;
		InputStream inputStream = new FileInputStream(filePath);
		AmazonS3Util.uploadFolder(folderName, fileName, inputStream);
		
	}
	@Test
	public void deleteFileTest() {
		String fileName= "test_upload/tri-tue-nhan-tao.jpeg";
		AmazonS3Util.deleteFile(fileName);
	}
	
}
