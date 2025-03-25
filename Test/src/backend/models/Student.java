package backend.models;

public class Student {
    private int id;
    private String first_name;
    private String last_name;
    private String student_number;
    private String gender;
    private String birthdate;
    private String major;
    private int cohort;
    private int total_ec;
    private String password;
    public Student() {}


    public Student(int id,String firstName, String lastName, String studentNumber, String gender, String birthdate) {
        this.id = id;
        this.first_name = firstName;
        this.last_name = lastName;
        this.student_number = studentNumber;
        this.gender = gender;
        this.setBirthdate(birthdate);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTotal_ec() {
        return total_ec;
    }

    public void setTotal_ec(int total_ec) {
        this.total_ec = total_ec;
    }

    public String getFirstName() {
        return first_name;
    }

    public void setFirstName(String firstName) {
        this.first_name = firstName;
    }

    public String getLastName() {
        return last_name;
    }

    public void setLastName(String lastName) {
        this.last_name = lastName;
    }

    public String getStudentNumber() {
        return student_number;
    }

    public void setStudentNumber(String studentNumber) {
        this.student_number = studentNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public int getCohort() {
        return cohort;
    }

    public void setCohort(int cohort) {
        this.cohort = cohort;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Student{id=" + id + ", firstName='" + first_name + "', lastName='" + last_name + "', studentNumber='" + student_number + "', gender='" + gender + "', birthdate='" + birthdate + "', major='" + major + "', cohort=" + cohort + ", totalEC=" + total_ec + "}";
    }
}
