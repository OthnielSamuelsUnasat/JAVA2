package backend.models;

import java.util.Date;

public class Exam {
    private int id;
    private int course_id;
    private String course_name;
    private int semester;
    private String type;
    private String exam_type;
    private Date date;
    private String exam_date;

    // Constructor
    public Exam(Integer course_id, String exam_type, String exam_date) {
        this.course_id = course_id;
        this.exam_type = exam_type;
        this.exam_date = exam_date;
    }

    public Exam(int id, int course_id, String course_name, int semester, String type, Date date) {
        this.id = id;
        this.course_id = course_id;
        this.course_name = course_name;
        this.semester = semester;
        this.type = type;
        this.date = date;
    }



    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getcourse_id() {
        return course_id;
    }

    public void setcourse_id(int course_id) {
        this.course_id = course_id;
    }

    public String getcourse_name() {
        return course_name;
    }

    public void setcourse_name(String course_name) {
        this.course_name = course_name;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Exam{" +
                "id=" + id +
                ", course_id=" + course_id +
                ", course_name='" + course_name + '\'' +
                ", semester=" + semester +
                ", type='" + type + '\'' +
                ", date=" + date +
                '}';
    }
}
