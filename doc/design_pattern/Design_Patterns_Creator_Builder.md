Here is your simplified, streamlined learning summary for the **Builder Pattern**, strictly matching your reference design layout, headers, and bilingual formatting for an **SDET** role.

The Builder Pattern - Creational Pattern!

The Principle in One Sentence / 一句话原则

**Separate the construction of a complex object from its representation so that the same construction process can create different representations.**\
**对象属性繁多、且需要灵活组合搭配**时使用 Builder。\
**将一个复杂对象的构建与它的表示分离，使得同样的构建过程可以创建不同的表示。**

What This Pattern Achieves / 此模式解决什么问题

- **No telescope constructors / 消除胖构造函数：** Prevents long methods containing dozens of confusing, positional parameters or endless `null` values. / 避免包含几十个让人混淆的、按位置传参或充斥着一堆 `null` 的庞大构造函数。
- **Immutability / 不变性：** Allows test objects to be completely ready and immutable upon creation, avoiding dangerous side effects from post-creation setters. / 允许测试对象在创建完成时即完全就绪且不可变，避免创建后使用 Setter 修改带来的副作用。
- **High readability / 高可读性：** Test data preparation reads naturally like a human-written checklist via method chaining. / 使得测试数据的准备过程通过链式调用变得像人类编写的清单一样自然、清晰。

UI & Automation Testing Application / 自动化测试应用

> **Real Automation Framework Use Case:** When an API test framework needs to construct complex HTTP Request Playloads or JSON bodies with highly flexible, selective parameters (e.g., custom headers, bodies, queries, and multi-part files) for different test cases.
>
> **真实自动化框架使用场景：** 当 API 测试框架需要构建复杂的 HTTP 请求体或 JSON 载荷，且不同测试用例需要高度灵活、选择性地组合参数（如自定义 Header、Body、Query 参数和上传文件）时使用。

📊 **Implementation Summary / 实现总结**

| Step / 步骤              | What to do / 做什么                                                  | Real-World Application in Automation / 自动化测试中的实际应用                                                                                                    |
| ---------------------- | ----------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------- |
| **1. Target / 目标类**    | Define the private target data container. / 定义私有的目标数据类。           | Create the `ApiRequest` class containing fields for headers, query params, cookies, and body. / 创建 `ApiRequest` 类，包含 Header、Query、Cookie 以及 Body 等属性。 |
| **2. Builder / 构建者**   | Build an inner class tracking target state. / 建立维护目标类状态的内部类。      | Implement `RequestBuilder` with fluent methods returning `this` for each selective attribute. / 实现 `RequestBuilder`，每个属性配置方法都返回 `this` 以进行链式调用。       |
| **3. Terminal / 终结方法** | Implement a `build()` method to lock data. / 实现 `build()` 方法锁定数据。 | Validate final field sanity inside `build()` and return the fully initialized target object. / 在 `build()` 内部验证属性健全性，并返回完全初始化的请求对象。                   |

0\. 💻 Compact Automation Testing Pseudo-code / 精简自动化测试伪代码

```java
// 1) Target object with private constructor and immutable fields.
//    包含私有构造函数和不可变属性的目标对象。
class ApiRequest {
    private final String url;
    private final String method;
    private final String body;
    private final int timeout;

    private ApiRequest(Builder builder) {
        this.url = builder.url;
        this.method = builder.method;
        this.body = builder.body;
        this.timeout = builder.timeout;
    }

    // 2) Inner Builder class maintaining the tracking state.
    //    维护属性构建状态的内部 Builder 类。
    public static class Builder {
        private String url;
        private String method = "GET"; // Default values can be set here / 可以在此设置默认值
        private String body;
        private int timeout = 5000;

        public Builder url(String url) { this.url = url; return this; }
        public Builder method(String method) { this.method = method; return this; }
        public Builder body(String body) { this.body = body; return this; }
        public Builder timeout(int timeout) { this.timeout = timeout; return this; }

        // 3) Terminal build method instantiating the target safely.
        //    终结构建方法，安全地实例化目标对象。
        public ApiRequest build() {
            if (this.url == null) throw new IllegalStateException("URL is mandatory");
            return new ApiRequest(this);
        }
    }
}

// 4) Fluent creation during test execution; highly flexible parameter layout.
//    在测试执行期间进行流畅的链式创建；高度灵活的参数布局。
ApiRequest customPostRequest = new ApiRequest.Builder()
    .url("https://example.com")
    .method("POST")
    .body("{\"name\": \"SDET\"}")
    .timeout(2000)
    .build();
```

**Essential implementation points / 核心实现要点**

| English                                                                                   | 中文                                          |
| ----------------------------------------------------------------------------------------- | ------------------------------------------- |
| Make target object constructors private to force usage of the builder interface.          | 将目标对象的构造函数设为私有，强迫团队统一使用 Builder 接口。         |
| Return `this` (the builder instance itself) at the end of every configuration method.     | 在每个配置属性方法的末尾返回 `this`（Builder 实例本身）以支持链式调用。 |
| Centralize parameter validation rules inside the terminal `build()` execution hook.       | 将所有参数的合法性验证规则集中在终结方法 `build()` 的执行钩子中。      |
| Provide sensible default values inside the builder step to keep test codes short.         | 在 Builder 步骤中提供合理的默认值，从而精简测试代码。             |
| Ensure the target object fields are `final` to secure thread safety during parallel runs. | 确保目标对象的属性为 `final`，以保障并行测试执行期间的线程安全。        |

