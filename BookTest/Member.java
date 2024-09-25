import java.util.*;
import java.io.*;
public class Member implements Serializable {

  private String id;
   
  public  Member (String  id) {
    this.id =  id;
  }

   
  public String getId() {
    return id;
  }
   
  public String toString() {
    String string =  " id " + id;
    return string;
  }
}
