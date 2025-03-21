import backend.api_requests;
import backend.models.Course;
import backend.models.Exam;
import backend.models.Grade;
import backend.models.GradeGetter;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static backend.api_requests.exam_toevoegen;

public class ExamManagementGUI {

    // Method to display the exams and the "View Exam Details" button in a JFrame
    public static void displayExams(JFrame frame) {

        DefaultTableModel model = new DefaultTableModel(new Object[][]{},
                new String[]{"Course ID", "Course Name", "Semester", "Type", "Date", "Actions"});

        JTable table = new JTable(model) {
            // Override the method to make the "Actions" column render buttons
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 5) {
                    return JButton.class;
                }
                return super.getColumnClass(columnIndex);
            }
        };
// Apply alternating row colors
        table.setDefaultRenderer(Object.class, new TableCellRenderer() {
            private final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = DEFAULT_RENDERER.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                // Alternate row colors: light orange for even rows, white for odd rows
                c.setBackground(row % 2 == 0 ? new Color(230, 174, 135) : Color.WHITE);
                return c;
            }
        });

        table.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Fetch data and populate the table
        fetchExamData(table);

        // Set frame size and center it
        frame.setSize(800, 400);
        frame.setLocationRelativeTo(null);  // Center the frame on the screen
        frame.setVisible(true);

        // Setting up the button rendering
        table.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox()));

        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setSize(800, 400);
        frame.setVisible(true);


        JButton addExamButton = new JButton("Add Exam");
        addExamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Course> courses = api_requests.getCoursesNotInExams(); // Fetch courses from API
                if (courses == null) {
                    JOptionPane.showMessageDialog(frame, "Failed to load courses.");
                    return;
                }

                JComboBox<Course> courseDropdown = new JComboBox<>(courses.toArray(new Course[0]));

                JTextField codeField = new JTextField();
                JTextField examTypeField = new JTextField();
                JTextField examDateField = new JTextField();

                JPanel panel = new JPanel(new GridLayout(0, 1));
                panel.add(new JLabel("Select Course:"));
                panel.add(courseDropdown);
                panel.add(new JLabel("Exam Type (Regulier/Her):"));
                panel.add(examTypeField);
                panel.add(new JLabel("Exam Date (YYYY-MM-DD, optional):"));
                panel.add(examDateField);

                int result = JOptionPane.showConfirmDialog(frame, panel, "Add New Exam", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    try {
                        Course selectedCourse = (Course) courseDropdown.getSelectedItem();
                        Integer courseId = selectedCourse != null ? selectedCourse.getId() : null;
                        String code = codeField.getText().trim().isEmpty() ? null : codeField.getText().trim();
                        String examType = examTypeField.getText().trim();

                        if (!examType.equals("Regulier") && !examType.equals("Her")) {
                            JOptionPane.showMessageDialog(frame, "Invalid exam type! Use 'Regulier' or 'Her'.");
                            return;
                        }

                        String examDateStr = examDateField.getText().trim().isEmpty() ? null : examDateField.getText().trim();

                        if (courseId == null && (code == null || code.isEmpty())) {
                            JOptionPane.showMessageDialog(frame, "Either Course ID or Code must be provided.");
                            return;
                        }

                        // Create the Exam object based on the user inputs
                        Exam newExam = new Exam(courseId, examType, examDateStr);

                        // Send the new exam data to the API
                        String success = exam_toevoegen(newExam);

                        if (success != null && success.contains("New exam inserted successfully")) {
                            JOptionPane.showMessageDialog(frame, "Exam successfully added.");
                            fetchExamData(table);  // Refresh the table with the updated exam list
                        } else {
                            JOptionPane.showMessageDialog(frame, "Error: " + (success != null ? success : "Unknown error"));
                        }

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, "An error occurred: " + ex.getMessage());
                    }
                }
            }
        });




