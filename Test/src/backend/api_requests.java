package backend;

import backend.models.*;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class api_requests{

    public static List<Student> students = new ArrayList<>();
    private static String apiUrl = "https://trajectplannerapi.dulamari.com/";

    public static List<Student> getStudents(String query) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            // Create the HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl + "students"))
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Parse the JSON response into a list of students
            Gson gson = new Gson();
            Student[] students = gson.fromJson(response.body(), Student[].class);

            return List.of(students);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static List<Course> getCourses() {
        try {
            HttpClient client = HttpClient.newHttpClient();

            // Create the HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl + "courses"))
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Parse the JSON response into a list of courses
            Gson gson = new Gson();
            Course[] courses = gson.fromJson(response.body(), Course[].class);

            return List.of(courses); // Convert the array to a list
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Course> getCoursesNotInExams() {
        try {
            List<Course> courses = getCourses();
            List<Exam> exams = getExams();

            if (courses == null || exams == null) {
                return Collections.emptyList();
            }

            Set<String> examCourseNames = exams.stream()
                    .map(Exam::getcourse_name)
                    .collect(Collectors.toSet());

            return courses.stream()
                    .filter(course -> !examCourseNames.contains(course.getName()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }




    public static String student_toevoegen(Student student) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            Gson gson = new Gson();
            String jsonInputString = gson.toJson(student);

            // Create the HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl + "students"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonInputString))
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Log response details
            System.out.println("Response Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());

            if (response.statusCode() == 201 || response.statusCode() == 200) {
                // Parse the JSON response into a Student object
                return response.body();
            } else {
                return ("Fout bij opslaan: " + response.statusCode() +  response.body());
            }
        } catch (JsonSyntaxException e) {
            return("Fout bij JSON-parsing: " + e.getMessage());

        } catch (Exception e) {
            return("Fout bij API-aanroep: " + e.getMessage());
        }
    }



    public static String exam_toevoegen(Exam exam) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            Gson gson = new Gson();
            String jsonInputString = gson.toJson(exam);

            // Create the HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl + "exams"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonInputString))
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Log response details
            System.out.println("Response Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());

            if (response.statusCode() == 201 || response.statusCode() == 200) {
                // Parse the JSON response into a Student object
                return response.body();
            } else {
                return ("Fout bij opslaan: " + response.statusCode() +  response.body());
            }
        } catch (JsonSyntaxException e) {
            return("Fout bij JSON-parsing: " + e.getMessage());

        } catch (Exception e) {
            return("Fout bij API-aanroep: " + e.getMessage());
        }
    }



    public static String student_bewerken(Student student) {
        try {

            HttpClient client = HttpClient.newHttpClient();
            Gson gson = new Gson();
            String jsonInputString = gson.toJson(student);


            // Create the HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl + "students")) // Append student ID to URL
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonInputString))
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Log response details
            System.out.println("Response Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());

            if (response.statusCode() == 200 || response.statusCode() == 204) {
                return "Student succesvol bijgewerkt.";
            } else {
                return "Fout bij bijwerken: " + response.statusCode() + " " + response.body();
            }
        } catch (JsonSyntaxException e) {
            return "Fout bij JSON-parsing: " + e.getMessage();
        } catch (Exception e) {
            return "Fout bij API-aanroep: " + e.getMessage();
        }
    }


    public static String cijfer_bewerken(Grade grade) {
        try {
            grade.setScore_id(grade.getId());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = sdf.format(new Date());  // No extra quotes
            grade.setScore_datetime(formattedDate);


            HttpClient client = HttpClient.newHttpClient();
            Gson gson = new Gson();

            String jsonInputString = gson.toJson(grade);

            // Create the HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl + "scores/")) // Include ID in the URL
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonInputString))
                    .build();


            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Log response details
            System.out.println("Response Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());

            if (response.statusCode() == 200 || response.statusCode() == 204) {
                return "Student succesvol bijgewerkt.";
            } else {
                return "Fout bij bijwerken: " + response.statusCode() + " " + response.body();
            }
        } catch (JsonSyntaxException e) {
            return "Fout bij JSON-parsing: " + e.getMessage();
        } catch (Exception e) {
            return "Fout bij API-aanroep: " + e.getMessage();
        }
    }





    public static String cijfer_verwijderen(Grade grade) {
        try {
            // Ensure grade ID is provided
            int gradeId = grade.getId();

            if (gradeId == 0) {
                return "Error: Grade ID is missing!";
            }

            HttpClient client = HttpClient.newHttpClient();

            // Create the DELETE request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl + "scores/" + gradeId)) // Correct API endpoint
                    .header("Content-Type", "application/json")
                    .DELETE()
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Log response details
            System.out.println("Response Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());

            // Handle success/failure based on status code
            if (response.statusCode() == 200 || response.statusCode() == 204) {
                return "Student succesvol verwijderd.";
            } else {
                return "Fout bij verwijderen: " + response.statusCode() + " " + response.body();
            }
        } catch (Exception e) {
            return "Fout bij API-aanroep: " + e.getMessage();
        }
    }






    public static String student_verwijderen(Student student) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            Gson gson = new Gson();

            // Convert the student object to JSON string using Gson
            String jsonInputString = gson.toJson(student);
            System.out.println("JSON Body: " + jsonInputString); // Debugging: Check JSON output

            // Construct the URI for the "DELETE" action (verify with your API endpoint)
            URI uri = URI.create(apiUrl + "students");

            // Manually send the request using HttpURLConnection since Java HttpRequest API does not support bodies in DELETE directly
            HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
            connection.setRequestMethod("DELETE");
            connection.setDoOutput(true); // Enable sending body
            connection.setRequestProperty("Content-Type", "application/json");
            connection.getOutputStream().write(jsonInputString.getBytes(StandardCharsets.UTF_8)); // Send the JSON body

            // Get the response code and handle it
            int responseCode = connection.getResponseCode();
            String responseMessage = connection.getResponseMessage();

            System.out.println("Response Code: " + responseCode);
            System.out.println("Response Body: " + responseMessage);

            if (responseCode == 200 || responseCode == 204) {
                return "Student succesvol verwijderd.";
            } else {
                return "Fout bij verwijderen: " + responseCode + " " + responseMessage;
            }
        } catch (Exception e) {
            return "Fout bij API-aanroep: " + e.getMessage();
        }
    }

    public static List<Semester> getSemesters() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl + "semesters"))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Gson gson = new Gson();
                Semester[] semesters = gson.fromJson(response.body(), Semester[].class);
                return List.of(semesters);

            } else {
                System.err.println("Error fetching semesters: " + response.statusCode());
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static List<Exam> getExams() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl + "exams"))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Gson gson = new Gson();
                Exam[] exams = gson.fromJson(response.body(), Exam[].class);
                return List.of(exams);

            } else {
                System.err.println("Error fetching semesters: " + response.statusCode());
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String exam_toevoegen(Grade grade) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            Gson gson = new Gson();
            String jsonInputString = gson.toJson(grade);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = "'" + sdf.format(new Date()) + "'";  // Add quotes around the formatted date
            grade.setScore_datetime(formattedDate);  // Now this will be sent as a string with quotes


            // Create the HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl + "scores"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonInputString))
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Log response details
            System.out.println("Response Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());

            if (response.statusCode() == 201 || response.statusCode() == 200) {
                // Parse the JSON response into a Student object
                return response.body();
            } else {
                return ("Fout bij opslaan: " + response.statusCode() +  response.body());
            }
        } catch (JsonSyntaxException e) {
            return("Fout bij JSON-parsing: " + e.getMessage());

        } catch (Exception e) {
            return("Fout bij API-aanroep: " + e.getMessage());
        }
    }

    public static List<GradeGetter> getGradesForExam(int exam_id) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl + "scores"))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Gson gson = new Gson();
                GradeGetter[] grades = gson.fromJson(response.body(), GradeGetter[].class);

                // Convert score_datetime string to Date if needed
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


                // Convert the array to a list and filter by exam_id
                List<GradeGetter> filteredGrades = new ArrayList<>(Arrays.asList(grades));
                filteredGrades = filteredGrades.stream()
                        .filter(grade -> grade.getExam_id() == exam_id)
                        .collect(Collectors.toList());

                return filteredGrades;
            } else {
                System.err.println("Error fetching grades: " + response.statusCode());
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }





