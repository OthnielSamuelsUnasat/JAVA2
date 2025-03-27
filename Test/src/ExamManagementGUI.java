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


public class ExamManagementGUI {
    api_requests api_requests = new api_requests();


    public void displayExams(JFrame frame) {

        DefaultTableModel model = new DefaultTableModel(new Object[][]{},
                new String[]{"Course ID", "Course Name", "Semester", "Type", "Date", "Actions"});

        JTable table = new JTable(model) {

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 5) {
                    return JButton.class;
                }
                return super.getColumnClass(columnIndex);
            }
        };

        table.setDefaultRenderer(Object.class, new TableCellRenderer() {
            private final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = DEFAULT_RENDERER.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                c.setBackground(row % 2 == 0 ? new Color(230, 174, 135) : Color.WHITE);
                return c;
            }
        });

        table.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);


        fetchExamData(table);


        frame.setSize(800, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


        table.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox()));

        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setSize(800, 400);
        frame.setVisible(true);


        JButton addExamButton = new JButton("Add Exam");
        addExamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Course> courses = api_requests.getCoursesNotInExams();
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


                        Exam newExam = new Exam(courseId, examType, examDateStr);


                        String success = api_requests.exam_toevoegen(newExam);

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





        JPanel topPanel = new JPanel();
        topPanel.add(addExamButton);
        frame.add(topPanel, BorderLayout.NORTH);

    }


    private  void fetchExamData(JTable table) {
        List<Exam> exams = api_requests.getExams();

        if (exams != null) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0); // Clear existing rows

            for (Exam exam : exams) {

                model.addRow(new Object[]{
                        exam.getcourse_id(),
                        exam.getcourse_name(),
                        exam.getSemester(),
                        exam.getType(),
                        exam.getDate(),
                        "Cijfers",
                        "Edit exam"
                });
            }

        } else {
            JOptionPane.showMessageDialog(null, "Error fetching exam data");
        }
    }

    private  void viewExamGrades(int examId) {



        List<GradeGetter> grades = api_requests.getGradesForExam(examId);


        if (grades == null) {
            grades = new ArrayList<>();
        }



            JFrame gradesFrame = new JFrame("Grades for Exam ID: " + examId);
            gradesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());


            String[] columnNames = {"Student Number", "Course", "Score", "Date"};


        String[][] data = new String[grades.isEmpty() ? 1 : grades.size()][4];
        if (grades.isEmpty()) {
            data[0] = new String[]{"", "", "", ""};
        } else {
            for (int i = 0; i < grades.size(); i++) {
                GradeGetter grade = grades.get(i);
                data[i][0] = grade.getStudent_number();
                data[i][1] = grade.getCourse_name();
                data[i][2] = String.valueOf(grade.getScore_value());
                data[i][3] = grade.getScore_datetime().toString();
            }
        }



            DefaultTableModel model = new DefaultTableModel(data, columnNames);

        JTable gradesTable = new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? new Color(230, 174, 135) : Color.WHITE);
                }
                return c;
            }
        };


        JScrollPane scrollPane = new JScrollPane(gradesTable);
            panel.add(scrollPane, BorderLayout.CENTER);


            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout());


        JButton updateGradeButton = new JButton("Update Grade");
        List<Grade> finalGrades = convertToGradeList(grades);

        updateGradeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = gradesTable.getSelectedRow();
                if (selectedRow != -1) {
                    String studentNumber = (String) gradesTable.getValueAt(selectedRow, 0);
                    String course = (String) gradesTable.getValueAt(selectedRow, 1);
                    String currentScore = gradesTable.getValueAt(selectedRow, 2).toString();

                    String newScore = JOptionPane.showInputDialog(gradesFrame,
                            "Update Score for " + studentNumber + " in " + course,
                            currentScore);

                    if (newScore != null && !newScore.trim().isEmpty()) {
                        try {
                            double score = Double.parseDouble(newScore);

                            Grade gradeToUpdate = finalGrades.get(selectedRow);


                            if (gradeToUpdate.getId() == 0) {
                                JOptionPane.showMessageDialog(gradesFrame, "Error: Grade ID is missing!", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            gradeToUpdate.setScore_value(score);

                            new Thread(() -> {
                                String response = api_requests.cijfer_bewerken(gradeToUpdate);
                                SwingUtilities.invokeLater(() -> {
                                    JOptionPane.showMessageDialog(gradesFrame, response);
                                    if (response.startsWith("Student succesvol bijgewerkt")) {
                                        gradesTable.setValueAt(newScore, selectedRow, 2);
                                    }
                                });
                            }).start();

                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(gradesFrame, "Invalid score format!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(gradesFrame, "Please select a grade to update.");
                }
            }
        });




        JButton deleteGradeButton = new JButton("Delete Grade");

        deleteGradeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = gradesTable.getSelectedRow();
                if (selectedRow != -1) {
                    int confirm = JOptionPane.showConfirmDialog(gradesFrame, "Are you sure you want to delete this grade?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        Grade gradeToDelete = finalGrades.get(selectedRow);

                        if (gradeToDelete.getId() == 0) {
                            JOptionPane.showMessageDialog(gradesFrame, "Error: Grade ID is missing!", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        new Thread(() -> {
                            String response = api_requests.cijfer_verwijderen(gradeToDelete);
                            SwingUtilities.invokeLater(() -> {
                                JOptionPane.showMessageDialog(gradesFrame, response);
                                if (response.startsWith("Student succesvol verwijderd")) {
                                    finalGrades.remove(selectedRow);
                                    ((DefaultTableModel) gradesTable.getModel()).removeRow(selectedRow);
                                }
                            });
                        }).start();
                    }
                } else {
                    JOptionPane.showMessageDialog(gradesFrame, "Please select a grade to delete.");
                }
            }
        });





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

        addGradeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentNumber = JOptionPane.showInputDialog(gradesFrame, "Enter Student Number");
                String scoreStr = JOptionPane.showInputDialog(gradesFrame, "Enter Score");

                if (studentNumber != null && scoreStr != null) {
                    try {
                        double score = Double.parseDouble(scoreStr);

                        Grade newGrade = new Grade();
                        newGrade.setStudent_number(studentNumber);
                        newGrade.setExam_id(examId);
                        newGrade.setScore_value(score);


                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String scoreDateString = sdf.format(new Date());
                        newGrade.setScore_datetime(scoreDateString);


                        String response = api_requests.exam_toevoegen(newGrade);
                        JOptionPane.showMessageDialog(gradesFrame, response);

                        if (response.startsWith("Fout")) {
                            System.err.println(response);
                        } else {

                            GradeGetter gradeGetter = new GradeGetter();
                            gradeGetter.setStudent_number(studentNumber);
                            gradeGetter.setCourse_name(gradeGetter.getCourse_name());
                            gradeGetter.setScore_value(score);
                            gradeGetter.setScore_datetime(sdf.parse(scoreDateString));
                            finalGrades1.add(gradeGetter);

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




            buttonPanel.add(updateGradeButton);

            buttonPanel.add(deleteGradeButton);
            buttonPanel.add(addGradeButton);

            panel.add(buttonPanel, BorderLayout.SOUTH);


            gradesFrame.add(panel);


            gradesFrame.setSize(600, 400);
            gradesFrame.setLocationRelativeTo(null);
            gradesFrame.setVisible(true);
    }



     class ButtonRenderer extends JButton implements TableCellRenderer {
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


     class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private int examId;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

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

    public  void main(String[] args) {
        JFrame frame = new JFrame("Exam Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        displayExams(frame);
    }

    public  List<Grade> convertToGradeList(List<GradeGetter> gradeGetters) {
        List<Grade> grades = new ArrayList<>();
        for (GradeGetter getter : gradeGetters) {
            grades.add(new Grade(
                    getter.getId(),
                    getter.getStudent_id(),
                    getter.getStudent_number(),
                    getter.getExam_id(),
                    getter.getCourse_name(),
                    getter.getScore_value(),
                    getter.getScore_datetime().toString()
            ));
        }
        return grades;
    }

}