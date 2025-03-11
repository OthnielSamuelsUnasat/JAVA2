import javax.swing.*;
import java.awt.*;

public class StudentToevoegenGUI {
    public static void openStudentForm() {
        JDialog dialog = new JDialog((Frame) null, "Voeg Student Toe", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new GridBagLayout());
        dialog.getContentPane().setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.gridy = 0;

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(20);
        JLabel idLabel = new JLabel("Student ID:");
        JTextField idField = new JTextField(20);
        JButton saveButton = new JButton("Opslaan");

        Color accentColor = new Color(255, 165, 0);
        saveButton.setBackground(accentColor);
        saveButton.setOpaque(true);
        saveButton.setBorderPainted(false);
        saveButton.setForeground(Color.BLACK);

        saveButton.addActionListener(e -> {
            String name = nameField.getText();
            String studentId = idField.getText();
            JOptionPane.showMessageDialog(dialog, "Student opgeslagen: " + name + " (ID: " + studentId + ")");
            dialog.dispose();
        });

        gbc.gridy++;
        dialog.add(nameLabel, gbc);
        gbc.gridy++;
        dialog.add(nameField, gbc);
        gbc.gridy++;
        dialog.add(idLabel, gbc);
        gbc.gridy++;
        dialog.add(idField, gbc);
        gbc.gridy++;
        dialog.add(saveButton, gbc);

        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}