// // BAD
// // Product interface
// interface ProductInterface { void run(); }
// // Factory interface
// // ESSENTIAL: the factory returns a concrete class; the client becomes
// coupled
// // to it.
// // A full Abstract Factory should return product interfaces instead.
// interface FactoryInterface { ObjectAClass createProduct(); }
// // Family A
// class ObjectAClass implements ProductInterface {
// public void run() { System.out.println("Bad: ObjectAClass is running!"); }
// }
// class FactoryAClass implements FactoryInterface { public ObjectAClass
// createProduct() { return new ObjectAClass(); } }
// // Family B
// class ObjectBClass implements ProductInterface {
// public void run() { System.out.println("Bad: ObjectBClass is running!"); }
// }
// // ERROR: ObjectBClass is not ObjectAClass.
// class FactoryBClass implements FactoryInterface { public ObjectAClass
// createProduct() { return new ObjectBClass(); } }
// // Client
// public class AbstractFactoryBad {
// public static void main(String[] args) {
// boolean chooseA = false;
// FactoryInterface myFactory;
// if (chooseA) {
// myFactory = new FactoryAClass();
// } else {
// myFactory = new FactoryBClass();
// }
// ObjectAClass myProduct = myFactory.createProduct();
// myProduct.run();
// }
// }
