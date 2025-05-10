package backend;

import backend.models.*;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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
import java.util.List;
import java.util.stream.Collectors;

public class api_requests implements api_interface{

    public  List<Student> students = new ArrayList<>();
    private  String apiUrl = "https://trajectplannerapi.dulamari.com/";

    public  List<Student> getStudents(String query) {
        try {
            HttpClient client = HttpClient.newHttpClient();


            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl + "students"))
                    .build();


            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


            Gson gson = new Gson();
            Student[] students = gson.fromJson(response.body(), Student[].class);

            return List.of(students);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public  List<Course> getCourses() {
        try {
            HttpClient client = HttpClient.newHttpClient();


            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl + "courses"))
                    .build();


            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


            Gson gson = new Gson();
            Course[] courses = gson.fromJson(response.body(), Course[].class);

            return List.of(courses);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public  String getGroepsleden() {
        return  "SE/1123/080... - Othniel Samuels\nSE1123/039... - Eleanor Lokhai\nSE/1123/... - Bindya\nSE/1123/... - Dharandjai Patan";
    }

    public  List<Course> getCoursesNotInExams() {
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




    public  String student_toevoegen(Student student) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            Gson gson = new Gson();
            String jsonInputString = gson.toJson(student);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl + "students"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonInputString))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


            System.out.println("Response Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());

            if (response.statusCode() == 201 || response.statusCode() == 200) {
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



    public  String exam_toevoegen(Exam exam) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            Gson gson = new Gson();
            String jsonInputString = gson.toJson(exam);


            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl + "exams"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonInputString))
                    .build();


            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


            System.out.println("Response Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());

            if (response.statusCode() == 201 || response.statusCode() == 200) {

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



    public  String student_bewerken(Student student) {
        try {

            HttpClient client = HttpClient.newHttpClient();
            Gson gson = new Gson();
            String jsonInputString = gson.toJson(student);



            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl + "students")) // Append student ID to URL
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonInputString))
                    .build();


            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


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


    public  String cijfer_bewerken(Grade grade) {
        try {
            grade.setScore_id(grade.getId());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = sdf.format(new Date());  // No extra quotes
            grade.setScore_datetime(formattedDate);


            HttpClient client = HttpClient.newHttpClient();
            Gson gson = new Gson();

            String jsonInputString = gson.toJson(grade);


            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl + "scores/")) // Include ID in the URL
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonInputString))
                    .build();



            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


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




    public  String cijfer_verwijderen(Grade grade) {
        try {
            grade.setId(grade.getId());
            HttpClient client = HttpClient.newHttpClient();
            Gson gson = new Gson();


            String jsonInputString = gson.toJson(grade);
            System.out.println("JSON Body: " + jsonInputString);


            URI uri = URI.create(apiUrl + "scores");


            HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
            connection.setRequestMethod("DELETE");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.getOutputStream().write(jsonInputString.getBytes(StandardCharsets.UTF_8));


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


    @Override
    public void clearFields(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }

    public void CalculateAverage(java.util.List<GradeGetter> grades) {

        Map<String, Map<Integer, Double>> courseGradesMap = new LinkedHashMap<>(); // Course -> (Semester -> Grade)
        Set<Integer> semesters = new TreeSet<>(); // Keep semesters in sorted order

        for (GradeGetter grade : grades) {
            courseGradesMap
                    .computeIfAbsent(grade.getCourse_name(), k -> new HashMap<>())
                    .put(grade.getSemester(), grade.getScore_value());
            semesters.add(grade.getSemester());
        }


        String[] columnNames = new String[semesters.size() + 2];
        columnNames[0] = "Course";
        int colIndex = 1;
        for (Integer semester : semesters) {
            columnNames[colIndex++] = "Semester " + semester;
        }
        columnNames[colIndex] = "Average";


        DefaultTableModel gradesTableModel = new DefaultTableModel(columnNames, 0);


        for (Map.Entry<String, Map<Integer, Double>> entry : courseGradesMap.entrySet()) {
            String courseName = entry.getKey();
            Map<Integer, Double> semesterGrades = entry.getValue();

            double total = 0;
            int count = 0;

            Object[] rowData = new Object[columnNames.length];
            rowData[0] = courseName;

            colIndex = 1;
            for (Integer semester : semesters) {
                Double grade = semesterGrades.get(semester);
                rowData[colIndex++] = (grade != null) ? grade : null;
                if (grade != null) {
                    total += grade;
                    count++;
                }
            }

            double average = count > 0 ? total / count : 0;
            rowData[colIndex] = average;

            gradesTableModel.addRow(rowData);
        }


        JTable gradesTable = new JTable(gradesTableModel);
        gradesTable.setRowHeight(30);
        gradesTable.setShowGrid(true);
        gradesTable.setGridColor(Color.LIGHT_GRAY);
        gradesTable.setIntercellSpacing(new Dimension(1, 1));


        JScrollPane scrollPane = new JScrollPane(gradesTable);


        JFrame gradesFrame = new JFrame("Grades for Student");
        gradesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gradesFrame.setSize(800, 400);
        gradesFrame.add(scrollPane, BorderLayout.CENTER);
        gradesFrame.setVisible(true);
    }

    public  String student_verwijderen(Student student) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            Gson gson = new Gson();


            String jsonInputString = gson.toJson(student);
            System.out.println("JSON Body: " + jsonInputString);


            URI uri = URI.create(apiUrl + "students");


            HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
            connection.setRequestMethod("DELETE");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.getOutputStream().write(jsonInputString.getBytes(StandardCharsets.UTF_8));


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

    public  List<Semester> getSemesters() {
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


    public  List<Exam> getExams() {
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


    public  List<GradeGetter> getGradesForStudent(String student_number) {
        try {
            String modifiedStudentNumber = student_number.replace("/", "-");

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl+"scores/"+ modifiedStudentNumber))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Gson gson = new Gson();

                GradeGetter[] allGrades = gson.fromJson(response.body(), GradeGetter[].class);

                return List.of(allGrades);
            } else {
                System.err.println("Error fetching grades: " + response.statusCode());
                return Collections.emptyList();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


    public  String cijfer_toevoegen(Grade grade) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            Gson gson = new Gson();
            String jsonInputString = gson.toJson(grade);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = "'" + sdf.format(new Date()) + "'";
            grade.setScore_datetime(formattedDate);



            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl + "scores"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonInputString))
                    .build();


            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


            System.out.println("Response Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());

            if (response.statusCode() == 201 || response.statusCode() == 200) {

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

    public  List<GradeGetter> getGradesForExam(int exam_id) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl + "scores"))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Gson gson = new Gson();
                GradeGetter[] grades = gson.fromJson(response.body(), GradeGetter[].class);


                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");



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


     class StudentsHandler implements HttpHandler {
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

            String query = exchange.getRequestURI().getQuery();
            int id = Integer.parseInt(query.split("=")[1]);

            students.removeIf(student -> student.getId() == id);

            exchange.sendResponseHeaders(200, -1); // OK
        }

    }
}
