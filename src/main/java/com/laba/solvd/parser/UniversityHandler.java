package com.laba.solvd.parser;

import com.laba.solvd.model.Department;
import com.laba.solvd.model.Professor;
import com.laba.solvd.model.Student;
import com.laba.solvd.model.University;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class UniversityHandler extends DefaultHandler {
    private StringBuilder currentValue = new StringBuilder();
    private University university;
    private Department department;
    private Professor professor;
    private Student student;
    private List<Department> departments = new ArrayList<>();
    private List<Student> students = new ArrayList<>();

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        currentValue.setLength(0); // сбрасываем текущее значение
        if (qName.equals("university")) {
            university = new University();
            departments.clear(); // очищаем список факультетов
            students.clear(); // очищаем список студентов
        } else if (qName.equals("department")) {
            department = new Department();
            department.setProfessors(new ArrayList<>()); // инициализируем список профессоров
        } else if (qName.equals("professor")) {
            professor = new Professor();
            professor.setCourses(new ArrayList<>()); // инициализируем список курсов
        } else if (qName.equals("student")) {
            student = new Student();
            student.setClubs(new ArrayList<>()); // инициализируем список клубов
            student.setGrades(new ArrayList<>()); // инициализируем список оценок
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (qName) {
            case "university":
                university.setDepartments(departments);
                university.setStudents(students);
                // Вывод информации о университете
                System.out.println("University Name: " + university.getName());
                System.out.println("Location: " + university.getLocation());
                System.out.println("Founded Year: " + university.getFoundedYear());
                break;

            case "name":
                if (university != null) university.setName(currentValue.toString());
                if (department != null) department.setName(currentValue.toString());
                if (professor != null) professor.setFirstName(currentValue.toString()); // можно будет улучшить
                if (student != null) student.setFirstName(currentValue.toString()); // можно будет улучшить
                break;

            case "location":
                if (university != null) university.setLocation(currentValue.toString());
                break;

            case "foundedYear":
                if (university != null) university.setFoundedYear(Integer.parseInt(currentValue.toString()));
                break;

            case "professor":
                if (department != null) {
                    department.getProfessors().add(professor); // добавляем профессора в список
                }
                break;

            case "department":
                if (university != null) {
                    departments.add(department); // добавляем факультет в список
                    // Вывод информации о факультете
                    System.out.println("Department Name: " + department.getName());
                    for (Professor prof : department.getProfessors()) {
                        System.out.println("Professor Name: " + prof.getFirstName());
                    }
                }
                break;

            case "student":
                if (university != null) {
                    students.add(student); // добавляем студента в список
                    // Вывод информации о студенте
                    System.out.println("Student Name: " + student.getFirstName());
                }
                break;

            // Дополнительные элементы...
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        currentValue.append(ch, start, length);
    }

    // Метод для получения объекта University
    public University getUniversity() {
        return university;
    }
}

