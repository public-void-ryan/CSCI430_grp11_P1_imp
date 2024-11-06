public abstract class LibState {
  protected static LibContext context;
  protected LibState() {
    //context = LibContext.instance();
  }
  public abstract void run();
}
