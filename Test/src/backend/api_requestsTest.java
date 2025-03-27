package backend;

import backend.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class ApiRequestsTest {

    private api_requests api;

    @BeforeEach
    void setUp() {

        api = new api_requests();
    }

    @Test
    void testGetStudents() {

        List<Student> students = api.getStudents("");


        assertNotNull(students, "Kan niet null zijn");
        assertTrue(students.size() > 0, "Er moet meer dan 0 studenten zin.");


        Student firstStudent = students.get(0);
        assertNotNull(firstStudent.getFirstName(), "Student needs a name.");
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
        Student newStudent = new Student();
        newStudent.setFirstName("Othniel");
        newStudent.setLastName("Samuels");
        newStudent.setMajor("SE");
        newStudent.setCohort(1129);
        newStudent.setGender("M");
        newStudent.setPassword("TestPassw");
        newStudent.setBirthdate("2003-10-16");

        String response = api.student_toevoegen(newStudent);

        // Assuming the response should indicate success
        assertEquals("{\"message\":\"New student inserted successfully.\"}", response, "Het is gelukt");
    }

    @Test
    void testExamToevoegen() {
        // Example of adding an exam
        Exam newExam = new Exam(2,"Regulier","2025-03-27");
        String response = api.exam_toevoegen(newExam);

        // Assuming the response should indicate success
        assertEquals("{\"message\":\"New exam inserted successfully.\"}", response, "Gelukt");
    }

    @Test
    void testCijferVerwijderen() {
        // Example of deleting a grade
        Grade grade = new Grade();
        grade.setId(21);
        String response = api.cijfer_verwijderen(grade);

        assertEquals("Student succesvol verwijderd.", response, "Delete gelukt.");
    }

    @Test
    void testStudentVerwijderen() {

        Student student = new Student();
        student.setStudentNumber("SE/1129/128");
        student.setId(129);
        String response = api.student_verwijderen(student);

        assertEquals("Student succesvol verwijderd.", response, "Delete gelukt");
    }

    @Test
    void testGetSemesters() {

        List<Semester> semesters = api.getSemesters();

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

        List<GradeGetter> grades = api.getGradesForStudent("SE/1123/40");

        assertNotNull(grades, "Kan niet null zijn.");
        assertTrue(grades.size() >= 0, "Er moet valide data zijn.");
    }

    @Test
    void testGetGradesForExam() {

        List<GradeGetter> grades = api.getGradesForExam(21);
        assertNotNull(grades, "Kan niet null zijn.");
        assertTrue(grades.size() >= 0, "Mag geen invalid data hebben.");
    }
}
