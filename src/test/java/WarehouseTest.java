import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Iterator;
import static org.junit.jupiter.api.Assertions.*;

public class WarehouseTest {
    private Warehouse warehouse;

    @BeforeEach
    void setUp() {
        warehouse = Warehouse.instance();
        Warehouse.resetInstance();
    }

    @Test
    void testFullScenario() {
        // Create clients
        Client c1 = warehouse.addClient("C1", "Address1", "Phone1");
        Client c2 = warehouse.addClient("C2", "Address2", "Phone2");
        Client c3 = warehouse.addClient("C3", "Address3", "Phone3");
        Client c4 = warehouse.addClient("C4", "Address4", "Phone4");
        Client c5 = warehouse.addClient("C5", "Address5", "Phone5");

        // Create products
        Product p1 = warehouse.addProduct("P1", 1.0, 10);
        Product p2 = warehouse.addProduct("P2", 2.0, 20);
        Product p3 = warehouse.addProduct("P3", 3.0, 30);
        Product p4 = warehouse.addProduct("P4", 4.0, 40);
        Product p5 = warehouse.addProduct("P5", 5.0, 50);

        // Display clients
        displayClients();

        // Display products
        displayProducts();

        // Add to wishlists
        warehouse.addProductToClientWishlist(c1.getId(), p1.getId(), 5);
        warehouse.addProductToClientWishlist(c1.getId(), p3.getId(), 5);
        warehouse.addProductToClientWishlist(c1.getId(), p5.getId(), 5);
        displayWishlist(c1);

        warehouse.addProductToClientWishlist(c2.getId(), p1.getId(), 7);
        warehouse.addProductToClientWishlist(c2.getId(), p2.getId(), 7);
        warehouse.addProductToClientWishlist(c2.getId(), p4.getId(), 7);
        displayWishlist(c2);

        warehouse.addProductToClientWishlist(c3.getId(), p1.getId(), 6);
        warehouse.addProductToClientWishlist(c3.getId(), p2.getId(), 6);
        warehouse.addProductToClientWishlist(c3.getId(), p5.getId(), 6);
        displayWishlist(c3);

        // Place orders
        warehouse.processClientOrder(c2.getId(), p1.getId(), 7);
        warehouse.processClientOrder(c2.getId(), p2.getId(), 7);
        warehouse.processClientOrder(c2.getId(), p4.getId(), 7);
        displayClients();

        warehouse.processClientOrder(c3.getId(), p1.getId(), 6);
        warehouse.processClientOrder(c3.getId(), p2.getId(), 6);
        warehouse.processClientOrder(c3.getId(), p5.getId(), 6);
        displayClients();

        displayWishlist(c2);
        displayWishlist(c3);

        // Waitlists (assuming waitlist functionality is implemented)
        displayWaitlist(p1);
        displayWaitlist(p2);

        // Place order for C1
        warehouse.processClientOrder(c1.getId(), p1.getId(), 5);
        warehouse.processClientOrder(c1.getId(), p3.getId(), 5);
        warehouse.processClientOrder(c1.getId(), p5.getId(), 5);
        displayClients();
        displayWishlist(c1);

        // Record payments
        warehouse.processClientPayment(c1.getId(), 100.0);
        warehouse.processClientPayment(c2.getId(), 100.0);
        displayClients();

        // Receive shipment
        warehouse.processProductShipment(p1.getId(), 100);
        displayProducts();
        displayClients();

        // Print invoices
        displayInvoices(c1);
        displayInvoices(c2);
    }

    private void displayClients() {
        Iterator<Client> clients = warehouse.getClients();
        while (clients.hasNext()) {
            Client client = clients.next();
            System.out.println(client);
        }
    }

    private void displayProducts() {
        Iterator<Product> products = warehouse.getProducts();
        while (products.hasNext()) {
            Product product = products.next();
            System.out.println(product);
        }
    }

    private void displayWishlist(Client client) {
        Iterator<Wishlist.WishlistItem> wishlistItems = warehouse.getClientWishlistItems(client.getId());
        System.out.println("Wishlist for " + client.getName() + ":");
        while (wishlistItems.hasNext()) {
            Wishlist.WishlistItem item = wishlistItems.next();
            System.out.println(item);
        }
    }

    private void displayWaitlist(Product product) {
        Iterator<Waitlist.WaitlistItem> waitlistItems = warehouse.getProductWaitlistItems(product.getId());
        System.out.println("Waitlist for " + product.getName() + ":");
        while (waitlistItems.hasNext()) {
            Waitlist.WaitlistItem item = waitlistItems.next();
            System.out.println(item);
        }
    }

    private void displayInvoices(Client client) {
        Iterator<Transaction> transactions = warehouse.getClientTransactions(client.getId());
        System.out.println("Invoices for " + client.getName() + ":");
        while (transactions.hasNext()) {
            Transaction transaction = transactions.next();
            System.out.println(transaction);
        }
    }
}