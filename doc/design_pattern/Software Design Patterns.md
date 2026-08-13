# Software Design Patterns

for me to understand, I need 
- 1 markdown table to compare good or bad, and what problem this pattern actually solve. in short, not academic words. 
- how to implement it,  Java Key Instructions (How to Code It)
- short key instructions about how to use it (per JAVA)
- short When to use this pattern
- Separate "How Things Are Arranged" from "How Things Are Executed"

The Three Steps to Look For in Every Pattern

Every clean design pattern you look at from now on will follow this exact same three-part choreography:

```text
  [ STEP 1: STARTUP SETUP ]          [ STEP 2: DEPENDENCY INJECTION ]          [ STEP 3: BLIND EXECUTION ]
   Ask the if/else questions          Pass the configured tool down              The business logic runs.
   ONCE to build your chain           into your main packages using              It asks NO questions and
   or factory family.                 a generic interface label.                 never changes its code lines.

           |                                         |                                         |
           v                                         v                                         v
   "We are on Level 1, so             "Here is your generic                     "CurrentEnemy.attack();"
   spawn a ZombieSpawner!"             SpawnerInterface tool."                   (Works blindly!)
```

Your Scanning Checklist for New Patterns ( examples for BUILDER design pattern)

When you study your next design pattern, ignore the academic jargon and focus strictly on finding these three elements:

1. **Where is the Interface "Mask"?** Find the generic parent type that hides the concrete subclass names from the rest of the application.
2. **Where is the Single `if/else` Choice?** Find the configuration block at startup where the app decides *which* concrete class to create based on environment settings.
3. **Where is the Blind Execution Loop?** Find the core method that stays completely frozen and untouched, even when you drastically change how the program behaves from the outside.

If you can spot those three pieces, you understand the pattern completely.



## Pattern Quick-Selection Matrix
| If you are trying to...                                                   | Use this pattern!           | Because it... (Simplified)                                                                                                                                          | How to implement it (Key Points)                                                                                                                                                                   |
| ------------------------------------------------------------------------- | --------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Stack infinite optional features onto an object**                       | **Decorator**               | Adds features like clothing layers. You wrap a base object (e.g., plain coffee) with wrappers (e.g., milk, sugar) without changing the original class.              | • Share a common interface. • Wrapper class holds an instance of that interface. • Wrapper forwards methods to the instance and adds its own behavior.                                             |
| **Handle a whole tree or nested folder group exactly like a single item** | **Composite**               | Treats a single file and a folder full of files exactly the same way. You can trigger a command on the folder, and it automatically trickles down.                  | • Create a component interface for both single items and groups. • The "Group" class contains a `List` of that interface. • The group loops through the list to execute methods.                   |
| **Create matching sets of styled components safely**                      | **Abstract Factory**        | Acts as a factory of factories. It ensures you don't accidentally mix Dark Mode buttons with Light Mode scrollbars.                                                 | • Define a factory interface with methods for each product type. • Create concrete factories for each theme/family. • Return objects via interfaces, never concrete classes.                       |
| **Create complex configurations with tons of optional settings**          | **Builder**                 | Replaces huge, ugly constructors with step-by-step method chaining (e.g., `setAge().setName().build()`).                                                            | • Create a static inner `Builder` class. • Give it the same fields as the main class. • Methods return `this` (the builder) for chaining. • Add a `.build()` method that returns the final object. |
| **Swap out calculation formulas or data logic on the fly**                | **Strategy**                | Swaps algorithms like video game cartridges. You can switch from Credit Card to PayPal payment instantly without rewriting the checkout class.                      | • Define an interface for the algorithm. • Implement concrete classes for each variant. • Context class stores a reference to the interface and executes it.                                       |
| **Pass a data request down a validation assembly line**                   | **Chain of Responsibility** | Works like a corporate approval chain. It passes a request along a line of handlers until one either processes it or blocks it (e.g., Auth → Validation → Logging). | • Create an abstract handler class. • Give it a reference to the `next` handler. • Implement a method that processes the task or forwards it to `next`.                                            |
| **Alert multiple unrelated background tasks when an event triggers**      | **Observer**                | Works like a YouTube subscription. When a channel posts a video (Subject), all subscribers (Observers) get a notification automatically.                            | • Subject maintains a `List<Observer>`. • Subject has `attach()`, `detach()`, and `notifyObservers()` methods. • Observers implement an `update()` method triggered by the notification.           |


