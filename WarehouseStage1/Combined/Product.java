public class Product {
    private String _id;
    private String _name;
    private double _price;
    private int _stockLevel;
    private Wishlist _wishlist;
    private Waitlist _waitlist;

    public Product(String id, String name, double price, int stockLevel) {
        _id = id;
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

    public void addToWishlist(Client client) {
        _wishlist.addProduct(this);
    }

    public void removeFromWishlist(Client client) {
        _wishlist.removeProduct(this);
    }

    public void addToWaitlist(Client client) {
        _waitlist.addClient(client);
    }

    public void removeFromWaitlist(Client client) {
        _waitlist.removeClient(client);
    }

    @Override
    public String toString() {
        return "Product{id='" + _id + "', name='" + _name + "', price=" + _price + ", stockLevel=" + _stockLevel + "}";
    }
}
