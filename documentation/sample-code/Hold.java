import java.util.*;
import java.io.*;
public class Hold implements Serializable {
  private Book book;
  private Member member;
  private Calendar date;
  public Hold(Member member, Book book, int duration) {
    this.book = book;
    this.member = member;
    date = new GregorianCalendar();
    date.setTimeInMillis(System.currentTimeMillis());
    date.add(Calendar.DATE, duration);
  }
  public Member getMember() {
    return member;
  }
  public Book getBook() {
    return book;
  }
  public Calendar getDate() {
    return date;
  }
  public boolean isValid(){
    return (System.currentTimeMillis() < date.getTimeInMillis());
  }
}

