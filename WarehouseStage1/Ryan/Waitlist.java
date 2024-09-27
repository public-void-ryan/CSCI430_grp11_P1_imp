import java.util.HashMap;
import java.util.Map;

public class Waitlist {
    private Map<Client, Product> waitlist;

    public Waitlist() {
        waitlist = new HashMap<>();
    }

    public void addProduct(Client client, Product product) {
        waitlist.put(client, product);
    }

    public Map<Client, Product> getWaitlist() {
        return waitlist;
    }
}
