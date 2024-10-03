import java.io.Serializable;

public class Client implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String id;
    private String name;
    private String address;
    private String phone;
    private static final String CLIENT_STRING = "C";
    private static int idCounter = 1;
    private final Wishlist wishlist;

    public Client(String name, String address, String phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.id = CLIENT_STRING + idCounter++;
        this.wishlist = new Wishlist();
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

    public String getId() {
        return id;
    }

    public Wishlist getWishlist() {
        return wishlist;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Client [Name=" + name + ", " + "Address=" + address + "Phone=" + phone + "ID=" + id + "]";
    }

    public void addToWishlist(Product product) {
        wishlist.addProduct(product);
    }

    public static void resetIdCounter() {
        idCounter = 1;
    }
}