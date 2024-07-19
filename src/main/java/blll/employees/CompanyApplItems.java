package blll.employees;

import java.util.*;

import bbl.employees.Company;
import bbl.employees.Employee;
import bbl.employees.Manager;
import bbl.employees.SalesPerson;
import bbl.employees.WageEmployee;
import bbl.view.InputOutput;
import bbl.view.Item;

public class CompanyApplItems {
	static Company company;
	static HashSet<String> departments;
public static List<Item> getCompanyItems(Company company,
		HashSet<String> departments) {
	CompanyApplItems.company = company;
	CompanyApplItems.departments = departments;
	Item[] items = {
		Item.of("add employee", CompanyApplItems::addEmployee)	,
		Item.of("display all employee data", CompanyApplItems::getAllEmployee),
		Item.of("display employee data", CompanyApplItems::getEmployee),
		Item.of("remove employee", CompanyApplItems::removeEmployee),
		Item.of("display department budget", CompanyApplItems::getDepartmentBudget),
		Item.of("display departments", CompanyApplItems::getDepartments),
		Item.of("display managers with most factor", CompanyApplItems::getManagersWithMostFactor),
	};
	return new ArrayList(List.of(items));
	
}
static void addEmployee(InputOutput io)
{
	Employee empl = readEmployee(io);
	String type = io.readStringOptions("Enter employee type",
			"Wrong Employee Type", new HashSet<String>
	(List.of("WageEmployee", "Manager", "SalesPerson")));
	Employee result = switch(type) {
	case "WageEmployee" -> getWageEmployee(empl, io);
	case "Manager" -> getManager(empl, io);
	case "SalesPerson" -> getSalesPerson(empl, io);
	default -> null;
	};
	company.addEmployee(result);
	io.writeLine("Employee has been added");
}


private static Employee getSalesPerson(Employee empl, InputOutput io)
{
	WageEmployee wageEmployee = (WageEmployee) getWageEmployee(empl, io);
	float percents = io.readNumberRange("Enter percents [0.5 - 2]", "Wrong percents value", 0.5, 2).floatValue();
	long sales = io.readNumberRange("Enter sales [500-50000]", "Wrong sales value", 500, 50000).longValue();
	return new SalesPerson(empl.getId(), empl.getBasicSalary(), empl.getDepartment(),
			wageEmployee.getHours(), wageEmployee.getWage(),
			percents, sales);
}
private static Employee getManager(Employee empl, InputOutput io)
{
	float factor = io.readNumberRange("Enter factor [1.5 - 5]",
			"Wrong factor value", 1.5, 5).floatValue();
	return new Manager(empl.getId(), empl.getBasicSalary(), empl.getDepartment(), factor );
}
private static Employee getWageEmployee(Employee empl, InputOutput io)
{	
	int hours = io.readNumberRange("Enter working hours [10 -200]",
			"Wrong hours value", 10, 200).intValue();
	int wage = io.readNumberRange("Enter hour wage [100-1000]",
			"Wrong wage value", 100, 1000).intValue();
	return new WageEmployee(empl.getId(), empl.getBasicSalary(), empl.getDepartment(), hours, wage);
}
private static Employee readEmployee(InputOutput io)
{	
	long id = io.readNumberRangeWithPredicate("Enter id value [1000 - 10000]", "Wrong id value","This id value already exists",
				1000, 10000,s->company.getEmployee(Long.parseLong(s))==null).longValue();
	int basicSalary = io.readNumberRange("Enter basic salary [2000-20000]", "Wrong basic salary", 2000, 20000).intValue();
	String department = io.readStringOptions("Enter department " + departments, "Wrong department", departments);
	return new Employee(id, basicSalary, department);
}

static void getAllEmployee(InputOutput io)
{
	for(Employee empl:company)
	{
		putEmployeeInfo(empl, io);
	}
}
static void getEmployee(InputOutput io)
{
	Employee empl=io.readObject("Enter id value", "Employee with this id value is not in company",
								s->
								{
									Employee em=company.getEmployee(Long.parseLong(s));
									if(em==null)
									{
										throw new RuntimeException();
									}
									return em;
								}
								);
	putEmployeeInfo(empl, io);
}

static void putEmployeeInfo(Employee empl, InputOutput io)
{
	String jsonStr = new String();
	jsonStr=empl.getJSON()+"\n";
	io.writeString(jsonStr);
}
static void removeEmployee(InputOutput io)
{
	Employee empl=(Employee) io.readObject("Enter id value", "Employee with this id value is not in company",
									s-> {return company.removeEmployee(Long.parseLong(s));	});
	putEmployeeInfo(empl, io);
}
static void getDepartmentBudget(InputOutput io)
{
	String department = io.readStringOptions("Enter department " + departments, "Wrong department", departments);
	int budget=company.getDepartmentBudget(department);
	io.writeString(String.format("Budget of department %s = %d\n", department,budget));
	
}
static void getDepartments(InputOutput io)
{
	String[] departmentsNow=company.getDepartments();
	io.writeString(String.format("departments of company\n"));
	for(String str:departmentsNow)
	{
		io.writeString(String.format("%s\n", str));
	}
}
static void getManagersWithMostFactor(InputOutput io)
{
	Manager[] menedgers=company.getManagersWithMostFactor();
	io.writeString(String.format("menedgers with most factor\n"));
	for(Manager man:menedgers)
	{
		putEmployeeInfo(man, io);
	}
	
}

}