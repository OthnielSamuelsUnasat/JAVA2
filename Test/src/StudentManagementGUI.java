import backend.api_requests;
import backend.custompackages.ButtonEditor;
import backend.custompackages.SwingStyling;
import backend.models.GradeGetter;
import backend.models.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.*;

public class StudentManagementGUI{

    api_requests api_requests = new api_requests();


    public void start() {
        SwingStyling.applyLookAndFeel();

        JFrame frame = new JFrame("Student Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());


        Color primaryColor = new Color(0, 28, 111);
        Color accentColor = new Color(210, 86, 0);
        Color lightColor = new Color(245, 245, 245);



        DefaultTableModel model = new DefaultTableModel(new Object[][]{},
                new String[]{"Student ID", "Voor Naam", "Achter Naam", "Student Nummer", "Geslacht", "Geboortedatum","Cijfers" ,"Bewerken", "Verwijderen"});

        JTable table_cijfers = new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);

                String columnName = getColumnName(column);
                if (!columnName.equals("Cijfers") && !columnName.equals("Verwijderen")) {
                    if (!isRowSelected(row)) {
                        c.setBackground(row % 2 == 0 ? new Color(230, 174, 135) : Color.WHITE); // Alternating row colors
                    } else {
                        c.setBackground(new Color(200, 200, 255)); // Selection color
                    }
                }

                if (c instanceof JLabel) {
                    ((JLabel) c).setHorizontalAlignment(JLabel.CENTER); // Center text
                }

                return c;
            }
        };




        table_cijfers.setRowHeight(30);
        table_cijfers.setShowGrid(true);
        table_cijfers.setGridColor(Color.LIGHT_GRAY);
        table_cijfers.setIntercellSpacing(new Dimension(1, 1));



        JTable table = new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);

                String columnName = getColumnName(column);
                if (!columnName.equals("Bewerken") && !columnName.equals("Verwijderen")) {
                    if (!isRowSelected(row)) {
                        c.setBackground(row % 2 == 0 ? new Color(230, 174, 135) : Color.WHITE); // Alternating row colors
                    } else {
                        c.setBackground(new Color(200, 200, 255)); // Selection color
                    }
                }

                if (c instanceof JLabel) {
                    ((JLabel) c).setHorizontalAlignment(JLabel.CENTER); // Center text
                }

                return c;
            }
        };

        table.setRowHeight(30);
        table.setShowGrid(true);
        table.setGridColor(Color.LIGHT_GRAY);
        table.setIntercellSpacing(new Dimension(1, 1));



        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }


        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(primaryColor);




        table.getColumn("Cijfers").setCellRenderer(new ButtonRenderer("Cijfers", new Color(0, 28, 111), Color.BLACK));
        table.getColumn("Cijfers").setCellEditor(new ButtonEditor(new JCheckBox(), model, true));


        table.getColumn("Cijfers").setCellEditor(new DefaultCellEditor(new JCheckBox()) {
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                String studentId = table.getValueAt(row, 3).toString();
                fetchStudentGrades(studentId);
                return super.getTableCellEditorComponent(table, value, isSelected, row, column);
            }
        });


        table.getColumn("Bewerken").setCellRenderer(new ButtonRenderer("Bewerken", new Color(0, 28, 111),Color.WHITE));
        table.getColumn("Bewerken").setCellEditor(new ButtonEditor(new JCheckBox(), model, true));

        table.getColumn("Verwijderen").setCellRenderer(new ButtonRenderer("Verwijderen", new Color(0, 28, 111), Color.RED));
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

        JButton btn_student_toevoegen = new JButton("Voeg Student Toe");
        btn_student_toevoegen.addActionListener(e -> {
            StudentToevoegenGUI.openStudentForm();


            SwingUtilities.invokeLater(() -> fetchStudentData("", table));
        });

        SemesterManagementGUI semesterManagementGUI = new SemesterManagementGUI();
        ExamManagementGUI examManagementGUI = new ExamManagementGUI();


        JFrame frame_view_semesters = new JFrame("Semesters Bekijken");
        frame_view_semesters.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame_view_semesters.setSize(1200, 1200);
        frame_view_semesters.setLayout(new BorderLayout());

        JButton btn_view_semesters = new JButton("Semesters Bekijken");
        btn_view_semesters.addActionListener(e -> {
            semesterManagementGUI.displaySemesters(frame_view_semesters);

            SwingUtilities.invokeLater(() -> fetchStudentData("", table));
        });

        JFrame frame_view_exams = new JFrame("Examens Bekijken");
        frame_view_exams.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame_view_exams.setSize(1200, 1200);
        frame_view_exams.setLayout(new BorderLayout());

        JButton btn_view_exams = new JButton("Examens Bekijken");
        btn_view_exams.addActionListener(e -> {
            examManagementGUI.displayExams(frame_view_exams);

            SwingUtilities.invokeLater(() -> fetchStudentData("", table));
        });


        JButton btn_instructions = new JButton("Instructies");
        btn_instructions.addActionListener(e -> {

            JFrame instructionsFrame = new JFrame("Instructies");
            instructionsFrame.setSize(600, 400);
            instructionsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


            instructionsFrame.getContentPane().setBackground(new Color(240, 240, 240));


            JTextArea instructionsArea = new JTextArea(15, 40);
            instructionsArea.setEditable(false);


            instructionsArea.setText("Welkom.\n" +
                    "- U kunt drukken op 'Student toevoegen' om een student bij te voegen. U kunt daarna de informatie bezichtigen op de homepagina. Daar zult u een verwijderknop zien, indien u een student wenst te verwijderen. Als u een verandering moet plegen bij een student, kunt u drukken op 'Bewerken'.\n" +
                    "- U kunt de semesters bekijken en de informatie daar toevoegen of bewerken.\n" +
                    "- U kunt de exameninformatie bekijken door op het knopje te drukken. Daar kunt u ook de informatie wijzigen, toevoegen of verwijderen.\n" +
                    "- U kunt bij examen per examen de cijfers doorgeven van de studenten.\n" +
                    "- Wij hopen u voldoende geïnformeerd te hebben met deze uitleg.");


            instructionsArea.setFont(new Font("Arial", Font.PLAIN, 14));
            instructionsArea.setForeground(new Color(50, 50, 50));
            instructionsArea.setBackground(new Color(255, 255, 255));


            instructionsArea.setLineWrap(true);
            instructionsArea.setWrapStyleWord(true);


            JScrollPane scrollPane = new JScrollPane(instructionsArea);
            scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            scrollPane.getViewport().setBackground(new Color(255, 255, 255));


            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            panel.setBackground(new Color(230, 174, 135));
            panel.add(scrollPane, BorderLayout.CENTER);


            JLabel titleLabel = new JLabel("Instructies", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
            titleLabel.setForeground(new Color(0, 28, 111));
            titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            panel.add(titleLabel, BorderLayout.NORTH);


            instructionsFrame.setLayout(new BorderLayout());
            instructionsFrame.add(panel, BorderLayout.CENTER);


            instructionsFrame.setVisible(true);
        });


        frame.add(btn_student_toevoegen);
        frame.add(btn_view_semesters);
        frame.add(btn_view_exams);
        frame.add(btn_instructions);
        frame.setVisible(true);


        JButton[] buttons = {btn_student_toevoegen, btn_view_semesters, btn_view_exams,btn_instructions};
        for (JButton button : buttons) {
            button.setFocusPainted(false);
            button.setBackground(accentColor);
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Arial", Font.BOLD, 14));
            button.setAlignmentX(Component.CENTER_ALIGNMENT);  // Center the buttons
            button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            button.setPreferredSize(new Dimension(200, 50));
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
        sidebar.add(btn_instructions);


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




     class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer(String text, Color backgroundColor, Color foregroundColor) {
            setText(text);
            setFocusPainted(false);
            setBackground(backgroundColor);
            setForeground(foregroundColor);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    private  void fetchStudentData(String query, JTable table) {
        java.util.List<Student> students = api_requests.getStudents(query);

        if (students != null) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0); // Clear existing rows

            for (Student student : students) {
                model.addRow(new Object[]{student.getId(), student.getFirstName(),student.getLastName(),student.getStudentNumber(),student.getGender(),student.getBirthdate(),"Cijfers","Bewerken", "Verwijderen"});
            }
        } else {
            JOptionPane.showMessageDialog(null, "Error fetching student data");
        }
    }

    private  void fetchStudentGrades(String studentId) {

        java.util.List<GradeGetter> grades = api_requests.getGradesForStudent(studentId);


        if (grades != null) {

            updateGradesTable(grades);
        } else {
            JOptionPane.showMessageDialog(null, "Error fetching grades");
        }
    }

    private  void updateGradesTable(java.util.List<GradeGetter> grades) {

        Map<String, Map<Integer, Double>> courseGradesMap = new LinkedHashMap<>(); // Course -> (Semester -> Grade)
        Set<Integer> semesters = new TreeSet<>(); // Keep semesters in sorted order

        for (GradeGetter grade : grades) {
            courseGradesMap
                    .computeIfAbsent(grade.getCourse_name(), k -> new HashMap<>())
                    .put(grade.getSemester(), grade.getScore_value());
            semesters.add(grade.getSemester());
        }


        String[] columnNames = new String[semesters.size() + 2]; // Course + Semesters + Average
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




    private  String getGroepsleden() {
       return  "SE/1123/080... - Othniel Samuels\nSE1123/039... - Eleanor Lokhai\nSE/1123/... - Bindya\nSE/1123/... - Dharandjai Patan";
    }

}
