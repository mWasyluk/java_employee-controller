package pl.mwasyluk.filereader;

import java.io.*;
import java.net.URISyntaxException;
import java.util.*;

//DAO
public class EmployeeFileDAO {
    public static String EMPLOYEE_FILE_PATH = "";
    static {
        try {
            EMPLOYEE_FILE_PATH = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath() + "!/../employees.txt";
        } catch (URISyntaxException e) {
            System.err.println("Niepoprawna ścieżka pliku. Zrestartuj aplikację i spróbuj ponownie.");
        }
    }
    public final static Integer MAX_ID_DIGITS_NUMBER = 3;
    public final static String EMPLOYEE_ID_FILE_REGEX = "#[0-9]{" + MAX_ID_DIGITS_NUMBER + "}";
    public final static String EMPLOYEE_FILE_STRING_REGEX = EMPLOYEE_ID_FILE_REGEX + " [A-Z][a-z]* [A-Z][a-z]*";
    private final File employeesFile;

    public EmployeeFileDAO() {
        employeesFile = new File(EMPLOYEE_FILE_PATH);
        if (!employeesFile.exists()) {
            try {
                employeesFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String getFileAsString(){
        StringBuilder employeesAsString = new StringBuilder();
        String currentLine = "";
        try {
            FileReader fileReader = new FileReader(employeesFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while((currentLine = bufferedReader.readLine()) != null){
                employeesAsString.append(currentLine.trim());
            }
            bufferedReader.close();
        } catch (Exception e){
            System.err.println("Nie można odczytać pliku. Sprawdź plik/ścieżkę pliku i spróbuj ponownie.");
        }
        if (!employeesAsString.toString().trim().isEmpty()) {
            return employeesAsString.toString();
        }
//        System.err.println("getFilesString() method returned empty string");
        return "";
    }

    public boolean editFile (EmployeeFileOperation operation, String string){
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        PrintWriter printWriter = null;
        boolean result = false;
        try {
            switch (operation){
                case APPEND_AT_END: {
                    fileWriter = new FileWriter(EMPLOYEE_FILE_PATH, true);
                    bufferedWriter = new BufferedWriter(fileWriter);
                    printWriter = new PrintWriter(bufferedWriter);
                    result = appendToFile(printWriter, string);
                    break;
                }
                case REMOVE_BY_ID: {
                    String fileContent = getFileAsString();
                    fileWriter = new FileWriter(EMPLOYEE_FILE_PATH, false);
                    bufferedWriter = new BufferedWriter(fileWriter);
                    printWriter = new PrintWriter(bufferedWriter);
                    result = overrideFile(printWriter,fileContent, string);
                    break;
                }
                default: {
                    break;
                }
            }

            fileWriter.close();
            bufferedWriter.close();
            printWriter.close();
        } catch (IOException e) {
            System.err.println("Edycja pliku się nie powiodła. Upewnij się, że plik jest poprawny i spróbuj ponownie.");
        }
        return result;
    }

    public Set<Employee> getEmployeeSetFromString(String employeesString){
        Set<Employee> employeeSet = new TreeSet<>();
        for( String employee : employeesString.split(",")){
            Employee tempEmp = Employee.extractEmployeeFromFileSplit(employee.trim());
            if (tempEmp != null) employeeSet.add(tempEmp);
        }
        return employeeSet;
    }

    private boolean appendToFile(PrintWriter printWriter, String toAppend){
        if (toAppend.trim().isEmpty() || printWriter == null) {
            return false;
        }
        printWriter.append(toAppend);
        printWriter.flush();

        return true;
    }

    private boolean overrideFile(PrintWriter printWriter, String content, String toRemovePrefix) throws IOException{
        if (!content.trim().isEmpty()) {
            int startIndexToRemove = content.indexOf(toRemovePrefix);
            if (startIndexToRemove > 0) startIndexToRemove --;
            int endIndexToRemove = content.indexOf(",", startIndexToRemove + 1);
            if (endIndexToRemove < 1)
                endIndexToRemove = content.length();
            String employeesStringAfterRemoving = content.substring(0, startIndexToRemove) + content.substring(endIndexToRemove, content.length());

            printWriter.write(employeesStringAfterRemoving);
            printWriter.flush();

            return true;
        }
        return false;
    }
}