## 1. Structural Patterns (System Composition)

### The Decorator Pattern
* **When to Use (The Rule):** Use this when you want to stack multiple optional features or behaviors onto an object at runtime (like adding toppings to a pizza).
* **Why to Use (The Reason):** If you try to use normal inheritance (subclasses) to handle every combination of features, you will end up creating too many files (e.g., `EncryptedData`, `CompressedData`, `EncryptedAndCompressedData`). The Decorator pattern lets you wrap an object inside another object like nesting Russian dolls. You can stack as many wrappers as you want at runtime without creating new classes.

#### Ready-to-Run Java Code
```java
// Save this file as: DecoratorPatternDemo.java
// Run command: java DecoratorPatternDemo.java

import java.util.*;

// 1. The common interface that both the core object and the wrappers must implement
interface DataStream {
    String read();
}

// 2. The core object that handles the basic task
class SimpleTextStream implements DataStream {
    @Override
    public String read() {
        return "Raw Text Data";
    }
}

// 3. The base wrapper class. It holds a reference to the inner object it is wrapping
abstract class StreamDecorator implements DataStream {
    protected DataStream wrappedStream; // This is the inner doll

    public StreamDecorator(DataStream stream) {
        this.wrappedStream = stream;
    }
}

// 4. A concrete wrapper that adds Encryption capability
class EncryptionDecorator extends StreamDecorator {
    public EncryptionDecorator(DataStream stream) {
        super(stream);
    }

    @Override
    public String read() {
        // HOW IT WORKS: It gets data from the inner object, then modifies it
        return "Encrypted(" + wrappedStream.read() + ")";
    }
}

// 5. Another concrete wrapper that adds Compression capability
class CompressionDecorator extends StreamDecorator {
    public CompressionDecorator(DataStream stream) {
        super(stream);
    }

    @Override
    public String read() {
        // HOW IT WORKS: It can wrap around the raw data OR around an already encrypted object!
        return "Compressed(" + wrappedStream.read() + ")";
    }
}

// Main class containing the entry point
public class DecoratorPatternDemo {
    public static void main(String[] args) {
        System.out.println("--- Decorator Pattern Running ---");

        // Step A: Create a plain, unwrapped object
        DataStream baseStream = new SimpleTextStream();
        System.out.println("Base: " + baseStream.read());

        // Step B: Wrap it with encryption dynamically at runtime
        DataStream secureStream = new EncryptionDecorator(baseStream);
        System.out.println("Wrapped 1x: " + secureStream.read());

        // Step C: Stack another wrapper (compression) on top of the encrypted object
        DataStream ultraSecureStream = new CompressionDecorator(secureStream);
        System.out.println("Wrapped 2x: " + ultraSecureStream.read());
    }
}
```

---

### The Composite Pattern
* **When to Use (The Rule):** Use this when you are dealing with tree structures (like folders containing files and other subfolders) and you want to treat individual items and groups of items exactly the same way.
* **Why to Use (The Reason):** Without this pattern, your code would be filled with messy `if/else` checks to find out if something is a single file or a folder container. The Composite pattern forces both single items ("leaves") and containers ("composites") to share the exact same interface. When you call a command on a container, it automatically loops through its children and runs the command on them, saving you from writing recursive loop logic in your main code.

