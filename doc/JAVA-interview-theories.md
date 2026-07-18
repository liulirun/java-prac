# OOP 4 Principles

| Principle         | Primary Tactical Tool         | Core Purpose in Production Architecture                                |
| ----------------- | ----------------------------- | ---------------------------------------------------------------------- |
| **Encapsulation** | `private` / Getters / Setters | Data integrity, control over state modification, isolation             |
| **Inheritance**   | `extends`                     | Drastically reduces boilerplate redundancy by establishing hierarchies |
| **Polymorphism**  | `@Override` / Overloading     | Decouples components by letting code deal with parent types safely     |
| **Abstraction**   | `interface` / `abstract`      | Minimizes domain friction by separating system definitions from tools  |

1\. Encapsulation (Data Hiding)

- **The Concept:** Bundling data (variables) and methods together inside a single unit (class), while hiding the internal implementation details from external interference.
- **Interview Focus:** Explain *how* it is achieved (making fields `private` and exposing controlled entry points via `public` getter/setter methods) and *why* it matters (validates state transitions to prevent corrupted object states).

```java
// ❌ WRONG: No Encapsulation. External classes can inject illegal, breaking values.
class BankAccount {
    public double balance; // Anyone can run: account.balance = -999999;
}

// ✅ CORRECT: Protected state via validation rules inside setters.
class SecureBankAccount {
    private double balance; // Access restricted to this class only

    public double getBalance() { return this.balance; }

    public void deposit(double amount) {
        if (amount > 0) {   // ✅ Check: Prevents corrupt state transitions
            this.balance += amount;
        }
    }
}
```

2\. Inheritance (Code Reuse)

- **The Concept:** The mechanism where a new class (subclass) derives properties and behaviors from an existing class (superclass).
- **Interview Focus:** Mention the **`extends`** keyword. Highlight the structural constraint: Java strictly enforces **single inheritance** for classes to avoid the *Diamond Problem* (ambiguity over which parent method to run), but allows **multiple inheritance** for interfaces.

```java
class Vehicle {
    protected int speed = 0;                  // Visible to children
    public void accelerate() { speed += 10; }
}

// ✅ Check: Car reuses speed field and accelerate() behavior from Vehicle
class Car extends Vehicle {
    private boolean convertible = false;

    public void openRoof() { 
        if (speed == 0) { convertible = true; } 
    }
}
```

3\. Polymorphism (Many Forms)

- **The Concept:** The capability of an action or message to behave differently depending on the specific object instance execution context.

- **Interview Focus:** You must distinguish between the two primary types:

  1. **Compile-Time Polymorphism (Static Binding / Method Overloading):** Handled via identical method names but entirely unique parameter sign-offs.
  2. **Runtime Polymorphism (Dynamic Binding / Method Overriding):** Resolved when a child class provides a targeted override implementation of an inherited parent method.

```java
class SmartCalculator {
    // ✅ Check: Compile-Time Polymorphism (Method Overloading)
    public int add(int a, int b) { return a + b; }
    public double add(double a, double b) { return a + b; }
}

class Animal { void makeSound() { System.out.println("Generic sound"); } }
class Dog extends Animal {
    @Override void makeSound() { System.out.println("Bark!"); } // ✅ Check: Runtime Polymorphism (Overriding)
}
```

4\. Abstraction (Complexity Hiding)

- **The Concept:** Hiding complex infrastructure execution steps and only surfacing relevant, essential structural components to users.
- **Interview Focus:** Do not confuse this with Encapsulation. *Encapsulation hides data elements to maintain security; Abstraction hides complex execution loops to reduce systemic mess.* This is implemented via **`abstract classes`** and **`interfaces`**.