// Add the button at the top of the frame
        JPanel topPanel = new JPanel();
        topPanel.add(addExamButton);
        frame.add(topPanel, BorderLayout.NORTH);

    }

    // Fetch and populate exam data
    private static void fetchExamData(JTable table) {
        List<Exam> exams = api_requests.getExams();

        if (exams != null) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0); // Clear existing rows

            for (Exam exam : exams) {
                // Add the exam details and a "View Exam Details" button for each exam
                model.addRow(new Object[]{
                        exam.getcourse_id(),
                        exam.getcourse_name(),
                        exam.getSemester(),
                        exam.getType(),
                        exam.getDate(),
                        "Cijfers"
                });
            }

        } else {
            JOptionPane.showMessageDialog(null, "Error fetching exam data");
        }
    }

    private static void viewExamGrades(int examId) {
        // Fetch the grades for the selected exam


        List<GradeGetter> grades = api_requests.getGradesForExam(examId);

// Check if grades is null and initialize it if necessary
        if (grades == null) {
            grades = new ArrayList<>();
        }

//        List<Grade> grades = api_requests.getGradesForExam(examId); // Assuming the API returns a list of grades for the exam


            // Create a new JFrame to display grades
            JFrame gradesFrame = new JFrame("Grades for Exam ID: " + examId);
            gradesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            // Create a JPanel to hold everything
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());

            // Column names for the JTable
            String[] columnNames = {"Student Number", "Course", "Score", "Date"};

            // Prepare data for the JTable
        String[][] data = new String[grades.isEmpty() ? 1 : grades.size()][4]; // If no grades, show one empty row
        if (grades.isEmpty()) {
            data[0] = new String[]{"", "", "", ""}; // Placeholder empty row if no grades
        } else {
            for (int i = 0; i < grades.size(); i++) {
                GradeGetter grade = grades.get(i);
                data[i][0] = grade.getStudent_number();
                data[i][1] = grade.getCourse_name();
                data[i][2] = String.valueOf(grade.getScore_value());
                data[i][3] = grade.getScore_datetime().toString(); // Assuming you want to display the full Date object
            }
        }


        // Create the JTable
            DefaultTableModel model = new DefaultTableModel(data, columnNames);
            JTable gradesTable = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(gradesTable);
            panel.add(scrollPane, BorderLayout.CENTER);

            // Create a JPanel for buttons
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout());

            // Create the Update Grade Button
            JButton updateGradeButton = new JButton("Update Grade");
        List<GradeGetter> finalGrades = grades;
        updateGradeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Get the selected row in the table
                    int selectedRow = gradesTable.getSelectedRow();
                    if (selectedRow != -1) {
                        String studentNumber = (String) gradesTable.getValueAt(selectedRow, 0);
                        String course = (String) gradesTable.getValueAt(selectedRow, 1);
                        String currentScore = (String) gradesTable.getValueAt(selectedRow, 2);

                        // Show a dialog to update the grade
                        String newScore = JOptionPane.showInputDialog(gradesFrame,
                                "Update Score for " + studentNumber + " in " + course,
                                "Current Score: " + currentScore);

                        if (newScore != null && !newScore.trim().isEmpty()) {
                            // Update the grade (send to API or update the list)
                            // You should implement the logic to update the grade in the backend (API, DB, etc.)
                            try {
                                double score = Double.parseDouble(newScore); // Convert String to double
                                finalGrades.get(selectedRow).setScore_value(score);
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(gradesFrame, "Invalid score format!", "Error", JOptionPane.ERROR_MESSAGE);
                            }

                            gradesTable.setValueAt(newScore, selectedRow, 2); // Update the table UI
                        }
                    } else {
                        JOptionPane.showMessageDialog(gradesFrame, "Please select a grade to update.");
                    }
                }
            });

            // Create the Add Grade Button
            JButton addGradeButton = new JButton("Add Grade");
            addGradeButton.setForeground(Color.WHITE);

        List<GradeGetter> finalGrades1 = grades;
