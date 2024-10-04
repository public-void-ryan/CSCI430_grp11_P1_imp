import java.io.Serializable;

public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String id;
    private String name;
    private double price;
    private int stockLevel;
    private static final String PRODUCT_STRING = "P";
    private static int idCounter = 1;

    public Product(String name, double price, int stockLevel) {
        this.id = PRODUCT_STRING + idCounter++;
        this.name = name;
        this.price = price;
        this.stockLevel = stockLevel;
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
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStockLevel(int stockLevel) {
        this.stockLevel = stockLevel;
    }

    @Override
    public String toString() {
        return "Product [Name=" + name + ", Price=" + price + ", StockLevel=" + stockLevel + ", ID=" + id + "]";
    }
}
