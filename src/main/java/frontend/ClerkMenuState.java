package frontend;

import backend.*;

import java.util.Iterator;

public class ClerkMenuState extends WarehouseState {
    private static final int LOGOUT = 0;
    private static final int ADD_CLIENT = 1;
    private static final int SHOW_PRODUCTS = 2;
    private static final int SHOW_CLIENTS = 3;
    private static final int SHOW_CLIENTS_WITH_BALANCE = 4;
    private static final int RECORD_PAYMENT = 5;
    private static final int BECOME_CLIENT = 6;
    private static final int HELP = 7;

    public ClerkMenuState(WarehouseContext warehouseContext, Warehouse warehouse) {
        super(warehouseContext, warehouse);
    }

    private int getCommand() {
        return InputUtils.getCommand(LOGOUT, HELP, "Enter command (" + HELP + " for help): ");
    }

    private void help() {
        System.out.println("Clerk Menu:");
        System.out.println(LOGOUT + " to Logout");
        System.out.println(ADD_CLIENT + " to Add a client");
        System.out.println(SHOW_PRODUCTS + " to Show list of products");
        System.out.println(SHOW_CLIENTS + " to Show list of clients");
        System.out.println(SHOW_CLIENTS_WITH_BALANCE + " to Show clients with outstanding balance");
        System.out.println(RECORD_PAYMENT + " to Record payment from a client");
        System.out.println(BECOME_CLIENT + " to Become a client");
        System.out.println(HELP + " for help");
    }

    @Override
    public void run() {
        int command;
        help();
        while ((command = getCommand()) != LOGOUT) {
            try {
                switch (command) {
                    case ADD_CLIENT:
                        addClient();
                        break;
                    case SHOW_PRODUCTS:
                        showProducts();
                        break;
                    case SHOW_CLIENTS:
                        showClients();
                        break;
                    case SHOW_CLIENTS_WITH_BALANCE:
                        showClientsWithOutstandingBalance();
                        break;
                    case RECORD_PAYMENT:
                        recordPayment();
                        break;
                    case BECOME_CLIENT:
                        becomeClient();
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

    private void addClient() {
        String name = InputUtils.getToken("Enter client name: ");
        String address = InputUtils.getToken("Enter address: ");
        String phone = InputUtils.getToken("Enter phone: ");
        Client result = warehouse.addClient(name, address, phone);
        System.out.println("Client added: " + result);
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

    private void showClients() {
        Iterator<Client> allClients = warehouse.getClients();
        System.out.println("Warehouse Clients:");

        if (!allClients.hasNext()) {
            System.out.println("No warehouse clients found.");
        }

        while (allClients.hasNext()) {
            Client client = allClients.next();
            System.out.println(client);
        }
    }

    private void showClientsWithOutstandingBalance() {
        Iterator<Client> allClients = warehouse.getClients();
        System.out.println("Clients with Outstanding Balance:");

        boolean found = false;
        while (allClients.hasNext()) {
            Client client = allClients.next();
            if (client.getBalance() > 0) {
                System.out.println(client);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No clients with outstanding balance.");
        }
    }

    private void recordPayment() {
        String clientId = InputUtils.getToken("Enter client ID: ");
        double amount = Double.parseDouble(InputUtils.getToken("Enter payment amount: "));
        String transactionId = warehouse.processClientPayment(clientId, amount);
        String transaction = warehouse.getClientTransaction(clientId, transactionId);
        System.out.println("Payment processed successfully.");
        System.out.println("Payment invoice: ");
        System.out.println(transaction);
    }

    private void becomeClient() {
        String clientID = InputUtils.getToken("Enter Client ID: ");
        warehouse.getClient(clientID);
        warehouseContext.setCurrentClientID(clientID);
        warehouseContext.changeState(WarehouseContext.BECOME_CLIENT);
    }

    private void logout() {
        warehouseContext.changeState(WarehouseContext.LOGOUT);
    }
}
