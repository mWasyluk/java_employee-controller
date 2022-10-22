package pl.mwasyluk.filereader;

import java.util.Objects;

import static pl.mwasyluk.filereader.EmployeeFileDAO.EMPLOYEE_FILE_STRING_REGEX;
import static pl.mwasyluk.filereader.EmployeeFileDAO.MAX_ID_DIGITS_NUMBER;

public class Employee implements Comparable<Employee> {
    private int id;
    private String name;
    private String surname;

    public static Employee extractEmployeeFromFileSplit(String splitPart){
        if (splitPart.matches(EMPLOYEE_FILE_STRING_REGEX)){
            int id = Integer.parseInt(splitPart.substring(1,4));
            String name = splitPart.split(" ")[1];
            String surname = splitPart.split(" ")[2];
            return new Employee(id, name, surname);
        }
//        else if (splitPart.trim().isEmpty()) {
//
//        }
//        System.err.println( splitPart + " < part does not match the regex [#000 Joe Doe].");
        return null;
    }

    public Employee(){
//        System.err.println("Nie powinieneś wywoływać domyślnego konstruktora klasy Employee: " + this.getClass().toString());
    }

    public Employee(int id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

    public Employee(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override
    public String toString() {
        return "Employee {" + "id=" + id + ", name='" + name + '\'' + ", surname='" + surname + '\'' + '}';
    }

    public String toStringPretty(){
        return "Employee " + String.format("#%0" + MAX_ID_DIGITS_NUMBER + "d, ", id) + name + " " + surname;
    }

    public String toStringFile(){
        return String.format("#%0" + MAX_ID_DIGITS_NUMBER + "d %s %s", id, name, surname);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return id == employee.id && name.equals(employee.name) && surname.equals(employee.surname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname);
    }

    @Override
    public int compareTo(Employee o) {
        return Integer.compare(this.id, o.id);
    }
}
