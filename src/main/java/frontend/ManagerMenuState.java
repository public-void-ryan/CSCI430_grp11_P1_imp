package frontend;

import backend.*;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class ManagerMenuState extends WarehouseState {
    private static final int LOGOUT = 0;
    private static final int ADD_PRODUCT = 1;
    private static final int DISPLAY_WAITLIST = 2;
    private static final int RECEIVE_SHIPMENT = 3;
    private static final int BECOME_CLERK = 4;
    private static final int HELP = 5;

    public ManagerMenuState(WarehouseContext warehouseContext, Warehouse warehouse) {
        super(warehouseContext, warehouse);
    }

    private int getCommand() {
        return InputUtils.getCommand(LOGOUT, HELP, "Enter command (" + HELP + " for help): ");
    }

    private void help() {
        System.out.println("Manager Menu:");
        System.out.println(LOGOUT + " to Logout");
        System.out.println(ADD_PRODUCT + " to Add a product");
        System.out.println(DISPLAY_WAITLIST + " to Display waitlist for a product");
        System.out.println(RECEIVE_SHIPMENT + " to Receive a shipment");
        System.out.println(BECOME_CLERK + " to Become a clerk");
        System.out.println(HELP + " for help");
    }

    @Override
    public void run() {
        int command;
        help();
        while ((command = getCommand()) != LOGOUT) {
            try {
                switch (command) {
                    case ADD_PRODUCT:
                        addProduct();
                        break;
                    case DISPLAY_WAITLIST:
                        displayWaitlist();
                        break;
                    case RECEIVE_SHIPMENT:
                        receiveShipment();
                        break;
                    case BECOME_CLERK:
                        becomeClerk();
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

    private void addProduct() {
        do {
            String name = InputUtils.getToken("Enter product name: ");
            double price = Double.parseDouble(InputUtils.getToken("Enter product price: "));
            int quantity = InputUtils.getNumber("Enter product quantity: ");
            Product result = warehouse.addProduct(name, price, quantity);
            System.out.println("Product added: " + result);
        } while (InputUtils.yesOrNo("Would you like to add another product?"));
    }

    private void displayWaitlist() {
        String productId = InputUtils.getToken("Enter product ID: ");
        Iterator<Waitlist.WaitlistItem> waitlist = warehouse.getProductWaitlistItems(productId);
        System.out.println("Waitlisted Clients for Product:");

        if (!waitlist.hasNext()) {
            System.out.println("No waitlist items found.");
        }

        while (waitlist.hasNext()) {
            Waitlist.WaitlistItem item = waitlist.next();
            System.out.println(item);
        }
    }

    private void receiveShipment() {
        String productId = InputUtils.getToken("Enter product ID: ");
        int quantityReceived = InputUtils.getNumber("Enter quantity received: ");
        Iterator<Map<String, String>> shipmentTransactionInfo = warehouse.processProductShipment(productId, quantityReceived);
        System.out.println("Shipment processed successfully.");

        if (shipmentTransactionInfo.hasNext()) {
            System.out.println("Shipment invoice: ");
        }

        while (shipmentTransactionInfo.hasNext()) {
            Map<String, String> transactionInfo = shipmentTransactionInfo.next();
            String clientId = transactionInfo.get("clientId");
            String transactionId = transactionInfo.get("transactionId");
            Client client = warehouse.getClient(clientId);
            String transaction = warehouse.getClientTransaction(clientId, transactionId);

            System.out.println("Client Details:");
            System.out.println(client);
            System.out.println("Transaction:");
            System.out.println(transaction);
            System.out.println();
        }
    }

    private void becomeClerk() {
        String managerPassword = InputUtils.getToken("Enter Manager Password: ");

        if (Objects.equals(managerPassword, "manager")) {
            warehouseContext.changeState(WarehouseContext.BECOME_CLERK);
        } else {
            System.out.println("Invalid Manager Password");
        }
    }

    private void logout() {
        warehouseContext.changeState(WarehouseContext.LOGOUT);
    }
}
