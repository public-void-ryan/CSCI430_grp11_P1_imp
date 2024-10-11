import java.io.Serializable;

public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String id;
    private String name;
    private double price;
    private int stockLevel;
    private static final String PRODUCT_STRING = "P";
    private static int idCounter = 1;
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

    public Waitlist getWaitlist() {
        return waitlist ;
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

    @Override
    public String toString() {
        return String.format("Product [Name=%s, Price=%.2f, StockLevel=%d, ID=%s]", name, price, stockLevel, id);
    }

    public Waitlist.WaitlistItem addToWaitlist(Client client, int quantity) {
        return waitlist.addClient(client, quantity);
    }

    public boolean removeFromWaitlist(Client client) {
        return waitlist.removeClient(client);
    }

    public void clearWaitlist() {
        waitlist.clear();
    }
}
