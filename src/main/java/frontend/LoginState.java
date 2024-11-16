package frontend;

import backend.*;

import javax.swing.*;

public class LoginState extends WarehouseState {

    public LoginState(WarehouseContext warehouseContext, Warehouse warehouse) {
        super(warehouseContext, warehouse);
    }

    @Override
    public void run() {
        JFrame mainFrame = warehouseContext.getMainFrame();

        // Use MenuPanelBuilder to create the menu panel
        MenuPanelBuilder menuBuilder = new MenuPanelBuilder("Login Menu");

        // Create the buttons and labels
        ClientMenuButton clientButton = new ClientMenuButton();
        JLabel clientLabel = new JLabel("Login as a client to place orders and view your account.");

        ClerkMenuButton clerkButton = new ClerkMenuButton();
        JLabel clerkLabel = new JLabel("Login as a clerk to manage clients and process orders.");

        ManagerMenuButton managerButton = new ManagerMenuButton();
        JLabel managerLabel = new JLabel("Login as a manager to manage inventory and oversee operations.");

        JButton quitButton = new JButton("Exit");
        JLabel quitLabel = new JLabel("Save and exit the system.");
        quitButton.addActionListener(e -> warehouseContext.changeState(WarehouseContext.LOGOUT));

        // Add buttons and labels to the menu
        menuBuilder.addButtonAndLabel(clientButton, clientLabel);
        menuBuilder.addButtonAndLabel(clerkButton, clerkLabel);
        menuBuilder.addButtonAndLabel(managerButton, managerLabel);
        menuBuilder.addButtonAndLabel(quitButton, quitLabel);

        // Build the panel and add it to the main frame
        JPanel panel = menuBuilder.build();

        mainFrame.getContentPane().removeAll();
        mainFrame.getContentPane().add(panel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }
}
