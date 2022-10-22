package pl.mwasyluk.filereader;

public class Main {
	public static void main(String... args){
		EmployeeController employeeController = new EmployeeController(new EmployeeFileService(new EmployeeFileDAO()));

		employeeController.collectOptionInLoop();
	}
}