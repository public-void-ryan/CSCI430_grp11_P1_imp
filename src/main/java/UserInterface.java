import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Scanner;
import java.util.StringTokenizer;

public class UserInterface {
    private static UserInterface userInterface;
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static Warehouse warehouse;
    private static int currentState;
    private static final int OPENING_STATE = 0;
    private static final int CLIENT_MENU_STATE = 1;
    private static final int CLERK_MENU_STATE = 2;
    private static final int MANAGER_MENU_STATE = 3;
    private static final int EXIT = 0;
    private static final int SHOW_CLIENT_DETAILS = 1;
    private static final int SHOW_PRODUCTS = 2;
    private static final int SHOW_CLIENT_TRANSACTIONS = 3;
    private static final int ADD_ITEM_TO_WISHLIST = 4;
    private static final int DISPLAY_WISHLIST = 5;
    private static final int PLACE_ORDER = 6;
    private static final int LOGOUT = 7;
    private static final int HELP = 8;

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

    public void save() {
        if (Warehouse.save()) {
            System.out.println("Warehouse data successfully saved.");
        } else {
            System.out.println("Error saving warehouse data.");
        }
    }

    public static UserInterface instance() {
        return userInterface == null ? (userInterface = new UserInterface()) : userInterface;
    }

    public void showClientDetails() {
        Client client = warehouse.getClient(currentClientId);
        System.out.println("Client Details:");
        System.out.println(client);
    }

    public void showProducts() {
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

    public void showClientTransactions() {
        String clientId = getToken("Enter client ID: ");
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

    public void addItemToWishlist() {
        String clientId = getToken("Enter client ID: ");
        String productId = getToken("Enter product ID: ");
        int quantity = getNumber("Enter product quantity: ");
        Wishlist.WishlistItem result = warehouse.addProductToClientWishlist(clientId, productId, quantity);
        System.out.println("Product added to client wishlist: " + result);
    }

    public void displayWishlist() {
        String clientId = getToken("Enter client ID: ");
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

    public void placeOrder() {
        String clientId = getToken("Enter client ID: ");
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

            if (yesOrNo("Would you like to purchase this product?")) {
                int quantity = getNumber("Enter product quantity: ");
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
            String transactionId = warehouse.processClientOrder(clientId, item.getProduct().getId(),
                    item.getQuantity());
            String transaction = warehouse.getClientTransaction(clientId, transactionId);
            System.out.println(transaction);
        }

        warehouse.clearClientWishlist(clientId);
    }

    public void logout() {
        System.out.println("Logging out...");
        currentState = OPENING_STATE;
    }

    public void help() {
        System.out.println("Enter a number between 0 and " + HELP + " as explained below:");
        System.out.println(EXIT + " to exit");
        System.out.println(SHOW_CLIENT_DETAILS + " to show client details");
        System.out.println(SHOW_PRODUCTS + " to show products");
        System.out.println(SHOW_CLIENT_TRANSACTIONS + " to show client transactions");
        System.out.println(ADD_ITEM_TO_WISHLIST + " to add item to wishlist");
        System.out.println(DISPLAY_WISHLIST + " to display wishlist");
        System.out.println(PLACE_ORDER + " to place an order");
        System.out.println(LOGOUT + " to logout");
        System.out.println(HELP + " for help");
    }

    public void process() {
        currentState = OPENING_STATE;
        while (true) {
            switch (currentState) {
                case OPENING_STATE:
                    openingState();
                    break;
                case CLIENT_MENU_STATE:
                    clientMenu();
                    break;
                case CLERK_MENU_STATE:
                    clerkMenu();
                    break;
                case MANAGER_MENU_STATE:
                    managerMenu();
                    break;
            }
        }
    }

    private String currentClientId;

    private void openingState() {
        System.out.println("Opening State:");
        System.out.println("1. Client Menu");
        System.out.println("2. Clerk Menu");
        System.out.println("3. Manager Menu");
        System.out.println("4. Save Warehouse Data");
        System.out.println("0. Exit");

        int choice = getNumber("Enter your choice: ");
        switch (choice) {
            case 1:
                currentClientId = getToken("Enter client ID: ");
                if (warehouse.getClient(currentClientId) != null) {
                    currentState = CLIENT_MENU_STATE;
                } else {
                    System.out.println("Invalid client ID.");
                }
                break;
            case 2:
                currentState = CLERK_MENU_STATE;
                break;
            case 3:
                currentState = MANAGER_MENU_STATE;
                break;
            case 4:
                save();
                break;
            case 0:
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void clientMenu() {
        int command;
        help();
        while ((command = getCommand()) != LOGOUT) {
            switch (command) {
                case SHOW_CLIENT_DETAILS:
                    showClientDetails();
                    break;
                case SHOW_PRODUCTS:
                    showProducts();
                    break;
                case SHOW_CLIENT_TRANSACTIONS:
                    showClientTransactions();
                    break;
                case ADD_ITEM_TO_WISHLIST:
                    addItemToWishlist();
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
                    System.out.println("Invalid command.");
            }
        }
        logout();
    }

    private void clerkMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        while (!exit) {
            System.out.println("Clerk Menu State:");
            System.out.println("1. Add New Client");
            System.out.println("2. Other Clerk Functionality");
            System.out.println("3. Logout");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addClient();
                    break;
                case 2:
                    // Implement other clerk functionalities here
                    break;
                case 3:
                    logout();
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public void addClient() {
        String name = getToken("Enter client name: ");
        String address = getToken("Enter address: ");
        String phone = getToken("Enter phone: ");
        Client result = warehouse.addClient(name, address, phone);
        System.out.println("Client added: " + result);
    }

    private void managerMenu() {
        // Implement manager-specific functionalities here
        System.out.println("Manager Menu State:");
        // Add manager-specific commands and functionalities

    }

    public static void main(String[] args) {
        UserInterface.instance().process();
    }
}