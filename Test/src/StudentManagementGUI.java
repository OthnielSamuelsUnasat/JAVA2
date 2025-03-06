import backend.api_requests;

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

        groepsleden.setText("SE/1123/... - Othniel\nSE1123/... - elle\nSE/1123/... - bindya\nSE/1123/... - Drishti\nSE/1123/056 - Dharandjai Patan");


        JButton studenten = new JButton("+ Voeg Student Toe");
        JButton cijfer = new JButton("+ Voeg Cijfer Toe");
        JButton gemiddelde = new JButton("+ Bereken Gemiddelde");
        JButton verwijderstudent = new JButton("- Verwijder Student");


        JButton[] buttons = {studenten, cijfer, gemiddelde, verwijderstudent};
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
        sidebar.add(studenten);
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
        JTable table = new JTable(new DefaultTableModel(new Object[][]{}, new String[]{"Studentnummer", "Naam", "Vak", "Cijfers"}));
        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);



        frame.add(sidebar, BorderLayout.WEST);
        frame.add(searchPanel, BorderLayout.NORTH);
        frame.add(tablePanel, BorderLayout.CENTER);



        frame.setVisible(true);

        fetchStudentData("", table);

    }
    private static void fetchStudentData(String query, JTable table) {
        // Call the api_requests.getStudents() method to fetch the data
        java.util.List<backend.Student> students = api_requests.getStudents(query);

        if (students != null) {
            // Populate the table with the fetched data
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0); // Clear existing rows

            for (backend.Student student : students) {
                model.addRow(new Object[]{student.getId(), student.getName()});
            }
        } else {
            JOptionPane.showMessageDialog(null, "Error fetching student data");
        }
    }
}