#### Ready-to-Run Java Code
```java
// Save this file as: CompositePatternDemo.java
// Run command: java CompositePatternDemo.java

import java.util.ArrayList;
import java.util.List;

// 1. The shared interface for both single items and item groups
interface FileSystemNode {
    void printDetails();
}

// 2. The Leaf object: represents a single item that cannot contain anything else
class IndividualFile implements FileSystemNode {
    private final String fileName;

    public IndividualFile(String name) {
        this.fileName = name;
    }

    @Override
    public void printDetails() {
        System.out.println("File: " + fileName);
    }
}

// 3. The Composite object: represents a container that holds a list of items (both files and other folders)
class FolderContainer implements FileSystemNode {
    private final String folderName;
    // HOW IT WORKS: It stores references to the interface type, so it can hold files OR other folders!
    private final List<FileSystemNode> children = new ArrayList<>();

    public FolderContainer(String name) {
        this.folderName = name;
    }

    public void addNode(FileSystemNode node) {
        children.add(node);
    }

    @Override
    public void printDetails() {
        System.out.println("-> Entering Folder: " + folderName);
        // HOW IT WORKS: It passes the work down to every child item inside it automatically
        for (FileSystemNode node : children) {
            node.printDetails();
        }
    }
}

// Main class containing the entry point
public class CompositePatternDemo {
    public static void main(String[] args) {
        System.out.println("--- Composite Pattern Running ---");

        // Create individual file nodes (Leaves)
        FileSystemNode file1 = new IndividualFile("Notes.txt");
        FileSystemNode file2 = new IndividualFile("Photo.png");
        FileSystemNode file3 = new IndividualFile("Index.html");

        // Create directory structures (Composites)
        FolderContainer rootFolder = new FolderContainer("Root");
        FolderContainer projectFolder = new FolderContainer("CodeProjects");

        // Assemble the hierarchy tree layout
        projectFolder.addNode(file3); // Add file to subfolder
        rootFolder.addNode(file1);    // Add file to root
        rootFolder.addNode(file2);    // Add file to root
        rootFolder.addNode(projectFolder); // Add subfolder into root folder!

        // HOW IT WORKS: You treat the whole tree structural group exactly like a single file node!
        rootFolder.printDetails();
    }
}
```

---

## 2. Creational Patterns (Object Generation)
### The Abstract Factory Pattern
Here is the updated markdown matrix, fully expanded to include **Dependency Injection (DI)** so you can see exactly how the interface, the factory, and DI work together as a team to keep your code clean.

| Feature                    | 🔴 Bad Version (No Factory / No DI)                                                  | 🟢 Good Version (Abstract Factory + DI)                                                           |
| -------------------------- | ------------------------------------------------------------------------------------ | ------------------------------------------------------------------------------------------------- |
| **The `if/else` Check**    | Scattered everywhere across multiple files and packages.                             | Done **exactly once** at the very beginning of startup.                                           |
| **Passing Objects Around** | Files instantiate their own dependencies internally using the `new` keyword.         | The active factory is **injected** through the constructor front door using an interface label.   |
| **Variable Type**          | Hardcoded to one specific brand class (e.g., `ObjectAClass`).                        | Generic parent **Interface** type (e.g., `ProductInterface`).                                     |
| **Component Mix-ups**      | High risk. Easy to accidentally mix an AWS database with an Azure storage bucket.    | Impossible. The active factory subclass physically restricts object creation to one brand family. |
| **Adding New Brand**       | Nightmare. You must hunt down and rewrite `if/else` loops in every file.             | Simple. Drop in 1 new factory subclass + add 1 option line at startup.                            |
| **Testing Code**           | Extremely hard. Runs real, expensive cloud code because classes are welded together. | Fast and free. You can inject a `FakeFactory` into your files without changing any core logic.    |

Summary of the Teamwork

- **The Interface** acts as the universal plug (the mask).
- **The Abstract Factory** isolates the creation logic and handles the `if/else` choice at startup.
- **Dependency Injection (DI)** is the delivery person that passes that chosen factory into your other packages without using any extra switches.

Would you like to explore how this teamwork applies to a new design pattern like the **Observer (Event Listener)** pattern, or do you want to break down another aspect of this chart?

Here is the same explanation written in the simplest way possible, using markdown to make it easy to read.

❌ The Problem: Life WITHOUT the Factory (The Messy Way)

