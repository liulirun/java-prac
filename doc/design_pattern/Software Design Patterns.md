# Software Design Patterns: Comprehensive Selection Guide

Choosing the wrong architectural pattern can lead to code that is rigid, difficult to test, and expensive to maintain. This guide isolates core structural, creational, and behavioral design patterns, detailing **exactly when to use them** based on general object-oriented design problems, accompanied by clear Java implementations.

---

## 1. Structural Patterns (System Composition)

### The Decorator Pattern
* **When to Use:** Use when you need to dynamically add or stack transparent responsibilities onto individual objects at runtime without subclassing or affecting other objects of the same class.
* **Detailed Architectural Reason:** Relying on inheritance to add optional behaviors leads to an exponential explosion of classes (e.g., `FeatureAWithFeatureB`, `FeatureAWithFeatureC`). The Decorator pattern addresses this by utilizing composition over inheritance. It wraps the core object inside wrapper classes that share the exact same interface. Each wrapper executes its custom behavior before or after delegating the request to the wrapped component, enabling infinite combinations of behavior at runtime.

#### Java Example
```java
interface Component {
    String operation();
}

class ConcreteComponent implements Component {
    @Override
    public String operation() { return "Base Data"; }
}

abstract class ComponentDecorator implements Component {
    protected Component wrapped;
    public ComponentDecorator(Component wrapped) { this.wrapped = wrapped; }
}

class EncryptionDecorator extends ComponentDecorator {
    public EncryptionDecorator(Component wrapped) { super(wrapped); }
    @Override
    public String operation() { return "Encrypted(" + wrapped.operation() + ")"; }
}

class CompressionDecorator extends ComponentDecorator {
    public CompressionDecorator(Component wrapped) { super(wrapped); }
    @Override
    public String operation() { return "Compressed(" + wrapped.operation() + ")"; }
}
```

---

### The Composite Pattern
* **When to Use:** Use when your domain model can be represented as a tree structure (part-whole hierarchy) and clients must treat individual objects and object collections uniformly.
* **Detailed Architectural Reason:** If your client code needs to handle primitive elements differently from branch elements, it becomes littered with conditional type-checking statements (`if (node instanceof Leaf)`). The Composite pattern eliminates this branching by forcing both individual primitives and composite containers to implement the same interface. The composite node fulfills operations by iterating over its child components recursively, allowing clients to ignore the differences between single objects and collections.

#### Java Example
```java
import java.util.ArrayList;
import java.util.List;

interface GraphicElement {
    void render();
}

class Shape implements GraphicElement {
    private final String name;
    public Shape(String name) { this.name = name; }
    @Override
    public void render() { System.out.println("Rendering Shape: " + name); }
}

class GroupNode implements GraphicElement {
    private final List<GraphicElement> children = new ArrayList<>();
    
    public void add(GraphicElement element) { children.add(element); }
    
    @Override
    public void render() {
        System.out.println("Entering Group Node...");
        for (GraphicElement child : children) {
            child.render();
        }
    }
}
```

---

## 2. Creational Patterns (Object Generation)

### The Abstract Factory Pattern
* **When to Use:** Use when a system must remain independent of how its products are created, and needs to be configured with one of multiple variant families of related or dependent objects.
* **Detailed Architectural Reason:** Hardcoding specific object variations throughout an application introduces tight coupling and makes it difficult to switch look-and-feel, runtime environments, or platforms. The Abstract Factory provides an interface for creating families of related objects without specifying their concrete classes. By instantiating a specific concrete factory at application startup, you guarantee that your code consistently uses objects from exactly one compatible variant family at a time.

#### Java Example
```java
interface Button { void paint(); }
interface ScrollBar { void scroll(); }

interface GUIFactory {
    Button createButton();
    ScrollBar createScrollBar();
}

class DarkThemeFactory implements GUIFactory {
    public Button createButton() { return () -> System.out.println("Dark Button"); }
    public ScrollBar createScrollBar() { return () -> System.out.println("Dark ScrollBar"); }
}

class LightThemeFactory implements GUIFactory {
    public Button createButton() { return () -> System.out.println("Light Button"); }
    public ScrollBar createScrollBar() { return () -> System.out.println("Light ScrollBar"); }
}
```

---

### The Builder Pattern
* **When to Use:** Use when the creation of a complex object involves a large number of parameters, many of which are optional, or when the initialization process must follow a strict sequential order.
* **Detailed Architectural Reason:** Relying on single constructors with long parameter lists results in a bad design known as "telescoping constructors." This code is difficult to read and error-prone because developers can easily swap identical data types (like mixing up two adjacent String parameters). The Builder pattern isolates complex assembly logic into a dedicated inner builder class, facilitating step-by-step configuration and ensuring that the final immutable object is only generated when valid.

#### Java Example
```java
class ConfigurationProfile {
    private final String profileName; // Required
    private final boolean enableLogging; // Optional
    private final int maxConnections; // Optional

    private ConfigurationProfile(Builder builder) {
        this.profileName = builder.profileName;
        this.enableLogging = builder.enableLogging;
        this.maxConnections = builder.maxConnections;
    }

    public static class Builder {
        private final String profileName;
        private boolean enableLogging = false;
        private int maxConnections = 10;

        public Builder(String profileName) { this.profileName = profileName; }
        public Builder withLogging(boolean enable) { this.enableLogging = enable; return this; }
        public Builder withMaxConnections(int limit) { this.maxConnections = limit; return this; }
        public ConfigurationProfile build() { return new ConfigurationProfile(this); }
    }
}
```

