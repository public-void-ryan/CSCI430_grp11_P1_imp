package frontend;

import backend.Warehouse;

import javax.swing.*;
import java.util.Arrays;
import java.util.Stack;

public class WarehouseContext {
    private static WarehouseContext instance;
    private static Warehouse warehouse;
    private static WarehouseState[] states;
    private static JFrame mainFrame;

    private final int[][] nextState;
    private final Stack<Integer> stateStack;

    private int currentState;
    private String currentClientID;

    // State IDs
    public static final int LOGIN_STATE = 0;
    public static final int CLIENT_MENU_STATE = 1;
    public static final int CLERK_MENU_STATE = 2;
    public static final int MANAGER_MENU_STATE = 3;

    // Transition IDs
    public static final int LOGIN_AS_CLIENT = 0;
    public static final int LOGIN_AS_CLERK = 1;
    public static final int LOGIN_AS_MANAGER = 2;
    public static final int LOGOUT = 3;
    public static final int BECOME_CLIENT = 4;
    public static final int BECOME_CLERK = 5;

    private WarehouseContext() {
        // Initialize the main frame
        mainFrame = MainFrame.getInstance();
        mainFrame.setVisible(true);

        // Request to load previous data
        int choice = JOptionPane.showConfirmDialog(
                mainFrame,
                "Look for saved data and use it?",
                "Load Data",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            retrieve();
        } else {
            warehouse = Warehouse.instance();
        }

        // Initialize states
        states = new WarehouseState[4];
        states[LOGIN_STATE] = new LoginState(this, warehouse);
        states[CLIENT_MENU_STATE] = new ClientMenuState(this, warehouse);
        states[CLERK_MENU_STATE] = new ClerkMenuState(this, warehouse);
        states[MANAGER_MENU_STATE] = new ManagerMenuState(this, warehouse);

        // Initialize transition table
        nextState = new int[4][6];

        // Fill the nextState table with invalid transition markers
        for (int[] invalidTransitions : nextState) {
            Arrays.fill(invalidTransitions, -1);
        }

        // Define the valid transitions
        // From LOGIN_STATE
        nextState[LOGIN_STATE][LOGIN_AS_CLIENT] = CLIENT_MENU_STATE;
        nextState[LOGIN_STATE][LOGIN_AS_CLERK] = CLERK_MENU_STATE;
        nextState[LOGIN_STATE][LOGIN_AS_MANAGER] = MANAGER_MENU_STATE;
        nextState[LOGIN_STATE][LOGOUT] = -2; // Exit the system

        // From CLIENT_MENU_STATE
        nextState[CLIENT_MENU_STATE][LOGOUT] = -2; // Return to previous state

        // From CLERK_MENU_STATE
        nextState[CLERK_MENU_STATE][BECOME_CLIENT] = CLIENT_MENU_STATE;
        nextState[CLERK_MENU_STATE][LOGOUT] = -2; // Return to previous state

        // From MANAGER_MENU_STATE
        nextState[MANAGER_MENU_STATE][BECOME_CLERK] = CLERK_MENU_STATE;
        nextState[MANAGER_MENU_STATE][LOGOUT] = -2; // Return to previous state

        // Initialize the current state and state stack
        currentState = LOGIN_STATE;
        stateStack = new Stack<>();
    }

    public static WarehouseContext instance() {
        if (instance == null) {
            instance = new WarehouseContext();
        }
        return instance;
    }

    public void changeState(int transition) {
        int next = nextState[currentState][transition];
        if (next == -1) {
            JOptionPane.showMessageDialog(mainFrame, "Invalid transition!", "Application Error", JOptionPane.ERROR_MESSAGE);
            terminate();
        } else if (next == -2) {
            // Return to the previous state in the stack
            // If the stack is empty that means exit from login page was selected
            if (!stateStack.isEmpty()) {
                currentState = stateStack.pop();
            } else {
                terminate();
            }
            states[currentState].run();
        } else {
            // Push the current state onto the stack before transitioning
            stateStack.push(currentState);
            currentState = next;
            states[currentState].run();
        }
    }

    public void setCurrentClientID(String clientID) {
        this.currentClientID = clientID;
    }

    public String getCurrentClientID() {
        return currentClientID;
    }

    public JFrame getMainFrame() {
        return mainFrame;
    }

    public void process() {
        states[currentState].run();
    }

    private void terminate() {
        int choice = JOptionPane.showConfirmDialog(
                mainFrame,
                "Would you like to save the data before exiting?",
                "Save Data",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            save();
        }

        System.exit(0);
    }


    private void retrieve() {
        try {
            Warehouse tempWarehouse = Warehouse.retrieve();
            if (tempWarehouse != null) {
                JOptionPane.showMessageDialog(
                        mainFrame,
                        "Warehouse data successfully retrieved.",
                        "Data Loaded",
                        JOptionPane.INFORMATION_MESSAGE
                );
                warehouse = tempWarehouse;
            } else {
                JOptionPane.showMessageDialog(
                        mainFrame,
                        "No saved data found. Creating a new warehouse.",
                        "Data Not Found",
                        JOptionPane.WARNING_MESSAGE
                );
                warehouse = Warehouse.instance();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    mainFrame,
                    "Error retrieving warehouse data: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            warehouse = Warehouse.instance();
        }
    }

    private void save() {
        if (Warehouse.save()) {
            JOptionPane.showMessageDialog(
                    mainFrame,
                    "Warehouse data successfully saved.",
                    "Save Successful",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog(
                    mainFrame,
                    "Error saving warehouse data.",
                    "Save Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static void main(String[] args) {
        WarehouseContext.instance().process();
    }
}