```java
// The Abstraction Contract: User knows what it does, but has no idea how it operates behind the scenes.
interface CloudStorage { void uploadFile(String name); }

class AWSStorage implements CloudStorage {
    public void uploadFile(String name) {
        // Concrete Execution Complexity is hidden here:
        // Establish HTTPS context, parse byte stream, handshake with Amazon S3 bucket...
        System.out.println("Uploaded safely to AWS Cloud storage instance.");
    }
}
```

Part 1: Detailed SOLID Principles Breakdown

1\. Single Responsibility Principle (SRP)

- **The Concept:** A class should have only one reason to change. It must address exactly one cohesive business responsibility.
- **Interview Focus:** Developers often confuse "one reason to change" with "only doing one thing." A class can have multiple methods, but they must all serve the exact same business actor.

```java
// ❌ WRONG: Violation of SRP. This class changes if DB logic OR invoice layout changes.
class InvoiceProcessor {
    public void calculateTotal() { /* Calculation logic */ }
    public void saveToDatabase() { /* Database logic */ }
    public void generatePdfReport() { /* Presentation logic */ }
}

// ✅ CORRECT: Separated into clean, single-responsibility units.
class InvoiceCalculator {
    public double calculateTotal(Invoice invoice) { return invoice.getAmount() * 1.13; }
}
class InvoiceRepository {
    public void save(Invoice invoice) { /* Database save logic */ }
}
class InvoicePrinter {
    public void generatePdf(Invoice invoice) { /* PDF generation logic */ }
}
```

2\. Open/Closed Principle (OCP)

- **The Concept:** Software entities should be open for extension, but closed for modification.
- **Interview Focus:** You must demonstrate how to add new behavior to a system *without editing existing, tested class files*. This is typically achieved using interfaces or abstract classes.

```java
// ❌ WRONG: Modifying this switch block for every new payment method breaks OCP.
class PaymentProcessor {
    public void process(String type) {
        if (type.equals("VISA")) { /* Process Visa */ } 
        else if (type.equals("PAYPAL")) { /* Process Paypal */ }
    }
}

// ✅ CORRECT: Open for extension via polymorphism. No need to touch existing classes.
interface PaymentMethod { void pay(); }

class VisaPayment implements PaymentMethod { public void pay() { /* Visa logic */ } }
class PayPalPayment implements PaymentMethod { public void pay() { /* PayPal logic */ } }

class OcpPaymentProcessor {
    public void process(PaymentMethod method) { method.pay(); } // Closed to modification
}
```

3\. Liskov Substitution Principle (LSP)

- **The Concept:** Objects of a superclass must be completely replaceable with objects of a subclass without breaking the application logic or throwing unexpected exceptions.
- **Interview Focus:** The classic "Square extends Rectangle" trap. Subclasses must not weaken pre-conditions or strengthen post-conditions defined by the parent class.

```java
// ❌ WRONG: Violation of LSP. Subclass throws an exception, breaking parent contract.
class Bird { public void fly() { System.out.println("Flying"); } }

class Ostrich extends Bird {
    @Override
    public void fly() { throw new UnsupportedOperationException("Ostriches cannot fly!"); }
}

// ✅ CORRECT: Model behaviors accurately using hierarchies or specific interfaces.
class RealBird { /* General bird traits */ }
class FlyingBird extends RealBird { public void fly() { System.out.println("Flying"); } }
class OstrichBird extends RealBird { public void run() { System.out.println("Running"); } }
```

4\. Interface Segregation Principle (ISP)

- **The Concept:** Clients should not be forced to depend on interfaces or methods they do not use.
- **Interview Focus:** Avoid bloated "fat" interfaces. Split them into smaller, highly specific components.

```java
// ❌ WRONG: Forcing a basic printer to implement fax and scan logic.
interface SmartDevice {
    void print();
    void fax();
    void scan();
}

// ✅ CORRECT: Clean, highly-segregated lean interfaces.
interface Printer { void print(); }
interface Scanner { void scan(); }
interface FaxMachine { void fax(); }

class BasicPrinter implements Printer {
    public void print() { System.out.println("Printing..."); }
}
```

