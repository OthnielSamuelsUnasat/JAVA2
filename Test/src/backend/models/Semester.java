package backend.models;

import java.util.List;

public class Semester {
    private int id;
    private String semester_name;
    private List<Course> courses;

    // Constructr
    public Semester(int id, String semesterName, List<Course> courses) {
        this.id = id;
        this.semester_name = semesterName;
        this.courses = courses;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSemesterName() {
        return semester_name;
    }

    public void setSemesterName(String semesterName) {
        this.semester_name = semesterName;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }
}
