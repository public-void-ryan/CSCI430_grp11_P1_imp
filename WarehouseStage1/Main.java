public class Main {
    public static void main(String[] args) {
        // Retrieve the singleton instance of Warehouse (loads data if available)
        Warehouse warehouse = Warehouse.instance();

        // Adding clients
        Client client1 = new Client("Alice");
        Client client2 = new Client("Bob");
        warehouse.addClient(client1);
        warehouse.addClient(client2);

        // Adding products
        Product product1 = new Product("P001", "Laptop", 1200.0, 10);
        Product product2 = new Product("P002", "Smartphone", 800.0, 5);
        warehouse.addProduct(product1);
        warehouse.addProduct(product2);

        // Processing an order
        warehouse.processOrder(client1, "P001", 2); // Succeeds
        warehouse.processOrder(client2, "P001", 9); // Fails, insufficient stock

        // Save warehouse data at the end
        Warehouse.save();
    }
}
