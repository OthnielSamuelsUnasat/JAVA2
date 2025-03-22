package backend.custompackages;

import backend.api_requests;
import backend.models.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;


public class ButtonEditor extends DefaultCellEditor {
    private JButton button;
    private DefaultTableModel model;
    private int row;
    private boolean isEditButton;

    public ButtonEditor(JCheckBox checkBox, DefaultTableModel model, boolean isEditButton) {
        super(checkBox);
        this.model = model;
        this.isEditButton = isEditButton;

        button = new JButton(isEditButton ? "Bewerken" : "Verwijderen");
        button.setForeground(Color.WHITE);
        button.setBackground(isEditButton ? Color.BLUE : Color.RED);
        button.setOpaque(true);

        button.addActionListener(e -> {
            if (isEditButton) {
                editRow(row);
            } else {
                deleteRow(row);
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.row = row;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return button.getText();
    }

private void editRow(int row) {
    String studentID = model.getValueAt(row, 0).toString();
    String firstName = model.getValueAt(row, 1).toString();
    String lastName = model.getValueAt(row, 2).toString();
    String studentNumber = model.getValueAt(row, 3).toString();
    String gender = model.getValueAt(row, 4).toString();
    String birthdate = model.getValueAt(row, 5).toString();

    JTextField firstNameField = new JTextField(firstName);
    JTextField lastNameField = new JTextField(lastName);
    JTextField studentNumberField = new JTextField(studentNumber);
    JTextField genderField = new JTextField(gender);
    JTextField birthdateField = new JTextField(birthdate);

    JPanel panel = new JPanel(new GridLayout(0, 2));
    panel.add(new JLabel("Voor Naam:"));
    panel.add(firstNameField);
    panel.add(new JLabel("Achter Naam:"));
    panel.add(lastNameField);
    panel.add(new JLabel("Student Nummer:"));
    panel.add(studentNumberField);
    panel.add(new JLabel("Geslacht:"));
    panel.add(genderField);
    panel.add(new JLabel("Geboortedatum:"));
    panel.add(birthdateField);

    Object[] options = {"Opslaan", "Annuleren", "Clear"};
    int result;

    do {
        result = JOptionPane.showOptionDialog(
                null, panel, "Bewerk Student",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]
        );

        if (result == 2) {  // If "Clear" is clicked
            firstNameField.setText("");
            lastNameField.setText("");
            studentNumberField.setText("");
            genderField.setText("");
            birthdateField.setText("");
        }

    } while (result == 2);  // Keep dialog open if "Clear" is clicked

    if (result == JOptionPane.YES_OPTION) {  // If "Opslaan" is clicked
        Student updatedStudent = new Student(
                Integer.parseInt(studentID),
                firstNameField.getText(),
                lastNameField.getText(),
                studentNumberField.getText(),
                genderField.getText(),
                birthdateField.getText()
        );

        String response = api_requests.student_bewerken(updatedStudent);

        if (response.contains("succesvol")) {
            model.setValueAt(updatedStudent.getFirstName(), row, 1);
            model.setValueAt(updatedStudent.getLastName(), row, 2);
            model.setValueAt(updatedStudent.getStudentNumber(), row, 3);
            model.setValueAt(updatedStudent.getGender(), row, 4);
            model.setValueAt(updatedStudent.getBirthdate(), row, 5);
            JOptionPane.showMessageDialog(null, "Student succesvol bijgewerkt!");
        } else {
            JOptionPane.showMessageDialog(null, "Fout bij bijwerken: " + response);
        }
    }
}

    private void deleteRow(int row) {
        if (JOptionPane.showConfirmDialog(null, "Weet je zeker dat je deze student wilt verwijderen?", "Verwijderen", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            // Get student information from the table and create a Student object
            int studentID = Integer.parseInt(model.getValueAt(row, 0).toString());
            String firstName = model.getValueAt(row, 1).toString();
            String lastName = model.getValueAt(row, 2).toString();
            String studentNumber = model.getValueAt(row, 3).toString();
            String gender = model.getValueAt(row, 4).toString();
            String birthdate = model.getValueAt(row, 5).toString();

            // Create a Student object with the fetched data
            Student studentToDelete = new Student();
            studentToDelete.setId(studentID); // Set the student ID
            studentToDelete.setFirstName(firstName);
            studentToDelete.setLastName(lastName);
            studentToDelete.setStudentNumber(studentNumber);
            studentToDelete.setGender(gender);
            studentToDelete.setBirthdate(birthdate);

            // Remove the row from the table
            model.removeRow(row);

            // Call API to delete student by sending the whole student object
            String response = api_requests.student_verwijderen(studentToDelete);

            // Optionally, you can show a message depending on the API response
            JOptionPane.showMessageDialog(null, response);
        }
    }


}