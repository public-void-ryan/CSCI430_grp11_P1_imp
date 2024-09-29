import java.util.Iterator;
import java.util.List;

public class WarehouseTest {
    private static Warehouse warehouse;

    public static void main(String[] args) {
        setUp();
        testAddClient();
        testAddProduct();
        testFindProduct();
        testAddToWaitlist();
        testProcessOrder();
        testGetTransactionStatus();
        testGetAllProducts();
        testGetAllClients();
    }

    public static void setUp() {
        warehouse = Warehouse.instance(); // Use the singleton instance
    }

    public static void testAddClient() {
        Client client = new Client("John Doe");
        warehouse.addClient(client);
        Iterator<Client> clients = warehouse.getAllClients();
        boolean found = false;
        while (clients.hasNext()) {
            if (clients.next().equals(client)) {
                found = true;
                break;
            }
        }
        if (found) {
            System.out.println("testAddClient passed");
        } else {
            System.out.println("testAddClient failed");
        }
    }

    public static void testAddProduct() {
        Product product = new Product("1", "Laptop", 999.99, 10);
        warehouse.addProduct(product);
        Product foundProduct = warehouse.findProduct("1");
        if (foundProduct != null && foundProduct.equals(product)) {
            System.out.println("testAddProduct passed");
        } else {
            System.out.println("testAddProduct failed");
        }
    }

    public static void testFindProduct() {
        Product product = new Product("2", "Smartphone", 499.99, 5);
        warehouse.addProduct(product);
        Product foundProduct = warehouse.findProduct("2");
        if (foundProduct != null && foundProduct.equals(product)) {
            System.out.println("testFindProduct passed");
        } else {
            System.out.println("testFindProduct failed");
        }
    }

    public static void testAddToWaitlist() {
        Client client = new Client("Jane Doe");
        Product product = new Product("4", "Monitor", 199.99, 0);
        warehouse.addClient(client);
        warehouse.addProduct(product);
        warehouse.addToWaitlist(client, product);
        if (product.getWaitlist().getClients().contains(client)) { // Use getClients() method
            System.out.println("testAddToWaitlist passed");
        } else {
            System.out.println("testAddToWaitlist failed");
        }
    }

    public static void testProcessOrder() {
        Client client = new Client("John Smith");
        Product product = new Product("5", "Keyboard", 49.99, 20);
        warehouse.addClient(client);
        warehouse.addProduct(product);
        warehouse.processOrder(client, "5", 5);
        if (product.stockLevel() == 15) {
            System.out.println("testProcessOrder passed");
        } else {
            System.out.println("testProcessOrder failed");
        }
    }

    public static void testGetTransactionStatus() {
        Client client = new Client("John Smith");
        Product product = new Product("6", "Mouse", 29.99, 10);
        warehouse.addClient(client);
        warehouse.addProduct(product);

        // Process the order
        warehouse.processOrder(client, "6", 2);

        // Retrieve the transaction by ID (assuming IDs start from 1)
        Transaction transaction = warehouse.getTransactionStatus(1);

        if (transaction != null &&
                transaction.getClient().equals(client) &&
                transaction.getProduct().equals(product) &&
                transaction.getQuantity() == 2) {
            System.out.println("testGetTransactionStatus passed");
        } else {
            System.out.println("testGetTransactionStatus failed");
        }
    }

    public static void testGetAllProducts() {
        Product product1 = new Product("7", "Printer", 149.99, 5);
        Product product2 = new Product("8", "Scanner", 89.99, 3);
        warehouse.addProduct(product1);
        warehouse.addProduct(product2);
        List<Product> products = warehouse.getAllProducts();
        if (products.contains(product1) && products.contains(product2)) {
            System.out.println("testGetAllProducts passed");
        } else {
            System.out.println("testGetAllProducts failed");
        }
    }

    public static void testGetAllClients() {
        Client client1 = new Client("Alice");
        Client client2 = new Client("Bob");
        warehouse.addClient(client1);
        warehouse.addClient(client2);
        Iterator<Client> clients = warehouse.getClients();
        boolean foundClient1 = false;
        boolean foundClient2 = false;
        while (clients.hasNext()) {
            Client client = clients.next();
            if (client.equals(client1)) {
                foundClient1 = true;
            }
            if (client.equals(client2)) {
                foundClient2 = true;
            }
        }
        if (foundClient1 && foundClient2) {
            System.out.println("testGetAllClients passed");
        } else {
            System.out.println("testGetAllClients failed");
        }
    }
}
