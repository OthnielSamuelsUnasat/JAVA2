package backend;

import backend.models.*;

import javax.swing.*;
import java.util.List;

public interface api_interface {

     String getGroepsleden();
     String student_toevoegen(Student student);
     String cijfer_toevoegen(Grade grade);
     void CalculateAverage(java.util.List<GradeGetter> grades);
     void clearFields(JTextField... fields);



     List<Student> getStudents(String query);
     List<Course> getCourses();
     List<Course> getCoursesNotInExams();
     String exam_toevoegen(Exam exam);
     String student_bewerken(Student student);
     String cijfer_bewerken(Grade grade);
     String cijfer_verwijderen(Grade grade);
     String student_verwijderen(Student student);
     List<Semester> getSemesters();
     List<Exam> getExams();
     List<GradeGetter> getGradesForStudent(String student_number);
     List<GradeGetter> getGradesForExam(int exam_id);
}
