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
    private static final int SHOW_CLIENT = 5;
    private static final int SHOW_CLIENTS = 6;
    private static final int SHOW_PRODUCT = 7;
    private static final int SHOW_PRODUCTS = 8;
    private static final int SHOW_CLIENT_WISHLIST = 9;
    private static final int SHOW_CLIENT_TRANSACTIONS = 10;
    private static final int SHOW_PRODUCT_WAITLIST = 11;
    private static final int SAVE = 12;
    private static final int HELP = 13;

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
        System.out.println("Product added to client's wishlist: " + result);
    }

    public void processClientOrder() {
        String clientId = getToken("Enter client ID: ");
        Iterator<Wishlist.WishlistItem> wishlist = warehouse.getClientWishlistItems(clientId);
        if (wishlist != null) {
            System.out.println("Client's Wishlist:");
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

                Product product = item.getProduct();
                if (product.getStockLevel() > item.getQuantity()) {
                    // Simply place order
                } else {
                    // Place order with available amount and place rest on waitlist
                }
            }

            warehouse.clearClientWishlist(clientId);
        } else {
            System.out.println("Client not found or wishlist is empty.");
        }
    }

    public void showClient() {
        String clientId = getToken("Enter client ID: ");
        Client client = warehouse.getClient(clientId);
        if (client != null) {
            System.out.println("Client Details:");
            System.out.println(client);
        } else {
            System.out.println("Client not found.");
        }
    }

    public void showClients() {
        Iterator<Client> allClients = warehouse.getClients();
        System.out.println("Warehouse Clients:");
        while (allClients.hasNext()) {
            Client client = allClients.next();
            System.out.println(client);
        }
    }

    public void showProduct() {
        String productId = getToken("Enter product ID: ");
        Product product = warehouse.getProduct(productId);
        if (product != null) {
            System.out.println("Product Details:");
            System.out.println(product);
        } else {
            System.out.println("Product not found.");
        }
    }

    public void showProducts() {
        Iterator<Product> allProducts = warehouse.getProducts();
        System.out.println("Warehouse Products:");
        while (allProducts.hasNext()) {
            Product product = allProducts.next();
            System.out.println(product);
        }
    }

    public void showClientWishlist() {
        String clientId = getToken("Enter client ID: ");
        Iterator<Wishlist.WishlistItem> wishlist = warehouse.getClientWishlistItems(clientId);
        if (wishlist != null) {
            System.out.println("Client's Wishlist:");
            while (wishlist.hasNext()) {
                Wishlist.WishlistItem item = wishlist.next();
                System.out.println(item);
            }
        } else {
            System.out.println("Client not found or wishlist is empty.");
        }
    }

    public void showClientTransactions() {
        String clientId = getToken("Enter client ID: ");
        Iterator<Transaction> transactions = warehouse.getClientTransactions(clientId);
        if (transactions != null) {
            System.out.println("Client's Transactions:");
            while (transactions.hasNext()) {
                Transaction transaction = transactions.next();
                System.out.println(transaction);
            }
        } else {
            System.out.println("Client not found or no transactions available.");
        }
    }

    public void showProductWaitlist() {
        String productId = getToken("Enter product ID: ");
        Iterator<Client> waitlist = warehouse.getProductWaitlist(productId);
        if (waitlist != null) {
            System.out.println("Waitlisted Clients for Product:");
            while (waitlist.hasNext()) {
                Client client = waitlist.next();
                System.out.println(client);
            }
        } else {
            System.out.println("Product not found or no clients on the waitlist.");
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
        System.out.println(SHOW_CLIENT + " to display a specific client");
        System.out.println(SHOW_CLIENTS + " to show all clients");
        System.out.println(SHOW_PRODUCT + " to display a specific product");
        System.out.println(SHOW_PRODUCTS + " to show all products");
        System.out.println(SHOW_CLIENT_WISHLIST + " to show a client's wishlist");
        System.out.println(SHOW_CLIENT_TRANSACTIONS + " to display client transactions");
        System.out.println(SHOW_PRODUCT_WAITLIST + " to display product waitlist");
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
