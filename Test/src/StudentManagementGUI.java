import backend.api_requests;
import backend.custompackages.ButtonRenderer;
import backend.custompackages.ButtonEditor;

import backend.custompackages.SwingStyling;
import backend.models.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudentManagementGUI {
    public static void main(String[] args) {
        SwingStyling.applyLookAndFeel();

        JFrame frame = new JFrame("Student Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());


        Color primaryColor = new Color(0, 28, 111);
        Color accentColor = new Color(210, 86, 0);
        Color lightColor = new Color(245, 245, 245);



        DefaultTableModel model = new DefaultTableModel(new Object[][]{},
                new String[]{"Student ID", "Voor Naam", "Achter Naam", "Student Nummer", "Geslacht", "Geboortedatum", "Bewerken", "Verwijderen"});

//        JTable table = new JTable(model);
//        table.setRowHeight(30);
//        table.setShowGrid(false);
//        table.setIntercellSpacing(new Dimension(0, 0));
//        table.setSelectionBackground(new Color(200, 200, 255));

        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setShowGrid(true);
        table.setGridColor(Color.LIGHT_GRAY);
        table.setIntercellSpacing(new Dimension(1, 1));
        table.setSelectionBackground(new Color(200, 200, 255));


// Center text in table cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }


        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(primaryColor);


        table.getColumn("Bewerken").setCellRenderer(new ButtonRenderer("Bewerken", new Color(0, 28, 111),Color.WHITE)); // Dark Blue
        table.getColumn("Bewerken").setCellEditor(new ButtonEditor(new JCheckBox(), model, true));

        table.getColumn("Verwijderen").setCellRenderer(new ButtonRenderer("Verwijderen", new Color(0, 28, 111), Color.RED)); // Orange background with red text
        table.getColumn("Verwijderen").setCellEditor(new ButtonEditor(new JCheckBox(), model, false));
        


        JLabel groep = new JLabel("Groepsleden");
        groep.setForeground(Color.WHITE);
        groep.setAlignmentX(Component.CENTER_ALIGNMENT);
        groep.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JTextArea groepsleden = new JTextArea(5, 15);
        groepsleden.setEditable(false);
        groepsleden.setLineWrap(true);
        groepsleden.setWrapStyleWord(true);
        JScrollPane groepPane = new JScrollPane(groepsleden);

        groepsleden.setText(getGroepsleden());


        JFrame frame_student_toevoegen = new JFrame("Student Toevoegen");
        frame_student_toevoegen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame_student_toevoegen.setSize(1200, 1200);
        frame_student_toevoegen.setLayout(new BorderLayout());

        JButton btn_student_toevoegen = new JButton("+ Voeg Student Toe");
        btn_student_toevoegen.addActionListener(e -> {
            StudentToevoegenGUI.openStudentForm();

            // Ensure the table refreshes after the dialog closes
            SwingUtilities.invokeLater(() -> fetchStudentData("", table));
        });



        JFrame frame_view_semesters = new JFrame("Semesters Bekijken");
        frame_view_semesters.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame_view_semesters.setSize(1200, 1200);
        frame_view_semesters.setLayout(new BorderLayout());

        JButton btn_view_semesters = new JButton("+ Semesters Bekijken");
        btn_view_semesters.addActionListener(e -> {
            SemesterManagementGUI.displaySemesters(frame_view_semesters);

            // Ensure the table refreshes after the dialog closes
            SwingUtilities.invokeLater(() -> fetchStudentData("", table));
        });

        JFrame frame_view_exams = new JFrame("Examens Bekijken");
        frame_view_exams.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame_view_exams.setSize(1200, 1200);
        frame_view_exams.setLayout(new BorderLayout());

        JButton btn_view_exams = new JButton("+ Examens Bekijken");
        btn_view_exams.addActionListener(e -> {
            ExamManagementGUI.displayExams(frame_view_exams);

            // Ensure the table refreshes after the dialog closes
            SwingUtilities.invokeLater(() -> fetchStudentData("", table));
        });


        frame.add(btn_student_toevoegen);
        frame.add(btn_view_semesters);
        frame.add(btn_view_exams);

        frame.setVisible(true);

        JButton cijfer = new JButton("+ Voeg Cijfer Toe");
        JButton gemiddelde = new JButton("+ Bereken Gemiddelde");
        JButton verwijderstudent = new JButton("- Verwijder Student");


        JButton[] buttons = {btn_student_toevoegen, btn_view_semesters,btn_view_exams,cijfer, gemiddelde, verwijderstudent};
        for (JButton button : buttons) {
            button.setFocusPainted(false);
            button.setBackground(accentColor);
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Arial", Font.BOLD, 14));
            button.setAlignmentX(Component.LEFT_ALIGNMENT);
            button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        }


        sidebar.add(Box.createVerticalStrut(20));
        sidebar.add(groep);
        sidebar.add(groepPane);
        sidebar.add(Box.createVerticalStrut(50));
        sidebar.add(btn_student_toevoegen);
        sidebar.add(Box.createVerticalStrut(50));
        sidebar.add(btn_view_semesters);
        sidebar.add(Box.createVerticalStrut(50));
        sidebar.add(btn_view_exams);
        sidebar.add(Box.createVerticalStrut(50));
        sidebar.add(cijfer);
        sidebar.add(Box.createVerticalStrut(50));
        sidebar.add(gemiddelde);
        sidebar.add(Box.createVerticalStrut(50));
        sidebar.add(verwijderstudent);

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(lightColor);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JTextField searchField = new JTextField("Zoek student op naam of nummer");
        searchField.setBorder(BorderFactory.createLineBorder(accentColor, 2));
        searchField.setFont(new Font("Arial", Font.ITALIC, 14));
        JButton searchButton = new JButton("Zoek");
        searchButton.setBackground(accentColor);
        searchButton.setForeground(Color.WHITE);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);


        JPanel tablePanel = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);


        frame.add(sidebar, BorderLayout.WEST);
        frame.add(searchPanel, BorderLayout.NORTH);
        frame.add(tablePanel, BorderLayout.CENTER);


        frame.setVisible(true);

        fetchStudentData("", table);

    }

// Custom Renderer to display buttons in the "Actions" column
    static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer(String text, Color backgroundColor, Color foregroundColor) {
            setText(text);
            setFocusPainted(false);
            setBackground(backgroundColor);
            setForeground(foregroundColor);  // Set the text color here
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }





    private static void fetchStudentData(String query, JTable table) {
        java.util.List<Student> students = api_requests.getStudents(query);

        if (students != null) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0); // Clear existing rows

            for (Student student : students) {
                model.addRow(new Object[]{student.getId(), student.getFirstName(),student.getLastName(),student.getStudentNumber(),student.getGender(),student.getBirthdate(),"Bewerken", "Verwijderen"});
            }
        } else {
            JOptionPane.showMessageDialog(null, "Error fetching student data");
        }
    }

    private static String getGroepsleden() {
       return  "SE/1123/080... - Othniel Samuels\nSE1123/039... - Eleanor Lokhai\nSE/1123/... - Bindya\nSE/1123/... - Dharandjai Patan";
    }
}
