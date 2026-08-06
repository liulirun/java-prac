- **Identity vs. Capability:** Historically, abstract classes defined *what an object is* (identity), while interfaces defined *what an object can do* (capability).

When to use Abstract Classes

- **Share code** among closely related classes.
- **Need non-static or non-final fields** to maintain an object's state.
- **Need non-public members** like `protected` methods for internal subclass access.
- **Want to declare constructors** to initialize base state upon subclass creation.

When to use Interfaces

- **Define contracts** for completely unrelated classes.
- **Support multiple inheritance** of behavior.
- **Expect the API to remain stable** because you can safely add `default` methods later.
- **Want to achieve loose coupling** between components.

> 💡 **Pro Tip:** A class can extend only one abstract class but implement multiple interfaces.

🚀 Java 21 Added Value: Sealed Interfaces & Pattern Matching

In **Java 21**, the boundary between abstract classes and interfaces has shifted due to **Sealed Types** and **Pattern Matching for `switch`**.

Instead of using a traditional abstract class hierarchy to model tightly coupled data variations, the modern Java 21 approach uses a `sealed interface` combined with `record` types. This guarantees **compile-time exhaustiveness checks** during pattern matching.

```java
// Define a strict, closed hierarchy using a sealed interface
public sealed interface Response permits Success, Failure {}

// Implement using lightweight, immutable records
public record Success(String data) implements Response {}
public record Failure(String errorMessage) implements Response {}

// Utilize Java 21 Pattern Matching for switch (No "default" branch required!)
public String handleResponse(Response response) {
    return switch (response) {
        case Success s -> "Data received: " + s.data();
        case Failure f -> "Error occurred: " + f.errorMessage();
    };
}
```

Why this matters for your design choices:

- **Identity vs. Capability:** Historically, abstract classes defined *what an object is* (identity), while interfaces defined *what an object can do* (capability).
- **The Modern Twist:** In Java 21, `sealed interfaces` are now highly preferred for defining **data structures and domain models** (Algebraic Data Types) because they offer the architectural control of an abstract class without sacrificing your single inheritance slot.