// GOOD
// Product interface
interface ProductInterface { void run(); }

// Factory interface
// ESSENTIAL: the funcs inside factory returns a product interface; the client
// uses interfaces.
// A full Abstract Factory usually creates several related product INTERFACE.
interface FactoryInterface { ProductInterface createProduct(); }

// Family A
class ObjectAClass implements ProductInterface {
  public void run() { System.out.println("Good: ObjectAClass is running!"); }
}

class FactoryAClass implements FactoryInterface {
  public ProductInterface createProduct() { return new ObjectAClass(); }
}

// Family B
class ObjectBClass implements ProductInterface {
  public void run() { System.out.println("Good: ObjectBClass is running!"); }
}

class FactoryBClass implements FactoryInterface {
  public ProductInterface createProduct() { return new ObjectBClass(); }
}

// Client
public class AbstractFactoryGood {
  public static void main(String[] args) {
    boolean chooseA = false;
    FactoryInterface myFactory;
    if (chooseA) {
      myFactory = new FactoryAClass();
    } else {
      myFactory = new FactoryBClass();
    }
    ProductInterface myProduct = myFactory.createProduct();
    myProduct.run();
  }
}
