package backend.models;

public class Course {
    private int id;
    private String course_name;
    private String name;
    private String code;
    private String semester_name;
    private int ec;
    private String course_code;
    private String course_description;
    private int block;


    public Course() {}

    public Course(int id, String course_name, String name, String code, String semester_name, int ec, String courseCode, String courseDescription, int block) {
        this.id = id;
        this.course_name = course_name;
        this.name = name;
        this.code = code;
        this.semester_name = semester_name;
        this.ec = ec;
        this.course_code = courseCode;
        this.course_description = courseDescription;
        this.block = block;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourseName() {
        return course_name;
    }

    public void setCourseName(String course_name) {
        this.course_name = course_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSemesterName() {
        return semester_name;
    }

    public void setSemesterName(String semester_name) {
        this.semester_name = semester_name;
    }

    public int getEc() {
        return ec;
    }

    public void setEc(int ec) {
        this.ec = ec;
    }

    public String getCourseCode() {
        return course_code;
    }

    public void setCourseCode(String courseCode) {
        this.course_code = courseCode;
    }

    public String getCourseDescription() {
        return course_description;
    }

    public void setCourseDescription(String courseDescription) {
        this.course_description = courseDescription;
    }

    public int getBlock() {
        return block;
    }

    public void setBlock(int block) {
        this.block = block;
    }

    @Override
    public String toString() {
        return "Name: " + this.name + ", Code: " + this.code;
    }

}
