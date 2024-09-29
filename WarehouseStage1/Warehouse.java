import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Warehouse implements Serializable {
    private static final long serialVersionUID = 1L;
    private static Warehouse warehouse;
    private ClientList clients;
    private ProductList products;
    private List<Transaction> transactions;

    private static final String DATA_FILE = "WarehouseData"; // Save file

    private Warehouse() {
        clients = new ClientList();
        products = new ProductList();
        transactions = new ArrayList<>();
    }

    public static Warehouse instance() {
        if (warehouse == null) {
            warehouse = retrieve();
            if (warehouse == null) {
                warehouse = new Warehouse();
            }
        }
        return warehouse;
    }

    public void addClient(Client client) {
        clients.addClient(client);
        save();
    }

    public void addProduct(Product product) {
        products.addProduct(product);
        save();
    }

    public void processOrder(Client client, String productId, int quantity) {
        Product product = findProduct(productId);
        if (product != null) {
            if (product.stockLevel() >= quantity) {
                Transaction transaction = new Transaction(client, product, quantity);
                transactions.add(transaction);
                product.setStockLevel(product.stockLevel() - quantity);
                save();
            } else {
                addToWaitlist(client, product);
            }
        } else {
            System.out.println("Product not found: " + productId);
        }
    }

    public Product findProduct(String productId) {
        return products.findProduct(productId);
    }

    public void addToWaitlist(Client client, Product product) {
        product.waitlist().addClient(client);
        save();
    }

    public static boolean save() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            out.writeObject(warehouse);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Warehouse retrieve() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            return (Warehouse) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }
}
