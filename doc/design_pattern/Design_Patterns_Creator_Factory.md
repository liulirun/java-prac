# The Factory Pattern - Creational Pattern!

## The Principle in One Sentence / 一句话原则

**Hide the complexities of instantiation; ask the factory for what you need by type or config instead of building it yourself.**\
**对象创建复杂、且根据配置动态切换**时使用 Factory。\
**隐藏实例化的复杂性；通过类型或配置向工厂索取对象，而不是自己去构建。**

## What This Pattern Achieves / 此模式解决什么问题

- **Centralized setup / 集中设置：** Object creation and complex configuration logic are managed in one single place. / 对象创建与繁琐的属性配置逻辑被集中管理。
- **Framework flexibility / 框架灵活性：** Adding a new component variant does not affect or break existing tests. / 增加新的组件变体不会影响或破坏已有的测试用例。
- **Environment independence / 环境独立性：** Tests ask for an abstract asset, letting the runtime environment determine the exact instance. / 测试层只需声明所需抽象资产，由运行环境决定注入哪个具体实例。

## UI & Automation Testing Application / 自动化测试应用

> **Real Automation Framework Use Case:** When a UI test framework must execute seamlessly across multiple browsers (Chrome, Firefox, Edge) or platforms based on configuration files or command-line parameters.
>
> **真实自动化框架使用场景：** 当 UI 测试框架需要根据配置文件或命令行参数，无缝地在多个浏览器（Chrome、Firefox、Edge）或平台上执行时使用。

📊 **Implementation Summary / 实现总结**

| Step / 步骤             | What to do / 做什么                                                           | Real-World Application in Automation / 自动化测试中的实际应用                                                                                    |
| --------------------- | -------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------- |
| **1. Interface / 接口** | Define one unified driver contract. / 定义统一的驱动契约。                           | Every browser driver must expose standard lifecycle actions like `initialize()` and `quit()`. / 所有驱动必须提供标准的初始化与退出操作。                  |
| **2. Products / 产品**  | Put specific driver configurations in separate classes. / 每种浏览器驱动配置独立成一个类。 | Implement `ChromeDriverImpl`, `FirefoxDriverImpl`, etc., to isolate individual binaries and capabilities. / 实现多个具体产品类，隔离各自的无头模式或路径设置。 |
| **3. Factory / 工厂**   | Build a centralized routing mechanism. / 建立中心化的分发路由。                       | Create a `DriverFactory` that accepts a string/enum parameter and returns the correct product. / 创建一个工厂类，根据传入的标识符返回对应产品实例。            |

0\. 💻 Compact Automation Testing Pseudo-code / 精简自动化测试伪代码

```java
// 1) Stable contract: The framework interacts strictly with this interface.
//    稳定契约：自动化框架仅与此抽象产品接口进行交互。
interface TestDriver {
    void initialize();
    void quit();
}

// 2) Concrete products: Encapsulate options and capabilities inside individual classes.
//    一个具体产品 = 一种浏览器或平台变体。
class ChromeDriverImpl implements TestDriver {
    public void initialize() { /* setup chrome options, headless flags, etc. */ }
    public void quit() { /* chrome.quit() */ }
}
class FirefoxDriverImpl implements TestDriver {
    public void initialize() { /* setup geckodriver profiles, paths, etc. */ }
    public void quit() { /* firefox.quit() */ }
}

// 3) Factory Class: Centralizes instantiation logic behind a clean router.
//    工厂类：将实例化的分发逻辑集中在一个清晰的路由中。
class DriverFactory {
    public static TestDriver createDriver(String browserType) {
        switch (browserType.toUpperCase()) {
            case "CHROME":  return new ChromeDriverImpl();
            case "FIREFOX": return new FirefoxDriverImpl();
            default: throw new IllegalArgumentException("Unknown browser: " + browserType);
        }
    }
}

// 4) Select the browser at test setup; keep the test execution layer blind.
//    在测试准备阶段选择浏览器；保持测试执行层无感知。
String configBrowser = System.getProperty("browser", "CHROME");
TestDriver driver = DriverFactory.createDriver(configBrowser);
driver.initialize();
```

**Essential implementation points / 核心实现要点**

| English                                                                          | 中文                               |
| -------------------------------------------------------------------------------- | -------------------------------- |
| Group varying instantiations under a unified, abstract interface contract.       | 将不同的实例化对象统一归于一个抽象的接口契约之下。        |
| Isolate constructor setups and driver parameters away from the test layer.       | 将构造函数的属性设置和驱动参数与测试用例层完全隔离。       |
| Use clean identifiers (like Strings or Enums) to trigger the creation branch.    | 使用清晰的标识符（如字符串或枚举）来触发正确的创建分支。     |
| Throw controlled exceptions for unsupported configurations in the default block. | 在默认未知分支中针对不支持的配置抛出受控异常。          |
| Ensure tests only care about *how to use* the tool, never *how to build* it.     | 确保测试用例只关心*如何使用*工具，而永远不关心*如何构建*它。 |

### Three Real-Life Scenarios / 三个真实使用场景

