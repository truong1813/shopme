package com.shopme.admin.user.exporter;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.shopme.common.entity.User;

public class UserPdfExporter extends AbstractExporter {
	public void export(List<User> listUsers, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "application/pdf", ".pdf");
		
		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, response.getOutputStream());
		document.open();
		Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
		font.setSize(18);
		font.setColor(Color.BLUE);
		
		Paragraph paragraph = new Paragraph("List of User", font);
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);
		
		document.add(paragraph);
		
		PdfPTable table = new PdfPTable(6);
		table.setWidthPercentage(100f);
		table.setSpacingBefore(10);
		
		table.setWidths(new float [] {1.5f, 3.5f, 3.0f, 3.0f, 3.0f, 2.0f});
		
		writeTableHeader(table);
		writeTableData(table, listUsers);
		document.add(table);
		document.close();
		
		
	}

	private void writeTableData(PdfPTable table, List<User> listUsers) throws DocumentException, IOException {
		BaseFont bFont = BaseFont.createFont("font/times.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		Font font = new Font(bFont,12);
		for (User user:listUsers) {
			
			table.addCell(new Paragraph(String.valueOf(user.getId()),font));
			table.addCell(new Paragraph(user.getEmail(),font));
			table.addCell(new Paragraph(user.getFirstName(),font));
			table.addCell(new Paragraph(user.getLastName(),font));
			table.addCell(new Paragraph(user.getRoles().toString(),font));
			table.addCell(new Paragraph(String.valueOf(user.isEnabled()),font));
			
		}
	}

	private void writeTableHeader(PdfPTable table) {
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.BLUE);
		cell.setPadding(5);
		
		Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
		font.setColor(Color.WHITE);
		font.setSize(14);
		
		cell.setPhrase(new Phrase("User ID",font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("E-Mail",font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("First Name",font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Last Name",font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Roles",font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Enabled",font));
		table.addCell(cell);
		
	}
}
