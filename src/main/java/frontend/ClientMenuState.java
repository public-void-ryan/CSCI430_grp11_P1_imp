package frontend;

import backend.*;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;

public class ClientMenuState extends WarehouseState {

    public ClientMenuState(WarehouseContext warehouseContext, Warehouse warehouse) {
        super(warehouseContext, warehouse);
    }

    @Override
    public void run() {
        JFrame mainFrame = warehouseContext.getMainFrame();

        // Use MenuPanelBuilder to create the menu panel
        MenuPanelBuilder menuBuilder = new MenuPanelBuilder("Client Menu");

        // Create the buttons and labels
        JButton showClientDetailsButton = new JButton("Show Client Details");
        JLabel showClientDetailsLabel = new JLabel("View your client details.");

        JButton showProductsButton = new JButton("Show Products");
        JLabel showProductsLabel = new JLabel("View the list of products.");

        JButton showTransactionsButton = new JButton("Show Transactions");
        JLabel showTransactionsLabel = new JLabel("View your transaction history.");

        JButton addToWishlistButton = new JButton("Add to Wishlist");
        JLabel addToWishlistLabel = new JLabel("Add a product to your wishlist.");

        JButton displayWishlistButton = new JButton("Display Wishlist");
        JLabel displayWishlistLabel = new JLabel("View your wishlist.");

        JButton placeOrderButton = new JButton("Place Order");
        JLabel placeOrderLabel = new JLabel("Place an order for items in your wishlist.");

        JButton logoutButton = new JButton("Logout");
        JLabel logoutLabel = new JLabel("Logout and return to the login menu.");

        // Add action listeners
        showClientDetailsButton.addActionListener(e -> showClientDetails());
        showProductsButton.addActionListener(e -> showProducts());
        showTransactionsButton.addActionListener(e -> showTransactions());
        addToWishlistButton.addActionListener(e -> addToWishlist());
        displayWishlistButton.addActionListener(e -> displayWishlist());
        placeOrderButton.addActionListener(e -> placeOrder());
        logoutButton.addActionListener(e -> logout());

        // Add buttons and labels to the menu
        menuBuilder.addButtonAndLabel(showClientDetailsButton, showClientDetailsLabel);
        menuBuilder.addButtonAndLabel(showProductsButton, showProductsLabel);
        menuBuilder.addButtonAndLabel(showTransactionsButton, showTransactionsLabel);
        menuBuilder.addButtonAndLabel(addToWishlistButton, addToWishlistLabel);
        menuBuilder.addButtonAndLabel(displayWishlistButton, displayWishlistLabel);
        menuBuilder.addButtonAndLabel(placeOrderButton, placeOrderLabel);
        menuBuilder.addButtonAndLabel(logoutButton, logoutLabel);

        // Build the panel and add it to the main frame
        JPanel panel = menuBuilder.build();

        mainFrame.getContentPane().removeAll();
        mainFrame.getContentPane().add(panel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private void showClientDetails() {
        String clientId = warehouseContext.getCurrentClientID();
        Client client = warehouse.getClient(clientId);

        if (client != null) {
            JTextArea textArea = new JTextArea(client.toString());
            textArea.setEditable(false);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));

            JOptionPane.showMessageDialog(
                    warehouseContext.getMainFrame(),
                    scrollPane,
                    "Client Details",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog(
                    warehouseContext.getMainFrame(),
                    "Client not found.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
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

    private void showTransactions() {
        String clientId = warehouseContext.getCurrentClientID();
        Iterator<TransactionList.TransactionItem> transactions = warehouse.getClientTransactions(clientId);
        StringBuilder transactionList = new StringBuilder();

        while (transactions.hasNext()) {
            TransactionList.TransactionItem transaction = transactions.next();
            transactionList.append(transaction).append("\n");
        }

        if (transactionList.length() == 0) {
            transactionList.append("No transactions found.");
        }

        JTextArea textArea = new JTextArea(transactionList.toString());
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));

        JOptionPane.showMessageDialog(
                warehouseContext.getMainFrame(),
                scrollPane,
                "Client's Transactions",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void addToWishlist() {
        String clientId = warehouseContext.getCurrentClientID();

        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JTextField productIdField = new JTextField();
        JTextField quantityField = new JTextField();

        inputPanel.add(new JLabel("Product ID:"));
        inputPanel.add(productIdField);
        inputPanel.add(new JLabel("Quantity:"));
        inputPanel.add(quantityField);

        int result = JOptionPane.showConfirmDialog(
                warehouseContext.getMainFrame(),
                inputPanel,
                "Add to Wishlist",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String productId = productIdField.getText().trim();
            String quantityStr = quantityField.getText().trim();

            if (!productId.isEmpty() && !quantityStr.isEmpty()) {
                try {
                    int quantity = Integer.parseInt(quantityStr);
                    Wishlist.WishlistItem item = warehouse.addProductToClientWishlist(clientId, productId, quantity);
                    JOptionPane.showMessageDialog(
                            warehouseContext.getMainFrame(),
                            "Product added to wishlist: " + item,
                            "Success",
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
                            "Error adding to wishlist: " + e.getMessage(),
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

    private void displayWishlist() {
        String clientId = warehouseContext.getCurrentClientID();
        Iterator<Wishlist.WishlistItem> wishlist = warehouse.getClientWishlistItems(clientId);
        StringBuilder wishlistItems = new StringBuilder();

        while (wishlist.hasNext()) {
            Wishlist.WishlistItem item = wishlist.next();
            wishlistItems.append(item).append("\n");
        }

        if (wishlistItems.length() == 0) {
            wishlistItems.append("No wishlist items found.");
        }

        JTextArea textArea = new JTextArea(wishlistItems.toString());
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));

        JOptionPane.showMessageDialog(
                warehouseContext.getMainFrame(),
                scrollPane,
                "Client Wishlist",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void placeOrder() {
        String clientId = warehouseContext.getCurrentClientID();
        Iterator<Wishlist.WishlistItem> wishlistIterator = warehouse.getClientWishlistItems(clientId);

        java.util.List<Wishlist.WishlistItem> wishlistItems = new java.util.ArrayList<>();
        while (wishlistIterator.hasNext()) {
            wishlistItems.add(wishlistIterator.next());
        }

        if (wishlistItems.isEmpty()) {
            JOptionPane.showMessageDialog(
                    warehouseContext.getMainFrame(),
                    "No wishlist items found.",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        java.util.List<Wishlist.WishlistItem> itemsToOrder = new java.util.ArrayList<>();

        for (Wishlist.WishlistItem item : wishlistItems) {
            String productId = item.getProduct().getId();
            String message = "Do you want to purchase this product?\n" + item;
            int choice = JOptionPane.showConfirmDialog(
                    warehouseContext.getMainFrame(),
                    message,
                    "Purchase Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (choice == JOptionPane.YES_OPTION) {
                String quantityStr = JOptionPane.showInputDialog(
                        warehouseContext.getMainFrame(),
                        "Enter quantity for product ID " + productId + ":",
                        "Enter Quantity",
                        JOptionPane.PLAIN_MESSAGE
                );

                if (quantityStr != null && !quantityStr.trim().isEmpty()) {
                    try {
                        int quantity = Integer.parseInt(quantityStr.trim());
                        warehouse.removeProductFromClientWishlist(clientId, productId);
                        warehouse.addProductToClientWishlist(clientId, productId, quantity);
                        itemsToOrder.add(new Wishlist.WishlistItem(item.getProduct(), quantity));
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(
                                warehouseContext.getMainFrame(),
                                "Invalid quantity entered.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(
                                warehouseContext.getMainFrame(),
                                "Error updating wishlist: " + e.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            } else {
                warehouse.removeProductFromClientWishlist(clientId, productId);
            }
        }

        if (!itemsToOrder.isEmpty()) {
            StringBuilder orderInvoice = new StringBuilder();
            orderInvoice.append("Order placed successfully.\n\nOrder Invoice:\n");

            for (Wishlist.WishlistItem item : itemsToOrder) {
                String transactionId = warehouse.processClientOrder(clientId, item.getProduct().getId(), item.getQuantity());
                String transaction = warehouse.getClientTransaction(clientId, transactionId);
                orderInvoice.append(transaction).append("\n");
            }

            JTextArea textArea = new JTextArea(orderInvoice.toString());
            textArea.setEditable(false);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 400));

            JOptionPane.showMessageDialog(
                    warehouseContext.getMainFrame(),
                    scrollPane,
                    "Order Confirmation",
                    JOptionPane.INFORMATION_MESSAGE
            );

            warehouse.clearClientWishlist(clientId);
        } else {
            JOptionPane.showMessageDialog(
                    warehouseContext.getMainFrame(),
                    "No items were ordered.",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private void logout() {
        warehouseContext.changeState(WarehouseContext.LOGOUT);
    }
}
