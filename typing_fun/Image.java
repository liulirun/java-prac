// Removed 'public' so it can live inside Image.java
interface Drawable {
  void draw();

  default void show() {
    System.out.println("Showing drawable");
    draw();
  }
}

// This remains non-public as well
interface Resizable {
  void resize(double factor);

  default void draw() {
    System.out.println("inteface draw()");
  };
}

// This is the ONLY public top-level class, matching the filename "Image.java"
public class Image implements Drawable, Resizable {
  private String filename;
  private int width;
  private int height;

  public Image(String filename, int width, int height) {
    this.filename = filename;
    this.width = width;
    this.height = height;
  }

  // @Override
  // public void draw() {
  // System.out.println("Drawing " + filename + " at " + width + "x" + height);
  // }

  @Override
  public void resize(double factor) {
    width = (int) (width * factor);
    height = (int) (height * factor);
    System.out.println("Resized to " + width + "x" + height);
  }

  public static void main(String[] args) {
    Image myImage = new Image("photo.png", 800, 600);
    myImage.show();
    System.out.println("---");
    myImage.resize(0.5);
  }
}
