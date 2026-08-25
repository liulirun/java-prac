# interview

A Mid Dev needs to write code that doesn't fall apart when requirements change. A Senior SDET needs to build robust, highly reusable **test automation frameworks** (like custom Selenium/Playwright or REST-Assured wrappers) where bad OOP architecture leads to flaky, unmaintainable test suites.

Here is a targeted 15-minute scenario and a set of probing questions designed to expose memorizers instantly.



2\. Probing Questions for Mid-Level Java Developers

Focus on architectural boundaries and the **"Fragile Base Class"** trap.

Q1: "Why should we avoid deep inheritance hierarchies (e.g., Class A extends B extends C extends D) in a production application?"

- **Why it works**: Memorizers will say "because it's confusing." You want structural depth.
- **Green Flag Answer**: They should discuss **tight coupling** and the violation of encapsulation. If a developer changes a single method in Class A to fix a bug, it can silently break downstream behavior in Class D (the Fragile Base Class problem). They should explicitly mention **"Composition over Inheritance"** as the remedy.

Q2: "In Spring Boot, if you create a `@Service` bean, it is a Singleton by default. If this service has an instance variable that changes on every HTTP request, what happens?"

- **Why it works**: Tests their understanding of state management and concurrency in singletons.
- **Green Flag Answer**: They should call out **thread-safety issues**. Because multiple threads handle concurrent HTTP requests through the same single bean instance, one user's request data will bleed into another user's session (Race Condition). They should state that Singletons must be *stateless*, or specify that the bean scope needs to be changed to `@RequestScope`.

3\. Probing Questions for Senior SDETs

Focus on framework architecture. Many SDETs write procedural code wrapped in Page Object classes; a Senior SDET must build a scalable architecture.

Q1: "How do you design a Page Object Model (POM) framework without creating a 'God Class' where a single Page class grows to 5,000 lines of code?"

- **Why it works**: Exposes whether they understand structural abstraction or just follow basic tutorials.
- **Green Flag Answer**: A Senior SDET will talk about **Single Responsibility** and **Component-Based Architecture**. Instead of a massive `HomePage` class, they break the page down into reusable fragments or components (e.g., a `HeaderComponent`, a `NavigationMenu`, a `FooterComponent`) via **Composition**. The `HomePage` class simply holds instances of these smaller component classes.

Q2: "Let's say you have a base test class (`BaseTest`) where you setup your WebDriver. Some tests need a database connection, some need an SSH tunnel, and some need API clients. How do you handle this without stuffing all these setups into `BaseTest`?"

- **Why it works**: Tests their application of the **Interface Segregation** and **Single Responsibility** principles to test automation.
- **Green Flag Answer**: They will strongly reject overloading `BaseTest` (which is a classic anti-pattern). They will suggest using **TestNG/JUnit Listeners**, custom **Rule/Extension mechanisms** (like JUnit 5 extensions), or composition-based fixtures. They want to compose test capabilities dynamically rather than forcing every test to inherit heavy, unused database or SSH connections.

# Quetion 1
1\. The 15-Minute Interview Scenario: The Notification Logger

Present this exact piece of messy code to the candidate (either via screen share or an online editor).

```java
// A sloppy, tightly-coupled system
public class OrderTestSuite {
    public void runOrderPlacementTest(String environment) {
        System.out.println("Step 1: UI Actions to place order...");

        // The Trap: Tight coupling and hardcoded conditional logic
        if (environment.equals("QA")) {
            System.out.println("[QA LOG]: Test passed. Writing result to local text file...");
        } else if (environment.equals("PROD")) {
            System.out.println("[PROD ALERT]: Sending critical slack alert to engineering channel...");
        } else if (environment.equals("PERFORMANCE")) {
            System.out.println("[PERF DATA]: Pushing execution time metric to Datadog database...");
        }
    }
}
```

The Prompt:

*"This is a test runner. Depending on the environment, it logs test results differently. Right now, it's a mess of `if-else` blocks. Show me how you would refactor this using solid OOP principles so that if a manual tester wants to add a new 'UAT' environment that sends an email, we don't have to touch this `OrderTestSuite` class."*

🎯 What to look for (Green Flags vs. Red Flags)

- **🔴 Red Flag (The Memorizer)**: They will try to clean up the string comparisons, extract the `if-else` block into a separate helper method within the same class, or use a Java `switch` statement. They might name-drop "Strategy Pattern" but fail to implement the polymorphism cleanly.

