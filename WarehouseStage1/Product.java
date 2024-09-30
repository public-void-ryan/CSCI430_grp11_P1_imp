import java.io.Serializable;

public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    private String _id;
    private String _name;
    private double _price;
    private int _stockLevel;
    private Wishlist _wishlist;
    private Waitlist _waitlist;
    private static final String PRODUCT_STRING = "P";
    private static int idCounter = 1;

    public Product(String name, double price, int stockLevel) {
        _id = PRODUCT_STRING + idCounter++;
        _name = name;
        _price = price;
        _stockLevel = stockLevel;
        _wishlist = new Wishlist();
        _waitlist = new Waitlist();
    }

    public String id() {
        return _id;
    }

    public String name() {
        return _name;
    }

    public double price() {
        return _price;
    }

    public int stockLevel() {
        return _stockLevel;
    }

    public Wishlist wishlist() {
        return _wishlist;
    }

    public Waitlist waitlist() {
        return _waitlist;
    }

    public void setStockLevel(int newStock) {
        _stockLevel = newStock;
    }
}
