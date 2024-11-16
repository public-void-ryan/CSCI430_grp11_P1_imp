package frontend;

import backend.*;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class ManagerMenuState extends WarehouseState {

    public ManagerMenuState(WarehouseContext warehouseContext, Warehouse warehouse) {
        super(warehouseContext, warehouse);
    }

    @Override
    public void run() {
        JFrame mainFrame = warehouseContext.getMainFrame();

        // Use MenuPanelBuilder to create the menu panel
        MenuPanelBuilder menuBuilder = new MenuPanelBuilder("Manager Menu");

        // Create the buttons and labels
        JButton addProductButton = new JButton("Add Product");
        JLabel addProductLabel = new JLabel("Add a new product to the warehouse.");

        JButton displayWaitlistButton = new JButton("Display Waitlist");
        JLabel displayWaitlistLabel = new JLabel("View the waitlist for a product.");

        JButton receiveShipmentButton = new JButton("Receive Shipment");
        JLabel receiveShipmentLabel = new JLabel("Process a shipment for a product.");

        JButton becomeClerkButton = new JButton("Become Clerk");
        JLabel becomeClerkLabel = new JLabel("Switch to clerk view to perform clerk operations.");

        JButton logoutButton = new JButton("Logout");
        JLabel logoutLabel = new JLabel("Logout and return to the login menu.");

        // Add action listeners
        addProductButton.addActionListener(e -> addProduct());
        displayWaitlistButton.addActionListener(e -> displayWaitlist());
        receiveShipmentButton.addActionListener(e -> receiveShipment());
        becomeClerkButton.addActionListener(e -> becomeClerk());
        logoutButton.addActionListener(e -> logout());

        // Add buttons and labels to the menu
        menuBuilder.addButtonAndLabel(addProductButton, addProductLabel);
        menuBuilder.addButtonAndLabel(displayWaitlistButton, displayWaitlistLabel);
        menuBuilder.addButtonAndLabel(receiveShipmentButton, receiveShipmentLabel);
        menuBuilder.addButtonAndLabel(becomeClerkButton, becomeClerkLabel);
        menuBuilder.addButtonAndLabel(logoutButton, logoutLabel);

        // Build the panel and add it to the main frame
        JPanel panel = menuBuilder.build();

        mainFrame.getContentPane().removeAll();
        mainFrame.getContentPane().add(panel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private void addProduct() {
        do {
            JPanel inputPanel = new JPanel(new GridLayout(0, 2, 10, 10));
            JTextField nameField = new JTextField();
            JTextField priceField = new JTextField();
            JTextField quantityField = new JTextField();

            inputPanel.add(new JLabel("Product Name:"));
            inputPanel.add(nameField);
            inputPanel.add(new JLabel("Price:"));
            inputPanel.add(priceField);
            inputPanel.add(new JLabel("Quantity:"));
            inputPanel.add(quantityField);

            int result = JOptionPane.showConfirmDialog(
                    warehouseContext.getMainFrame(),
                    inputPanel,
                    "Add Product",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (result == JOptionPane.OK_OPTION) {
                String name = nameField.getText().trim();
                String priceStr = priceField.getText().trim();
                String quantityStr = quantityField.getText().trim();

                if (!name.isEmpty() && !priceStr.isEmpty() && !quantityStr.isEmpty()) {
                    try {
                        double price = Double.parseDouble(priceStr);
                        int quantity = Integer.parseInt(quantityStr);
                        Product product = warehouse.addProduct(name, price, quantity);
                        JOptionPane.showMessageDialog(
                                warehouseContext.getMainFrame(),
                                "Product added: " + product,
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(
                                warehouseContext.getMainFrame(),
                                "Invalid price or quantity.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(
                                warehouseContext.getMainFrame(),
                                "Error adding product: " + e.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                } else {
                    JOptionPane.showMessageDialog(
                            warehouseContext.getMainFrame(),
                            "All fields are required.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            } else {
                break;
            }
        } while (JOptionPane.showConfirmDialog(
                warehouseContext.getMainFrame(),
                "Would you like to add another product?",
                "Add Another",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        ) == JOptionPane.YES_OPTION);
    }

    private void displayWaitlist() {
        String productId = JOptionPane.showInputDialog(
                warehouseContext.getMainFrame(),
                "Enter Product ID:",
                "Display Waitlist",
                JOptionPane.PLAIN_MESSAGE
        );

        if (productId != null && !productId.trim().isEmpty()) {
            Iterator<Waitlist.WaitlistItem> waitlist = warehouse.getProductWaitlistItems(productId.trim());
            StringBuilder waitlistItems = new StringBuilder();

            while (waitlist.hasNext()) {
                Waitlist.WaitlistItem item = waitlist.next();
                waitlistItems.append(item).append("\n");
            }

            if (waitlistItems.length() == 0) {
                waitlistItems.append("No waitlist items found.");
            }

            JTextArea textArea = new JTextArea(waitlistItems.toString());
            textArea.setEditable(false);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 400));

            JOptionPane.showMessageDialog(
                    warehouseContext.getMainFrame(),
                    scrollPane,
                    "Waitlist for Product ID " + productId.trim(),
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private void receiveShipment() {
        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JTextField productIdField = new JTextField();
        JTextField quantityField = new JTextField();

        inputPanel.add(new JLabel("Product ID:"));
        inputPanel.add(productIdField);
        inputPanel.add(new JLabel("Quantity Received:"));
        inputPanel.add(quantityField);

        int result = JOptionPane.showConfirmDialog(
                warehouseContext.getMainFrame(),
                inputPanel,
                "Receive Shipment",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String productId = productIdField.getText().trim();
            String quantityStr = quantityField.getText().trim();

            if (!productId.isEmpty() && !quantityStr.isEmpty()) {
                try {
                    int quantityReceived = Integer.parseInt(quantityStr);
                    Iterator<Map<String, String>> shipmentTransactionInfo = warehouse.processProductShipment(productId, quantityReceived);

                    StringBuilder shipmentInfo = new StringBuilder();
                    shipmentInfo.append("Shipment processed successfully.\n\n");

                    if (shipmentTransactionInfo.hasNext()) {
                        shipmentInfo.append("Shipment invoice:\n");
                    }

                    while (shipmentTransactionInfo.hasNext()) {
                        Map<String, String> transactionInfo = shipmentTransactionInfo.next();
                        String clientId = transactionInfo.get("clientId");
                        String transactionId = transactionInfo.get("transactionId");
                        Client client = warehouse.getClient(clientId);
                        String transaction = warehouse.getClientTransaction(clientId, transactionId);

                        shipmentInfo.append("Client Details:\n");
                        shipmentInfo.append(client).append("\n");
                        shipmentInfo.append("Transaction:\n");
                        shipmentInfo.append(transaction).append("\n\n");
                    }

                    JTextArea textArea = new JTextArea(shipmentInfo.toString());
                    textArea.setEditable(false);
                    textArea.setLineWrap(true);
                    textArea.setWrapStyleWord(true);

                    JScrollPane scrollPane = new JScrollPane(textArea);
                    scrollPane.setPreferredSize(new Dimension(500, 400));

                    JOptionPane.showMessageDialog(
                            warehouseContext.getMainFrame(),
                            scrollPane,
                            "Shipment Processed",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(
                            warehouseContext.getMainFrame(),
                            "Invalid quantity.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(
                            warehouseContext.getMainFrame(),
                            "Error processing shipment: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
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

    private void becomeClerk() {
        String managerPassword = JOptionPane.showInputDialog(
                warehouseContext.getMainFrame(),
                "Enter Manager Password:",
                "Become Clerk",
                JOptionPane.PLAIN_MESSAGE
        );

        if (managerPassword != null) {
            if (Objects.equals(managerPassword, "manager")) {
                warehouseContext.changeState(WarehouseContext.BECOME_CLERK);
            } else {
                JOptionPane.showMessageDialog(
                        warehouseContext.getMainFrame(),
                        "Invalid Manager Password",
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