Each example below creates a different dynamic object. The test layer declares its conceptual need, while the factory handles the instantiation details.\
下面每个示例负责创建不同的动态对象；测试层仅声明概念需求，由工厂负责具体的实例化细节。

1. **Multi-Environment Test Data Generation / 多环境测试数据生成**Use Factory when tests need specific personas or mock payloads that differ fundamentally by role context (e.g., Admin privileges vs. Guest restrictions). The test requests a user type from the factory without dealing with manual field setups or data seeding libraries.当测试需要特定的角色数据或 Mock 载荷，且这些数据因角色不同（如：系统管理员与匿名访客）而有本质区别时使用工厂。测试向工厂索取特定用户画像，无需手动配置字段。
   ```java
   interface TestUser { String getRole(); String getToken(); }

   class AdminUser implements TestUser {
       public String getRole() { return "SUPER_ADMIN"; }
       public String getToken() { return "token_abc_123"; }
   }

   class GuestUser implements TestUser {
       public String getRole() { return "READ_ONLY"; }
       public String getToken() { return "token_xyz_789"; }
   }

   class UserFactory {
       public static TestUser getProfile(String userRole) {
           if ("ADMIN".equalsIgnoreCase(userRole)) return new AdminUser();
           return new GuestUser();
       }
   }
   ```
2. **Cross-Platform Microservice Client Factory / 跨平台微服务客户端工厂**Use Factory when your API testing engine directs requests to a local mock stub during unit testing, or to a live environment client during integration testing based on system flags.当你的 API 测试引擎需要根据运行标识，在单元测试期间将请求导向本地 Mock Stub，或者在集成测试期间导向真实环境的 Http 客户端时使用工厂。
   ```java
   interface ServiceClient { Response send(Request req); }

   class MockStubClient implements ServiceClient { /* Returns hardcoded local JSON */ }
   class LiveStagingClient implements ServiceClient { /* Real HTTP execution with proxy setup */ }

   class ServiceFactory {
       public static ServiceClient connect(String environment) {
           if ("LOCAL".equals(environment)) return new MockStubClient();
           return new LiveStagingClient();
       }
   }
   ```
3. **Multi-Format Report Exporter / 多格式报告导出器**Use Factory when a test lifecycle listener must automatically generate test execution summaries in completely different formats (e.g., interactive Allure HTML vs. standard Markdown summaries for pull requests) depending on CI parameters.当测试生命周期监听器需要根据 CI 持续集成参数，自动生成完全不同格式的执行总结报告（如：交互式 Allure HTML 仪表盘与用于 Pull Request 的标准 Markdown 表格）时使用工厂。
   ```java
   interface TestReporter { void generate(TestResults results); }

   class AllureReporterImpl implements TestReporter { /* Generates HTML dashboards */ }
   class MarkdownReporterImpl implements TestReporter { /* Generates standard MD tables */ }

   class ReporterFactory {
       public static TestReporter create(String formatType) {
           if ("ALLURE".equalsIgnoreCase(formatType)) return new AllureReporterImpl();
           return new MarkdownReporterImpl();
       }
   }
   ```

## 📊 **Good vs. Bad Comparison / 好的实现 vs. 糟糕的实现**

| Area / 维度               | 🔴 Bad: Hardcoded / Direct Design / 差：硬编码/直接设计                                                                                                        | 🟢 Good: Factory Design / 好：Factory 设计                                                                                                                       |
| ----------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| **Responsibility / 职责** | Test scripts or lifecycle hooks handle parameters, capabilities, and concrete initialization. **测试脚本或钩子函数承担了**参数配置、底层属性及具体的实例化工作。                     | Tests state an intent identifier, delegating structural setup details to a specialized class. **测试脚本仅指定一个意图标识符**，将所有的结构组装细节委派给专用类。                           |
| **Boilerplate / 样板代码**  | Repetitive setup blocks clutter multiple testing files to handle initialization variants. **不同测试文件中充斥着重复的实例化代码**以应对各种初始化变体。                           | Single-line factory calls keep tests focused purely on execution. **单行的工厂调用**使测试方法能够纯粹专注于测试用例本身的执行。                                                          |
| **Scaling Risk / 扩展风险** | Adding a new option (e.g., Edge browser) forces modifying conditional logic inside every single test file. **增加新选项**（如 Edge 浏览器）迫使框架必须修改每个测试文件中的条件分支。 | Add a new concrete implementation class and plug it into the factory router; existing tests remain untouched. **只需新增一个具体 product 类**并将其注册进工厂路由；既有测试代码完全不受影响。 |

🕒 **When to Use This Pattern / 何时使用此模式**

Use Factory when your SDET infrastructure interacts with **polymorphic components that must be dynamically selected based on external configurations** (such as choosing different browsers, runtime execution environments, mock levels, or reporting tools). It works best when these components are expected to grow or change independently over time.

当自动化测试基础设施需要与**必须根据外部配置进行动态选择的多态组件**交互时（例如在不同浏览器、运行环境、Mock 层级或报告工具之间切换），使用 Factory 模式。当这些组件选项随时间推移预计会不断增加时，它的解耦价值最高。
