public class Main {
    public static void main(String[] args) {
        // Retrieve the singleton instance of Warehouse (loads data if available)
        Warehouse warehouse = Warehouse.instance();

        // Adding clients
        Client client1 = new Client("Alice", "Address 1", "111-111-1111");
        Client client2 = new Client("Bob", "Address 2", "222-222-2222");
        warehouse.addClient(client1);
        warehouse.addClient(client2);

        // Adding products
        Product product1 = new Product("Laptop", 1200.0, 10);
        Product product2 = new Product("Smartphone", 800.0, 5);
        warehouse.addProduct(product1);
        warehouse.addProduct(product2);

        // Display initial state
        System.out.println("Initial state:");
        displayCurrentData(warehouse);

        // Processing an order
        System.out.println("\nProcessing an order for Alice:");
        warehouse.processOrder(client1, product1.id(), 2); // Succeeds
        displayCurrentData(warehouse);

        System.out.println("\nProcessing an order for Bob:");
        warehouse.processOrder(client2, product1.id(), 9); // Fails, insufficient stock
        displayCurrentData(warehouse);

        // Save warehouse data at the end
        Warehouse.save();
    }

    private static void displayCurrentData(Warehouse warehouse) {
        System.out.println("Clients:");
        for (Client client : warehouse.getAllClients()) {
            System.out.println(" - " + client);
        }

        System.out.println("\nProducts:");
        for (Product product : warehouse.getAllProducts()) {
            System.out.println(" - " + product);
        }

        System.out.println("\nTransactions:");
        for (Transaction transaction : warehouse.getTransactions()) {
            System.out.println(" - " + transaction);
        }
    }
}