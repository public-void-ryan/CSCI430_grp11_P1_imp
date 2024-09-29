import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Warehouse {
    private ClientList clients;
    private ProductList products;
    private List<Transaction> transactions;

    public Warehouse() {
        clients = new ClientList();
        products = new ProductList();
        transactions = new ArrayList<>();
    }

    // Add new client to the system
    public void addClient(Client client) {
        clients.addClient(client);
    }

    // Add product to the system using ProductList
    public void addProduct(Product product) {
        products.addProduct(product);
    }

    // Find product by ID using ProductList
    public Product findProduct(String productId) {
        return products.findProduct(productId);
    }

    // Add a product to a client's wishlist
    public void addToWishlist(Client client, Product product) {
        product.addToWishlist(client); // Use Product's method to add client to its wishlist
    }

    // Add a product to a client's waitlist
    public void addToWaitlist(Client client, Product product) {
        product.addToWaitlist(client); // Use Product's method to add client to its waitlist
    }

    // Process an order for a client
    public void processOrder(Client client, String productId, int quantity) {
        Product product = findProduct(productId); // Find product using ProductList
        if (product != null) {
            if (product.stockLevel() >= quantity) {
                Transaction transaction = new Transaction(client, product, quantity);
                transactions.add(transaction); // Add transaction to the list
                product.setStockLevel(product.stockLevel() - quantity); // Decrease stock level
            } else {
                addToWaitlist(client, product); // Add to waitlist if insufficient stock
            }
        } else {
            System.out.println("Product not found: " + productId);
        }
    }

    // status of a transaction
    public Transaction getTransactionStatus(int transactionId) {
        for (Transaction transaction : transactions) {
            if (transaction.getId() == transactionId) {
                return transaction;
            }
        }
        return null;
    }

    // retrieve all products
    public List<Product> getAllProducts() {
        return products.getProducts();
    }

    // retrieve all clients
    public Iterator<Client> getAllClients() {
        return clients.getClients();
    }
}
