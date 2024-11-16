package frontend;

import backend.*;

import java.util.Objects;

public class LoginState extends WarehouseState {
    private static final int EXIT = 0;
    private static final int CLIENT_LOGIN = 1;
    private static final int CLERK_LOGIN = 2;
    private static final int MANAGER_LOGIN = 3;
    private static final int HELP = 4;

    public LoginState(WarehouseContext warehouseContext, Warehouse warehouse) {
        super(warehouseContext, warehouse);
    }

    private int getCommand() {
        return InputUtils.getCommand(EXIT, HELP, "Enter command (" + HELP + " for help): ");
    }

    private void help() {
        System.out.println("Login Menu:");
        System.out.println(EXIT + " to Exit");
        System.out.println(CLIENT_LOGIN + " to Login as Client");
        System.out.println(CLERK_LOGIN + " to Login as Clerk");
        System.out.println(MANAGER_LOGIN + " to Login as Manager");
        System.out.println(HELP + " for help");
    }

    @Override
    public void run() {
        int command;
        help();
        while ((command = getCommand()) != EXIT) {
            switch (command) {
                case CLIENT_LOGIN:
                    loginAsClient();
                    break;
                case CLERK_LOGIN:
                    loginAsClerk();
                    break;
                case MANAGER_LOGIN:
                    loginAsManager();
                    break;
                case HELP:
                    help();
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        }
        warehouseContext.changeState(WarehouseContext.LOGOUT);
    }

    private void loginAsClient() {
        String clientID = InputUtils.getToken("Enter Client Username: ");
        String clientPassword = InputUtils.getToken("Enter Client Password: ");

        try {
            warehouse.getClient(clientID);
        } catch (Exception e) {
            System.out.println("Invalid Client Username");
            return;
        }

        if (!Objects.equals(clientID, clientPassword)) {
            System.out.println("Invalid Client Password");
            return;
        }

        warehouseContext.setCurrentClientID(clientID);
        warehouseContext.changeState(WarehouseContext.LOGIN_AS_CLIENT);
    }

    private void loginAsClerk() {
        String clerkID = InputUtils.getToken("Enter Clerk Username: ");
        String clerkPassword = InputUtils.getToken("Enter Client Password: ");

        if (!Objects.equals(clerkID, "clerk")) {
            System.out.println("Invalid Clerk Username");
            return;
        }

        if (!Objects.equals(clerkPassword, "clerk")) {
            System.out.println("Invalid Clerk Password");
            return;
        }

        warehouseContext.changeState(WarehouseContext.LOGIN_AS_CLERK);
    }

    private void loginAsManager() {
        String managerID = InputUtils.getToken("Enter Manager Username: ");
        String managerPassword = InputUtils.getToken("Enter Manager Password: ");

        if (!Objects.equals(managerID, "manager")) {
            System.out.println("Invalid Clerk Username");
            return;
        }

        if (!Objects.equals(managerPassword, "manager")) {
            System.out.println("Invalid Manager Password");
            return;
        }

        warehouseContext.changeState(WarehouseContext.LOGIN_AS_MANAGER);
    }
}
