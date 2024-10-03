import java.util.*;

public class WarehouseTest {
  
  public static void main(String[] args) {
     // Create clients
     Client client1 = new Client("Alice");
     Client client2 = new Client("Bob");

     // Create products
     Product product1 = new Product("P001", 1200.0, 10);
     Product product2 = new Product("P002", 800.0, 5);

     // Create warehouse instance
     Warehouse warehouse = new Warehouse();

     // Add clients to warehouse
     warehouse.addClient(client1);
     warehouse.addClient(client2);

     // Add products to warehouse
     warehouse.addProduct(product1);
     warehouse.addProduct(product2);

     // Display initial state
     System.out.println("Initial state:");
     displayCurrentData(warehouse);

     // Issue product to client1
     System.out.println("\nIssuing product P001 to Alice:");
     warehouse.processOrder(client1, "P001", 2);
     displayCurrentData(warehouse);

     // Try issuing the same product to client2 (insufficient stock)
     System.out.println("\nTrying to issue product P001 to Bob (insufficient stock):");
     warehouse.processOrder(client2, "P001", 9);
     displayCurrentData(warehouse);

     // Add product to wishlist
     System.out.println("\nAdding product P002 to Alice's wishlist:");
     warehouse.addToWishlist(client1, product2);
     displayCurrentData(warehouse);

     // Return product
     System.out.println("\nReturning product P001 from Alice:");
     // Assuming there is a method to return a product
     // warehouse.returnProduct(client1, "P001", 2);
     displayCurrentData(warehouse);
  }

  private static void displayCurrentData(Warehouse warehouse) {
     System.out.println("Clients:");
     Iterator<Client> clients = warehouse.getAllClients();
     while (clients.hasNext()) {
         Client client = clients.next();
         System.out.println(client.toString());
     }

     System.out.println("\nProducts:");
     List<Product> products = warehouse.getAllProducts();
     for (Product product : products) {
         System.out.println(product.toString());
     }

     System.out.println("\nTransactions:");
     for (Transaction transaction : warehouse.getTransactions()) {
         System.out.println(transaction.toString());
     }
    }
}