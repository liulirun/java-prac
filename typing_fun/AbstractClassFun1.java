public class AbstractClassFun1 {
  // 1. Added static so Java recognizes it as the program entry point
  public static void main(String[] args) {
    Shape circle = new Circle("Red", 5.0);
    Shape rectangle = new Rectangle("Blue", 4.0, 6.0);

    circle.display();
    rectangle.display();
  }

  public static abstract class Shape {
    protected String color;

    // =========================================================================
    // WHAT IS NOT SUPPORTED IN AN ABSTRACT CLASS
    // =========================================================================

    // ❌ NOT SUPPORTED: Direct Instantiation.
    // You cannot create an object of an abstract class using the 'new' keyword.
    // Un-commenting this line inside main or here will throw a compilation error:
    // "Shape is abstract; cannot be instantiated"
    // Shape testShape = new Shape("Green");

    // ❌ NOT SUPPORTED: Multiple Inheritance.
    // A class can only extend ONE class. An abstract class cannot extend multiple
    // abstract classes.
    // If we had another abstract class called 'Asset', this would fail compilation:
    // public static abstract class Shape extends Asset, Device { }

    // ❌ NOT SUPPORTED: Abstract methods with a body.
    // Abstract methods are blueprints only. They cannot contain any code or curly
    // braces.
    // Un-commenting this will cause a compilation error: "Abstract methods cannot
    // have a body"
    // public abstract void reset() { System.out.println("Resetting..."); }

    // ❌ NOT SUPPORTED: Combinations of 'abstract' with 'private', 'static', or
    // 'final'.
    // Abstract methods MUST be overridden by subclasses. Therefore, they cannot be:
    // - private (subclasses can't see them)
    // - static (belongs to the class, cannot be overridden)
    // - final (explicitly prevents overriding)
    // Un-commenting any of these will cause severe compilation errors:
    // private abstract void privateAbstract();
    // public static abstract void staticAbstract();
    // public final abstract void finalAbstract();

    // =========================================================================
    // SUPPORTED FEATURES
    // =========================================================================

    public Shape(String color) {
      this.color = color;
    }

    public abstract double calculateArea();

    public void displayColor() {
      System.out.println("Color: " + color);
    }

    public final void display() {
      displayColor();
      System.out.println("Area: " + calculateArea());
    }
  }

  public static class Circle extends Shape {
    private double radius;

    public Circle(String color, double radius) {
      super(color);
      this.radius = radius;
    }

    @Override
    public double calculateArea() {
      return Math.PI * radius * radius;
    }
  }

  public static class Rectangle extends Shape {
    private double width;
    private double height;

    public Rectangle(String color, double width, double height) {
      super(color);
      this.width = width;
      this.height = height;
    }

    @Override
    public double calculateArea() {
      return width * height;
    }
  }
}