Imagine your app needs to work with different companies like **AWS** and **Azure**.

Without a factory, you are forced to write `if/else` questions **every single time** you want to make an object, all over your code:

- *File 1 (Database):* "Are we using AWS? If yes, `new AwsDatabase()`. Else, `new AzureDatabase()`."
- *File 2 (Storage):* "Are we using AWS? If yes, `new AwsStorage()`. Else, `new AzureStorage()`."
- *File 3 (Security):* "Are we using AWS? If yes, `new AwsSecurity()`. Else, `new AzureSecurity()`."

Why this is bad:

- **`if/else` is everywhere:** Your code becomes flooded with the same repetitive questions.
- **Hard to update:** If you want to add **Google Cloud** tomorrow, you have to go hunting through every single file to manually add an `else if` line.

The Solution: Life WITH the Factory (The Smart Way)

With the Abstract Factory pattern, you only ask the `if/else` question **exactly once** at the very beginning when your app boots up.

1. **Step 1 (The Choice):** At startup, the app checks your settings and picks **one** concrete child factory class to activate.
   ```java
   // The question is asked ONCE right here:
   CloudFactory myFactory = new AwsFactory(); 
   ```
2. **Step 2 (Blind Trust):** For the rest of the day, the rest of your app completely ignores `if/else`. Whenever it needs something, it blindly commands the factory to do it:
   ```java
   myFactory.createDatabase();
   myFactory.createStorage();
   ```

Why this is great:

- **The Main App is blind:** The core code does not know (and does not care) if it is using AWS or Azure. It just trusts the factory interface to hand over the correct items.
- **Perfect matching:** It is impossible to accidentally mix up your parts. You will never accidentally hook up an AWS Database to Azure Storage because the active factory *only* knows how to make AWS parts.

📋 Summary Comparison

- **No Factory:**

  - Massive `if/else` blocks scattered across dozens of files.
  - High risk of accidentally mixing matching parts.

- **With Factory:**

  - Exactly **one** `if/else` check at the very beginning.
  - Clean, brand-free code everywhere else that uses generic interface variables.

If you are ready to see how this applies to another concept, would you like to see how a **Video Game Engine** uses this exact trick to switch between **PlayStation** and **Xbox** controllers, or should we look at how to **safely change factories at runtime**?

#### Ready-to-Run Java Code
```java
// Save this file as: AbstractFactoryDemo.java
// Run command: java AbstractFactoryDemo.java

// 1. Define the abstract product families
interface InterfaceButton { void render(); }
interface InterfaceWindow { void open(); }

// 2. Abstract Factory Interface: Forces factories to provide an entire set of matching tools
interface ThemeFactory {
    InterfaceButton createButton();
    InterfaceWindow createWindow();
}

// 3. Concrete Family A: Dark Theme Parts
class DarkButton implements InterfaceButton { public void render() { System.out.println("[X] Dark Style Button Rendered"); } }
class DarkWindow implements InterfaceWindow { public void open() { System.out.println("=== Dark Theme Window Open ==="); } }

class DarkThemeFactory implements ThemeFactory {
    public InterfaceButton createButton() { return new DarkButton(); }
    public InterfaceWindow createWindow() { return new DarkWindow(); }
}

// 4. Concrete Family B: Light Theme Parts
class LightButton implements InterfaceButton { public void render() { System.out.println("[O] Light Style Button Rendered"); } }
class LightWindow implements InterfaceWindow { public void open() { System.out.println("=== Light Theme Window Open ==="); } }

class LightThemeFactory implements ThemeFactory {
    public InterfaceButton createButton() { return new LightButton(); }
    public InterfaceWindow createWindow() { return new LightWindow(); }
}

// Main class containing the entry point
public class AbstractFactoryDemo {
    public static void main(String[] args) {
        System.out.println("--- Abstract Factory Running ---");

        // Simulated Configuration: Decide the UI variant style at runtime
        boolean userPrefersDarkMode = true;
        ThemeFactory appFactory;

        // HOW IT WORKS: The application picks one concrete factory class at setup
        if (userPrefersDarkMode) {
            appFactory = new DarkThemeFactory();
        } else {
            appFactory = new LightThemeFactory();
        }

        // The application code uses the factory interface blindly to create matching sets
        InterfaceWindow window = appFactory.createWindow();
        InterfaceButton button = appFactory.createButton();

        window.open();
        button.render();
    }
}
```

