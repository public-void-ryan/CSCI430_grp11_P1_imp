package frontend;

import backend.*;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;

public class ClerkMenuState extends WarehouseState {

    public ClerkMenuState(WarehouseContext warehouseContext, Warehouse warehouse) {
        super(warehouseContext, warehouse);
    }

    @Override
    public void run() {
        JFrame mainFrame = warehouseContext.getMainFrame();

        // Use MenuPanelBuilder to create the menu panel
        MenuPanelBuilder menuBuilder = new MenuPanelBuilder("Clerk Menu");

        // Create the buttons and labels
        JButton addClientButton = new JButton("Add Client");
        JLabel addClientLabel = new JLabel("Add a new client to the warehouse.");

        JButton showProductsButton = new JButton("Show Products");
        JLabel showProductsLabel = new JLabel("View the list of products in the warehouse.");

        JButton showClientsButton = new JButton("Show Clients");
        JLabel showClientsLabel = new JLabel("View the list of clients.");

        JButton showClientsWithBalanceButton = new JButton("Show Clients with Outstanding Balance");
        JLabel showClientsWithBalanceLabel = new JLabel("View clients with outstanding balances.");

        JButton recordPaymentButton = new JButton("Record Payment");
        JLabel recordPaymentLabel = new JLabel("Record a payment from a client.");

        JButton becomeClientButton = new JButton("Become Client");
        JLabel becomeClientLabel = new JLabel("Switch to client view to perform client operations.");

        JButton logoutButton = new JButton("Logout");
        JLabel logoutLabel = new JLabel("Logout and return to the login menu.");

        // Add action listeners
        addClientButton.addActionListener(e -> addClient());
        showProductsButton.addActionListener(e -> showProducts());
        showClientsButton.addActionListener(e -> showClients());
        showClientsWithBalanceButton.addActionListener(e -> showClientsWithOutstandingBalance());
        recordPaymentButton.addActionListener(e -> recordPayment());
        becomeClientButton.addActionListener(e -> becomeClient());
        logoutButton.addActionListener(e -> logout());

        // Add buttons and labels to the menu
        menuBuilder.addButtonAndLabel(addClientButton, addClientLabel);
        menuBuilder.addButtonAndLabel(showProductsButton, showProductsLabel);
        menuBuilder.addButtonAndLabel(showClientsButton, showClientsLabel);
        menuBuilder.addButtonAndLabel(showClientsWithBalanceButton, showClientsWithBalanceLabel);
        menuBuilder.addButtonAndLabel(recordPaymentButton, recordPaymentLabel);
        menuBuilder.addButtonAndLabel(becomeClientButton, becomeClientLabel);
        menuBuilder.addButtonAndLabel(logoutButton, logoutLabel);

        // Build the panel and add it to the main frame
        JPanel panel = menuBuilder.build();

        mainFrame.getContentPane().removeAll();
        mainFrame.getContentPane().add(panel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private void addClient() {
        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JTextField nameField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField phoneField = new JTextField();

        inputPanel.add(new JLabel("Client Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Address:"));
        inputPanel.add(addressField);
        inputPanel.add(new JLabel("Phone:"));
        inputPanel.add(phoneField);

        int result = JOptionPane.showConfirmDialog(
                null,
                inputPanel,
                "Add Client",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String address = addressField.getText().trim();
            String phone = phoneField.getText().trim();

            if (!name.isEmpty() && !address.isEmpty() && !phone.isEmpty()) {
                Client client = warehouse.addClient(name, address, phone);
                JOptionPane.showMessageDialog(
                        warehouseContext.getMainFrame(),
                        "Client added: " + client,
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                        warehouseContext.getMainFrame(),
                        "All fields are required.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void showProducts() {
        Iterator<Product> allProducts = warehouse.getProducts();
        StringBuilder productList = new StringBuilder();

        while (allProducts.hasNext()) {
            Product product = allProducts.next();
            productList.append(product).append("\n");
        }

        if (productList.length() == 0) {
            productList.append("No warehouse products found.");
        }

        JTextArea textArea = new JTextArea(productList.toString());
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));

        JOptionPane.showMessageDialog(
                warehouseContext.getMainFrame(),
                scrollPane,
                "Warehouse Products",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void showClients() {
        Iterator<Client> allClients = warehouse.getClients();
        StringBuilder clientList = new StringBuilder();

        while (allClients.hasNext()) {
            Client client = allClients.next();
            clientList.append(client).append("\n");
        }

        if (clientList.length() == 0) {
            clientList.append("No warehouse clients found.");
        }

        JTextArea textArea = new JTextArea(clientList.toString());
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));

        JOptionPane.showMessageDialog(
                warehouseContext.getMainFrame(),
                scrollPane,
                "Warehouse Clients",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void showClientsWithOutstandingBalance() {
        Iterator<Client> allClients = warehouse.getClients();
        StringBuilder clientList = new StringBuilder();

        while (allClients.hasNext()) {
            Client client = allClients.next();
            if (client.getBalance() > 0) {
                clientList.append(client).append("\n");
            }
        }

        if (clientList.length() == 0) {
            clientList.append("No clients with outstanding balance.");
        }

        JTextArea textArea = new JTextArea(clientList.toString());
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));

        JOptionPane.showMessageDialog(
                warehouseContext.getMainFrame(),
                scrollPane,
                "Clients with Outstanding Balance",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void recordPayment() {
        String clientId = JOptionPane.showInputDialog(
                warehouseContext.getMainFrame(),
                "Enter Client ID:",
                "Record Payment",
                JOptionPane.PLAIN_MESSAGE
        );

        if (clientId != null && !clientId.trim().isEmpty()) {
            String amountStr = JOptionPane.showInputDialog(
                    warehouseContext.getMainFrame(),
                    "Enter Payment Amount:",
                    "Record Payment",
                    JOptionPane.PLAIN_MESSAGE
            );

            if (amountStr != null && !amountStr.trim().isEmpty()) {
                try {
                    double amount = Double.parseDouble(amountStr.trim());
                    String transactionId = warehouse.processClientPayment(clientId.trim(), amount);
                    String transaction = warehouse.getClientTransaction(clientId.trim(), transactionId);

                    JTextArea textArea = new JTextArea("Payment processed successfully.\n\nPayment Invoice:\n" + transaction);
                    textArea.setEditable(false);
                    textArea.setLineWrap(true);
                    textArea.setWrapStyleWord(true);

                    JScrollPane scrollPane = new JScrollPane(textArea);
                    scrollPane.setPreferredSize(new Dimension(500, 400));

                    JOptionPane.showMessageDialog(
                            warehouseContext.getMainFrame(),
                            scrollPane,
                            "Payment Processed",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(
                            warehouseContext.getMainFrame(),
                            "Invalid payment amount.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(
                            warehouseContext.getMainFrame(),
                            "Error processing payment: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }
    }

    private void becomeClient() {
        String clientID = JOptionPane.showInputDialog(
                warehouseContext.getMainFrame(),
                "Enter Client ID:",
                "Become Client",
                JOptionPane.PLAIN_MESSAGE
        );

        if (clientID != null && !clientID.trim().isEmpty()) {
            try {
                warehouse.getClient(clientID.trim());
                warehouseContext.setCurrentClientID(clientID.trim());
                warehouseContext.changeState(WarehouseContext.BECOME_CLIENT);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        warehouseContext.getMainFrame(),
                        "Client ID not found.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void logout() {
        warehouseContext.changeState(WarehouseContext.LOGOUT);
    }
}