//
//
//    public static List<Grade> getGradesForExam(int exam_id) {
//        try {
//            HttpClient client = HttpClient.newHttpClient();
//            HttpRequest request = HttpRequest.newBuilder()
//                    .uri(URI.create(apiUrl + "scores"))
//                    .build();
//
//            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//            if (response.statusCode() == 200) {
//                Gson gson = new Gson();
//                Grade[] grades = gson.fromJson(response.body(), Grade[].class);
//
//                // Convert the array to a list and filter by exam_id
//                List<Grade> filteredGrades = new ArrayList<>(Arrays.asList(grades));
//                filteredGrades = filteredGrades.stream()
//                        .filter(grade -> grade.getExam_id() == exam_id)
//                        .collect(Collectors.toList());
//
//                return filteredGrades;
//
//            } else {
//                System.err.println("Error fetching grades: " + response.statusCode());
//                return null;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }





    static class StudentsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            switch (exchange.getRequestMethod()) {
                case "GET":
                    handleGetRequest(exchange);
                    break;
                case "POST":
                    handlePostRequest(exchange);
                    break;
                case "PUT":
                    handlePutRequest(exchange);
                    break;
                case "DELETE":
                    handleDeleteRequest(exchange);
                    break;
                default:
                    exchange.sendResponseHeaders(405, -1); // Method Not Allowed
                    break;
            }
        }

        private void handleGetRequest(HttpExchange exchange) throws IOException {
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(students);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, jsonResponse.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(jsonResponse.getBytes());
            os.close();
        }

        private void handlePostRequest(HttpExchange exchange) throws IOException {
            InputStream is = exchange.getRequestBody();
            String body = new String(is.readAllBytes());
            Gson gson = new Gson();
            Student newStudent = gson.fromJson(body, Student.class);

            students.add(newStudent);

            String newStudentData = "\n" + newStudent;

            exchange.sendResponseHeaders(201, -1); // Created
        }

        private void handlePutRequest(HttpExchange exchange) throws IOException {
            // Implementation for handling PUT request to update an existing student
            InputStream is = exchange.getRequestBody();
            String body = new String(is.readAllBytes());
            Gson gson = new Gson();
            Student updatedStudent = gson.fromJson(body, Student.class);

            for (Student student : students) {
                if (student.getId() == updatedStudent.getId()) {
                    student.setFirstName(updatedStudent.getFirstName());
                    break;
                }
            }
            exchange.sendResponseHeaders(200, -1); // OK
        }

        private void handleDeleteRequest(HttpExchange exchange) throws IOException {
            // Implementation for handling DELETE request to remove a student
            String query = exchange.getRequestURI().getQuery();
            int id = Integer.parseInt(query.split("=")[1]);

            students.removeIf(student -> student.getId() == id);

            exchange.sendResponseHeaders(200, -1); // OK
        }



}
}
class StudentDeleteRequest {
    private int student_id;

    // Constructor, Getter, and Setter
    public StudentDeleteRequest(int student_id) {
        this.student_id = student_id;
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }
}