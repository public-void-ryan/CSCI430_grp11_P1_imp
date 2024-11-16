package backend;

import java.io.*;
import java.util.Iterator;
import java.util.Map;

public class Warehouse implements Serializable {
    private static final long serialVersionUID = 1L;
    private static Warehouse warehouse;
    private final ClientList clients;
    private final ProductList products;

    private static final String DATA_FILE = "WarehouseData";

    private Warehouse() {
        clients = ClientList.instance();
        products = ProductList.instance();
    }

    public static Warehouse instance() {
        if (warehouse == null) {
            warehouse = new Warehouse();
        }
        return warehouse;
    }

    public static void resetInstance() {
        warehouse = null;
    }

    public Client addClient(String name, String address, String phone) {
        Iterator<Client> clientsIterator = clients.getClients();
        while (clientsIterator.hasNext()) {
            Client existingClient = clientsIterator.next();
            if (existingClient.getName().equalsIgnoreCase(name) &&
                    existingClient.getAddress().equalsIgnoreCase(address) &&
                    existingClient.getPhone().equals(phone)) {
                throw new IllegalArgumentException("Client with the same details already exists.");
            }
        }

        Client client = new Client(name, address, phone);
        return clients.addClient(client);
    }

    public Product addProduct(String name, double price, int quantity) {
        Product product = new Product(name, price, quantity);
        return products.addProduct(product);
    }

    public Wishlist.WishlistItem addProductToClientWishlist(String clientId, String productId, int wishlistQuantity) {
        Client client = clients.findClient(clientId);
        Product product = products.findProduct(productId);
        return client.addToWishlist(product, wishlistQuantity);
    }

    public void removeProductFromClientWishlist(String clientId, String productId) {
        Client client = clients.findClient(clientId);
        Product product = products.findProduct(productId);
        client.removeFromWishlist(product);
    }

    public void clearClientWishlist(String clientId) {
        Client client = clients.findClient(clientId);
        client.clearWishlist();
    }

    public String processClientOrder(String clientId, String productId, int orderQuantity) {
        Client client = clients.findClient(clientId);
        Product product = products.findProduct(productId);
        return client.processOrder(product, orderQuantity);
    }

    public String processClientPayment(String clientId, double paymentAmount) {
        Client client = clients.findClient(clientId);
        return client.processPayment(paymentAmount);
    }

    public Iterator<Map<String, String>> processProductShipment(String productId, int shipmentQuantity) {
        Product product = products.findProduct(productId);
        return product.processShipment(shipmentQuantity);
    }

    public Client getClient(String clientId) {
        return clients.findClient(clientId);
    }

    public Iterator<Client> getClients() {
        return clients.getClients();
    }

    public Product getProduct(String productId) {
        return products.findProduct(productId);
    }

    public Iterator<Product> getProducts() {
        return products.getProducts();
    }

    public Iterator<Wishlist.WishlistItem> getClientWishlistItems(String clientId) {
        Client client = clients.findClient(clientId);
        return client.getWishlistItems();
    }

    public Iterator<TransactionList.TransactionItem> getClientTransactions(String clientId) {
        Client client = clients.findClient(clientId);
        return client.getTransactions();
    }

    public String getClientTransaction(String clientId, String transactionId) {
        Client client = clients.findClient(clientId);
        return client.getTransaction(transactionId);
    }

    public Iterator<Waitlist.WaitlistItem> getProductWaitlistItems(String productId) {
        Product product = products.findProduct(productId);
        return product.getWaitlistItems();
    }

    public static boolean save() {
        File file = getResourceFile();
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(warehouse);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Warehouse retrieve() {
        File file = getResourceFile();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            warehouse = (Warehouse) in.readObject();
            return warehouse;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static File getResourceFile() {
        String projectRoot = System.getProperty("user.dir");
        File binMainDir = new File(projectRoot, "bin/main");
        return new File(binMainDir, DATA_FILE);
    }
}
