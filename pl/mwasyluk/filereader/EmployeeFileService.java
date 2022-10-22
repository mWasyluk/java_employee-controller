package pl.mwasyluk.filereader;

import java.util.Set;

import static pl.mwasyluk.filereader.EmployeeFileDAO.MAX_ID_DIGITS_NUMBER;

public class EmployeeFileService {

    private final EmployeeFileDAO fileDAO;

    public EmployeeFileService(EmployeeFileDAO fileDAO) {
        this.fileDAO = fileDAO;
    }

    public Set<Employee> getEmployeeSet(){
        return fileDAO.getEmployeeSetFromString(fileDAO.getFileAsString());
    }

    public boolean addEmployeeToFile(Employee employee){
        if (getEmployeeSet().stream().anyMatch(tmpEmployee -> tmpEmployee.getId() == employee.getId())){
            return false;
        }
        String employeeString = "#" + String.format("%0" + MAX_ID_DIGITS_NUMBER + "d", employee.getId()) + " " + employee.getName() + " " + employee.getSurname();
        if (!employeeString.matches(EmployeeFileDAO.EMPLOYEE_FILE_STRING_REGEX)){
            return false;
        }
        return fileDAO.editFile(EmployeeFileOperation.APPEND_AT_END, "," + employeeString);
    }

    public boolean removeEmployeeFromFileById (int id){
        if (getEmployeeSet().stream().noneMatch(tmpEmployee -> tmpEmployee.getId() == id)){
            return false;
        }
        String idMatchingRegex = String.format("#%0" + MAX_ID_DIGITS_NUMBER + "d", id);
        return fileDAO.editFile(EmployeeFileOperation.REMOVE_BY_ID, idMatchingRegex);
    }
}
