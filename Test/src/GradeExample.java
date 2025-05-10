import backend.models.Student;
import backend.models.Grade;
import javax.swing.*;
import java.awt.*;


public class GradeExample {


    public static void main(String[] args) {

        Grade grade = new Grade();


        grade.setStudent_id(56);
        grade.setStudent_number("SE/1101/56");
        grade.setExam_id(2);
        grade.setCourse_name("Tentamen Requirements engineering en UX");
        grade.setScore_value(9.5);
        grade.setScore_datetime("2025-03-22 15:30:00");


        String response = grade_toevoegen(grade);


        System.out.println("Response from backend: " + response);
    }


    public static String grade_toevoegen(Grade grade) {

        return "Grade successfully added for student " + grade.getStudent_number();
    }
}
