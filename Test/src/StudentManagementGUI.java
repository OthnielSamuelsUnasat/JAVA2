import backend.api_requests;
import backend.models.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
//import backend.api_requests.*;

public class StudentManagementGUI {
    public static void main(String[] args) {

        JFrame frame = new JFrame("Student Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());


        Color primaryColor = new Color(70, 130, 180);
        Color accentColor = new Color(255, 165, 0);
        Color lightColor = new Color(245, 245, 245);


        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(primaryColor);


        JLabel groep = new JLabel("Groepsleden");
        groep.setForeground(Color.WHITE);
        groep.setAlignmentX(Component.CENTER_ALIGNMENT);
        groep.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JTextArea groepsleden = new JTextArea(5, 15);
        groepsleden.setEditable(false);
        groepsleden.setLineWrap(true);
        groepsleden.setWrapStyleWord(true);
        JScrollPane groepPane = new JScrollPane(groepsleden);

        groepsleden.setText("SE/1123/080... - Othniel\nSE1123/039... - Eleanor Lokhai\nSE/1123/... - Bindya\nSE/1123/... - Dharandjai Patan");


        JFrame frame_student_toevoegen = new JFrame("Student Toevoegen");
        frame_student_toevoegen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame_student_toevoegen.setSize(1200, 1200);
        frame_student_toevoegen.setLayout(new BorderLayout());

        JButton btn_student_toevoegen = new JButton("+ Voeg Student Toe");
        btn_student_toevoegen.addActionListener(e -> StudentToevoegenGUI.openStudentForm()); // Call the popup method

        frame.add(btn_student_toevoegen);
        frame.setVisible(true);


        JButton cijfer = new JButton("+ Voeg Cijfer Toe");
        JButton gemiddelde = new JButton("+ Bereken Gemiddelde");
        JButton verwijderstudent = new JButton("- Verwijder Student");


        JButton[] buttons = {btn_student_toevoegen, cijfer, gemiddelde, verwijderstudent};
        for (JButton button : buttons) {
            button.setFocusPainted(false);
            button.setBackground(accentColor);
            button.setForeground(Color.WHITE);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        }


        sidebar.add(Box.createVerticalStrut(20));
        sidebar.add(groep);
        sidebar.add(groepPane);
        sidebar.add(Box.createVerticalStrut(50));
        sidebar.add(btn_student_toevoegen);
        sidebar.add(Box.createVerticalStrut(50));
        sidebar.add(cijfer);
        sidebar.add(Box.createVerticalStrut(50));
        sidebar.add(gemiddelde);
        sidebar.add(Box.createVerticalStrut(50));
        sidebar.add(verwijderstudent);


        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(lightColor);

        JTextField searchField = new JTextField("Zoek student op naam of nummer");
        JButton searchButton = new JButton("Zoek");
        searchButton.setBackground(accentColor);
        searchButton.setForeground(Color.WHITE);

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);


        JPanel tablePanel = new JPanel(new BorderLayout());
        JTable table = new JTable(new DefaultTableModel(new Object[][]{}, new String[]{"Student ID", "Voor Naam", "Achter Naam", "Student Nummer","Geslacht","Geboortedatum", "Richting", "Cohort"}));
        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);



        frame.add(sidebar, BorderLayout.WEST);
        frame.add(searchPanel, BorderLayout.NORTH);
        frame.add(tablePanel, BorderLayout.CENTER);



        frame.setVisible(true);

        fetchStudentData("", table);

    }
    private static void fetchStudentData(String query, JTable table) {
        java.util.List<Student> students = api_requests.getStudents(query);

        if (students != null) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0); // Clear existing rows

            for (Student student : students) {
                model.addRow(new Object[]{student.getId(), student.getFirstName(),student.getLastName(),student.getStudentNumber(),student.getGender(),student.getBirthdate()});
            }
        } else {
            JOptionPane.showMessageDialog(null, "Error fetching student data");
        }
    }
}
