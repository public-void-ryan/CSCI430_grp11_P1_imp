import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ClientList implements Serializable {
    private static final long serialVersionUID = 1L;
    private final List<Client> clients = new LinkedList<>();

    public ClientList() {
    }

    public void addClient(Client client) {
        clients.add(client);
    }

    public Iterator<Client> getClients() {
        return clients.iterator();
    }

    public Client findClient(String clientId) {
        for (Client client : clients) {
            if (client.getId().equals(clientId)) {
                return client;
            }
        }
        return null;
    }
}
