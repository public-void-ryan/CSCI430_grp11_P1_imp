import java.io.Serializable;

public class Client implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String id;
    private static final String CLIENT_STRING = "C";
    private static int idCounter = 1;

    public Client(String name) {
        this.name = name;
        this.id = CLIENT_STRING + idCounter++;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public boolean equals(String id) {
        return this.id.equals(id);
    }

    @Override
    public String toString() {
        return "Client [Name=" + name + ", ID=" + id + "]";
    }
}
