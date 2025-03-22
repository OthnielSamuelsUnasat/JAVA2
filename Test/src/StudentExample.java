import backend.models.Student;

import javax.swing.*;
import java.awt.*;

import static backend.api_requests.student_toevoegen;
public class StudentExample {

    public static void main(String[] args) {

        Student student = new Student(100, "Johnny", "Doe", "SE/1121/121", "M", "2000-01-01");


        student.setMajor("SE");
        student.setCohort(1101);
        student.setTotal_ec(120);
        student.setPassword("SecurePassword123");


        String response = student_toevoegen(student);


        System.out.println(response);
    }
}
