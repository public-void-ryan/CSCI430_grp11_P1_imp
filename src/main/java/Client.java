import java.io.Serializable;

public class Client implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String name;
    private final String id;
    private static final String CLIENT_STRING = "C";
    private static int idCounter = 1;
    private final Wishlist wishlist;

    public Client(String name) {
        this.name = name;
        this.id = CLIENT_STRING + idCounter++;
        this.wishlist = new Wishlist();
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public boolean equals(String id) {
        return this.id.equals(id);
    }

    public Wishlist getWishlist() {
        return wishlist;
    }

    @Override
    public String toString() {
        return "Client [Name=" + name + ", ID=" + id + "]";
    }

    public void addToWishlist(Product product) {
        wishlist.addProduct(product);
    }
}