5\. Dependency Inversion Principle (DIP)

- **The Concept:** High-level modules should not depend on low-level modules. Both must depend on abstractions. Abstractions must not depend on details; details must depend on abstractions.
- **Interface Focus:** This underpins Dependency Injection (Spring Framework). Do not instantiate dependencies using `new` inside your business logic classes.

```java
// ❌ WRONG: High-level Car is tightly coupled to the low-level V8Engine detail class.
class V8Engine { void start() {} }
class Car {
    private V8Engine engine = new V8Engine(); // Hardcoded dependency
    void drive() { engine.start(); }
}

// ✅ CORRECT: Both depend on the Engine abstraction.
interface Engine { void start(); }
class ElectricEngine implements Engine { public void start() {} }

class DipCar {
    private final Engine engine;
    public DipCar(Engine engine) { this.engine = engine; } // Injected abstraction
    void drive() { engine.start(); }
}
```

Part 2: Interface vs. Abstract Class vs. Concrete Class

This structural comparison tests your foundational knowledge of OOP design tradeoffs in Java.

```text
               ┌──────────────────────┐
               │      INTERFACE       │ ──> Pure Contract (What it does)
               └──────────────────────┘
                           │
                           ▼
               ┌──────────────────────┐
               │    ABSTRACT CLASS    │ ──> Partial Blueprint (Common state/behavior)
               └──────────────────────┘
                           │
                           ▼
               ┌──────────────────────┐
               │    CONCRETE CLASS    │ ──> Full Execution Blueprint (Instantiable)
               └──────────────────────┘
```

1\. Interface

- **Purpose:** Defines a strict functional contract ("What an object can do").
- **Rules:** Cannot hold instance states. Multiple interfaces can be implemented by a single class.
- **Java 8/9 Upgrades:** Can now contain `default` methods, `static` methods, and `private` helper methods.

2\. Abstract Class

- **Purpose:** Defines a partial conceptual blueprint ("What an object fundamentally is").
- **Rules:** Can hold state variables (instance fields). Cannot be instantiated via `new`. A class can only extend one abstract class.

3\. Concrete Class

- **Purpose:** A finalized blueprint ready for object construction.
- **Rules:** Must provide complete concrete implementations for all inherited abstract contracts.

Deep-Dive Structural Code Comparison

The code block below outlines what can and cannot be compiled across these types:

```java
package OOP.comparison;

interface DataContract {
    // String name = "Default";               // ❌ ERROR: Variables are implicitly 'public static final' constants
    public static final int TIMEOUT = 5000;   // ✅ Check: Constants are perfectly legal

    void execute();                           // ✅ Check: Abstract method declaration

    default void log(String msg) {            // ✅ Check: Java 8 default method allows implementation body
        printPrivate(msg);
    }

    private void printPrivate(String msg) {   // ✅ Check: Java 9 allows private helper methods inside interfaces
        System.out.println("Log: " + msg);
    }
}

abstract class BaseWorker {
    private int workerId;                     // ✅ Check: Abstract classes CAN hold mutable instance state vars
    public BaseWorker(int id) { this.workerId = id; } // ✅ Check: Constructors are legal (called via super())

    public abstract void performTask();       // ✅ Check: Abstract method without body

    public void breakTime() {                 // ✅ Check: Fully concrete instance methods are legal
        System.out.println("Taking a break.");
    }
}

class ConcreteEmployee extends BaseWorker implements DataContract {
    public ConcreteEmployee(int id) { super(id); } // ✅ Check: Invoking parent abstract constructor

    @Override
    public void performTask() {               // ✅ Check: Mandatory implementation of abstract class method
        System.out.println("Working...");
    }

    @Override
    public void execute() {                   // ✅ Check: Mandatory implementation of interface method
        System.out.println("Executing contract...");
    }
}
```

Part 3: Easily Confusing & Frequently Asked Interview Questions

