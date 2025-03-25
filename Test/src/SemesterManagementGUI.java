import backend.api_requests;
import backend.models.Course;
import backend.models.Semester;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class SemesterManagementGUI {
    backend.api_requests api_requests = new api_requests();

    public  void displaySemesters(JFrame frame) {


        DefaultTableModel model = new DefaultTableModel(new Object[][]{},
                new String[]{"Semester Name", "Actions"});
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JTable table = new JTable(model) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 1) {
                    return JButton.class;
                }
                return super.getColumnClass(columnIndex);
            }
        };

        table.setRowHeight(30);
        table.setFillsViewportHeight(true);

        table.setDefaultRenderer(Object.class, new TableCellRenderer() {
            private final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = DEFAULT_RENDERER.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(row % 2 == 0 ? new Color(230, 174, 135) : Color.WHITE);
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        fetchSemesterData(table);

        table.getColumnModel().getColumn(1).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(1).setCellEditor(new ButtonEditor(new JCheckBox()));

        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private  void fetchSemesterData(JTable table) {
        List<Semester> semesters = api_requests.getSemesters();

        if (semesters != null) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);

            for (Semester semester : semesters) {
                model.addRow(new Object[]{
                        semester.getSemesterName(),
                        "View Courses"
                });
            }
        } else {
            JOptionPane.showMessageDialog(null, "Error fetching semester data");
        }
    }

    private  void viewCoursesForSemester(String semesterName) {
        List<Semester> semesters = api_requests.getSemesters();
        Semester selectedSemester = null;

        for (Semester semester : semesters) {
            if (semester.getSemesterName().equals(semesterName)) {
                selectedSemester = semester;
                break;
            }
        }

        if (selectedSemester != null) {
            JFrame coursesFrame = new JFrame("Courses for " + semesterName);
            coursesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            DefaultTableModel model = new DefaultTableModel(new Object[][]{},
                    new String[]{"Course Name", "EC", "Course Code", "Course Description", "Block"});

            JTable table = new JTable(model);
            table.setRowHeight(30);

            JScrollPane scrollPane = new JScrollPane(table);
            coursesFrame.add(scrollPane);

            for (Course course : selectedSemester.getCourses()) {
                model.addRow(new Object[]{
                        course.getCourseName(),
                        course.getEc(),
                        course.getCourseCode(),
                        course.getCourseDescription(),
                        course.getBlock()
                });
            }

            table.setDefaultRenderer(Object.class, new TableCellRenderer() {
                private final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = DEFAULT_RENDERER.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    c.setBackground(row % 2 == 0 ? new Color(230, 174, 135) : Color.WHITE);
                    return c;
                }
            });

            coursesFrame.setSize(600, 400);
            coursesFrame.setLocationRelativeTo(null);
            coursesFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Error: Semester not found.");
        }
    }

     class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setText("View Courses");
            setFocusPainted(false);
            setBackground(new Color(0, 28, 111));
            setForeground(Color.WHITE);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

     class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String semesterName;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.setBackground(new Color(230, 174, 135));
            button.setForeground(Color.WHITE);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JTable table = (JTable) SwingUtilities.getAncestorOfClass(JTable.class, button);
                    int row = table.getSelectedRow();
                    semesterName = (String) table.getValueAt(row, 0);
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

    public  void main(String[] args) {
        JFrame frame = new JFrame("Semester Management");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        displaySemesters(frame);
    }
}
