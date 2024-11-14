public class SessionContext {
    private int primaryRole;
    private int currentRole;

    public void intializeSessionContext(int role) {
        primaryRole = role;
        currentRole = role;
    }

    public void UpdateCurrentRole(int role) {
        currentRole = role;
    }

    public int getPrimaryRole() {
        return primaryRole;
    }

    public int getCurrentRole() {
        return currentRole;
    }
}
