package frontend;

import javax.swing.*;
import java.awt.*;

public class MenuPanelBuilder {
    private final JPanel mainPanel;
    private final GridBagConstraints gbc;
    private int row;

    public MenuPanelBuilder(String titleText) {
        mainPanel = new JPanel(new GridBagLayout());

        // Create the title label
        JLabel menuTitle = new JLabel(titleText, SwingConstants.CENTER);
        menuTitle.setFont(new Font("Arial", Font.BOLD, 24));

        // Set constraints for the title label
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span two columns
        gbc.insets = new Insets(20, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(menuTitle, gbc);

        row = 1;
    }

    public void addButtonAndLabel(JButton button, JLabel label) {
        // Set constraints for the button
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(button, gbc);

        // Set constraints for the label
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 0.0;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(label, gbc);

        row++;
    }

    public JPanel build() {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.PAGE_END;
        mainPanel.add(Box.createVerticalGlue(), gbc);

        return mainPanel;
    }
}
