import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ClientList implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Client> clients = new LinkedList<>();
    private static ClientList clientList;

    private ClientList() {
    }

    public static ClientList instance() {
        if (clientList == null) {
            clientList = new ClientList();
        }
        return clientList;
    }

    public boolean addClient(Client client) {
        return clients.add(client);
    }

    public Iterator<Client> getClients() {
        return clients.iterator();
    }

    public Client search(String clientId) {
        for (Client client : clients) {
            if (client.getId().equals(clientId)) {
                return client;
            }
        }
        return null;
    }

    private Object readResolve() {
        return instance();
    }

    @Override
    public String toString() {
        return clients.toString();
    }
}