---

### The Builder Pattern
Here is a clean, scannable comparison table comparing the **Anti-Pattern (Bad)** approach with the **Builder Pattern (Good)** approach.

Builder Pattern Comparison

| Feature             | ❌ Anti-Pattern (Telescoping / Dirty Constructors)             | Builder Pattern (Chained Configurator)                         |
| ------------------- | ------------------------------------------------------------- | -------------------------------------------------------------- |
| **Readability**     | `new Profile("Amy", null, true, 0, "dark");`                  | `new Profile.Builder("Amy").withTheme("dark").build();`        |
| **Optional Params** | Forces passing `null`, `0`, or `false` placeholders.          | Only call methods for the specific parameters you need.        |
| **Parameter Order** | Extremely easy to accidentally swap adjacent strings.         | Order does not matter; each method explicitly names the field. |
| **Immutability**    | Requires mutable `setter` methods to avoid huge constructors. | Keeps fields `final` and immutable after creation.             |
| **Validation**      | Data checked across multiple scattered setter methods.        | Centralized verification happens safely inside `.build()`.     |

1\. Static Inner Class

- **Why static:** It must be `static` so the client can instantiate it *before* the parent class exists (e.g., `new SystemProfile.Builder()`).
- **Why inner:** It has unique access privileges. It can read and modify the parent class’s `private` constructor and fields.

2\. Matching Attributes

- **Why duplicate fields:** The `Builder` acts as a temporary workspace. It holds your configurations step-by-step.
- **The Hand-off:** Once your settings are finalized, the Builder passes its complete set of attributes into the parent's constructor at the very end when you call `.build()`.


* **When to Use (The Rule):** need optional config.
* **Why to Use (The Reason):** Relying on huge class constructors like `new UserProfile("John", null, 0, true, "admin", null)` is confusing, ugly, and highly error-prone. You can easily accidentally swap the order of adjacent text values. The Builder pattern moves all the configuration properties to a dedicated, step-by-step assistant object. You configure only the parameters you actually need using clean, readable chained methods, and the final immutable object is safely created only at the very end when you call `.build()`.

📊 The Real Problem It Solves

| Feature              | ❌ The Bad Way (Giant Setup)                                                      | The Builder Pattern                                                      |
| -------------------- | -------------------------------------------------------------------------------- | ------------------------------------------------------------------------ |
| **Handling Options** | Forces you to provide placeholders like `null` or `0` for things you don't need. | You completely ignore things you don't need.                             |
| **Ordering**         | A strict, rigid line of choices where one misstep ruins the whole setup.         | Highly flexible; you configure items in whatever order makes sense.      |
| **Safety**           | Creating half-finished or broken objects is easy.                                | The final object cannot be made until everything is verified at the end. |

**The Problem Solved:** It eliminates rigid, confusing, and error-prone setup lines by breaking a complex assembly job into safe, named, step-by-step decisions.

🔑 Java Key Instructions (How to Code It)

1. **Hide the Parent:** Make the main class constructor `private`. This blocks anyone from creating the object the old, messy way.
2. **Nest the Assistant:** Create a `public static class Builder` inside the main class that mirrors the fields you need to configure.
3. **Chain with `this`**: Every configuration method in the builder must return `this` (the builder itself). This enables dot-chaining (`.step1().step2()`).
4. **Deliver with `build()`**: End the builder class with a `public MainClass build()` method that passes the builder's gathered data into the private parent constructor.

⏱️ When to Use This Pattern

Use this pattern when an object has **more than three optional configuration settings**, or when creating the object requires an incremental, step-by-step process where order shouldn't matter to the person writing the code.