- **🟢 Green Flag (Mid Dev / Senior SDET)**: They will recognize a violation of the **Open/Closed Principle (OCP)**. They will instantly decouple the logging mechanism from the test execution.

  - They will create a `ResultLogger` interface with a `log(String message)` method.
  - They will create distinct concrete classes: `FileLogger`, `SlackLogger`, and `DatadogLogger`.
  - They will inject the interface into `OrderTestSuite` (Dependency Injection), allowing the environment behavior to be swapped dynamically without altering the core test workflow.
  
They will apply the **Open/Closed Principle (OCP)** and **Dependency Injection (DI)** to completely separate the test execution logic from the notification infrastructure.

Step 1: Create the Strategy Interface

First, define a clean, single-responsibility contract for what a logger must do.

```java
// The strategy interface
public interface TestResultHandler {
    void handleResult(String message);
}
```

Step 2: Implement Concrete Strategy Classes

Each environment's unique logging logic gets isolated into its own dedicated class. This completely eliminates the original `if-else` block.

```java
// QA Environment Strategy
public class FileResultHandler implements TestResultHandler {
    @Override
    public void handleResult(String message) {
        System.out.println("[QA LOG]: Test passed. Writing result to local text file: " + message);
    }
}

// PROD Environment Strategy
public class SlackAlertHandler implements TestResultHandler {
    @Override
    public void handleResult(String message) {
        System.out.println("[PROD ALERT]: Sending critical slack alert to engineering channel: " + message);
    }
}

// PERFORMANCE Environment Strategy
public class DatadogMetricHandler implements TestResultHandler {
    @Override
    public void handleResult(String message) {
        System.out.println("[PERF DATA]: Pushing execution time metric to Datadog database: " + message);
    }
}
```

Step 3: Refactor the Test Runner (The Core Class)

The `OrderTestSuite` class is now beautifully simple. It does not know—and does not care—*how* the logging happens. It simply executes the test and calls the handler it was given (Dependency Injection).

```java
public class OrderTestSuite {
    private final TestResultHandler resultHandler;

    // The Green Flag: Injecting the dependency via the constructor
    public OrderTestSuite(TestResultHandler resultHandler) {
        this.resultHandler = resultHandler;
    }

    public void runOrderPlacementTest() {
        System.out.println("Step 1: UI Actions to place order...");

        // Simulating a successful test execution message
        String statusMessage = "Order #12345 placed successfully.";

        // Polymorphic call: The exact behavior depends on the injected object
        resultHandler.handleResult(statusMessage);
    }
}
```

Step 4: How It's Instantiated (The Factory or Main Routine)

To prove how easy it is to manage, here is how a framework or factory would spin this up.

```java
public class Main {
    public static void main(String[] args) {
        // Simulating the environment lookup (could come from a config file or system property)
        String currentEnv = "PROD"; 

        TestResultHandler handler;

        // Map the environment string to the correct object once at startup
        switch (currentEnv) {
            case "QA" -> handler = new FileResultHandler();
            case "PROD" -> handler = new SlackAlertHandler();
            case "PERFORMANCE" -> handler = new DatadogMetricHandler();
            default -> throw new IllegalArgumentException("Unknown environment: " + currentEnv);
        }

        // Inject the chosen handler into the suite
        OrderTestSuite suite = new OrderTestSuite(handler);
        suite.runOrderPlacementTest();
    }
}
```

Why this code earns a "Green Flag" in an interview:

1. **Zero Modifications to Test Code**: If you ask them to add a new "UAT" environment that sends an email, they only need to create a new `EmailResultHandler` class and update the factory. They **never** have to change or re-test the core `OrderTestSuite` class again.
2. **Highly Testable**: If the candidate wants to unit test `OrderTestSuite`, they don't need a real database or real Slack API keys. They can easily pass a dummy mock handler (`msg -> {}`) into the constructor.
3. **Clean Architecture**: It demonstrates an understanding of the **Strategy Pattern**, which is highly valued for scaling up automation frameworks.

If you are planning out the rest of the technical panel, let me know:

To execute multiple notifications simultaneously without changing the `OrderTestSuite` class, a strong candidate will use the **Composite Pattern** or the **Observer Pattern**.

The **Composite Pattern** is the absolute cleanest choice here. It allows you to treat a *group* of loggers exactly like a *single* logger, keeping your core test logic 100% unchanged.

The Composite Code Solution

We create a new class called `CompositeResultHandler` that implements the same `TestResultHandler` interface, but holds a list of other handlers inside it.

```java
import java.util.ArrayList;
import java.util.List;

// 1. It looks like a single handler to the outside world
public class CompositeResultHandler implements TestResultHandler {

    // 2. Inside, it manages a collection of handlers
    private final List<TestResultHandler> handlers = new ArrayList<>();

    public void addHandler(TestResultHandler handler) {
        handlers.add(handler);
    }

    // 3. When called, it broadcasts the message to EVERY handler in the list
    @Override
    public void handleResult(String message) {
        for (TestResultHandler handler : handlers) {
            handler.handleResult(message);
        }
    }
}
```

