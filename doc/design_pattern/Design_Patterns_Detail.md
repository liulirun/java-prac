# Software Design Patterns


## Working Complementary Group / 工作互补组合

Use these patterns together in an enterprise-grade automation framework. / 在企业级自动化框架中可以将这些模式高能组合使用：

| Pattern / 模式                      | Classification / 分类  | Responsibility / 职责                                          | Framework Runtime Lifecycle Role / 自动化框架生命周期中的具体角色                                                                                                                                                                                                                                                                   |
| --------------------------------- | -------------------- | ------------------------------------------------------------ | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Template Method / 模板方法**        | **Behavioral** / 行为型 | Enforce fixed global pipeline workflows. / 强控全局固定的测试流水线。     | Governs the strict execution order of infrastructure setup, run stages, and teardown flushes. / 严格锁定底层初始化、用例执行、以及清理上报的闭环顺序。                                                                                                                                                                                          |
| **Singleton / 单例**                | **Creational** / 创建型 | Restrict shared mutable resource pools. / 全局锁定唯一共享资源池。       | Safely manages `ThreadLocal` storage of long-lived configurations, web drivers, or database pools. / 安全管理 `ThreadLocal` 状态下的全局配置、浏览器驱动或数据库池。                                                                                                                                                                         |
| **Factory + DI / 工厂 + 依赖注入**      | **Creational** / 创建型 | Resolve component layout shifts dynamically. / 动态解析和分发组件布局。  | Evaluates CLI environment variables (`-Denv=staging`) and seeds required dependencies once at launch. / 传输命令行环境变量，在测试启动时进行依赖的一次性注入。                                                                                                                                                                                  |
| **Builder / 构建者**                 | **Creational** / 创建型 | Compose large variable entities fluently. / 流畅组装高复杂度多变实体。    | Chains selective parameters to build intricate HTTP Request Payloads or nested JSON data mock profiles. / 通过链式方法高度灵活地组合参数，构建复杂的请求载荷或嵌套的 JSON 模型。                                                                                                                                                                     |
| **Strategy / 策略**                 | **Behavioral** / 行为型 | Swap business algorithms interchangeably. / 自由平替底层可变业务算法。    | Dispatches selected verification behaviors, transport retry rules, or API authentication routines. / 调度执行选中的认证方式、接口重试逻辑或特定版本的契约校验。                                                                                                                                                                                   |
| **Decorator / 装饰者**               | **Structural** / 结构型 | Overlay dynamic runtime behaviors cleanly. / 运行时透明包裹叠加新行为。   | Attaches automatic element logging, latency capturing metrics, or chaos-delays without updating core logic. / 在不触动核心代码的前提下，为网络或操作层动态附加高亮、耗时度量或混沌延迟。                                                                                                                                                                  |
| **Facade / 外观**                   | **Structural** / 结构型 | Conceal jagged low-level multi-step flows. / 彻底收治零散丑陋的底层操作流。 | Groups dozens of messy individual backend controller routes into high-level business scenarios. / 将多步零散的底层微服务请求或数据库交互打包成人类可读的高层业务链。                                                                                                                                                                                  |
| **Chain of Responsibility / 责任链** | **Behavioral** / 行为型 | Pipe cascading sequential validation steps. / 链式驱动漏斗式的顺序校验流。 | Sequentially evaluates responses (Status![](data:image/gif;base64,R0lGODlhAQABAIAAAP///wAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==)JSON Schema![](data:image/gif;base64,R0lGODlhAQABAIAAAP///wAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==)Business Data) and triggers clean fail-fast loops. / 按顺序层层推进响应体校验，遇到断裂点立即终止并抛出清晰边界。 |
| **Observer / 观察者**                | **Behavioral** / 行为型 | Broaden isolated fan-out event streams. / 无感发散相互独立的事件流。      | Automatically pushes test milestones concurrently out to downstream listeners like Slack, Allure, or Grafana. / 捕获执行过程中的里程碑事件，并行分发通知给独立的第三方告警、报告或大盘指标系统。                                                                                                                                                             |

Complete End-to-End Execution Flow / 完整闭环执行流

![](data:image/gif;base64,R0lGODlhAQABAIAAAP///wAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==)

Phase 1: Infrastructure Initialization / 阶段一：基础架构初始化

1. **`Template Method`** triggers the unalterable core framework lifecycle pipeline hook: `setupPlatform()`.
2. **`Singleton`** boots up to create a isolated, thread-safe, locked `ThreadLocal` global configuration registry.
3. **`Factory/DI`** reads the run variables at startup and injects the corresponding environment adapters.

Phase 2: Request Preparation & Decoration / 阶段二：请求准备与高级包装

4. **`Builder`** constructs complex test data, model entities, or payload structures using fluid method chains.
5. **`Strategy`** plugs the active algorithm context (like specific auth types or retry rules) into the active client layer.
6. **`Decorator`** wraps the core execution engine transparently to intercept and monitor network metrics.

Phase 3: High-Level Execution / 阶段三：高层业务执行

7. **`Facade`** masks messy backend sequences, allowing the test file to call a single clean business transaction.

Phase 4: Validation & Reporting Telemetry / 阶段四：校验与报告监控

8. **`Chain of Responsibility`** intercepts response packets, streaming status and schemas through a clean fail-fast check.
9. **`Observer`** catches runtime lifecycle state hooks, pushing parallel alerts to monitoring, alerting, and logging tools.


## 1. Structural Patterns (System Composition)

### [The Decorator Pattern](Design_Patterns_Structural_Decorator.md)
* **When to Use (The Rule):** Use this when you want to stack multiple optional features or behaviors onto an object at runtime (like adding toppings to a pizza).
* **Why to Use (The Reason):** If you try to use normal inheritance (subclasses) to handle every combination of features, you will end up creating too many files (e.g., `EncryptedData`, `CompressedData`, `EncryptedAndCompressedData`). The Decorator pattern lets you wrap an object inside another object like nesting Russian dolls. You can stack as many wrappers as you want at runtime without creating new classes.

---

### The Composite Pattern
* **When to Use (The Rule):** Use this when you are dealing with tree structures (like folders containing files and other subfolders) and you want to treat individual items and groups of items exactly the same way.
* **Why to Use (The Reason):** Without this pattern, your code would be filled with messy `if/else` checks to find out if something is a single file or a folder container. The Composite pattern forces both single items ("leaves") and containers ("composites") to share the exact same interface. When you call a command on a container, it automatically loops through its children and runs the command on them, saving you from writing recursive loop logic in your main code.

---

### [The Facade Pattern](Design_Patterns_Structural_Facade.md)

## 2. Creational Patterns (Object Generation)
### [The Singleton Pattern](Design_Patterns_Creator_Singleton.md)

###  [The Factory Pattern](Design_Patterns_Creator_Factory.md)


###  [The Builder Pattern](Design_Patterns_Creator_Builder.md)
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
### [The Strategy Pattern](Design_Patterns_Behaviour_Strategy.md)

### [The Template Method Pattern](Design_Patterns_Behaviour_Template_Method.md)

### [The Chain of Responsibility Pattern](Design_Patterns_Behaviour_Chain_of_Responsibility.md)

### [The Observer Pattern](Design_Patterns_Behaviour_Observer.md)

## 4. Others
### [The Page Object Model (POM)](Design_Patterns_Testing_Page_Object_Model.md)

### [TBD](Design_Patterns_Others_TBD.md)
