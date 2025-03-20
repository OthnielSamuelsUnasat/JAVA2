import backend.api_requests;
import backend.models.Exam;
import backend.models.Grade;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

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
        List<Grade> grades = api_requests.getGradesForExam(examId); // Assuming the API returns a list of grades for the exam

        if (grades != null) {
            // Create a new JFrame to display grades
            JFrame gradesFrame = new JFrame("Grades for Exam ID: " + examId);
            gradesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            // Create a JPanel to hold everything
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());

            // Column names for the JTable
            String[] columnNames = {"Student Number", "Course", "Score", "Date"};

            // Prepare data for the JTable
            String[][] data = new String[grades.size()][4];
            for (int i = 0; i < grades.size(); i++) {
                Grade grade = grades.get(i);
                data[i][0] = grade.getStudent_number();
                data[i][1] = grade.getCourse_name();
                data[i][2] = grade.getScore_value();
                data[i][3] = grade.getScore_datetime().toString(); // Assuming you want to display the full Date object
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
                            grades.get(selectedRow).setScore_value(newScore);
                            gradesTable.setValueAt(newScore, selectedRow, 2); // Update the table UI
                        }
                    } else {
                        JOptionPane.showMessageDialog(gradesFrame, "Please select a grade to update.");
                    }
                }
            });

            // Create the Add Grade Button
            JButton addGradeButton = new JButton("Add Grade");
            addGradeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Show a dialog to add a new grade
                    String studentNumber = JOptionPane.showInputDialog(gradesFrame, "Enter Student Number");
                    String course = JOptionPane.showInputDialog(gradesFrame, "Enter Course Name");
                    String score = JOptionPane.showInputDialog(gradesFrame, "Enter Score");

                    if (studentNumber != null && course != null && score != null) {
                        // Create a new Grade object
                        Grade newGrade = new Grade();
                        newGrade.setStudent_number(studentNumber);
                        newGrade.setCourse_name(course);
                        newGrade.setScore_value(score);
                        newGrade.setScore_datetime(new Date()); // Set the current date/time

                        // Add to the grades list and update the table
                        grades.add(newGrade);
                        Object[] newRow = {studentNumber, course, score, newGrade.getScore_datetime()};
                        model.addRow(newRow); // Add row to the JTable model
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
        } else {
            JOptionPane.showMessageDialog(null, "Error: Grades not found.");
        }
    }


    // Custom Renderer to display buttons in the "Actions" column
    static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setText("Cijfers");
            setFocusPainted(false);
            setBackground(Color.LIGHT_GRAY);
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
            button.setText("View Exam Details");
            return button;
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Exam Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        displayExams(frame);
    }
}
