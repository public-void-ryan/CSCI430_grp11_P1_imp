import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;
public class ClerkButton extends JButton implements ActionListener{ 
  //private static ClerkButton instance;
  //private JButton userButton;
  public ClerkButton() {
      super("Clerk");
      this.setListener();
  }

/*  public static ClerkButton instance() {
    if (instance == null) {
      instance = new ClerkButton();
    }
    return instance;
  }*/

  public void setListener(){
    //System.out.println("In clerkButton setListener\n");
    this.addActionListener(this);
  }

  public void actionPerformed(ActionEvent event) {
     //System.out.println("In clerk \n");
    (LibContext.instance()).setLogin(LibContext.IsClerk);
     Loginstate.instance().clear();
    (LibContext.instance()).changeState(0);
  } 
}
