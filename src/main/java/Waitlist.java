import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Waitlist implements Serializable {
    private static final long serialVersionUID = 1L;
    private final List<WaitlistItem> waitlist;

    public Waitlist() {
        waitlist = new LinkedList<>();
    }

    public static class WaitlistItem implements Serializable {
        private static final long serialVersionUID = 1L;
        private final Client client;
        private int quantity;

        public WaitlistItem(Client client, int quantity) {
            this.client = client;
            this.quantity = quantity;
        }

        // Getters
        public Client getClient() {
            return client;
        }

        public int getQuantity() {
            return quantity;
        }

        // Setters
        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        @Override
        public String toString() {
            return String.format("WaitlistItem [Client=%s, Quantity=%d]", client, quantity);
        }
    }

    public WaitlistItem addClient(Client client, int quantity) {
        if (client == null) {
            throw new IllegalArgumentException("Client cannot be null.");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }

        for (WaitlistItem item : waitlist) {
            if (item.getClient().getId().equals(client.getId())) {
                // Update the quantity if client exists
                item.setQuantity(item.getQuantity() + quantity);
                return item;
            }
        }
        WaitlistItem newItem = new WaitlistItem(client, quantity);
        waitlist.add(newItem);
        return newItem;
    }

    public boolean removeClient(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("Client cannot be null.");
        }

        return waitlist.removeIf(item -> item.getClient().getId().equals(client.getId()));
    }

    public Iterator<WaitlistItem> getWaitlistItems() {
        return waitlist.iterator();
    }

    public void clear() {
        waitlist.clear();
    }
}
