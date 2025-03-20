package backend.custompackages;

import javax.swing.*;
import java.awt.*;

public class SwingStyling {
    public static void applyLookAndFeel() {
        try {
            UIManager.put("Button.background", new Color(210, 86, 0)); // #d25600
            UIManager.put("Button.foreground", Color.WHITE);
            UIManager.put("Button.font", new Font("Arial", Font.BOLD, 14));
            UIManager.put("Button.border", BorderFactory.createEmptyBorder(10, 20, 10, 20));

            UIManager.put("Label.foreground", Color.WHITE);
            UIManager.put("Label.font", new Font("SansSerif", Font.BOLD, 16));

            UIManager.put("TextField.font", new Font("SansSerif", Font.PLAIN, 14));
            UIManager.put("TextField.border", BorderFactory.createEmptyBorder(5, 10, 5, 10));

            UIManager.put("Table.font", new Font("Arial", Font.PLAIN, 14));
            UIManager.put("Table.rowHeight", 30);
            UIManager.put("Table.gridColor", Color.LIGHT_GRAY);

            UIManager.put("Panel.background", new Color(0, 28, 111)); // #001c6f
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
