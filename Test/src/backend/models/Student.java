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

    // Getters and Setters for each field

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getMajor() {return major;}

    public void setMajor(String major) {
        if (major.equals("SE") || major.equals("BI") || major.equals("NE")) {
            this.major = major;
        } else {
            throw new IllegalArgumentException("Richting moet SE, BI, of NE zijn");
        }
    }

    public int getCohort() {return cohort;}

    public void setCohort(int cohort) {
        if (cohort >= 1101 && cohort <= 1199) {
            this.cohort = cohort;
        } else {
            throw new IllegalArgumentException("Cohort meot tussen 1101 en 1199 zijn");
        }
    }


    // Override toString() for easier debugging and printing
    @Override
    public String toString() {
        return "Student{id=" + id + ", firstName='" + first_name + "', lastName='" + last_name + "', studentNumber='" + student_number + "', gender='" + gender + "', birthdate='" + birthdate + "', major='" + major + "', cohort=" + cohort + "}";
    }
}
