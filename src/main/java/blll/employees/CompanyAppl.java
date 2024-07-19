package blll.employees;

import java.util.HashSet;
import java.util.List;

import bbl.employees.*;

import bbl.io.Persistable;
import bbl.view.*;



public class CompanyAppl {

	private static final String FILE_NAME = "employeesTest.data";

	public static void main(String[] args) {
		Company company = new CompanyMapsImpl();
		try {
			((Persistable)company).restore(FILE_NAME);
		} catch (Exception e) {
			
		}
		List<Item> companyItems =
				CompanyApplItems.getCompanyItems(company,
						new HashSet<String>(List.of("Audit", "Development", "QA")));
		companyItems.add(Item.of("Exit & save",
				io -> ((Persistable)company).save(FILE_NAME), true));
		companyItems.add(Item.ofExit());
		Menu menu = new Menu("Company CLI Application",
				companyItems.toArray(Item[]::new));
		InputOutput io=new SystemInputOutput();
		menu.perform(io);
		io.writeString("Application ended");
	}

}
