import javax.swing.*;
import java.awt.*;

public class StudentToevoegenGUI {
    public static void openStudentForm() {
        JDialog dialog = new JDialog((Frame) null, "Voeg Student Toe", true);
        dialog.setSize(400, 600);
        dialog.setLayout(new GridBagLayout());
        dialog.getContentPane().setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.gridy = 0;

        JLabel firstNameLabel = new JLabel("Voornaam:");
        JTextField firstNameField = new JTextField(20);
        JLabel lastNameLabel = new JLabel("Achternaam:");
        JTextField lastNameField = new JTextField(20);
        JLabel studentNumberLabel = new JLabel("Studentennummer:");
        JTextField studentNumberField = new JTextField(20);
        JLabel genderLabel = new JLabel("Geslacht:");
        String[] genders = {"Man", "Vrouw"};
        JComboBox<String> genderBox = new JComboBox<>(genders);
        JLabel birthDateLabel = new JLabel("Geboortedatum:");
        JTextField birthDateField = new JTextField(20);
        JButton saveButton = new JButton("Opslaan");

        Color accentColor = new Color(255, 165, 0);
        saveButton.setBackground(accentColor);
        saveButton.setOpaque(true);
        saveButton.setBorderPainted(false);
        saveButton.setForeground(Color.BLACK);

        saveButton.addActionListener(e -> {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String studentNumber = studentNumberField.getText();
            String gender = (String) genderBox.getSelectedItem();
            String birthDate = birthDateField.getText();
            JOptionPane.showMessageDialog(dialog, "Student opgeslagen: " + firstName + " " + lastName + " (ID: " + studentNumber + ")\nGeslacht: " + gender + "\nGeboortedatum: " + birthDate);
            dialog.dispose();
        });

        gbc.gridy++;
        dialog.add(firstNameLabel, gbc);
        gbc.gridy++;
        dialog.add(firstNameField, gbc);
        gbc.gridy++;
        dialog.add(lastNameLabel, gbc);
        gbc.gridy++;
        dialog.add(lastNameField, gbc);
        gbc.gridy++;
        dialog.add(studentNumberLabel, gbc);
        gbc.gridy++;
        dialog.add(studentNumberField, gbc);
        gbc.gridy++;
        dialog.add(genderLabel, gbc);
        gbc.gridy++;
        dialog.add(genderBox, gbc);
        gbc.gridy++;
        dialog.add(birthDateLabel, gbc);
        gbc.gridy++;
        dialog.add(birthDateField, gbc);
        gbc.gridy++;
        dialog.add(saveButton, gbc);

        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