//        addGradeButton.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    String studentNumber = JOptionPane.showInputDialog(gradesFrame, "Enter Student Number");
//                    String scoreStr = JOptionPane.showInputDialog(gradesFrame, "Enter Score");
//
//                    if (studentNumber != null && scoreStr != null) {
//                        try {
//                            double score = Double.parseDouble(scoreStr); // Convert score input to double
//
//                            Grade newGrade = new Grade();
//                            newGrade.setStudent_number(studentNumber);
//                            newGrade.setExam_id(examId); // Use the exam ID from the method parameter
//                            newGrade.setScore_value(score);
//
//                            String response = exam_toevoegen(newGrade);
//                            JOptionPane.showMessageDialog(gradesFrame, response);
//
//                            if (response.startsWith("Fout")) {
//                                System.err.println(response);
//                            } else {
//                                finalGrades1.add(newGrade);
//                                Object[] newRow = {studentNumber, examId, score, newGrade.getScore_datetime()};
//                                model.addRow(newRow);
//                            }
//                        } catch (NumberFormatException ex) {
//                            JOptionPane.showMessageDialog(gradesFrame, "Invalid number format for Score!", "Error", JOptionPane.ERROR_MESSAGE);
//                        }
//                    }
//                }
//            });
// Inside the Add Grade action listener
        addGradeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentNumber = JOptionPane.showInputDialog(gradesFrame, "Enter Student Number");
                String scoreStr = JOptionPane.showInputDialog(gradesFrame, "Enter Score");

                if (studentNumber != null && scoreStr != null) {
                    try {
                        double score = Double.parseDouble(scoreStr); // Convert score input to double

                        Grade newGrade = new Grade();
                        newGrade.setStudent_number(studentNumber);
                        newGrade.setExam_id(examId); // Use the exam ID from the method parameter
                        newGrade.setScore_value(score);

                        // Get current date as a String for the new grade
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String scoreDateString = sdf.format(new Date());
                        newGrade.setScore_datetime(scoreDateString); // Set the date as String

                        // Send new grade to API
                        String response = exam_toevoegen(newGrade);
                        JOptionPane.showMessageDialog(gradesFrame, response);

                        if (response.startsWith("Fout")) {
                            System.err.println(response);
                        } else {
                            // Convert String to Date when adding to GradeGetter list
                            GradeGetter gradeGetter = new GradeGetter();
                            gradeGetter.setStudent_number(studentNumber);
                            gradeGetter.setCourse_name(gradeGetter.getCourse_name());
                            gradeGetter.setScore_value(score);
                            gradeGetter.setScore_datetime(sdf.parse(scoreDateString)); // Convert String to Date
                            finalGrades1.add(gradeGetter); // Add to GradeGetter list

                            Object[] newRow = {studentNumber, examId, score, scoreDateString};
                            model.addRow(newRow);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(gradesFrame, "Invalid number format for Score!", "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (ParseException ex) {
                        JOptionPane.showMessageDialog(gradesFrame, "Error parsing date.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });



            // Add buttons to the button panel
            buttonPanel.add(updateGradeButton);
            buttonPanel.add(addGradeButton);
            panel.add(buttonPanel, BorderLayout.SOUTH);

            // Add the panel to the JFrame
            gradesFrame.add(panel);

            // Set up the frame size and visibility
            gradesFrame.setSize(600, 400);
            gradesFrame.setLocationRelativeTo(null);  // This will center the frame on the screen
            gradesFrame.setVisible(true);
    }


    // Custom Renderer to display buttons in the "Actions" column
    static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setText("Cijfers");
            setFocusPainted(false);
            setBackground(new Color(0, 28, 111));

        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // Custom Editor to handle button clicks in the "Actions" column
    static class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private int examId;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Get the table from the event's source
                    JTable table = (JTable) SwingUtilities.getAncestorOfClass(JTable.class, button);
                    int row = table.getSelectedRow();
                    examId = (int) table.getValueAt(row, 0); // Get exam ID
                    viewExamGrades(examId);
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            button.setText("Cijfers");
            return button;
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Exam Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        displayExams(frame);
    }


}