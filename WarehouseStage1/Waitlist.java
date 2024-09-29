import java.util.ArrayList;
import java.util.List;

public class Waitlist {
    protected List<Client> clients;

    public Waitlist() {
        clients = new ArrayList<>();
    }

    public void addClient(Client client) {
        clients.add(client);
    }

    public void removeClient(Client client) {
        clients.remove(client);
    }
}