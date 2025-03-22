package backend.models;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.JsonAdapter;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Grade {
    public Grade (){}

    private int id;
    private int score_id;
    private int student_id;
    private String student_number;
    private int exam_id;
    private String course_name;
    private double score_value;
    @JsonAdapter(DateDeserializer.class)
    private String score_datetime;



    public static class DateDeserializer implements JsonDeserializer<Date> {
        @Override
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            String dateString = json.getAsString();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                return format.parse(dateString);
            } catch (ParseException e) {
                throw new JsonParseException("Failed to parse date: " + dateString, e);
            }
        }
    }
    // Constructor
    public Grade(int id, int student_id, String student_number, int exam_id, String course_name, double score_value, String score_datetime) {
        this.id = id;
        this.student_id = student_id;
        this.student_number = student_number;
        this.exam_id = exam_id;
        this.course_name = course_name;
        this.score_value = score_value;
        this.score_datetime = score_datetime;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        this.score_id = id;

    }

    public int getScore_id() {
        return score_id;
    }

    public void setScore_id(int score_id) {
        this.score_id = score_id;
    }




    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public String getStudent_number() {
        return student_number;
    }

    public void setStudent_number(String student_number) {
        this.student_number = student_number;
    }

    public int getExam_id() {
        return exam_id;
    }

    public void setExam_id(int exam_id) {
        this.exam_id = exam_id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public double getScore_value() {
        return score_value;
    }

    public void setScore_value(double score_value) {
        this.score_value = score_value;
    }

    public String getScore_datetime() {
        return score_datetime;
    }

    public void setScore_datetime(String score_datetime) {
        this.score_datetime = score_datetime;
    }

    // Optional: Override toString() for easy printing
    @Override
    public String toString() {
        return "Grade{" +
                "id=" + id +
                ", student_id=" + student_id +
                ", student_number='" + student_number + '\'' +
                ", exam_id=" + exam_id +
                ", course_name='" + course_name + '\'' +
                ", score_value='" + score_value + '\'' +
                ", score_datetime=" + score_datetime +
                '}';
    }
}
