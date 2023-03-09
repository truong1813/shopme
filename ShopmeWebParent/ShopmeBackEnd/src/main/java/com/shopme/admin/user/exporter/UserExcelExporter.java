package com.shopme.admin.user.exporter;


import java.io.IOException;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;


import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.shopme.common.entity.User;

public class UserExcelExporter extends AbstractExporter {
	private XSSFWorkbook  workbook ;
	private XSSFSheet sheet;
	String [] title = {"User ID", "E_mail", "First Name", "Last Name", "Roles", "Enabled"};
	
	public UserExcelExporter() {
		workbook= new XSSFWorkbook();
	}
	
	private void writeHeaderLine() {
		sheet = workbook.createSheet("Users");
		XSSFRow row = sheet.createRow(0);
		int columnIdex =0;
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);
		cellStyle.setFont(font);
		for(String item :title) {
			createCell(row, columnIdex++, item, cellStyle);
		}
	}
	
	private void createCell(XSSFRow row, int columnIndex, Object value, CellStyle style) {
		XSSFCell cell = row.createCell(columnIndex);
		sheet.autoSizeColumn(columnIndex);
		if(value instanceof Integer ) {
			cell.setCellValue((Integer) value);
			} else if (value instanceof Boolean){
				cell.setCellValue((boolean) value);
			}else {
				cell.setCellValue((String) value);
			}
		cell.setCellStyle(style);
	}
	public void export(List<User> listUsers, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response,"application/octet-stream",".xlsx");
		
		
		writeHeaderLine();
		writeDataLine(listUsers);
		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}

	private void writeDataLine(List<User> listUsers) {
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(false);
		font.setFontHeight(14);
		cellStyle.setFont(font);
		int rowIndex=1;
		for(User user:listUsers) {
			XSSFRow row = sheet.createRow(rowIndex++);
			int columnIdex = 0;
			createCell(row, columnIdex++, user.getId(), cellStyle);
			createCell(row, columnIdex++, user.getEmail(), cellStyle);
			createCell(row, columnIdex++, user.getFirstName(), cellStyle);
			createCell(row, columnIdex++, user.getLastName(), cellStyle);
			createCell(row, columnIdex++, user.getRoles().toString(), cellStyle);
			createCell(row, columnIdex++, user.isEnabled(), cellStyle);
		}
		
		
	}
}
