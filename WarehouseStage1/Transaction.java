public class Transaction {
    private static int nextId = 0;
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
}
