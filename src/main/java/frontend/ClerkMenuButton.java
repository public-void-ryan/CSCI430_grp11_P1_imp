package frontend;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class ClerkMenuButton extends JButton implements ActionListener {
    private final WarehouseContext warehouseContext;

    public ClerkMenuButton() {
        super("Clerk");
        this.warehouseContext = WarehouseContext.instance();
        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String clerkID = JOptionPane.showInputDialog("Enter Clerk Username:");
        if (clerkID == null) {
            return;
        }
        if (clerkID.trim().isEmpty()) {
            JOptionPane.showMessageDialog(warehouseContext.getMainFrame(), "Clerk Username cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JPasswordField passwordField = new JPasswordField();
        int option = JOptionPane.showConfirmDialog(warehouseContext.getMainFrame(), passwordField, "Enter Clerk Password:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option != JOptionPane.OK_OPTION) {
            return;
        }
        String clerkPassword = new String(passwordField.getPassword());
        if (clerkPassword.trim().isEmpty()) {
            JOptionPane.showMessageDialog(warehouseContext.getMainFrame(), "Clerk Password cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!Objects.equals(clerkID.trim(), "clerk")) {
            JOptionPane.showMessageDialog(warehouseContext.getMainFrame(), "Invalid Clerk Username.", "Authentication Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!Objects.equals(clerkPassword.trim(), "clerk")) {
            JOptionPane.showMessageDialog(warehouseContext.getMainFrame(), "Invalid Clerk Password.", "Authentication Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        warehouseContext.changeState(WarehouseContext.LOGIN_AS_CLERK);
    }
}
