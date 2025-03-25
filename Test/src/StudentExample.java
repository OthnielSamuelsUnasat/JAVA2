import backend.api_requests;
import backend.models.Student;

public class StudentExample {
    backend.api_requests api_requests = new api_requests();

    public void main(String[] args) {

        Student student = new Student(100, "Johnny", "Doe", "SE/1121/121", "M", "2000-01-01");


        student.setMajor("SE");
        student.setCohort(1101);
        student.setTotal_ec(120);
        student.setPassword("SecurePassword123");


        String response = api_requests.student_toevoegen(student);


        System.out.println(response);
    }
}
