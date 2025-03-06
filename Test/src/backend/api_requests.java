package backend;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class api_requests{

    public static List<backend.Student> students = new ArrayList<>();
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

            // Print the raw JSON response in the console
            System.out.println("Response Body (Raw JSON):");
            System.out.println(response.body());

            // Parse the JSON response into a list of students
            Gson gson = new Gson();
            Student[] students = gson.fromJson(response.body(), Student[].class);

            // Print the parsed students list as JSON
            System.out.println("Parsed Students (JSON):");
            System.out.println(gson.toJson(students));

            return List.of(students);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }




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

            // Append the new student to the CSV file
            String newStudentData = "\n" + newStudent.getCsv();
//            Files.write(Paths.get(CSV_FILE_PATH), newStudentData.getBytes(), StandardOpenOption.APPEND);

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
                    student.setName(updatedStudent.getName());
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