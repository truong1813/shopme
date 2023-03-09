package com.shopme.admin.user.exporter;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

public class AbstractExporter {
	public void setResponseHeader(HttpServletResponse response, String contentType,String extension) throws IOException{
		DateFormat dateFomatter= new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
		String timesamp = dateFomatter.format(new Date());
		String fileName= "users_" + timesamp + extension;
		
		response.setContentType(contentType);
		
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; fileName=" +fileName;
		response.setHeader(headerKey, headerValue);
	}
}