#### Ready-to-Run Java Code
```java
// Save this file as: BuilderPatternDemo.java
// Run command: java BuilderPatternDemo.java

class ServerConfig {
    // All variables are final, making the object safely immutable after creation
    private final String serverName;  // Required field
    private final int portNumber;     // Optional field (has default)
    private final boolean sslEnabled; // Optional field (has default)

    // Private constructor ensures objects can ONLY be made through the helper Builder class
    private ServerConfig(Builder builder) {
        this.serverName = builder.serverName;
        this.portNumber = builder.portNumber;
        this.sslEnabled = builder.sslEnabled;
    }

    public void showConfig() {
        System.out.println("Server: " + serverName + " | Port: " + portNumber + " | SSL: " + sslEnabled);
    }

    // The inner Builder assistant class
    public static class Builder {
        private final String serverName; // Required parameters go here
        private int portNumber = 8080;   // Default value for optional field
        private boolean sslEnabled = false; // Default value for optional field

        // Constructor enforces required values up-front
        public Builder(String serverName) {
            this.serverName = serverName;
        }

        // Chained configuration methods return 'this' (the current builder instance)
        public Builder withPort(int port) {
            this.portNumber = port;
            return this;
        }

        public Builder enableSSL(boolean useSSL) {
            this.sslEnabled = useSSL;
            return this;
        }

        // The final method that validates rules and outputs the real ServerConfig object
        public ServerConfig build() {
            return new ServerConfig(this);
        }
    }
}

// Main class containing the entry point
public class BuilderPatternDemo {
    public static void main(String[] args) {
        System.out.println("--- Builder Pattern Running ---");

        // HOW IT WORKS: Build a basic setup using defaults
        ServerConfig simpleConfig = new ServerConfig.Builder("Production-Server").build();
        
        // HOW IT WORKS: Use readable method chains to build a custom complex setup step-by-step
        ServerConfig customConfig = new ServerConfig.Builder("Secure-API")
                                                .withPort(443)
                                                .enableSSL(true)
                                                .build();

        simpleConfig.showConfig();
        customConfig.showConfig();
    }
}
```

---

## 3. Behavioral Patterns (Process Interaction)

### The Strategy Pattern

📊 Good vs. Bad Comparison

| Feature                     | 🔴 Bad Version (No Strategy)                                                                                                               | 🟢 Good Version (With Strategy)                                                         |
| --------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------ | --------------------------------------------------------------------------------------- |
| **How Code Thinks**         | *"Let me look at this list of choices, run an `if/else` test, and do the heavy math right here."*                                          | *"I will blindly pass the data to my injected helper tool and let it handle the math."* |
| **Adding a New Rule**       | You must open up your core logic file and insert a new `else if` block.                                                                    | You leave the core file completely alone and just write one new standalone class file.  |
| **The Core Problem Solved** | **Bloated, multi-line nested math or logic loops.** It stops one single file from becoming a thousand lines of messy conditional formulas. |                                                                                         |

🕒 When to Use This Pattern

Use this pattern when you have **one single goal** (like calculating a price, sorting data, or moving a character), but there are **multiple different ways or formulas** to get the job done, and your program needs to choose the right formula dynamically.

Step 1: Create the Strategy Interface

Write an interface that defines the generic action method.

```java
interface ActionStrategy {
    int executeFormula(int input);
}
```

Step 2: Create the Concrete Subclasses

Write separate classes for each different formula. They must all implement that interface.

```java
class FormulaA implements ActionStrategy {
    public int executeFormula(int input) { return input * 2; }
}

class FormulaB implements ActionStrategy {
    public int executeFormula(int input) { return input + 5; }
}
```

Step 3: Pass it into the Main Class (Dependency Injection)

Create a main context class that holds the generic interface variable. It receives the chosen formula through its constructor and triggers it blindly.

```java
class ContextApp {
    private ActionStrategy currentStrategy;

    // Pass the chosen strategy through the front door
    public ContextApp(ActionStrategy strategy) {
        this.currentStrategy = strategy;
    }

    public void runLogic(int value) {
        // Blind execution! No if/else questions asked.
        int result = currentStrategy.executeFormula(value);
        System.out.println("Result: " + result);
    }
}
```

