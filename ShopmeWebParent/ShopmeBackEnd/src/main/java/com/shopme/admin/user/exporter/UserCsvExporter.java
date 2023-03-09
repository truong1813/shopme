package com.shopme.admin.user.exporter;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.common.entity.User;

public class UserCsvExporter extends AbstractExporter {
	public void export(List<User>listUsers, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response,"text/csv", ".csv");
		
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
				CsvPreference.STANDARD_PREFERENCE);
		String[] csvHeader = {"User ID", "E-Mail", "First Name", "LastName", "Roles", "Enabled"};
		String[] fileMapping = {"id", "email","firstName", "lastName", "roles", "enabled"};
		
		csvWriter.writeHeader(csvHeader);
		
		for(User user: listUsers) {
			csvWriter.write(user, fileMapping);
		}
		
		csvWriter.close();
	}
}
