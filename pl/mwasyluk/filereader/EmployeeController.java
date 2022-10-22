package pl.mwasyluk.filereader;

import java.util.Scanner;

public class EmployeeController {
    private final EmployeeFileService employeeFileService;

    public EmployeeController(EmployeeFileService employeeFileService) {
        this.employeeFileService = employeeFileService;
    }

    private final Scanner scanner = new Scanner(System.in);

    public void collectOptionInLoop(){
        do {
            System.out.println("----------------------------------------------------------------\n" +
                    "Wpisz numer opcji i zatwierdź klawiszem Enter, aby kontynuować: \n" +
                    "1. Wyświetl listę pracowników\n" +
                    "2. Dodaj nowego pracownika\n" +
                    "3. Usuń pracownika\n" +
                    "4. Zamknij aplikację");
            String option = scanner.next();
            System.out.println("----------------------------------------------------------------");
            switch (option){
                case "1": {
                    printAllEmployees();
                    break;
                }
                case "2": {
                    collectAndSaveNewEmployee();
                    break;
                }
                case "3": {
                    collectIdAndRemoveEmployee();
                    break;
                }
                case "4": {
                    System.exit(0);
                }
                default: {
                    System.out.println("Wprowadzona linia: '"+ option +"' nie jest opcją. Spróbuj ponownie.");
                }
            }
        } while (true);
    }

    private void printAllEmployees(){
        employeeFileService.getEmployeeSet().forEach(tmp -> System.out.println(tmp.toStringPretty()));
    }

    private void collectAndSaveNewEmployee(){
        System.out.print("Wprowadź identyfikator (0-999): ");
        String id = scanner.next();
        System.out.print("Wprowadź imię (rozpoczęte dużą literą): ");
        String name = scanner.next();
        System.out.print("Wprowadź nazwisko (rozpoczęte dużą literą): ");
        String surname = scanner.next();

        Employee newEmployee = new Employee(Integer.parseInt(id), name, surname);
        boolean isEmployeeCorrect = newEmployee.toStringFile().matches(EmployeeFileDAO.EMPLOYEE_FILE_STRING_REGEX);
        if (isEmployeeCorrect && employeeFileService.addEmployeeToFile(newEmployee)){
            System.out.println("Dodano nowego pracownika.");
        } else {
            System.out.println("Nie dodano pracownika. Sprawdź wprowadzone dane i spróbuj ponownie.");
        }
    }

    private void collectIdAndRemoveEmployee() {
        System.out.print("Wprowadź identyfikator (0-999): ");
        int id = Integer.parseInt(scanner.next());

        System.out.println(employeeFileService.removeEmployeeFromFileById(id) ?
                "Usunięto pracownika." :
                "Nie usunięto pracownika. Sprawdź wprowadzone dane i spróbuj ponownie.");
    }
}