#### Ready-to-Run Java Code
```java
// Save this file as: StrategyPatternDemo.java
// Run command: java StrategyPatternDemo.java

// 1. The common interface for all alternative variations
interface TextFormatter {
    String formatText(String rawInput);
}

// 2. Strategy Option A: Uppercase format conversion
class UpperCaseStrategy implements TextFormatter {
    @Override
    public String formatText(String rawInput) {
        return rawInput.toUpperCase();
    }
}

// 3. Strategy Option B: Lowercase format conversion
class LowerCaseStrategy implements TextFormatter {
    @Override
    public String formatText(String rawInput) {
        return rawInput.toLowerCase();
    }
}

// 4. The Context class that accepts and executes whatever strategy is injected into it
class ProcessingContext {
    private TextFormatter activeStrategy;

    // HOW IT WORKS: Factory Injection point allows swapping strategies at any time
    public void setStrategy(TextFormatter strategy) {
        this.activeStrategy = strategy;
    }

    public void outputMessage(String message) {
        if (activeStrategy == null) {
            System.out.println(message); // No conversion if strategy isn't set
        } else {
            // Run the formula on the fly
            System.out.println(activeStrategy.formatText(message));
        }
    }
}

// Main class containing the entry point
public class StrategyPatternDemo {
    public static void main(String[] args) {
        System.out.println("--- Strategy Pattern Running ---");

        ProcessingContext context = new ProcessingContext();
        String sampleText = "Hello Junior Developer";

        // HOW IT WORKS: Inject the Uppercase formula strategy at runtime
        context.setStrategy(new UpperCaseStrategy());
        context.outputMessage(sampleText);

        // HOW IT WORKS: Dynamically switch to the Lowercase strategy without altering context code
        context.setStrategy(new LowerCaseStrategy());
        context.outputMessage(sampleText);
    }
}
```

---

### The Chain of Responsibility Pattern
* **When to Use (The Rule):** Use this when a request needs to go through a linear pipeline of multiple verification checkpoints, where any link in the chain can choose to handle it or block it.
* **Why to Use (The Reason):** Hardcoding sequence logic inside a single manager module forces that file to be tightly coupled to every single validation dependency. The Chain of Responsibility decouples this completely by separating each verification checkpoint into its own self-contained link class. Each link only tracks the immediate item directly behind it, processing its part of the job and deciding whether to pass the task forward or break the chain early.

#### Ready-to-Run Java Code
```java
// Save this file as: ChainPatternDemo.java
// Run command: java ChainPatternDemo.java

// 1. Abstract node representing a link in the processing pipeline chain
abstract class SecurityCheck {
    protected SecurityCheck nextLink;

    // Links objects end-to-end to assemble the chain pipeline
    public void setNext(SecurityCheck next) {
        this.nextLink = next;
    }

    // Abstract method every validation link must implement
    public abstract boolean verify(String userRole, String dataPayload);
}

// 2. First Checkpoint Link: Verifies access rights
class AuthenticationCheck extends SecurityCheck {
    @Override
    public boolean verify(String userRole, String dataPayload) {
        System.out.println("Step 1: Running Identity Authentication...");
        if (!userRole.equals("ADMIN")) {
            System.out.println(">> REJECTED at Step 1: User is not an administrator!");
            return false; // Break the chain execution immediately
        }
        
        if (nextLink == null) return true; // End of chain reached safely
        return nextLink.verify(userRole, dataPayload); // Pass along to the next step
    }
}

// 3. Second Checkpoint Link: Cleanses incoming text payload strings
class SanitizationCheck extends SecurityCheck {
    @Override
    public boolean verify(String userRole, String dataPayload) {
        System.out.println("Step 2: Checking payload strings for harmful script code...");
        if (dataPayload.contains("<script>")) {
            System.out.println(">> REJECTED at Step 2: Harmful injection code detected!");
            return false; // Break execution immediately
        }
        
        if (nextLink == null) return true;
        return nextLink.verify(userRole, dataPayload);
    }
}

// Main class containing the entry point
public class ChainPatternDemo {
    public static void main(String[] args) {
        System.out.println("--- Chain of Responsibility Running ---");

        // Instantiate independent validator checkpoint blocks
        SecurityCheck step1 = new AuthenticationCheck();
        SecurityCheck step2 = new SanitizationCheck();

        // Connect the separate modules together to form the chain
        step1.setNext(step2);

        System.out.println("\n--- Test Case A: Valid Input Payload ---");
        boolean resultA = step1.verify("ADMIN", "Clean safe data profile text.");
        System.out.println("Final System Access Approved? " + resultA);

        System.out.println("\n--- Test Case B: Malicious Payload ---");
        boolean resultB = step1.verify("ADMIN", "Payload containing an exploit <script> block!");
        System.out.println("Final System Access Approved? " + resultB);
    }
}
```

