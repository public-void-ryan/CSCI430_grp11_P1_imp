import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Warehouse implements Serializable {
    private static final long serialVersionUID = 1L;
    private static Warehouse warehouse;
    private ClientList clients;
    private ProductList products;
    private List<Transaction> transactions;

    private static final String DATA_FILE = "WarehouseData.ser"; // File to save data

    // Private constructor for singleton pattern
    Warehouse() {
        clients = new ClientList();
        products = new ProductList();
        transactions = new ArrayList<>();
    }

    // Singleton instance
    public static Warehouse instance() {
        if (warehouse == null) {
            warehouse = retrieve(); // Try to load existing data
            if (warehouse == null) {
                warehouse = new Warehouse(); // Create new if no data found
            }
        }
        return warehouse;
    }

    // Add new client to the system
    public void addClient(Client client) {
        clients.addClient(client);
        save(); // Save data after adding a client
    }

    // Add product to the system using ProductList
    public void addProduct(Product product) {
        products.addProduct(product);
        save(); // Save data after adding a product
    }

    // Find product by ID using ProductList
    public Product findProduct(String productId) {
        return products.findProduct(productId);
    }

    // Add a product to a client's wishlist
    public void addToWishlist(Client client, Product product) {
        client.getWishlist().addProduct(product); // Add product to the client's wishlist
        save(); // Save data after modifying wishlist
    }

    // Add a client to a product's waitlist
    public void addToWaitlist(Client client, Product product) {
        product.getWaitlist().addClient(client); // Add client to the product's waitlist
        save(); // Save data after adding to waitlist
    }

    public void processOrder(Client client, String productId, int quantity) {
        Product product = findProduct(productId); // Find product using ProductList
        if (product != null) {
            if (product.stockLevel() >= quantity) {
                Transaction transaction = new Transaction(client, product, quantity);
                transactions.add(transaction); // Add transaction to the list
                product.setStockLevel(product.stockLevel() - quantity); // Decrease stock level
                save(); // Save data after processing the order
            } else {
                addToWaitlist(client, product); // Add to waitlist if insufficient stock
            }
        } else {
            System.out.println("Product not found: " + productId);
        }
    }

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

    // Save warehouse data to file
    public static boolean save() {
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            output.writeObject(warehouse);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Load warehouse data from file
    public static Warehouse retrieve() {
        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            return (Warehouse) input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null; // Return null if there's no saved data or an error occurred
        }
    }
}
