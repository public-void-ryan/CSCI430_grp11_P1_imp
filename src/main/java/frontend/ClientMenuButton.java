package frontend;

import backend.Warehouse;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class ClientMenuButton extends JButton implements ActionListener {
    private final WarehouseContext warehouseContext;
    private final Warehouse warehouse;

    public ClientMenuButton() {
        super("Client");
        this.warehouseContext = WarehouseContext.instance();
        this.warehouse = Warehouse.instance();
        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String clientID = JOptionPane.showInputDialog("Enter Client Username:");
        if (clientID == null) {
            return;
        }
        if (clientID.trim().isEmpty()) {
            JOptionPane.showMessageDialog(warehouseContext.getMainFrame(), "Client Username cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JPasswordField passwordField = new JPasswordField();
        int option = JOptionPane.showConfirmDialog(warehouseContext.getMainFrame(), passwordField, "Enter Client Password:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option != JOptionPane.OK_OPTION) {
            return;
        }
        String clientPassword = new String(passwordField.getPassword());
        if (clientPassword.trim().isEmpty()) {
            JOptionPane.showMessageDialog(warehouseContext.getMainFrame(), "Client Password cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            warehouse.getClient(clientID);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(warehouseContext.getMainFrame(), "Invalid Client Username.", "Authentication Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!Objects.equals(clientID, clientPassword)) {
            JOptionPane.showMessageDialog(warehouseContext.getMainFrame(), "Invalid Client Password.", "Authentication Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        warehouseContext.setCurrentClientID(clientID);
        warehouseContext.changeState(WarehouseContext.LOGIN_AS_CLIENT);
    }
}