---

## 3. Behavioral Patterns (Process Interaction)

### The Strategy Pattern
* **When to Use:** Use when you have a family of interchangeable algorithms or behaviors, and you need to dynamically select or switch between them at runtime based on the execution context.
* **Detailed Architectural Reason:** Embedding multiple variations of an algorithm directly inside an execution block requires complex `if-else` or `switch` branches. This design violates the Open/Closed Principle because adding a new algorithm requires modifying and recompiling the host class. The Strategy pattern extracts these algorithms into distinct classes that implement a unified interface, allowing the host context to remain completely agnostic of the chosen algorithmic mechanics.

#### Java Example
```java
interface CompressionStrategy {
    void compress(String filePath);
}

class ZipCompression implements CompressionStrategy {
    @Override public void compress(String path) { System.out.println("Zipping: " + path); }
}

class RarCompression implements CompressionStrategy {
    @Override public void compress(String path) { System.out.println("Rar-ing: " + path); }
}

class ContextManager {
    private CompressionStrategy strategy;
    public void setStrategy(CompressionStrategy strategy) { this.strategy = strategy; }
    public void executeCompression(String file) { strategy.compress(file); }
}
```

---

### The Chain of Responsibility Pattern
* **When to Use:** Use when more than one object can handle a request, and you want to avoid coupling the sender to a specific receiver by passing the request along a pipeline of potential handlers.
* **Detailed Architectural Reason:** When a request requires sequential, multi-stage processing or parsing, coding the entire processing flow inside a single coordinator class couples it to every downstream processor. The Chain of Responsibility decouples this by turning each processing phase into an independent link object. Each link contains a reference to its successor in the chain; it handles its assigned part of the request and decides whether to stop processing or pass the data along to the next node.

#### Java Example
```java
abstract class LogHandler {
    protected LogHandler nextHandler;
    public void setNext(LogHandler nextHandler) { this.nextHandler = nextHandler; }
    public abstract void processMessage(int level, String message);
}

class CriticalErrorHandler extends LogHandler {
    @Override
    public void processMessage(int level, String message) {
        if (level >= 3) System.out.println("CRITICAL ERROR: " + message);
        else if (nextHandler != null) nextHandler.processMessage(level, message);
    }
}

class InfoLogHandler extends LogHandler {
    @Override
    public void processMessage(int level, String message) {
        if (level < 3) System.out.println("INFO LOG: " + message);
        else if (nextHandler != null) nextHandler.processMessage(level, message);
    }
}
```

---

### The Observer Pattern
* **When to Use:** Use when a one-to-many dependency exists between objects, such that when one central object changes its state, all its dependent objects are notified and updated automatically.
* **Detailed Architectural Reason:** Hardcoding explicit method invocations from a single publisher object into multiple downstream subscriber systems introduces massive cross-dependencies. If a subscriber system needs to be added, modified, or removed, the publisher class must be rewritten and re-tested. The Observer pattern breaks this coupling by providing a register/unregister subscription interface, allowing any number of listening systems to receive broadcasts without the publisher knowing their concrete class types.

#### Java Example
```java
import java.util.ArrayList;
import java.util.List;

interface SystemObserver { void update(String systemState); }

class DashboardService implements SystemObserver {
    @Override public void update(String state) { System.out.println("Dashboard UI adjusted to: " + state); }
}

class DiagnosticsLogger implements SystemObserver {
    @Override public void update(String state) { System.out.println("Diagnostic file logged state: " + state); }
}

class StatePublisher {
    private final List<SystemObserver> observers = new ArrayList<>();
    
    public void register(SystemObserver obs) { observers.add(obs); }
    public void unregister(SystemObserver obs) { observers.remove(obs); }
    
    public void changeState(String newState) {
        System.out.println("Publisher state changed to: " + newState);
        for (SystemObserver obs : observers) {
            obs.update(newState);
        }
    }
}
```

---

## Pattern Strategy Summary Matrix

| General Engineering Problem | Optimal Design Pattern | Core Architectural Strategy |
| :--- | :--- | :--- |
| **Additive runtime feature compilation without structural inheritance** | **Decorator** | Wrap individual objects inside transparent behavioral layers uniformly. |
| **Recursive part-whole structural hierarchy parsing** | **Composite** | Implement a shared base interface for single objects and nested groupings. |
| **Runtime safety boundaries across distinct component groups** | **Abstract Factory** | Consolidate object instantiation points behind a uniform context-driven engine. |
| **Sequential, error-free setup of customizable configuration entities** | **Builder** | Separate data schema validation from structural object creation parameters. |
| **Runtime isolation and dynamic replacement of complete algorithms** | **Strategy** | Encapsulate mathematical calculations and behaviors into discrete strategy classes. |
| **Decoupled linear request processing pipelines and quick verification aborts** | **Chain of Responsibility** | Link individual handler classes end-to-end and process sequentially. |
| **Asynchronous state broadcast signaling with zero explicit downstream binding** | **Observer** | Decouple publisher notifications using a dynamic registration registry array. |