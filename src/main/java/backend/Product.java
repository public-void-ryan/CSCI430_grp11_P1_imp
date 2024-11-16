package backend;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String PRODUCT_STRING = "P";
    private static int idCounter = 1;

    private final String id;
    private String name;
    private double price;
    private int stockLevel;
    private final Waitlist waitlist;

    public Product(String name, double price, int stockLevel) {
        this.id = PRODUCT_STRING + idCounter++;
        this.waitlist = new Waitlist();
        setName(name);
        setPrice(price);
        setStockLevel(stockLevel);
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getStockLevel() {
        return stockLevel;
    }

    // Setters
    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        this.name = name;
    }

    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        this.price = price;
    }

    public void setStockLevel(int stockLevel) {
        if (stockLevel < 0) {
            throw new IllegalArgumentException("Stock level cannot be negative.");
        }
        this.stockLevel = stockLevel;
    }

    private int getTotalOutstandingQuantity() {
        return waitlist.getTotalQuantity();
    }

    @Override
    public String toString() {
        return String.format(
                "Product [ID=%s, Name=%s, Price=%.2f, StockLevel=%d, OutstandingWaitlistQuantity=%d]",
                id, name, price, stockLevel, getTotalOutstandingQuantity());
    }

    public Iterator<Waitlist.WaitlistItem> getWaitlistItems() {
        return waitlist.getWaitlistItems();
    }

    public Waitlist.WaitlistItem addToWaitlist(Client client, int quantity) {
        return waitlist.addClient(client, quantity);
    }

    public void removeFromWaitlist(Client client) {
        waitlist.removeClient(client);
    }

    public void updateWaitlistQuantity(Client client, int quantity) {
        waitlist.updateClientQuantity(client, quantity);
    }

    public void clearWaitlist() {
        waitlist.clear();
    }

    public Iterator<Map<String, String>> processShipment(int shipmentQuantity) {
        // Update the stock quantity before processing the waitlist items
        setStockLevel(getStockLevel() + shipmentQuantity);

        LinkedList<Map<String, String>> transactionInfoList = new LinkedList<>();
        Iterator<Waitlist.WaitlistItem> waitlistItems = getWaitlistItems();

        while (waitlistItems.hasNext() && getStockLevel() > 0) {
            Waitlist.WaitlistItem waitlistItem = waitlistItems.next();
            Client waitlistClient = waitlistItem.getClient();
            int waitlistQuantity = waitlistItem.getQuantity();

            String transactionId;

            if (waitlistQuantity < getStockLevel()) {
                // Remove client from the waitlist
                removeFromWaitlist(waitlistClient);

                // Process the client's order with the waitlisted quantity
                transactionId = waitlistClient.processOrder(this, waitlistQuantity);
            } else {
                // Update the waitlist quantity
                int newQuantity = waitlistQuantity - getStockLevel();
                updateWaitlistQuantity(waitlistClient, newQuantity);

                // Process the client's order with the partial quantity
                transactionId = waitlistClient.processOrder(this, newQuantity);
            }

            // Record transaction information
            Map<String, String> transactionInfo = new HashMap<>();
            transactionInfo.put("clientId", waitlistClient.getId());
            transactionInfo.put("transactionId", transactionId);

            transactionInfoList.add(transactionInfo);
        }

        return transactionInfoList.iterator();
    }
}
