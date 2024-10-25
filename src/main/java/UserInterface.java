import java.util.*;
import java.io.*;

public class UserInterface {
    private static UserInterface userInterface;
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static Warehouse warehouse;
    private static final int EXIT = 0;
    private static final int ADD_CLIENT = 1;
    private static final int ADD_PRODUCT = 2;
    private static final int ADD_PRODUCT_TO_CLIENT_WISHLIST = 3;
    private static final int PROCESS_CLIENT_ORDER = 4;
    private static final int PROCESS_CLIENT_PAYMENT = 5;
    private static final int PROCESS_PRODUCT_SHIPMENT = 6;
    private static final int SHOW_CLIENT = 7;
    private static final int SHOW_CLIENTS = 8;
    private static final int SHOW_PRODUCT = 9;
    private static final int SHOW_PRODUCTS = 10;
    private static final int SHOW_CLIENT_WISHLIST = 11;
    private static final int SHOW_CLIENT_TRANSACTIONS = 12;
    private static final int SHOW_PRODUCT_WAITLIST = 13;
    private static final int SAVE = 14;
    private static final int HELP = 15;

    // Private utility methods
    private UserInterface() {
        if (yesOrNo("Look for saved data and use it?")) {
            retrieve();
        } else {
            warehouse = Warehouse.instance();
        }
    }

    private boolean yesOrNo(String prompt) {
        String more = getToken(prompt + " (Y|N): ");
        return more.charAt(0) == 'y' || more.charAt(0) == 'Y';
    }

    private String getToken(String prompt) {
        do {
            try {
                System.out.print(prompt);
                String line = reader.readLine();
                StringTokenizer tokenizer = new StringTokenizer(line, "\n\r\f");
                if (tokenizer.hasMoreTokens()) {
                    return tokenizer.nextToken();
                }
            } catch (IOException ioe) {
                System.exit(0);
            }
        } while (true);
    }

    private int getNumber(String prompt) {
        do {
            try {
                String item = getToken(prompt);
                return Integer.parseInt(item);
            } catch (NumberFormatException nfe) {
                System.out.println("Please input a number: ");
            }
        } while (true);
    }

    private int getCommand() {
        do {
            try {
                int value = Integer.parseInt(getToken("Enter command (" + HELP + " for help): "));
                if (value >= EXIT && value <= HELP) {
                    return value;
                } else {
                    System.out.println("Command out of range.");
                }
            } catch (NumberFormatException nfe) {
                System.out.println("Enter a valid number.");
            }
        } while (true);
    }

