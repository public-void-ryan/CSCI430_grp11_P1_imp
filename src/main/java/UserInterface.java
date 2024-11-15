import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.NoSuchElementException;
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
    private SecuritySystem securitySystem = new SecuritySystem();
    private SessionContext sessionContext = new SessionContext();

    private UserInterface() {
        retrieve();
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

                // Reset the ID generator
                Iterator<Client> clientsIterator = warehouse.getClients();
                int lastId = 0;
                while (clientsIterator.hasNext()) {
                    String clientId = clientsIterator.next().getId().replace("C", "");
                    lastId = Math.max(lastId, Integer.parseInt(clientId));
                }
                Client.resetIdCounter(lastId);
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
        Iterator<TransactionList.TransactionItem> transactions = warehouse.getClientTransactions(currentClientId);
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
        String productId = getToken("Enter product ID: ");
        int quantity = getNumber("Enter product quantity: ");
        Wishlist.WishlistItem result = warehouse.addProductToClientWishlist(currentClientId, productId, quantity);
        System.out.println("Product added to client wishlist: " + result);
    }

    public void displayWishlist() {
        Iterator<Wishlist.WishlistItem> wishlist = warehouse.getClientWishlistItems(currentClientId);
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
        Iterator<Wishlist.WishlistItem> wishlist = warehouse.getClientWishlistItems(currentClientId);
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
                warehouse.removeProductFromClientWishlist(currentClientId, productId);
                warehouse.addProductToClientWishlist(currentClientId, productId, quantity);
            } else {
                warehouse.removeProductFromClientWishlist(currentClientId, productId);
            }
        }

        wishlist = warehouse.getClientWishlistItems(currentClientId);
        if (wishlist.hasNext()) {
            System.out.println("Order placed successfully.");
            System.out.println("Order invoice: ");
        } else {
            System.out.println("No items to order.");
        }

        while (wishlist.hasNext()) {
            Wishlist.WishlistItem item = wishlist.next();
            String transactionId = warehouse.processClientOrder(currentClientId, item.getProduct().getId(),
                    item.getQuantity());
            String transaction = warehouse.getClientTransaction(currentClientId, transactionId);
            System.out.println(transaction);
        }

        warehouse.clearClientWishlist(currentClientId);
    }

    public void logout() {
        System.out.println("Logging out...");
        currentState = OPENING_STATE;
    }

    public void clientHelp() {
        System.out.println("Client Menu:");
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
                    sessionContext.intializeSessionContext(1);
                    clientMenu();
                    break;
                case CLERK_MENU_STATE:
                    sessionContext.intializeSessionContext(2);
                    clerkMenu();
                    break;
                case MANAGER_MENU_STATE:
                    sessionContext.intializeSessionContext(3);
                    managerMenu();
                    break;
            }
        }
    }

    private String currentClientId;

    private void openingState() {
        System.out.println("1. Client Menu");
        System.out.println("2. Clerk Menu");
        System.out.println("3. Manager Menu");
        System.out.println("0. Save and Exit");

        int choice = getNumber("Enter your choice: ");
        switch (choice) {
            case 1:
                String[] clientCredentials = securitySystem.promptCredentials();
                String clientId = clientCredentials[0];
                try {
                    if (warehouse.getClient(clientId) != null
                            && securitySystem.validateClientCredentials(clientId, clientCredentials)) {
                        currentClientId = clientId;
                        currentState = CLIENT_MENU_STATE;
                    } else {
                        System.out.println("Invalid client credentials.");
                    }
                } catch (NoSuchElementException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case 2:
                String[] clerkCredentials = securitySystem.promptCredentials();
                if (securitySystem.validateClerkCredentials(clerkCredentials)) {
                    currentState = CLERK_MENU_STATE;
                } else {
                    System.out.println("Invalid clerk credentials.");
                }
                break;
            case 3:
                String[] managerCredentials = securitySystem.promptCredentials();
                if (securitySystem.validateManagerCredentials(managerCredentials)) {
                    currentState = MANAGER_MENU_STATE;
                } else {
                    System.out.println("Invalid manager credentials.");
                }
                break;
            case 0:
                save();
                System.out.println("Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void clientMenu() {
        int command;
        clientHelp();
        while ((command = getCommand()) != LOGOUT) {
            switch (command) {
                case EXIT:
                    System.exit(0);
                    break;
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
                    clientHelp();
                    break;
                default:
                    System.out.println("Invalid command.");
            }
        }
        logout();
    }

    private void clerkMenu() {
        int command;
        clerkHelp();
        while ((command = getClerkCommand()) != 7) {
            switch (command) {
                case 1:
                    addClient();
                    break;
                case 2:
                    showProducts();
                    break;
                case 3:
                    showClients();
                    break;
                case 4:
                    showClientsOutstandingBalance();
                    break;
                case 5:
                    recordClientPayment();
                    break;
                case 6:
                    becomeClient();
                    break;
                case 8:
                    clerkHelp();
                    break;
                default:
                    System.out.println("Invalid command.");
            }
        }
        logout();
    }

    private int getClerkCommand() {
        do {
            try {
                int value = Integer.parseInt(getToken("Enter command (8 for help): "));
                if (value >= 1 && value <= 8) {
                    return value;
                } else {
                    System.out.println("Command out of range.");
                }
            } catch (NumberFormatException nfe) {
                System.out.println("Enter a valid number.");
            }
        } while (true);
    }

    private void clerkHelp() {
        System.out.println("Clerk Menu:");
        System.out.println("1. Add New Client");
        System.out.println("2. Show Product List");
        System.out.println("3. Show Client List");
        System.out.println("4. Show Clients with Outstanding Balance");
        System.out.println("5. Record Client Payment");
        System.out.println("6. Become a Client");
        System.out.println("7. Logout");
        System.out.println("8. Help");
    }

    public void addClient() {
        try {
            String name = getToken("Enter client name: ");
            String address = getToken("Enter address: ");
            String phone = getToken("Enter phone: ");
            Client result = warehouse.addClient(name, address, phone);
            System.out.println("Client added: " + result);
            currentClientId = result.getId(); // Update the current client ID
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred while adding the client: " + e.getMessage());
        }
    }

    private void showClients() {
        Iterator<Client> clients = warehouse.getClients();
        while (clients.hasNext()) {
            System.out.println("Client Details: " + clients.next());
        }
    }

    private void showClientsOutstandingBalance() {
        Iterator<Client> clients = warehouse.getClients();
        while (clients.hasNext()) {
            var client = clients.next();
            if (client.getBalance() < 0.0) {
                System.out.println("Client Details: " + client);
            }
        }
    }

    private void recordClientPayment() {
        String clientID = getToken("Enter Client ID: ");
        double paymentAmount = Double.parseDouble(getToken("Enter Payment Amount: "));
        warehouse.processClientPayment(clientID, paymentAmount);
    }

    private void becomeClient() {
        String clientID;
        do {
            clientID = getToken("Enter Client ID (or 'Q' to quit): ");
            if (clientID.equalsIgnoreCase("Q")) {
                return;
            }
            try {
                if (warehouse.getClient(clientID) != null) {
                    break; // Exit loop when valid client is found
                }
            } catch (NoSuchElementException e) {
                System.out.println("Client with ID " + clientID + " not found. Please try again.");
            }
        } while (true);

        currentClientId = clientID;
        sessionContext.UpdateCurrentRole(1);
        currentState = CLIENT_MENU_STATE;
        clientMenu();
    }

    private void managerMenu() {
        int command;
        managerHelp();
        while ((command = getManagerCommand()) != 5) {
            switch (command) {
                case 1:
                    addProduct();
                    break;
                case 2:
                    displayProductWaitlist();
                    break;
                case 3:
                    receiveShipment();
                    break;
                case 4:
                    becomeClerk();
                    break;
                case 5:
                    logout();
                    break;
                case 6:
                    managerHelp();
                    break;
                default:
                    System.out.println("Invalid command.");
            }
        }
        logout();
    }

    private int getManagerCommand() {
        do {
            try {
                int value = Integer.parseInt(getToken("Enter command (6 for help): "));
                if (value >= 1 && value <= 6) {
                    return value;
                } else {
                    System.out.println("Command out of range.");
                }
            } catch (NumberFormatException nfe) {
                System.out.println("Enter a valid number.");
            }
        } while (true);
    }

    private void managerHelp() {
        System.out.println("Manager Menu:");
        System.out.println("1. Add a Product");
        System.out.println("2. Display Product Waitlist");
        System.out.println("3. Receive a Shipment");
        System.out.println("4. Become a Clerk");
        System.out.println("5. Logout");
        System.out.println("6. Help");
    }

    private void addProduct() {
        String name = getToken("Enter product name: ");
        double price = Double.parseDouble(getToken("Enter product price: "));
        int quantity = getNumber("Enter product quantity: ");
        Product result = warehouse.addProduct(name, price, quantity);
        System.out.println("Product added: " + result);
    }

    private void displayProductWaitlist() {
        String productId = getToken("Enter product ID: ");
        try {
            Iterator<Waitlist.WaitlistItem> waitlist = warehouse.getProductWaitlistItems(productId);
            System.out.println("Waitlisted Clients for Product:");
            if (!waitlist.hasNext()) {
                System.out.println("No waitlist items found.");
            }
            while (waitlist.hasNext()) {
                Waitlist.WaitlistItem item = waitlist.next();
                System.out.println(item);
            }
        } catch (NoSuchElementException e) {
            System.out.println("Product with ID " + productId + " not found. Please try again.");
        }
    }

    private void receiveShipment() {
        String productId = getToken("Enter product ID: ");
        int quantityReceived = getNumber("Enter quantity received: ");
        warehouse.processProductShipment(productId, quantityReceived);
        System.out.println("Shipment processed successfully.");
    }

    private void becomeClerk() {
        currentState = CLERK_MENU_STATE;
        clerkMenu();
    }

    public static void main(String[] args) {
        UserInterface.instance().process();
    }
}