How You Wire It Up (In Main or your Framework Setup)

Look at how beautifully this scales. The `OrderTestSuite` still thinks it is dealing with one single handler object, but underneath, multiple actions are firing at once:

```java
public class Main {
    public static void main(String[] args) {
        // Create the composite container
        CompositeResultHandler multiLogger = new CompositeResultHandler();

        // Add as many simultaneous notifications as you want!
        multiLogger.addHandler(new FileResultHandler());   // Logs to local file
        multiLogger.addHandler(new SlackAlertHandler());   // Sends to Slack
        multiLogger.addHandler(new DatadogMetricHandler()); // Sends to Datadog

        // Inject the composite into the test suite
        // (OrderTestSuite code stays EXACTLY the same as before!)
        OrderTestSuite suite = new OrderTestSuite(multiLogger);

        // This will now trigger all three logging behaviors simultaneously
        suite.runOrderPlacementTest();
    }
}
```

Why this is a "Super Green Flag" in an interview:

- **Zero Regression Risk**: You achieved simultaneous broadcasting without modifying a single line of `OrderTestSuite`, `FileResultHandler`, or `SlackAlertHandler`.
- **Infinite Flexibility**: You can dynamically add or remove notification targets at runtime based on user configuration.

If you are ready to round out your interview script, let me know if you want to explore:

- How a candidate should handle **exceptions** here (e.g., if the Slack logger fails, should it stop the File logger from running?)
- A quick screen for **Java Multi-threading/Concurrency** so these notifications run in parallel instead of blocking each other.

This is exactly where the combination of the **Strategy** and **Composite** patterns shines in a real-world framework. A senior candidate or SDET will use a **Factory** class to automatically build the correct setup—whether it's a single handler or a composite of multiple handlers—based on the environment.

The core `OrderTestSuite` code remains **completely untouched**. All the complexity is pushed to the framework setup.

Here is how you handle different environments with varying numbers of handlers cleanly.

1\. The Environment Configuration Factory

This factory reads the environment and packages the required handlers inside a `CompositeResultHandler` if multiple are needed. If only one is needed, it can return that single handler directly.

```java
public class TestResultHandlerFactory {

    public static TestResultHandler getHandlerForEnvironment(String env) {
        // We use the composite as a flexible container
        CompositeResultHandler composite = new CompositeResultHandler();

        switch (env.toUpperCase()) {
            case "QA" -> {
                // QA only needs 1 local file log
                composite.addHandler(new FileResultHandler());
            }
            case "PERFORMANCE" -> {
                // Performance only needs Datadog metrics
                composite.addHandler(new DatadogMetricHandler());
            }
            case "PROD" -> {
                // PROD is critical! We want MULTIPLE notifications simultaneously
                composite.addHandler(new FileResultHandler());    // Keep a local log
                composite.addHandler(new SlackAlertHandler());   // Ping engineering instantly
            }
            default -> throw new IllegalArgumentException("Unsupported environment: " + env);
        }

        return composite;
    }
}
```

2\. Putting it Together in the Test Runner Framework

When your automation engine runs, it detects the environment, calls the factory to get the custom handler package, and injects it into the test suite.

```java
public class AutomationRunner {
    public static void main(String[] args) {
        // Imagine this comes from a Jenkins parameter, a Maven property, or a config.properties file
        String currentEnv = "PROD"; 

        // The factory handles the logic of 1 vs multiple automatically
        TestResultHandler dynamicHandler = TestResultHandlerFactory.getHandlerForEnvironment(currentEnv);

        // Inject the handler(s). The suite doesn't know if it's 1 or 3 handlers!
        OrderTestSuite suite = new OrderTestSuite(dynamicHandler);

        // Runs all UI steps and broadcasts results to the entire environment package
        suite.runOrderPlacementTest();
    }
}
```

Why this answers the candidate screening perfectly:

- **Polymorphism at its best**: Because `CompositeResultHandler` implements the `TestResultHandler` interface, it can masquerade as a single object even when it holds a massive array of sub-handlers.
- **Env-Specific Isolation**: Your environments are completely decoupled. You can safely add 5 more loggers to `PROD` tomorrow, and your `QA` environment setup remains completely safe, isolated, and unchanged.

If you want to push a Senior SDET candidate to their absolute limit with this scenario, let me know if we should talk about **Thread Safety**: *If tests run in parallel across 10 threads, how do we make sure our `FileResultHandler` doesn't scramble the text file data?*