import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomButtonEditor extends DefaultCellEditor {
    private final JButton button;
    private final boolean isEditButton;

    public CustomButtonEditor(JCheckBox checkBox, boolean isEditButton) {
        super(checkBox);
        this.isEditButton = isEditButton;
        button = new JButton();
        button.setOpaque(true);
        button.setBackground(new Color(200, 200, 200)); // Gray button

        if (isEditButton) {
            button.setForeground(new Color(0, 0, 200)); // Blue text
            button.setText("Bewerken");
        } else {
            button.setForeground(new Color(200, 0, 0)); // Red text
            button.setText("Verwijderen");
        }

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return button.getText();
    }
}
