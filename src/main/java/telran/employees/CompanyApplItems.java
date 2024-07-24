package telran.employees;

import java.util.*;

import telran.view.*;

public class CompanyApplItems {
	static Company company;
	static HashSet<String> departments;
	static HashSet<Long> idList;
public static List<Item> getCompanyItems(Company company,HashSet<String> departments,HashSet<Long> idList)
{
	CompanyApplItems.company = company;
	CompanyApplItems.departments = departments;
	CompanyApplItems.idList=idList;
	Item[] items = {
		Item.of("add employee", CompanyApplItems::menuAddEmployee)	,
		Item.of("display all employee data", CompanyApplItems::getAllEmployee),
		Item.of("display employee data", CompanyApplItems::getEmployee),
		Item.of("remove employee", CompanyApplItems::removeEmployee),
		Item.of("display department budget", CompanyApplItems::getDepartmentBudget),
		Item.of("display departments", CompanyApplItems::getDepartments),
		Item.of("display managers with most factor", CompanyApplItems::getManagersWithMostFactor),
	};
	return new ArrayList(List.of(items));	
}

public static void menuAddEmployee(InputOutput io)
{
	List<Item> addItems =getAddEmployeeItems();
	addItems.add(Item.ofExit());
	Menu menu = new Menu("Add employee nenu",addItems.toArray(Item[]::new));
	menu.perform(io);
	
}

public static List<Item> getAddEmployeeItems()
{
	Item[] items = 
	{
		Item.of("add employee", CompanyApplItems::addEmployee)	,
		Item.of("add manager", CompanyApplItems::addManager),
		Item.of("add sales person", CompanyApplItems::addSalesPerson),
		Item.of("add wage employee", CompanyApplItems::addWageEmployee),
	};
	return new ArrayList(List.of(items));	
}
static void addEmployee(InputOutput io)
{
	Employee empl=readEmployee(io);
	company.addEmployee(empl);
	idList.add(empl.getId());
}

static void addWageEmployee(InputOutput io)
{
	Employee empl = readEmployee(io);
	Employee wageEmpl = readWageEmployee(empl,io);
	company.addEmployee(wageEmpl);
	idList.add(wageEmpl.getId());
}

static void addManager(InputOutput io)
{
	Employee empl = readEmployee(io);
	Employee man = readManager(empl,io);
	company.addEmployee(man);
	idList.add(man.getId());
}

static void addSalesPerson(InputOutput io)
{
	Employee empl = readEmployee(io);
	Employee salesPerson = readSalesPerson(empl,io);
	company.addEmployee(salesPerson);
	idList.add(salesPerson.getId());
}

public static Employee readEmployee(InputOutput io)
{	
	String idString=idList.toString();
	long id = io.readNumberRangeWithPredicate("Enter id value [1000 - 10000]. Now: "+idString, "Wrong id value","This id value already exists",
				1000, 10000,s-> !idList.contains(Long.parseLong(s)) ).longValue();
	int basicSalary = io.readNumberRange("Enter basic salary [2000-20000]", "Wrong basic salary", 2000, 20000).intValue();
	String department = io.readStringOptions("Enter department " + departments, "Wrong department", departments);
	return new Employee(id, basicSalary, department);
}

private static Employee readSalesPerson(Employee empl, InputOutput io)
{
	WageEmployee wageEmployee = (WageEmployee)readWageEmployee(empl, io);
	float percents = io.readNumberRange("Enter percents [0.5 - 2]", "Wrong percents value", 0.5, 2).floatValue();
	long sales = io.readNumberRange("Enter sales [500-50000]", "Wrong sales value", 500, 50000).longValue();
	return new SalesPerson(empl.getId(), empl.getBasicSalary(), empl.getDepartment(),
			wageEmployee.getHours(), wageEmployee.getWage(),
			percents, sales);
}
private static Employee readManager(Employee empl, InputOutput io)
{
	float factor = io.readNumberRange("Enter factor [1.5 - 5]",
			"Wrong factor value", 1.5, 5).floatValue();
	return new Manager(empl.getId(), empl.getBasicSalary(), empl.getDepartment(), factor );
}
private static Employee readWageEmployee(Employee empl, InputOutput io)
{	
	int hours = io.readNumberRange("Enter working hours [10 -200]",
			"Wrong hours value", 10, 200).intValue();
	int wage = io.readNumberRange("Enter hour wage [100-1000]",
			"Wrong wage value", 100, 1000).intValue();
	return new WageEmployee(empl.getId(), empl.getBasicSalary(), empl.getDepartment(), hours, wage);
}


static void getAllEmployee(InputOutput io)
{
	if(company.getSize()>0)
	{
		for(Employee empl:company)
		{
			putEmployeeInfo(empl, io);
		}
	}
	else
	{
		io.writeString("List of employyes is empty");
	}
}
static void getEmployee(InputOutput io)
{
	if(company.getSize()>0)
	{
		String idString=idList.toString();		
	Employee empl=io.readObject("Enter id value Now "+idString, "Employee with this id value is not in company",
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
	else
	{
		io.writeString("List of employyes is empty");
	}
}

static void putEmployeeInfo(Employee empl, InputOutput io)
{
	String jsonStr = new String();
	jsonStr=empl.getJSON()+"\n";
	io.writeString(jsonStr);
}
static void removeEmployee(InputOutput io)
{
	String idString=idList.toString();
	Employee empl=(Employee) io.readObject("Enter id value Now "+idList, "Employee with this id value is not in company",
									s-> {return company.removeEmployee(Long.parseLong(s));	});
	putEmployeeInfo(empl, io);
	idList.remove(empl.getId());
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