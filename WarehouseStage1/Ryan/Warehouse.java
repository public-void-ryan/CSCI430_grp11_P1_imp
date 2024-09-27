import java.util.*;
import java.io.*;

public class Warehouse implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final int PRODUCT_NOT_FOUND = 1;
    public static final int PRODUCT_NOT_ISSUED = 2;
    public static final int PRODUCT_HAS_HOLD = 3;
    public static final int PRODUCT_ISSUED = 4;
    public static final int HOLD_PLACED = 5;
    public static final int NO_HOLD_FOUND = 6;
    public static final int OPERATION_COMPLETED = 7;
    public static final int OPERATION_FAILED = 8;
    public static final int NO_SUCH_CLIENT = 9;

    private ProductList productList;
    private ClientList clientList;
    private static Warehouse warehouse;

    private Warehouse() {
        productList = ProductList.instance();
        clientList = ClientList.instance();
    }

    public static Warehouse instance() {
        if (warehouse == null) {
            ClientIdServer.instance(); // instantiate all singletons
            return (warehouse = new Warehouse());
        } else {
            return warehouse;
        }
    }

    public void addProduct(String name, String id, int stock) {
        Product product = new Product(id, name, 0.0, stock);
        productList.addProduct(product);
    }

    public Client addClient(String name, String address, String phone) {
        Client client = new Client(name, address, phone);
        if (clientList.insertClient(client)) {
            return client;
        }
        return null;
    }

    public Iterator<Product> getProducts() {
        return productList.getProducts().iterator();
    }

    public Iterator<Client> getClients() {
        return clientList.getClients();
    }

    public static Warehouse retrieve() {
        try {
            FileInputStream file = new FileInputStream("WarehouseData");
            ObjectInputStream input = new ObjectInputStream(file);
            input.readObject();
            ClientIdServer.retrieve(input);
            return warehouse;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            return null;
        }
    }

    public static boolean save() {
        try {
            FileOutputStream file = new FileOutputStream("WarehouseData");
            ObjectOutputStream output = new ObjectOutputStream(file);
            output.writeObject(warehouse);
            output.writeObject(ClientIdServer.instance());
            return true;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
    }

    private void writeObject(ObjectOutputStream output) {
        try {
            output.defaultWriteObject();
            output.writeObject(warehouse);
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }

    private void readObject(ObjectInputStream input) {
        try {
            input.defaultReadObject();
            if (warehouse == null) {
                warehouse = (Warehouse) input.readObject();
            } else {
                input.readObject();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String toString() {
        return productList + "\n" + clientList;
    }
}