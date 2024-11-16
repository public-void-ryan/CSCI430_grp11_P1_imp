package frontend;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class ManagerMenuButton extends JButton implements ActionListener {
    private final WarehouseContext warehouseContext;

    public ManagerMenuButton() {
        super("Manager");
        this.warehouseContext = WarehouseContext.instance();
        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String managerID = JOptionPane.showInputDialog("Enter Manager Username:");
        if (managerID == null) {
            return;
        }
        if (managerID.trim().isEmpty()) {
            JOptionPane.showMessageDialog(warehouseContext.getMainFrame(), "Manager Username cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JPasswordField passwordField = new JPasswordField();
        int option = JOptionPane.showConfirmDialog(warehouseContext.getMainFrame(), passwordField, "Enter Manager Password:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option != JOptionPane.OK_OPTION) {
            return;
        }
        String managerPassword = new String(passwordField.getPassword());
        if (managerPassword.trim().isEmpty()) {
            JOptionPane.showMessageDialog(warehouseContext.getMainFrame(), "Manager Password cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!Objects.equals(managerID.trim(), "manager")) {
            JOptionPane.showMessageDialog(warehouseContext.getMainFrame(), "Invalid Manager Username.", "Authentication Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!Objects.equals(managerPassword.trim(), "manager")) {
            JOptionPane.showMessageDialog(warehouseContext.getMainFrame(), "Invalid Manager Password.", "Authentication Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        warehouseContext.changeState(WarehouseContext.LOGIN_AS_MANAGER);
    }
}