---

### The Observer Pattern
* **When to Use (The Rule):** Use this when something important changes state inside a central component, and multiple separate background modules need to be instantly updated or notified.
* **Why to Use (The Reason):** If your central processing object contains explicit, hardcoded method calls to background logging engines, metrics counters, and email senders, modifying or removing any downstream service breaks the publisher class. The Observer pattern breaks this coupling by adding a generic array subscription registry. Subscribing listener classes can plug into or unplug from the registry array dynamically, allowing the central publisher to broadcast updates seamlessly without knowing who is listening.

#### Ready-to-Run Java Code
```java
// Save this file as: ObserverPatternDemo.java
// Run command: java ObserverPatternDemo.java

import java.util.ArrayList;
import java.util.List;

// 1. The generic interface that all listeners must implement
interface AlertSubscriber {
    void onStatusChange(String updatedStatus);
}

// 2. Concrete Background Listener A
class LogModule implements AlertSubscriber {
    @Override
    public void onStatusChange(String updatedStatus) {
        System.out.println("[LogService] Writing event timestamp to disk for: " + updatedStatus);
    }
}

// 3. Concrete Background Listener B
class UserAlertModule implements AlertSubscriber {
    @Override
    public void onStatusChange(String updatedStatus) {
        System.out.println("[AlertService] Broadcasting toast window banner alert to active sessions: " + updatedStatus);
    }
}

// 4. The Central Core Subject that broadcasts state shifts
class CoreStatePublisher {
    // HOW IT WORKS: Anonymous array list avoids tight coupling to concrete module types
    private final List<AlertSubscriber> registry = new ArrayList<>();
    private String systemStatus = "NORMAL";

    public void addListener(AlertSubscriber sub) { registry.add(sub); }
    public void removeListener(AlertSubscriber sub) { registry.remove(sub); }

    public void updateSystemStatus(String newStatus) {
        this.systemStatus = newStatus;
        System.out.println("\n>> Central System shifted state to: " + systemStatus);
        
        // HOW IT WORKS: Iterate through the anonymous observer list to fire update signals
        for (AlertSubscriber subscriber : registry) {
            subscriber.onStatusChange(systemStatus);
        }
    }
}

// Main class containing the entry point
public class ObserverPatternDemo {
    public static void main(String[] args) {
        System.out.println("--- Observer Pattern Running ---");

        // Create the core central engine
        CoreStatePublisher publisher = new CoreStatePublisher();

        // Instantiate downstream decoupled worker components
        AlertSubscriber logger = new LogModule();
        AlertSubscriber notifier = new UserAlertModule();

        // Subscribe elements into the dynamic registration list
        publisher.addListener(logger);
        publisher.addListener(notifier);

        // Fire status changes to observe automated background delivery
        publisher.updateSystemStatus("CRITICAL_TEMPERATURE_WARNING");
        
        // Remove an element from the active update list at runtime
        publisher.removeListener(notifier);
        
        // Next broadcast will bypass the removed notifier module automatically
        publisher.updateSystemStatus("SYSTEM_SHUTDOWN_INITIATED");
    }
}
```