    private void retrieve() {
        try {
            Warehouse tempWarehouse = Warehouse.retrieve();
            if (tempWarehouse != null) {
                System.out.println("Warehouse data successfully retrieved.");
                warehouse = tempWarehouse;
            } else {
                System.out.println("No saved data found. Creating new warehouse.");
                warehouse = Warehouse.instance();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Public operation methods
    public static UserInterface instance() {
        return Objects.requireNonNullElseGet(userInterface, () -> userInterface = new UserInterface());
    }

    public void addClient() {
        String name = getToken("Enter client name: ");
        String address = getToken("Enter address: ");
        String phone = getToken("Enter phone: ");
        Client result = warehouse.addClient(name, address, phone);
        System.out.println("Client added: " + result);
    }

    public void addProduct() {
        String name = getToken("Enter product name: ");
        double price = Double.parseDouble(getToken("Enter product price: "));
        int quantity = getNumber("Enter product quantity: ");
        Product result = warehouse.addProduct(name, price, quantity);
        System.out.println("Product added: " + result);
    }

    public void addProductToClientWishlist() {
        String clientId = getToken("Enter client ID: ");
        String productId = getToken("Enter product ID: ");
        int quantity = getNumber("Enter product quantity: ");
        Wishlist.WishlistItem result = warehouse.addProductToClientWishlist(clientId, productId, quantity);
        System.out.println("Product added to client" + clientId + "wishlist: " + result);
    }

    public void processClientOrder() {
        String clientId = getToken("Enter client ID: ");
        Iterator<Wishlist.WishlistItem> wishlist = warehouse.getClientWishlistItems(clientId);
        System.out.println("Client" + clientId + "Wishlist:");
        while (wishlist.hasNext()) {
            Wishlist.WishlistItem item = wishlist.next();
            System.out.println(item);

            String productId = item.getProduct().getId();

            if (yesOrNo("Would you like to purchase this product?")) {
                int quantity = getNumber("Enter product quantity: ");
                warehouse.removeProductFromClientWishlist(clientId, productId);
                warehouse.addProductToClientWishlist(clientId, productId, quantity);
            } else {
                warehouse.removeProductFromClientWishlist(clientId, productId);
            }
        }

        wishlist = warehouse.getClientWishlistItems(clientId);
        while (wishlist.hasNext()) {
            Wishlist.WishlistItem item = wishlist.next();
            warehouse.processClientOrder(clientId, item.getProduct().getId(), item.getQuantity());
        }

        warehouse.clearClientWishlist(clientId);

        System.out.println("Order invoice: ");
    }

    public void processClientPayment() {
        String clientId = getToken("Enter client ID: ");
        double amount = Double.parseDouble(getToken("Enter payment amount: "));
        String transactionId = warehouse.processClientPayment(clientId, amount);
        String transaction = warehouse.getClientTransaction(clientId, transactionId);
        System.out.println("Payment processed successfully.");
        System.out.println(transaction);
    }

    public void processProductShipment() {
        String productId = getToken("Enter product ID: ");
        int quantityReceived = getNumber("Enter quantity received: ");
        warehouse.processProductShipment(productId, quantityReceived);
        System.out.println("Shipment processed successfully.");
    }

    public void showClient() {
        String clientId = getToken("Enter client ID: ");
        Client client = warehouse.getClient(clientId);
        System.out.println("Client Details:");
        System.out.println(client);
    }

    public void showClients() {
        Iterator<Client> allClients = warehouse.getClients();
        System.out.println("Warehouse Clients:");
        while (allClients.hasNext()) {
            Client client = allClients.next();
            System.out.println(client);
        }
    }

    public void showClientWishlist() {
        String clientId = getToken("Enter client ID: ");
        Iterator<Wishlist.WishlistItem> wishlist = warehouse.getClientWishlistItems(clientId);
        System.out.println("Client " + clientId + " Wishlist:");
        while (wishlist.hasNext()) {
            Wishlist.WishlistItem item = wishlist.next();
            System.out.println(item);
        }
    }

    public void showClientTransactions() {
        String clientId = getToken("Enter client ID: ");
        // Iterator<Transaction> transactions =
        // warehouse.getClientTransactions(clientId);
        // System.out.println("Client's Transactions:");
        // while (transactions.hasNext()) {
        // Transaction transaction = transactions.next();
        // System.out.println(transaction);
        // }
    }

    public void showProduct() {
        String productId = getToken("Enter product ID: ");
        Product product = warehouse.getProduct(productId);
        System.out.println("Product Details:");
        System.out.println(product);
    }

    public void showProducts() {
        Iterator<Product> allProducts = warehouse.getProducts();
        System.out.println("Warehouse Products:");
        while (allProducts.hasNext()) {
            Product product = allProducts.next();
            System.out.println(product);
        }
    }

    public void showProductWaitlist() {
        String productId = getToken("Enter product ID: ");
        Iterator<Waitlist.WaitlistItem> waitlist = warehouse.getProductWaitlistItems(productId);
        System.out.println("Waitlisted Clients for Product:");
        while (waitlist.hasNext()) {
            Waitlist.WaitlistItem item = waitlist.next();
            System.out.println(item);
        }
    }

    public void save() {
        if (Warehouse.save()) {
            System.out.println("Warehouse data successfully saved.");
        } else {
            System.out.println("Error saving warehouse data.");
        }
    }

    public void help() {
        System.out.println("Enter a number between 0 and " + HELP + " as explained below:");
        System.out.println(EXIT + " to exit");
        System.out.println(ADD_CLIENT + " to add a client");
        System.out.println(ADD_PRODUCT + " to add a product");
        System.out.println(ADD_PRODUCT_TO_CLIENT_WISHLIST + " to add a product to a client's wishlist");
        System.out.println(PROCESS_CLIENT_ORDER + " to process a client order");
        System.out.println(PROCESS_CLIENT_PAYMENT + " to process a client payment");
        System.out.println(PROCESS_PRODUCT_SHIPMENT + " to process a product shipment");
        System.out.println(SHOW_CLIENT + " to show a specific client");
        System.out.println(SHOW_CLIENTS + " to show all clients");
        System.out.println(SHOW_PRODUCT + " to show a specific product");
        System.out.println(SHOW_PRODUCTS + " to show all products");
        System.out.println(SHOW_CLIENT_WISHLIST + " to show a client's wishlist");
        System.out.println(SHOW_CLIENT_TRANSACTIONS + " to show client transactions");
        System.out.println(SHOW_PRODUCT_WAITLIST + " to show product waitlist");
        System.out.println(SAVE + " to save data");
        System.out.println(HELP + " for help");
    }

    public void process() {
        int command;
        help();
        while ((command = getCommand()) != EXIT) {
            try {
                switch (command) {
                    case ADD_CLIENT:
                        addClient();
                        break;
                    case ADD_PRODUCT:
                        addProduct();
                        break;
                    case ADD_PRODUCT_TO_CLIENT_WISHLIST:
                        addProductToClientWishlist();
                        break;
                    case PROCESS_CLIENT_ORDER:
                        processClientOrder();
                        break;
                    case PROCESS_CLIENT_PAYMENT:
                        processClientPayment();
                        break;
                    case PROCESS_PRODUCT_SHIPMENT:
                        processProductShipment();
                        break;
                    case SHOW_CLIENT:
                        showClient();
                        break;
                    case SHOW_CLIENTS:
                        showClients();
                        break;
                    case SHOW_PRODUCT:
                        showProduct();
                        break;
                    case SHOW_PRODUCTS:
                        showProducts();
                        break;
                    case SHOW_CLIENT_WISHLIST:
                        showClientWishlist();
                        break;
                    case SHOW_CLIENT_TRANSACTIONS:
                        showClientTransactions();
                        break;
                    case SHOW_PRODUCT_WAITLIST:
                        showProductWaitlist();
                        break;
                    case SAVE:
                        save();
                        break;
                    case HELP:
                        help();
                        break;
                    default:
                        System.out.println("Invalid command!");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        UserInterface.instance().process();
    }
}
