import backend.api_requests;
import backend.models.Course;
import backend.models.Semester;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class SemesterManagementGUI {

    // Method to display the semesters and the "View Courses" button in a JFrame
    public static void displaySemesters(JFrame frame) {
        DefaultTableModel model = new DefaultTableModel(new Object[][]{},
                new String[]{"Semester Name", "Actions"});
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JTable table = new JTable(model) {
            // Override the method to make the "Actions" column render buttons
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 1) {
                    return JButton.class;
                }
                return super.getColumnClass(columnIndex);
            }
        };

        table.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Fetch data and populate the table
        fetchSemesterData(table);

        // Set frame size and center it
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);  // Center the frame on the screen
        frame.setVisible(true);

        table.setRowHeight(30);

        // Setting up the button rendering
        table.getColumnModel().getColumn(1).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(1).setCellEditor(new ButtonEditor(new JCheckBox()));

        frame.add(scrollPane, BorderLayout.CENTER);

        // Fetch data and populate the table
        fetchSemesterData(table);

        frame.setSize(600, 400);
        frame.setVisible(true);
    }

    // Fetch and populate semester data
    private static void fetchSemesterData(JTable table) {
        List<Semester> semesters = api_requests.getSemesters();

        if (semesters != null) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0); // Clear existing rows

            for (Semester semester : semesters) {
                // Add the semester name and a "View Courses" button for each semester
                model.addRow(new Object[]{
                        semester.getSemesterName(),
                        "View Courses"
                });
            }
        } else {
            JOptionPane.showMessageDialog(null, "Error fetching semester data");
        }
    }

    // Method to display courses for a selected semester
    private static void viewCoursesForSemester(String semesterName) {
        // Fetch courses for the selected semester
        List<Semester> semesters = api_requests.getSemesters();
        Semester selectedSemester = null;

        // Find the selected semester by name
        for (Semester semester : semesters) {
            if (semester.getSemesterName().equals(semesterName)) {
                selectedSemester = semester;
                break;
            }
        }

        if (selectedSemester != null) {
            // Create a new JFrame to display courses
            JFrame coursesFrame = new JFrame("Courses for " + semesterName);
            coursesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            DefaultTableModel model = new DefaultTableModel(new Object[][]{},
                    new String[]{"Course Name", "EC", "Course Code", "Course Description", "Block"});

            JTable table = new JTable(model);
            table.setRowHeight(30);

            JScrollPane scrollPane = new JScrollPane(table);
            coursesFrame.add(scrollPane);

            // Populate the courses table
            for (Course course : selectedSemester.getCourses()) {
                model.addRow(new Object[]{
                        course.getCourseName(),
                        course.getEc(),
                        course.getCourseCode(),
                        course.getCourseDescription(),
                        course.getBlock()
                });
            }

            // Center the frame on the screen
            coursesFrame.setSize(600, 400);
            coursesFrame.setLocationRelativeTo(null);  // This will center the frame on the screen
            coursesFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Error: Semester not found.");
        }
    }


    // Custom Renderer to display buttons in the "Actions" column
    static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setText("View Courses");
            setFocusPainted(false);
            setBackground(Color.LIGHT_GRAY);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // Custom Editor to handle button clicks in the "Actions" column
    // Custom Editor to handle button clicks in the "Actions" column
    static class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String semesterName;

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
                    semesterName = (String) table.getValueAt(row, 0); // Get semester name
                    viewCoursesForSemester(semesterName);
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            button.setText("View Courses");
            return button;
        }
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("Semester Management");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close this window only, keep the application running
        displaySemesters(frame);
    }

}
