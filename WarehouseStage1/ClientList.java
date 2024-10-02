import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ClientList implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Client> clients = new LinkedList<>();

    public ClientList() {
    }

    public boolean addClient(Client client) {
        return clients.add(client);
    }

    public Iterator<Client> getClients() {
        return clients.iterator();
    }

    public Client search(String clientId) {
        for (Client client : clients) {
            if (client.id().equals(clientId)) {
                return client;
            }
        }
        return null;
    }
}
