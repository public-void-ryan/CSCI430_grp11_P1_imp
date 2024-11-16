package backend;

import java.io.Serializable;
import java.util.Iterator;

public class Client implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String CLIENT_STRING = "C";
    private static ClientIdGenerator idGenerator = ClientIdGenerator.instance();

    private final String id;
    private String name;
    private String address;
    private String phone;
    private double balance;
    private final Wishlist wishlist;
    private final TransactionList transactions;

    public Client(String name, String address, String phone) {
        this.id = CLIENT_STRING + idGenerator.getNextId();
        this.wishlist = new Wishlist();
        this.transactions = new TransactionList();
        setName(name);
        setAddress(address);
        setPhone(phone);
        setBalance(0);
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public double getBalance() {
        return balance;
    }

    public String getId() {
        return id;
    }

    // Setters
    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        this.name = name;
    }

    public void setAddress(String address) {
        if (address == null || address.isEmpty()) {
            throw new IllegalArgumentException("Address cannot be null or empty.");
        }
        this.address = address;
    }

    public void setPhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            throw new IllegalArgumentException("Phone cannot be null or empty.");
        }
        this.phone = phone;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return String.format("Client [ID=%s, Name=%s, Address=%s, Phone=%s, Balance=%.2f]", id, name, address, phone,
                balance);
    }

    public Iterator<Wishlist.WishlistItem> getWishlistItems() {
        return wishlist.getWishlistItems();
    }

    public Wishlist.WishlistItem addToWishlist(Product product, int quantity) {
        return wishlist.addProduct(product, quantity);
    }

    public void removeFromWishlist(Product product) {
        wishlist.removeProduct(product);
    }

    public void clearWishlist() {
        wishlist.clear();
    }

    public String addTransaction(String description, double dollarAmount) {
        return transactions.addTransaction(description, dollarAmount);
    }

    public Iterator<TransactionList.TransactionItem> getTransactions() {
        return transactions.getTransactions();
    }

    public String getTransaction(String transactionId) {
        return transactions.getTransaction(transactionId);
    }

    public String processPayment(double paymentAmount) {
        double newBalance = getBalance() - paymentAmount;
        setBalance(newBalance);

        String description = "Payment received";
        return addTransaction(description, paymentAmount);
    }

    public String processOrder(Product product, int orderQuantity) {
        String productName = product.getName();
        double productPrice = product.getPrice();
        int stockLevel = product.getStockLevel();

        if (stockLevel >= orderQuantity) {
            // Enough stock to fulfill the entire order
            product.setStockLevel(stockLevel - orderQuantity);
            double totalCost = productPrice * orderQuantity;
            setBalance(getBalance() + totalCost);

            String description = String.format(
                    "Order placed: (%d x %s) at $%.2f each",
                    orderQuantity, productName, productPrice);
            return addTransaction(description, totalCost);

        } else if (stockLevel > 0) {
            // Partial fulfillment
            int waitlistedQuantity = orderQuantity - stockLevel;
            product.setStockLevel(0);
            double fulfilledCost = productPrice * stockLevel;
            setBalance(getBalance() + fulfilledCost);

            // Waitlist the remaining quantity
            product.addToWaitlist(this, waitlistedQuantity);

            String description = String.format(
                    "Partial order: (%d x %s) at $%.2f each with %d units waitlisted",
                    stockLevel, productName, productPrice, waitlistedQuantity);
            return addTransaction(description, fulfilledCost);

        } else {
            // No stock available, entire quantity goes to waitlist
            product.addToWaitlist(this, orderQuantity);

            String description = String.format(
                    "Waitlisted: (%d x %s)", orderQuantity, productName);
            return addTransaction(description, 0.0);
        }
    }

    public static void resetIdCounter(int lastId) {
        ClientIdGenerator.instance().resetIdCounter(lastId);
    }

    public static class ClientIdGenerator {
        public static ClientIdGenerator instance;
        public int idCounter;

        public ClientIdGenerator() {
            idCounter = 1;
        }

        public static ClientIdGenerator instance() {
            if (instance == null) {
                instance = new ClientIdGenerator();
            }
            return instance;
        }

        public synchronized int getNextId() {
            return idCounter++;
        }

        public synchronized void resetIdCounter(int lastId) {
            if (lastId >= idCounter) {
                idCounter = lastId + 1;
            }
        }
    }

}
