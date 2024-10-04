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
    private static final int SHOW_CLIENTS = 4;
    private static final int SHOW_PRODUCTS = 5;
    private static final int SHOW_CLIENT_WISHLIST = 6;
    private static final int SAVE = 7;
    private static final int HELP = 8;

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
        if (result != null) {
            System.out.println("Client added: " + result);
        } else {
            System.out.println("Could not add client.");
        }
    }

    public void addProduct() {
        String name = getToken("Enter product name: ");
        double price = Double.parseDouble(getToken("Enter product price: "));
        int quantity = getNumber("Enter product quantity: ");
        Product result = warehouse.addProduct(name, price, quantity);
        if (result != null) {
            System.out.println("Product added: " + result);
        } else {
            System.out.println("Could not add product.");
        }
    }

    public void addProductToClientWishlist() {
        String clientId = getToken("Enter client ID: ");
        String productId = getToken("Enter product ID: ");
        int quantity = getNumber("Enter product quantity: ");
        Wishlist.WishlistItem result = warehouse.addProductToClientWishlist(clientId, productId, quantity);
        if (result != null) {
            System.out.println("Product added to client's wishlist: " + result);
        } else {
            System.out.println("Could not add product to client's wishlist.");
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
        System.out.println(SHOW_CLIENTS + " to show all clients");
        System.out.println(SHOW_PRODUCTS + " to show all products");
        System.out.println(SHOW_CLIENT_WISHLIST + " to show a client's wishlist");
        System.out.println(SAVE + " to save data");
        System.out.println(HELP + " for help");
    }

    public void process() {
        int command;
        help();
        while ((command = getCommand()) != EXIT) {
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
            case SHOW_CLIENTS:
                showClients();
                break;
            case SHOW_PRODUCTS:
                showProducts();
                break;
            case SHOW_CLIENT_WISHLIST:
                showClientWishlist();
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
        }
    }

    public static void main(String[] args) {
        UserInterface.instance().process();
    }
}
