# Dependency Injection (DI) Modes Summary

## Direct Comparison

| Feature | Mode 1: Constructor Injection | Mode 2: Factory Injection |
| :--- | :--- | :--- |
| **Injection Point** | Constructor (`public Order(List<OrderItem> items)`) | Method (`public void addItem(..., Function<..., OrderItem> factory)`) |
| **Order Lifecycle** | Fixed / Immutable at creation | Flexible / Changes dynamically |
| **When Math Happens** | Only at the very end (`getTotal()`) | Instantly as items are created |
| **Code Complexity** | Low / Very Clean | Medium / Abstract |

## Mode 1: Constructor Injection

Dependencies are created first and injected all at once during the initialization of the parent object.

```
[ Items List ] ──► Injected via Constructor ──► [ Order Object ]
```

### Java Implementation

```java
import java.util.Collections;
import java.util.List;

// Dependency
class OrderItem {
    private final String name;
    private final double price;

    public OrderItem(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }
}

// Client
class Order {
    private final List<OrderItem> items;

    // Constructor Injection
    public Order(List<OrderItem> items) {
        this.items = List.copyOf(items); // Immutable / Fixed snapshot
    }

    public double getTotal() {
        return items.stream()
                .mapToDouble(OrderItem::getPrice)
                .sum(); // Math happens at the end
    }
}

// Usage
public class ConstructorInjectionDemo {
    public static void main(String[] args) {
        List<OrderItem> cart = List.of(
            new OrderItem("Laptop", 1200.00),
            new OrderItem("Mouse", 25.00)
        );

        // Injecting ready-made dependencies
        Order order = new Order(cart);
        System.out.println("Total: $" + order.getTotal());
    }
}
```

* **How it works:** You build your list of `OrderItem` objects outside the `Order` class, then pass the complete list into `new Order(items)`.
* **Best Used For:** Bulk updates, loading a saved shopping cart from a database, or APIs receiving a complete JSON payload all at once.
* **Pros:**
  * Highly secure and immutable (prevents mid-process data changes).
  * Easiest to unit test.
* **Cons:**
  * Rigid; you cannot easily start with an empty order and build it up dynamically.

---

## Mode 2: Factory Injection (Dynamic)

An object-creation blueprint (a function or service) is injected into the class so dependencies can be generated on demand.

```
[ Factory Rule ] ──► Injected via method ──► [ Order Object ] ──► Creates [ OrderItem ]
```

### Java Implementation

```java
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
 
// Dependency
class DynamicOrderItem {
    private final String name;
    private final double finalPrice;

    public DynamicOrderItem(String name, double finalPrice) {
        this.name = name;
        this.finalPrice = finalPrice;
    }

    public double getFinalPrice() {
        return finalPrice;
    }
}

// Client
class DynamicOrder {
    private final List<DynamicOrderItem> items = new ArrayList<>();
    private double currentRunningTotal = 0.0;

    // Factory Injection via Method
    public void addItem(String name, double basePrice, BiFunction<String, Double, DynamicOrderItem> factory) {
        // The injected factory rule handles creation on demand
        DynamicOrderItem item = factory.apply(name, basePrice);
        items.add(item);
        
        // Math happens instantly upon creation
        currentRunningTotal += item.getFinalPrice(); 
    }

    public double getRunningTotal() {
        return currentRunningTotal;
    }
}

// Usage
public class FactoryInjectionDemo {
    public static void main(String[] args) {
        DynamicOrder order = new DynamicOrder();

        // Factory rule: Apply a 10% discount blueprint dynamically
        BiFunction<String, Double, DynamicOrderItem> discountFactory = 
            (name, price) -> new DynamicOrderItem(name, price * 0.90);

        // Items are built and calculated dynamically on demand
        order.addItem("Premium Headphones", 200.00, discountFactory);
        System.out.println("Running Total: $" + order.getRunningTotal()); // 180.0

        order.addItem("Mechanical Keyboard", 100.00, discountFactory);
        System.out.println("Running Total: $" + order.getRunningTotal()); // 270.0
    }
}
```

* **How it works:** You pass a factory function blueprint into an order method. The `Order` uses that injected function to safely build and calculate properties (like individual product discounts) on the fly.
* **Best Used For:** Interactive, live user sessions (e.g., a customer clicking "Add to Cart" one item at a time over a long period).
* **Pros:**
  * Highly flexible and dynamic.
  * Calculates item-specific rules (like discounts or tax tiers) instantly upon addition.
* **Cons:**
  * Increases architectural complexity by introducing an extra layer of abstraction.