Three Real-Life Scenarios / 三个真实使用场景

Each example below simplifies complex object construction. The test layer mixes and matches only the values required for the current test case.\
下面每个示例负责简化复杂对象的构建；测试层仅组装当前测试用例所需的属性值。

1. **Flexible Complex Test Data / User Persona Setup / 灵活复杂的测试数据/用户画像设置**Use Builder when a registration or checkout test data object has 20+ fields (first name, address, billing, references) but a specific validation test only cares about mutating the `zipcode` and `phoneNumber` fields.当注册或结账测试数据对象拥有 20 多个字段（名字、地址、账单、偏好等），而某个特定的校验测试只需要改动 `zipcode` 和 `phoneNumber` 字段时使用 Builder。
   ```java
   class UserProfile { /* details omitted */ }

   UserProfile validUser = new UserProfile.Builder()
       .firstName("John")
       .lastName("Doe")
       .email("john.doe@test.com")
       .zipcode("94043") // Only care about geography in this test scenario
       .build();
   ```

````

2. **Custom Playwright/Selenium Browser Context Options / 自定义浏览器上下文选项**

   Use Builder when initializing a test session that requires explicit, fine-grained control over browser capabilities, extensions, geo-locations, and screen resolutions on a per-test-case basis.

   当初始化测试会话需要基于每个用例进行细粒度控制，显式开启或关闭浏览器 Capabilities、插件扩展、地理定位以及屏幕分辨率时使用 Builder。

   ```java
   BrowserContext config = new BrowserContext.Builder()
       .enableGeolocation(true)
       .setPermissions(List.of("notifications"))
       .setViewport(1920, 1080)
       .acceptInsecureCerts(true)
       .build();
````

3. **Database Mock/Stub Component Configuration / 数据库 Mock/Stub 组件配置**Use Builder when setting up embedded wiremock stubs or database entities where you need to cleanly configure ports, connection limits, query delays, and initial schema paths without passing an endless array of numbers.当在自动化脚本中组装内嵌式 WireMock 桩或数据库实体时使用 Builder，能够清晰地配置端口号、连接上限、延迟时间以及初始 Schema 路径，避免传递令人费解的数字数组。
   ```java
   MockServer server = new MockServer.Builder()
       .port(8081)
       .simulateLatency(3000) // simulation of network delay
       .responseHeader("Content-Type", "application/json")
       .build();
   ```

```

***

📊 **Good vs. Bad Comparison / 好的实现 vs. 糟糕的实现**

| Area / 维度 | 🔴 Bad: Telescoping / Mutator Design / 差：胖构造函数/Setter设计 | 🟢 Good: Builder Design / 好：Builder 设计 |
| --- | --- | --- |
| **Safety / 安全性** | Objects use generic Setters; fields can be modified accidentally midway through test verification. **对象使用普通的 Setter**；在测试验证中途，属性容易被意外篡改。 | Immutable instances protect data sanity; once built, the payload remains completely locked. **不可变实例确保数据纯净**；一旦构建完成，载荷状态被完全锁定。 |
| **Maintainability / 维护性** | Adding a new optional field forces modifying dozens of existing matching constructors or breaking tests. **增加一个新的可选字段**会强迫重构几十个既有的对应构造函数，极易破坏存量测试。 | Introduce a single new method to the builder; all existing test chain flows remain fully backwards compatible. **只需在 Builder 中新增一个链式方法**；所有既有的测试链条完美向下兼容。 |
| **Clarity / 清晰度** | Constructors use raw literals like `new User(null, true, false, 0, "US")`, making it unreadable. **构造函数充斥着底层字面量**如 `new User(null, true, false, 0, "US")`，缺乏可读性。 | Explicit named methods like `.isVip(true).country("US")` explicitly document the exact test configuration. **显式命名的链式方法**如 `.isVip(true).country("US")` 让测试配置一目了然。 |

***

🕒 **When to Use This Pattern / 何时使用此模式**

Use Builder when your SDET automation scripts need to create **objects holding a wide range of optional configuration combinations or complex nesting** (such as multi-attribute API payloads, distinct user profiles, or environment parameters). It is ideal when you want to achieve clean readability and preserve target immutability, ensuring that tests never conflict during multi-threaded executions.

当自动化脚本需要创建**包含大量可选配置组合或具有复杂嵌套结构的对象**时（例如多属性 API 载荷、具有差异的用户画像或环境参数），使用 Builder 模式。当你希望获得极佳的代码可读性并保证目标对象的不可变性时，它是最理想的选择，这能有效确保测试在多线程高并发执行期间互不干扰。

***

Since you have now mastered **Strategy**, **Factory**, and **Builder** patterns for automation infrastructures, would you like to see how to chain them together to **generate complex test data models via a Factory engine**?
```