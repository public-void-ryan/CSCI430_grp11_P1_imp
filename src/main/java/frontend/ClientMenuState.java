package frontend;

import backend.*;

import java.util.Iterator;

public class ClientMenuState extends WarehouseState {
    private static final int LOGOUT = 0;
    private static final int SHOW_CLIENT_DETAILS = 1;
    private static final int SHOW_PRODUCTS = 2;
    private static final int SHOW_TRANSACTIONS = 3;
    private static final int ADD_TO_WISHLIST = 4;
    private static final int DISPLAY_WISHLIST = 5;
    private static final int PLACE_ORDER = 6;
    private static final int HELP = 7;

    public ClientMenuState(WarehouseContext warehouseContext, Warehouse warehouse) {
        super(warehouseContext, warehouse);
    }

    private int getCommand() {
        return InputUtils.getCommand(LOGOUT, HELP, "Enter command (" + HELP + " for help): ");
    }

    private void help() {
        System.out.println("Client Menu:");
        System.out.println(LOGOUT + " to Logout");
        System.out.println(SHOW_CLIENT_DETAILS + " to Show client details");
        System.out.println(SHOW_PRODUCTS + " to Show list of products");
        System.out.println(SHOW_TRANSACTIONS + " to Show client transactions");
        System.out.println(ADD_TO_WISHLIST + " to Add item to client's wishlist");
        System.out.println(DISPLAY_WISHLIST + " to Display client's wishlist");
        System.out.println(PLACE_ORDER + " to Place an order");
        System.out.println(HELP + " for help");
    }

    @Override
    public void run() {
        int command;
        help();
        while ((command = getCommand()) != LOGOUT) {
            try {
                switch (command) {
                    case SHOW_CLIENT_DETAILS:
                        showClientDetails();
                        break;
                    case SHOW_PRODUCTS:
                        showProducts();
                        break;
                    case SHOW_TRANSACTIONS:
                        showTransactions();
                        break;
                    case ADD_TO_WISHLIST:
                        addToWishlist();
                        break;
                    case DISPLAY_WISHLIST:
                        displayWishlist();
                        break;
                    case PLACE_ORDER:
                        placeOrder();
                        break;
                    case HELP:
                        help();
                        break;
                    default:
                        System.out.println("Invalid choice");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        logout();
    }

    private void showClientDetails() {
        String clientId = warehouseContext.getCurrentClientID();
        Client client = warehouse.getClient(clientId);
        System.out.println("Client Details:");
        System.out.println(client);
    }

    private void showProducts() {
        Iterator<Product> allProducts = warehouse.getProducts();
        System.out.println("Warehouse Products:");

        if (!allProducts.hasNext()) {
            System.out.println("No warehouse products found.");
        }

        while (allProducts.hasNext()) {
            Product product = allProducts.next();
            System.out.println(product);
        }
    }

    private void showTransactions() {
        String clientId = warehouseContext.getCurrentClientID();
        Iterator<TransactionList.TransactionItem> transactions = warehouse.getClientTransactions(clientId);
        System.out.println("Client's Transactions:");

        if (!transactions.hasNext()) {
            System.out.println("No transactions found.");
        }

        while (transactions.hasNext()) {
            TransactionList.TransactionItem transaction = transactions.next();
            System.out.println(transaction);
        }
    }

    private void addToWishlist() {
        String clientId = warehouseContext.getCurrentClientID();
        String productId = InputUtils.getToken("Enter product ID: ");
        int quantity = InputUtils.getNumber("Enter product quantity: ");
        Wishlist.WishlistItem result = warehouse.addProductToClientWishlist(clientId, productId, quantity);
        System.out.println("Product added to client wishlist: " + result);
    }

    private void displayWishlist() {
        String clientId = warehouseContext.getCurrentClientID();
        Iterator<Wishlist.WishlistItem> wishlist = warehouse.getClientWishlistItems(clientId);
        System.out.println("Client Wishlist:");

        if (!wishlist.hasNext()) {
            System.out.println("No wishlist items found.");
        }

        while (wishlist.hasNext()) {
            Wishlist.WishlistItem item = wishlist.next();
            System.out.println(item);
        }
    }

    private void placeOrder() {
        String clientId = warehouseContext.getCurrentClientID();
        Iterator<Wishlist.WishlistItem> wishlist = warehouse.getClientWishlistItems(clientId);
        System.out.println("Client Wishlist:");

        if (!wishlist.hasNext()) {
            System.out.println("No wishlist items found.");
            return;
        }

        while (wishlist.hasNext()) {
            Wishlist.WishlistItem item = wishlist.next();
            System.out.println(item);

            String productId = item.getProduct().getId();

            if (InputUtils.yesOrNo("Would you like to purchase this product?")) {
                int quantity = InputUtils.getNumber("Enter product quantity: ");
                warehouse.removeProductFromClientWishlist(clientId, productId);
                warehouse.addProductToClientWishlist(clientId, productId, quantity);
            } else {
                warehouse.removeProductFromClientWishlist(clientId, productId);
            }
        }

        wishlist = warehouse.getClientWishlistItems(clientId);
        if (wishlist.hasNext()) {
            System.out.println("Order placed successfully.");
            System.out.println("Order invoice: ");
        } else {
            System.out.println("No items to order.");
        }

        while (wishlist.hasNext()) {
            Wishlist.WishlistItem item = wishlist.next();
            String transactionId = warehouse.processClientOrder(clientId, item.getProduct().getId(), item.getQuantity());
            String transaction = warehouse.getClientTransaction(clientId, transactionId);
            System.out.println(transaction);
        }

        warehouse.clearClientWishlist(clientId);
    }

    private void logout() {
        warehouseContext.changeState(WarehouseContext.LOGOUT);
    }
}
