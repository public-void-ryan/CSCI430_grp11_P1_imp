import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ClientList implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Client> clients;
    private static ClientList clientList;

    private ClientList() {
        clients = new LinkedList<>();
    }

    public static ClientList instance() {
        if (clientList == null) {
            clientList = new ClientList();
        }
        return clientList;
    }

    public Client addClient(Client client) {
        clients.add(client);
        return client;
    }

    public Client findClient(String clientId) {
        for (Client client : clients) {
            if (client.getId().equals(clientId)) {
                return client;
            }
        }
        return null;
    }

    public Iterator<Client> getClients() {
        return clients.iterator();
    }

    public void clear() {
        clients.clear();
    }

    private void writeObject(ObjectOutputStream output) throws IOException {
        output.defaultWriteObject();
    }

    private void readObject(ObjectInputStream input) throws IOException, ClassNotFoundException {
        input.defaultReadObject();
        if (clients == null) {
            clients = new LinkedList<>();
        }
    }

    public String toString() {
        return clients.toString();
    }
}