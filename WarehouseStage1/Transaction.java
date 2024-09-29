import java.io.Serializable;

public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    private static transient int nextId = 0; // Marked as transient
    private int id;
    private Client client;
    private Product product;
    private int quantity;

    public Transaction(Client client, Product product, int quantity) {
        this.id = ++nextId;
        this.client = client;
        this.product = product;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "Transaction{id=" + id + ", client=" + client + ", product=" + product + ", quantity=" + quantity + "}";
    }
}
