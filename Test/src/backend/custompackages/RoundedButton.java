package backend.custompackages;

import javax.swing.*;
import java.awt.*;

public class RoundedButton extends JButton {
    public RoundedButton(String text) {
        super(text);
        setFont(new Font("Arial", Font.BOLD, 14));
        setForeground(Color.WHITE);
        setBackground(new Color(210, 86, 0)); // #d25600
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(true);
        setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        setPreferredSize(new Dimension(200, 40));
    }
}