Q1: What is the difference between `final`, `finally`, and `finalize`?

- **`final` (Keyword):** Used to mark variables un-reassignable, methods un-overridable, or classes un-extendable.
- **`finally` (Block):** A control flow block used with `try-catch` to ensure cleanup code runs, even if exceptions are thrown.
- **`finalize` (Method):** A deprecated protected method in `java.lang.Object` historically invoked by the Garbage Collector before an object's memory reclamation. **Do not use it.**

```java
public class ControlFlowDemo {
    public final int maxAttempts = 3;         // ✅ Check: Value cannot be reassigned

    public void process() {
        try {
            System.out.println("Processing");
        } finally {
            System.out.println("Always runs"); // ✅ Check: Guarantees resource cleanup execution
        }
    }
}
```

Q2: Why are `String` objects immutable in Java?

- **Security:** Prevents sensitive values (like database connection URLs or network credentials) from being changed maliciously at runtime.
- **String Pool Economy:** Saves memory by allowing multiple string reference variables to point to the exact same memory space in heap storage.
- **Thread Safety:** Multi-threaded code can access strings concurrently without synchronization risks.

Q3: What is the difference between `==` and `.equals()`?

- **`==` (Operator):** Compares primitive values directly, or evaluates whether two object references point to the exact same **memory address** (shallow comparison).
- **`.equals()` (Method):** Evaluates whether the internal **contents** of two separate objects match logically (deep comparison), provided the method is appropriately overridden.

```java
String s1 = new String("Java");
String s2 = new String("Java");

boolean refCheck = (s1 == s2);               // ❌ Evaluates to false: Different objects in heap memory
boolean contentCheck = s1.equals(s2);        // ✅ Evaluates to true: Both contain identical characters
```

Q4: Can you override a `static` method?

- **The Answer:** No. Method overriding relies on dynamic, runtime polymorphism. `static` methods are bound to the class type at compile-time (static binding).
- **Method Hiding:** Redefining a static method in a subclass compiles, but it hides the parent method rather than overriding it.

```java
class SuperClass {
    public static void show() { System.out.println("Super"); }
}
class SubClass extends SuperClass {
    // @Override                              // ❌ ERROR: Compiler blocks @Override tag on static methods
    public static void show() { System.out.println("Sub"); } // ✅ Check: This is Method Hiding, not overriding
}
```

Q5: What is the risk of calling an overridable method inside a Constructor?

- **The Answer:** It introduces a subtle vulnerability where the subclass method can run **before** the subclass instance fields have completed their initialization process, causing unpredictable data corruption or unexpected `NullPointerException` errors.

```java
class ParentConfig {
    public ParentConfig() {
        init();                               // ❌ INTERVIEW TRAP: Calling overridable method inside constructor
    }
    public void init() { System.out.println("Parent Init"); }
}

class ChildConfig extends ParentConfig {
    private String sensitiveData;

    public ChildConfig() {
        this.sensitiveData = "Initialized!";
    }

    @Override
    public void init() {
        // ❌ Prints "null" because this method runs BEFORE the Child constructor completes initialization!
        System.out.println("Child Init: " + sensitiveData.toUpperCase()); 
    }
}
```

Core Structural Cheat Sheet

| Question Concept       | Binding Stage    | Targets                     | Primary Objective                                             |
| ---------------------- | ---------------- | --------------------------- | ------------------------------------------------------------- |
| **Method Overloading** | **Compile-Time** | Same class methods          | Enhances method readability via distinct parameter structures |
| **Method Overriding**  | **Runtime**      | Subclass methods            | Specialized polymorphism execution                            |
| **Method Hiding**      | **Compile-Time** | Parent/Child static methods | Re-scoping utility features within explicit class bounds      |

Would you like to drill down into the details of **Garbage Collection algorithms** or explore the runtime performance differences between **`ArrayList` and `LinkedList`** internals?