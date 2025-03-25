//package backend;
//
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class api_requestsTest {
//
//    @Test
//    void getStudents() {
//    }
//
//    @Test
//    void getCourses() {
//    }
//
//    @Test
//    void getCoursesNotInExams() {
//    }
//
//    @Test
//    void student_toevoegen() {
//    }
//
//    @Test
//    void exam_toevoegen() {
//    }
//
//    @Test
//    void student_bewerken() {
//    }
//
//    @Test
//    void cijfer_bewerken() {
//    }
//
//    @Test
//    void cijfer_verwijderen() {
//    }
//
//    @Test
//    void student_verwijderen() {
//    }
//
//    @Test
//    void getSemesters() {
//    }
//
//    @Test
//    void getExams() {
//    }
//
//    @Test
//    void getGradesForStudent() {
//    }
//
//    @Test
//    void testExam_toevoegen() {
//    }
//
//    @Test
//    void getGradesForExam() {
//    }
//}




package backend;




import backend.models.Student;

import backend.models.Course;
import backend.models.Exam;
import backend.models.Grade;
import java.util.List;






import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import backend.models.GradeGetter;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;





package backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class ApiRequestsTest {

    private api_requests api;

    @BeforeEach
    void setUp() {

        api = new api_requests();
    }

    @Test
    void testGetStudents() {

        List<Student> students = api.getStudents();


        assertNotNull(students, "Kan niet null zijn");
        assertTrue(students.size() > 0, "Er moet meer dan 0 studenten zin.");


        Student firstStudent = students.get(0);
        assertNotNull(firstStudent.getName(), "Student needs a name.");
        assertNotNull(firstStudent.getId(), "Student needs an ID.");
    }

    @Test
    void testGetCourses() {
        // Example test case for fetching courses
        List<Course> courses = api.getCourses();

        assertNotNull(courses, "Kan niet null zijn");
        assertTrue(courses.size() > 0, "Er moet meer dan 0 courses zijn.");

        // Optionally, check if the first course has valid data
        Course firstCourse = courses.get(0);
        assertNotNull(firstCourse.getName(), "Er is een naam nodig");
        assertNotNull(firstCourse.getId(), "Er is een ID nodig.");
    }

    @Test
    void testGetCoursesNotInExams() {
        // Test for fetching courses not included in exams
        List<Course> courses = api.getCoursesNotInExams();

        assertNotNull(courses, "Kan niet null zijn");
        assertTrue(courses.size() >= 0, "Er is data");
    }

    @Test
    void testStudentToevoegen() {
        // Example of adding a student
        Student newStudent = new Student("SE/1123/030", "Jason Doe");
        String response = api.student_toevoegen(newStudent);

        // Assuming the response should indicate success
        assertEquals("Student added successfully", response, "Het is gelukt");
    }

    @Test
    void testExamToevoegen() {
        // Example of adding an exam
        Exam newExam = new Exam(1, "Math", "2023-01-01", "Regular");
        String response = api.exam_toevoegen(newExam);

        // Assuming the response should indicate success
        assertEquals("Exam added successfully", response, "Gelukt");
    }

    @Test
    void testStudentBewerken() {
        // Example of editing a student
        Student existingStudent = new Student("SE/1123/030", "Jason Doe");
        existingStudent.setName("jonhathan Doe");
        String response = api.student_bewerken(existingStudent);

        assertEquals("Student updated successfully", response, "Update gelukt");
    }

    @Test
    void testCijferBewerken() {
        // Example of editing a grade
        Grade grade = new Grade("SE/1123/030", 90, "2023-01-01");
        grade.setScore(95);  // Editing the grade score
        String response = api.cijfer_bewerken(grade);

        assertEquals("Grade updated successfully", response, "Update gelukt.");
    }

    @Test
    void testCijferVerwijderen() {
        // Example of deleting a grade
        Grade grade = new Grade("SE/1123/030", 90, "2023-01-01");
        String response = api.cijfer_verwijderen(grade);

        assertEquals("Grade deleted successfully", response, "Delete gelukt.");
    }

    @Test
    void testStudentVerwijderen() {

        Student student = new Student("SE/1123/030", "Jason Doe");
        String response = api.student_verwijderen(student);

        assertEquals("Student deleted successfully", response, "Delete gelukt");
    }

    @Test
    void testGetSemesters() {

        List<String> semesters = api.getSemesters();

        assertNotNull(semesters, "Mag niet nul zijn.");
        assertTrue(semesters.size() > 0, "Er moet tenminste 1 sem zijn");
    }

    @Test
    void testGetExams() {

        List<Exam> exams = api.getExams();

        assertNotNull(exams, "Kan niet null zijn");
        assertTrue(exams.size() > 0, "Er moet tenminsite 1 exam zijn.");
    }

    @Test
    void testGetGradesForStudent() {

        List<Grade> grades = api.getGradesForStudent("SE/1123/030");

        assertNotNull(grades, "Kan niet null zijn.");
        assertTrue(grades.size() >= 0, "Er moet valide data zijn.");
    }

    @Test
    void testExamToevoegenResponse() {

        Exam exam = new Exam(1, "Bedrijfssimulatie", "2023-05-01", "Regular");
        String response = api.exam_toevoegen(exam);

        assertEquals("Exam added successfully", response, "Gelukt");
    }

    @Test
    void testGetGradesForExam() {

        List<Grade> grades = api.getGradesForExam(1);

        assertNotNull(grades, "Kan niet null zijn.");
        assertTrue(grades.size() >= 0, "Mag geen invalid data hebben.");
    }
}
