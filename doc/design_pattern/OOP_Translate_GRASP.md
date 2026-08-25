```
learning OOP Design
1. add Chinese comment on proper  line of WHY
2. convert to JAVA example
3. DO not ask questions
4. convert to simple markdown doc
```
# The 9 GRASP Principles

| Principle                | Core Question / Rule                                                                                       | Practical Example                                                                                                         |
| ------------------------ | ---------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------- |
| **Information Expert**   | Assign responsibility to the class that has the **information needed** to fulfill it.                      | A `Sale` class contains all `SaleLineItem` objects, so `Sale` should calculate its own total price.                       |
| **Creator**              | Class `B` should create Class `A` if `B` **aggregates, contains, or records** `A`.                         | An `Order` class should instantiate `OrderItem` objects because items cannot exist outside an order.                      |
| **Controller**           | Assign responsibility for receiving and handling **UI system events** to a dedicated class.                | A `LoginController` intercepts a login button click, extracts credentials, and passes them to the backend domain logic.   |
| **Low Coupling**         | Keep **dependencies between classes minimal** to reduce the cascading impact of changes.                   | Depend on a generic `PaymentMethod` interface instead of tightly binding your system to `VisaCard` and `PayPal` classes.  |
| **High Cohesion**        | Keep responsibilities **highly focused and closely related** inside a single class.                        | Avoid making a `User` class handle both user details and database connection logic; split the DB logic away.              |
| **Polymorphism**         | Handle alternative behaviors **based on type** using polymorphic operations rather than `if/else` checks.  | Create a `draw()` method on a `Shape` base class, allowing `Circle` and `Square` subclasses to implement their own logic. |
| **Pure Fabrication**     | Create a **fictional class** (not found in the real-world domain) to maintain clean cohesion and coupling. | A `DatabaseLogger` or `EmailService` class. These don't exist as tangible domain concepts but keep other classes clean.   |
| **Indirection**          | Assign responsibility to an **intermediate object** to decouple two components.                            | Using an adapter or a broker class so that Component A doesn't have to talk directly to Component B.                      |
| **Protected Variations** | Isolate unstable points of code **behind a stable interface** so changes don't break the system.           | Wrapping a third-party payment gateway API behind your own internal payment interface.                                    |

# GRASP Pattern 1: Information Expert
Part 1: Original Context & Chinese Explanation

- **Original Definition:** Assign responsibility to the class that has the information needed to fulfill it.
- **中文概念翻译：** 将职责分配给拥有完成该职责所需信息（数据）的类。哪个类手里的数据最齐全，事情就该由哪个类来办。

Why we need it (为什么需要它)

- **Encapsulation (封装性)：** Classes should keep their data private and manage their own state. Forcing external classes to fetch internal data violates encapsulation. (类应该隐藏自己的数据并自主管理状态。强迫外部类去调取其内部数据会破坏封装性。)
- **Low Coupling & High Cohesion (低耦合与高内聚)：** When a class manages its own data, external modules do not need to know the internal data structure. This stops changes from cascading through the codebase. (当一个类自己处理自己的数据时，外部模块就不需要了解其内部数据结构。这能防止因数据结构变更而导致其他代码大面积崩溃。)

Part 2: Java Code Comparison

❌ The Bad Way (坏的做法)

```java
import java.util.List;

// ❌ Bad: External class calculates order total
public class OrderCalculator {

    public double calculateTotal(Order order) {
        double total = 0.0;

        // WHY: 严重违反信息专家原则。计算类强行伸手向 Order 和 OrderItem 索要它们的私有数据。
        // 这导致计算类与订单的内部数据结构高度耦合（暴露了 items 列表和具体属性）。
        // 一旦未来修改了商品总价的计算规则（比如加入税率），你就必须满项目去修改这个外部计算类。
        for (OrderItem item : order.getItems()) {
            total += item.getPrice() * item.getQuantity();
        }
        return total - order.getDiscount();
    }
}
```

✅ The Good Way (好的做法)

```java
import java.util.List;
import java.util.ArrayList;

// ✅ Good: Order calculates its own total (it has the data)
public class Order {
    private final List<OrderItem> items = new ArrayList<>();
    private double discount = 0.0;

    // WHY: 遵循信息专家原则。Order 类本身拥有所有的订单项（items）和折扣数据（discount），
    // 因此“计算总价”的职责理所当然应当分配给 Order 类自己，不允许外部随意窥探其内部集合。
    public double getTotal() {
        double subtotal = 0.0;
        for (OrderItem item : this.items) {
            subtotal += item.getSubtotal();
        }
        return subtotal - this.discount;
    }

    public List<OrderItem> getItems() {
        return this.items;
    }

    public double getDiscount() {
        return this.discount;
    }
}
```

Part 3: UI/Runner Interaction with the Expert

```java
public class Main {
    public static void main(String[] args) {
        Product laptop = new Product("Laptop", 1000.0);
        Order order = new Order();

        // 模拟向订单添加商品项
        order.getItems().add(new OrderItem(laptop, 2));

        // WHY: 外部调用者（如UI层或启动类）不需要了解复杂的计算逻辑，更不需要手动去遍历循环。
        // 它只需要简单地下达指令：“告诉我总价是多少”，剩下的工作由信息专家（Order）在内部完美搞定。
        double finalTotal = order.getTotal();
        System.out.println("Order Total: " + finalTotal);
    }
}
```

🎯 Ultimate Summary (一句话总结)

- **Bad Design:** You hold your wallet, but a stranger reaches inside, counts your cash, and subtracts your bills for you. (你揣着钱包，却让外人强行把手伸进你的口袋里数钱、算账、扣钱。)
- **Good Design:** You hold your wallet, and when someone asks for the total, you count your own cash inside your pocket and tell them the final number. (你揣着钱包，别人问你要多少钱，你自己在口袋里把钱算好，直接告诉对方一个最终数字。)

# GRASP Pattern 2: Creator (Simple Version)

Part 1: Original Context & Chinese Explanation

- **Original Definition:** Assign class B the responsibility to create class A if B contains, aggregates, or closely uses A.
- **中文概念翻译：** 如果类 B 包含或管理类 A，那么类 B 就应该负责“创建”类 A。谁是老大，谁来招募小弟。

Why we need it (为什么需要它)

- **Simplifies the Client (简化外部调用)：** The user does not need to know how to build the inside parts. They just give the raw info to the container. (外部用户不需要知道怎么组装内部零件，只需要把原材料传给容器即可。)

Part 2: Java Code Comparison

❌ The Bad Way (坏的做法)

```java
// ❌ Bad: The external runner has to manually build the internal child object
public class Main {
    public static void main(String[] args) {
        BookShelf shelf = new BookShelf();

        // WHY: 违反创造者原则。外部代码必须自己手动 new Book，然后再塞给书架。
        // 这强迫外部代码必须了解 Book 的构造细节，增加了外部代码的负担。
        Book book = new Book("Java Basics");
        shelf.addBook(book);
    }
}
```

✅ The Good Way (好的做法)

```java
import java.util.ArrayList;
import java.util.List;

// ✅ Good: BookShelf manages and creates its own Books
public class BookShelf {
    private final List<Book> books = new ArrayList<>();

    // WHY: 遵循创造者原则。书架包含书籍，所以书架应该负责创建书籍。
    // 外部调用者只需要传一个简单的字符串（标题），不需要知道 Book 类是如何实例化的。
    public void createAndAddBook(String title) {
        Book newBook = new Book(title);
        this.books.add(newBook);
    }
}
```

```java
// 内部被管理的对象
public class Book {
    private final String title;

    public Book(String title) {
        this.title = title;
    }
}
```

Part 3: UI/Runner Interaction with the Creator

```java
public class Main {
    public static void main(String[] args) {
        BookShelf shelf = new BookShelf();

        // WHY: 外部调用者非常轻松。
        // 它不需要自己去 new Book()，只需告诉书架：“帮我放一本叫 Java Basics 的书进去”。
        shelf.createAndAddBook("Java Basics");
    }
}
```

🎯 Ultimate Summary (一句话总结)

- **Bad Design:** You buy a photo album, but you have to manufacture the plastic photo pockets yourself and glue them into the album. (你买了个相册，但你得自己去买塑料膜并亲手把照片口袋剪裁贴进相册里。)
- **Good Design:** You buy a photo album, and it already has the slots inside. You just slide your pictures directly into the album. (你买了个相册，它自带照片插槽，你直接把照片塞进去就行了。)

# GRASP Pattern 3: Controller

Part 1: Original Context & Chinese Explanation

- **Original Definition:** Assign responsibility for handling system events to a class that represents the overall system or a use case scenario.
- **中文概念翻译：** 将处理系统事件（如点击、提交、请求）的职责，分配给一个代表整个系统或代表特定业务场景（用例）的类。

Why we need it (为什么需要它)

- **UI should be dumb (UI 应该是盲目的)：** The UI layer (buttons, screens) should only capture user input and trigger an event. It should not contain business logic. (UI 界面只负责收集输入和触发点击，绝对不该包含业务逻辑。)
- **Reusability (可复用性)：** If business logic is inside a button, you cannot reuse that logic if you decide to add a Mobile App, a Web CLI, or automated test scripts. (如果业务逻辑写在按钮里，未来想做手机 App 或写自动化测试脚本时，这段逻辑根本没办法复用。)

Part 2: Java Code Comparison

❌ The Bad Way (坏的做法)

```java
public class CheckoutButton {
    // 模拟用户点击界面的结算按钮
    public void onClick() {
        // WHY: 违反控制器原则。UI组件直接包含了核心业务流程（创建订单、扣款、发信、扣库存）。
        // 这导致UI层知道得太多。一旦业务规则改变，必须修改UI代码，导致界面与业务极度耦合，无法复用。
        Order order = new Order();
        order.addItems(cart.getItems());
        order.applyDiscount(discountCode);

        paymentGateway.charge(order.getTotal());
        emailService.sendConfirmation(order);
        inventory.reduce(order.getItems());
    }
}
```

✅ The Good Way (好的做法)

```java
public class CheckoutController {
    private final OrderService orderService;
    private final PaymentGateway paymentGateway;
    private final NotificationService notificationService;

    // WHY: 依赖抽象接口而非具体实现类。
    // 这降低了控制器与具体服务的耦合度，方便以后随意替换底层服务（例如将邮件换成短信通知），同时也让单元测试更容易。
    public CheckoutController(OrderService orderService, PaymentGateway paymentGateway, NotificationService notificationService) {
        this.orderService = orderService;
        this.paymentGateway = paymentGateway;
        this.notificationService = notificationService;
    }

    // WHY: 控制器作为系统事件的单一接收点，只负责对业务流进行整体的协调和编排。
    // 具体的底层脏活累活仍然委派给各自的领域服务。该方法不再依赖任何UI组件，可以被网页、App或测试脚本安全复用。
    public Order checkout(ShoppingCart cart, String discountCode) {
        Order order = this.orderService.createFromCart(cart);

        if (discountCode != null && !discountCode.isEmpty()) {
            order.applyDiscount(discountCode);
        }

        this.paymentGateway.charge(order.getTotal());
        this.notificationService.sendConfirmation(order);

        return order;
    }
}
```

Part 3: UI Interaction with the Controller

```java
public class CheckoutButton {
    // WHY: UI组件不再直接持有各种细碎的业务服务，而是仅仅持有一个控制器的引用。
    // 保持UI层与核心业务细节的隔离。
    private final CheckoutController checkoutController;

    public CheckoutButton(CheckoutController checkoutController) {
        this.checkoutController = checkoutController;
    }

    public void onClick() {
        // WHY: 此时UI层变得非常薄、非常“笨”。它只负责收集必要的数据，然后立刻把职责转交给控制器。
        // 以后不论后端如何修改结账的业务步骤，这里的UI点击事件代码都完全不需要变动。
        Order completedOrder = checkoutController.checkout(currentCart, userDiscountCode);

        showSuccessMessage(completedOrder.getId());
    }
}
```

🎯 Ultimate Summary (一句话总结)

- **Bad UI:** The UI does all the cooking, cleaning, and deliveries by itself. (UI 既当大厨，又当保洁，还送外卖。)
- **Good UI:** The UI is just a waiter. It takes your order and hands it to the **Controller (the Kitchen manager)**. The Kitchen manager tells everyone else what to do. (UI 只是个传菜员。它拿到订单直接扔给**控制器（后厨主管）**，由主管去调度洗菜切菜和做饭。)

# GRASP Pattern 4: Low Coupling (低耦合原则)

Part 1: Original Context & Chinese Explanation

- **Original Definition:** Assign responsibilities so that coupling remains low. Use this principle to evaluate alternatives. Coupling is a measure of how strongly a element is connected to, has knowledge of, or relies on other elements.

- **中文概念翻译：** 在分配职责时，要保证类与类之间的**耦合度（依赖紧密程度）保持在较低水平**。耦合度是用来衡量一个类对另一个类的了解程度、连接紧密程度或依赖程度的指标。

- **Why we need it (为什么需要它)：**

  - **High Coupling destroys systems (高耦合会毁掉系统)：** If Class A is tightly coupled to Class B, any small change in Class B will cause a domino effect, breaking Class A, Class C, and the rest of the application. (如果类 A 强依赖类 B，只要类 B 改动一个变量名，类 A 就会直接编译报错，引发多米诺骨牌式的系统崩溃。)
  - **Isolation for maintenance (隔离以便于维护)：** Low coupling ensures that you can rip out, replace, or update a specific module (like changing a MySQL database to MongoDB) without altering your core business logic classes. (低耦合能确保你可以随时拆卸、替换或升级某个模块——比如把 MySQL 数据库换成 MongoDB，而核心业务代码不需要做任何修改。)

Part 2: Java Code Comparison

为了让你彻底看懂耦合的区别，我们设计一个**电子发票系统（Invoice System）**&#x7684;场景。

❌ The Bad Way (坏的做法)

```java
// ❌ Bad: Core billing system is tightly coupled to a specific email brand implementation
// ❌ 坏的做法：核心计费系统与某一个特定品牌的邮件发送类强行绑定
public class BillingSystem {

    // Reason: Instantiating a concrete, highly specific third-party service directly inside the constructor.
    // WHY: The BillingSystem now HAS to know about SendGridEmailer. If SendGrid goes out of business tomorrow 
    // and you want to switch to Mailchimp, you are forced to rewrite this entire BillingSystem class.
    // 核心原因：直接在内部 new 出具体的第三方服务类。
    // 计费系统现在死死绑定了 SendGrid。明天如果想换成阿里云邮件，你必须把计费系统的源码拆开重写。
    private SendGridEmailer emailer;

    public BillingSystem() {
        this.emailer = new SendGridEmailer(); 
    }

    public void processInvoice(Invoice invoice) {
        // Business logic here...
        System.out.println("Processing invoice: " + invoice.getId());

        // Reason: Calling a method name that is entirely unique to SendGrid's SDK.
        // WHY: If the next provider uses a method named .sendMail(), this line breaks.
        // 原因：调用了针对 SendGrid 自定义的方法名。如果新邮件商的方法叫 sendMail()，这行代码就废了。
        emailer.sendWithSendGridApi(invoice.getCustomerEmail(), "Your Invoice");
    }
}

// 具体的第三方低层服务类
class SendGridEmailer {
    public void sendWithSendGridApi(String to, String subject) {
        System.out.println("Sending email via SendGrid API to " + to);
    }
}
```

✅ The Good Way (好的做法)

```java
// ✅ Good: Use an interface to break the direct connection (Polymorphism lowers coupling)
// ✅ 好的做法：引入一个抽象接口来切断直接联系（利用多态性大幅降低耦合）

// Step 1: Create a generic interface that defines the BEHAVIOR, not the vendor.
// 步骤 1：定义一个通用的通知接口，只定义“行为”，不关心具体是哪家服务商。
interface EmailService {
    void sendEmail(String to, String subject);
}

// Step 2: Implement the interface for specific vendors.
// 步骤 2：让具体的第三方服务商去实现这个接口。
class SendGridEmailerImpl implements EmailService {
    @Override
    public void sendEmail(String to, String subject) {
        System.out.println("Sending email via SendGrid API to " + to);
    }
}

class MailchimpEmailerImpl implements EmailService {
    @Override
    public void sendEmail(String to, String subject) {
        System.out.println("Sending email via Mailchimp Server to " + to);
    }
}

// Step 3: The Core Billing System now depends ONLY on the loose interface.
// 步骤 3：核心计费系统现在仅仅依赖于这个宽松的接口。
public class LowCouplingBillingSystem {

    // Reason: Dependency Injection (DI) using the interface type.
    // WHY: LowCouplingBillingSystem does not know (and does not care) which vendor is being used.
    // It only cares that whatever object is passed in knows how to execute .sendEmail().
    // 原因：通过接口进行依赖注入。
    // 计费系统完全不知道、也不关心背后是哪家供应商。它只要求传进来的对象必须会调用 .sendEmail()。
    private final EmailService emailService;

    public LowCouplingBillingSystem(EmailService emailService) {
        this.emailService = emailService;
    }

    public void processInvoice(Invoice invoice) {
        System.out.println("Processing invoice: " + invoice.getId());

        // Reason: The method call remains identical regardless of the underlying vendor.
        // WHY: You can now swap vendors instantly at runtime without touching a single line of code in this class.
        // 原因：无论底层怎么换，这行调用代码永远不需要变。
        // 你现在可以在运行时一秒钟切换邮件供应商，而当前这个类的源码连一个标点符号都不用改。
        emailService.sendEmail(invoice.getCustomerEmail(), "Your Invoice");
    }
}
```

Part 3: Visualizing how they run in UI/Main (它们在实际运行时长怎样)

为了让你看清低耦合的威力，我们来看看在系统入口（`Main` 类）中，好坏两种做法的使用差异：

```java
public class MainApp {
    public static void main(String[] args) {
        Invoice invoice = new Invoice("INV-1001", "user@example.com");

        // ❌ 坏的做法：无法选择，写死了就是 SendGrid
        BillingSystem badSystem = new BillingSystem();
        badSystem.processInvoice(invoice);

        // ==========================================================

        // ✅ 好的做法：控制权在外部（Main 方法）手里，你可以自由拼装组合！
        // 如果今天想用 SendGrid：
        EmailService sendGrid = new SendGridEmailerImpl();
        LowCouplingBillingSystem goodSystem1 = new LowCouplingBillingSystem(sendGrid);
        goodSystem1.processInvoice(invoice);

        // 如果明天你想换成 Mailchimp，核心类 LowCouplingBillingSystem 根本不用改：
        EmailService mailchimp = new MailchimpEmailerImpl();
        LowCouplingBillingSystem goodSystem2 = new LowCouplingBillingSystem(mailchimp);
        goodSystem2.processInvoice(invoice);
    }
}
```

🎯 Ultimate Summary (一句话总结)

- **High Coupling (高耦合):** You weld the phone charger directly into the wall outlet. If the charger breaks, you must tear down the whole wall. (你把手机充电器直接焊死在墙上的电线里。充电器一旦坏了，你得把整面墙砸了。)
- **Low Coupling (低耦合):** You install a standard USB wall socket. You can plug in an iPhone cable, an Android cable, or a desk lamp anytime you want. (你在墙上装一个标准的通用插座。今天插苹果线，明天插安卓线，后天插台灯，随你心意。)

# GRASP Pattern 5: High Cohesion

Part 1: Original Context & Chinese Explanation

- **Original Definition:** Assign responsibilities so that cohesion remains high. A class should focus on a single, well-defined set of responsibilities.
- **中文概念翻译：** 分配职责时要确保类保持“高内聚”。一个类应当专注于做一件事情，拥有单一且明确的职责。

Why we need it (为什么需要它)

- **Maintainability (易维护性)：** If a class does too many unrelated things, changing one feature risks breaking a completely different feature. (如果一个类做了太多不相关的事，修改 A 功能时很容易意外改坏 B 功能。)
- **Readability & Reusability (易读与可复用)：** Small, focused classes are easier for developers to understand and can be reused in different parts of the system. (小而专注的类更通俗易懂，而且可以轻松被系统的其他模块重复调用。)

Part 2: Java Code Comparison

❌ The Bad Way (坏的做法)

```java
// ❌ Bad: A single class manages data, prints reports, and handles database storage
public class UserProfile {
    private String username;
    private String email;

    // WHY: 严重违反高内聚原则（属于低内聚）。
    // 这个类不仅保存用户信息，还要负责“打印格式化报告”和“连接数据库保存数据”。
    // 一旦未来数据库换了（如 MySQL 换到 MongoDB），或者打印排版变了，你都得来修改这个核心的用户类。
    public void printReport() {
        System.out.println("User Report: " + username + " (" + email + ")");
    }

    public void saveToDatabase() {
        System.out.println("Connecting to Database... Saving " + username);
    }
}
```

✅ The Good Way (好的做法)

```java
// ✅ Good: Focuses ONLY on holding user data
public class UserProfile {
    private final String username;
    private final String email;

    public UserProfile(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() { return username; }
    public String getEmail() { return email; }
}
```

```java
// ✅ Good: Focuses ONLY on printing logic
public class UserReportPrinter {

    // WHY: 将打印职责剥离出来。
    // UserProfile 发生改变或数据库改变时，完全不影响打印逻辑。
    public void print(UserProfile user) {
        System.out.println("User Report: " + user.getUsername() + " (" + user.getEmail() + ")");
    }
}
```

```java
// ✅ Good: Focuses ONLY on database logic
public class UserRepository {

    // WHY: 将持久化（存储）职责剥离出来。
    // 数据库升级或 SQL 语句修改只局限在这个类中，UserProfile 保持绝对的干净。
    public void save(UserProfile user) {
        System.out.println("Connecting to Database... Saving " + user.getUsername());
    }
}
```

Part 3: UI/Runner Interaction with High Cohesion

```java
public class Main {
    public static void main(String[] args) {
        // 创建职责单一的数据对象
        UserProfile user = new UserProfile("Alice", "alice@example.com");

        // WHY: 外部调用者根据需要组合这些各司其职的干净类。
        // 想存盘就找 Repository，想打印就找 Printer。每个工具各司其职，互不干扰。
        UserRepository repo = new UserRepository();
        UserReportPrinter printer = new UserReportPrinter();

        repo.save(user);
        printer.print(user);
    }
}
```

🎯 Ultimate Summary (一句话总结)

- **Bad Design:** A smartphone that also acts as an electric shaver and a microwave. If the microwave wire breaks, your entire phone stops working. (一部同时集成了电动剃须刀和微波炉功能的手机。一旦微波炉的变压器烧了，你连电话都没得打了。)
- **Good Design:** A dedicated smartphone for calling, a separate razor for shaving, and a separate microwave for cooking. If one breaks, the others still work perfectly. (手机只管通信，剃须刀只管刮胡子，微波炉只管热菜。哪怕其中一个坏了，其他两个依然能正常运转。)


# GRASP Pattern 6: Polymorphism

Part 1: Original Context & Chinese Explanation

- **Original Definition:** When related alternatives or behaviors vary by type (class), assign responsibility for the behavior—using polymorphic operations—to the types for which the behavior varies.
- **中文概念翻译：** 多态原则。当同一类行为在不同的类型（类）中有着不同的实现方式时，将该行为的职责分配给对应的具体类型。用多态方法代替繁琐的 `if/else` 或 `switch` 判断。

Why we need it (为什么需要它)

- **Extensibility (极佳的扩展性)：** Adding a new variation or type requires zero changes to the existing client logic. You just create a new class implementing the interface. (增加新类型或新变体时，现有客户端逻辑完全不需要改动，只需要新建一个类实现接口即可。)
- **Prevents Spaghetti Code (避免代码臃肿)：** It eliminates nested conditional blocks that look up type flags, keeping the logic clean and maintainable. (它消除了满屏幕检查类型标识的嵌套条件语句，让核心流程保持干净。)

Part 2: Java Code Comparison

❌ The Bad Way (坏的做法)

```java
// ❌ Bad: Using a flag and if/else conditions to handle different payment types
public class PaymentProcessor {

    // WHY: 严重违反多态原则。每次增加一种新的支付方式（比如加上 ApplePay），
    // 你都必须来修改这个核心类的 processPayment 方法，强行塞入一个 else-if 分支。
    // 这导致代码非常脆弱，极易因为改动现有业务而引入潜在 Bug。
    public void processPayment(String type, double amount) {
        if (type.equals("CREDIT_CARD")) {
            System.out.println("Charging credit card: $" + amount);
        } else if (type.equals("PAYPAL")) {
            System.out.println("Processing PayPal account: $" + amount);
        }
    }
}
```

✅ The Good Way (好的做法)

```java
// ✅ Good: Define a common interface for the behavior
public interface PaymentMethod {
    void pay(double amount);
}
```

```java
// ✅ Good: Move specific behavior into the specialized class
public class CreditCardPayment implements PaymentMethod {
    @Override
    public void pay(double amount) {
        // WHY: 信用卡支付的专属逻辑内聚在自己类中
        System.out.println("Charging credit card: $" + amount);
    }
}
```

```java
// ✅ Good: Move specific behavior into another specialized class
public class PayPalPayment implements PaymentMethod {
    @Override
    public void pay(double amount) {
        // WHY: PayPal 支付的专属逻辑内聚在自己类中
        System.out.println("Processing PayPal account: $" + amount);
    }
}
```

Part 3: UI/Runner Interaction with Polymorphism

```java
public class Main {
    public static void main(String[] args) {
        // 模拟用户在前端界面勾选了 PayPal 支付
        PaymentMethod userChoice = new PayPalPayment();

        // WHY: 外部执行者（或收银台系统）不需要关心底层到底是什么支付卡种。
        // 它只管调用抽象的统一指令 pay()，Java 会根据具体对象的类型自动找到对应正确的代码去执行。
        // 如果以后加上比特币支付，这段 Main 方法一行代码都不需要改。
        userChoice.pay(150.0);
    }
}
```

🎯 Ultimate Summary (一句话总结)

- **Bad Design:** A universal remote control with a unique physical button for every TV brand on earth. If you buy a new TV, you have to buy a whole new remote. (一个万能遥控器，上面为地球上所有电视品牌各做了一个物理按钮。一旦出了新品牌，你就得换个新遥控器。)
- **Good Design:** The remote has one universal "Power" button. It sends a standard signal, and each specific TV model intercepts it and turns itself on in its own way. (遥控器上只有一个通用的“电源”键。它只发通用信号，具体什么电视接收到，就由该电视自己去执行开机。)

# GRASP Pattern 7: Indirection

Part 1: Original Context & Chinese Explanation

- **Original Definition:** Assign the responsibility to an intermediate object to mediate between other components or services, so that they are not directly coupled.
- **中文概念翻译：** 间接性原则（中介原则）。将职责分配给一个中间对象，让它在两个组件或服务之间起到媒介、过渡的作用，从而避免它们之间产生直接的紧密耦合。

Why we need it (为什么需要它)

- **Decoupling (解耦)：** It ensures Component A does not need to know the specific details, names, or locations of Component B. They only talk to the mediator. (确保组件 A 不需要知道组件 B 的具体细节、名称或具体部署位置。它们只和中间人打交道。)
- **Flexibility (极高的灵活性)：** If the ultimate destination provider or third-party vendor changes, you only update the intermediate adapter, leaving your system completely untouched. (如果最终的目的地供应商或第三方接口换了，你只需要修改那个中间适配器，你自己的主系统完全不需要改动。)

Part 2: Java Code Comparison

❌ The Bad Way (坏的做法)

```java
// ❌ Bad: App directly depends on a specific, hardcoded third-party library
public class NotificationManager {
    // 直接硬编码绑定具体的第三方服务商
    private final TwilioSmsClient twilioClient = new TwilioSmsClient();

    // WHY: 严重违反间接性原则。你的系统和外部的 Twilio SDK 产生了直接死绑定。
    // 如果下个月公司嫌 Twilio 费用太贵，要求换成阿里云短信，你必须把这块核心代码整个推倒重写。
    public void sendAlert(String message) {
        twilioClient.sendRawSms("+12345678", "[ALERT] " + message);
    }
}

// 模拟的第三方闭源 SDK 
class TwilioSmsClient {
    public void sendRawSms(String phone, String text) { }
}
```

✅ The Good Way (好的做法)

```java
// ✅ Good: Create an interface acting as the middleman (Indirection boundary)
public interface SmsGateway {
    void send(String msg);
}
```

```java
// ✅ Good: The intermediate adapter wraps the actual third-party component
public class TwilioAdapter implements SmsGateway {
    private final TwilioSmsClient twilioClient = new TwilioSmsClient();

    @Override
    public void send(String msg) {
        // WHY: 让适配器在中间做传话筒，隔绝外部复杂性
        twilioClient.sendRawSms("+12345678", msg);
    }
}
```

```java
// ✅ Good: NotificationManager now talks ONLY to the indirection wrapper
public class NotificationManager {
    private final SmsGateway smsGateway;

    // WHY: 完美遵循间接性原则。通知管理器只认 SmsGateway 这个中间代理。
    // 它根本不知道、也不关心背后真正干活的是 Twilio 还是阿里云。
    public NotificationManager(SmsGateway smsGateway) {
        this.smsGateway = smsGateway;
    }

    public void sendAlert(String message) {
        this.smsGateway.send("[ALERT] " + message);
    }
}
```

Part 3: UI/Runner Interaction with Indirection

```java
public class Main {
    public static void main(String[] args) {
        // 组装依赖：在入口处注入中间代理对象
        SmsGateway gateway = new TwilioAdapter();
        NotificationManager manager = new NotificationManager(gateway);

        // WHY: 核心业务组件发消息时非常轻松。
        // 明天如果要把底层换成阿里云短信，我们只需要新写一个 AliCloudAdapter，
        // 然后在 Main 入口处替换一行即可：SmsGateway gateway = new AliCloudAdapter();
        manager.sendAlert("System temperature high!");
    }
}
```

🎯 Ultimate Summary (一句话总结)

- **Bad Design:** You want to rent an apartment, so you knock on the doors of every house in the city one by one to ask the owners if they want to rent to you. (你想租房子，于是自己在全城挨家挨户敲门去问房东租不租，直接和所有房东死磕。)
- **Good Design:** You go to a **Real Estate Agency (Indirection)**. You tell the agency your requirements, and the agency matches you with the right landlord behind the scenes. (你去找**房屋中介（间接层）**。你向中介提要求，中介在背后去帮你搞定房东，你和房东互不认识。)


# GRASP Pattern 8: Pure Fabrication

Part 1: Original Context & Chinese Explanation

- **Original Definition:** Assign a highly cohesive set of responsibilities to a fictional or artificial class that does not represent a problem domain concept, purely to support high cohesion, low coupling, and reuse.
- **中文概念翻译：** 纯虚构原则。当现实业务领域中找不到合适的类来承担某些职责时，凭空虚构一个现实中不存在的类（如各种 Utility, Service, Helper, DAO），专门用来承担这些职责，从而确保系统的其他核心类维持高内聚、低耦合。

Why we need it (为什么需要它)

- **Prevents Domain Pollution (保护业务模型干净)：** It stops you from forcing technical logic (like file I/O, database queries, or encryption) into clean domain objects like `User` or `Product`. (防止把纯技术实现的代码（如数据库读写、文件输入输出、加密算法）硬塞进干净的业务类中，避免污染业务模型。)
- **Keeps Cohesion High (维持高内聚)：** Instead of making a domain class a "jack-of-all-trades," tech-heavy workflows are cleanly offloaded into dedicated service classes. (不需要让一个业务对象变成“全能杂家”，把重度依赖技术实现的操作干净地剥离到专属的工具或服务类中。)

Part 2: Java Code Comparison

❌ The Bad Way (坏的做法)

```java
// ❌ Bad: Forcing a real-world concept class to handle technical database infrastructure
public class Customer {
    private String id;
    private String name;

    // WHY: 严重违反纯虚构原则。在真实世界中，“顾客”这个人根本不会自己把自己塞进数据库。
    // 把数据库的 SQL 语句和连接逻辑硬写在 Customer 类里面，会导致业务实体和底层数据库框架严重死绑定。
    // 这破坏了高内聚，也让 Customer 对象在没有数据库环境的情况下完全无法进行单元测试。
    public void saveToDatabase() {
        String sql = "INSERT INTO customers (id, name) VALUES ('" + id + "', '" + name + "')";
        System.out.println("Executing SQL against database: " + sql);
    }
}
```

✅ The Good Way (好的做法)

```java
// ✅ Good: Keep the real-world concept class pure and focused only on business data
public class Customer {
    private final String id;
    private final String name;

    public Customer(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() { return id; }
    public String getName() { return name; }
}
```

```java
// ✅ Good: Pure Fabrication. This class represents a purely artificial technical concept.
public class CustomerRepository {

    // WHY: 完美遵循纯虚构原则。CustomerRepository（数据仓库）在现实世界中并不存在。
    // 它是软件工程为了解决持久化问题而凭空捏造出来的实体。
    // 这样，底层的 SQL 执行和数据库连接就被干净地锁定在这个技术层类里，丝毫不干扰纯洁的 Customer 业务对象。
    public void save(Customer customer) {
        String sql = "INSERT INTO customers (id, name) VALUES ('" + customer.getId() + "', '" + customer.getName() + "')";
        System.out.println("Executing SQL cleanly inside Repository: " + sql);
    }
}
```

Part 3: UI/Runner Interaction with Pure Fabrication

```java
public class Main {
    public static void main(String[] args) {
        // 创建业务实体
        Customer customer = new Customer("C001", "Bob");

        // 调用凭空虚构的技术工具类
        CustomerRepository repo = new CustomerRepository();

        // WHY: 外部调用者只需要清晰地将“业务实体”打包传给“纯虚构的持久化服务”即可。
        // 两者职责清晰分明，以后不管是升级数据库还是重构 Customer 属性，都可以互不干扰。
        repo.save(customer);
    }
}
```

🎯 Ultimate Summary (一句话总结)

- **Bad Design:** A real-world business contract document that somehow possesses arms and legs, walks itself over to the office safe, opens the combination lock, and jumps inside. (一份真实的业务合同纸张自己长出了手和脚，自己走到保险箱前输入密码，然后自己跳进去把自己存起来。)
- **Good Design:** The contract paper remains just a quiet piece of information. You invent an artificial tool called a **File Cabinet (Pure Fabrication)** whose only job in the office is to hold and organize contracts. (合同纸安安静静地只当数据。你凭空发明了一个叫作\*\*文件柜（纯虚构）\*\*的工具，由它来专门负责收纳和保管合同。)

# GRASP Pattern 9: Protected Variations

Part 1: Original Context & Chinese Explanation

- **Original Definition:** Identify points of predicted instability or variation and assign responsibilities to create a stable interface around them.
- **中文概念翻译：** 受保护变化原则。找出系统中那些容易发生变化或极不稳定的核心点（如第三方接口变动、算法升级、配置变更），并围绕这些不稳定的点构建一层稳定、统一的接口层，从而隔离和保护系统其余部分不因底层变化而崩溃。

Why we need it (为什么需要它)

- **Shields from Chaos (杜绝连锁反应崩盘)：** It guarantees that when an unstable part of your system changes, the blast radius is restricted to just one single file, leaving 99% of your codebase safe and untouched. (确保系统内那些反复无常的模块在变动时，产生的冲击波被死死挡在接口外，其余 99% 的核心代码连一个标点符号都不需要变。)
- **Overshadows Open-Closed Principle (完美践行开闭原则)：** It is the core implementation strategy behind the Open-Closed Principle (OCP) in SOLID design. (它是软件开发中“对扩展开放，对修改关闭”最核心的底层演练套路。)

Part 2: Java Code Comparison

❌ The Bad Way (坏的做法)

```java
// ❌ Bad: High vulnerability. The system is directly naked and exposed to an unstable variable.
public class TaxCalculator {

    // WHY: 严重违反受保护变化原则。税率计算规则（或者外部税务局提供的 API）在业务上是极度频繁变化的。
    // 如果你直接把具体的硬编码税率算法暴露并绑定在核心代码中，一旦下个月国家出台新税率政策，
    // 你的核心计算方法就得彻底重构，这导致整套软件在外部变化面前毫无防御能力。
    public double calculateTaxForRegion(String region, double amount) {
        if (region.equals("US")) {
            return amount * 0.08; // 极其不稳定的硬编码规则
        } else {
            return amount * 0.15;
        }
    }
}
```

✅ The Good Way (好的做法)

```java
// ✅ Good: Wrap the unstable point behind a stable, unmoving structural interface
public interface TaxStrategy {
    double determineTax(double income);
}
```

```java
// ✅ Good: Protect the core system by moving the specific variant out into its own leaf class
public class UsTaxStrategy implements TaxStrategy {
    @Override
    public double determineTax(double income) {
        // WHY: 具体的易变业务规则被收拢在叶子节点里
        return income * 0.08;
    }
}
```

```java
// ✅ Good: The core manager is now 100% protected against future variation rules
public class TaxCalculator {
    private final TaxStrategy taxStrategy;

    // WHY: 完美遵循受保护变化原则。TaxCalculator 现在完全依托于一条“永远不会变的稳定接口线”开展工作。
    // 至于背后的扣税算法明天要改成 10% 还是 20%，甚至是接入外部税务局的 Web API 升级，
    // 这条接口线都不会断，核心控制器 TaxCalculator 的逻辑永远安全，永远不需要被修改。
    public TaxCalculator(TaxStrategy taxStrategy) {
        this.taxStrategy = taxStrategy;
    }

    public double compute(double baseAmount) {
        return this.taxStrategy.determineTax(baseAmount);
    }
}
```

Part 3: UI/Runner Interaction with Protected Variations

```java
public class Main {
    public static void main(String[] args) {
        // 动态配置阶段：根据当前上下文选择对应的税率策略
        TaxStrategy currentLaw = new UsTaxStrategy();
        TaxCalculator calculator = new TaxCalculator(currentLaw);

        // WHY: 外部核心流程极其稳固。
        // 未来国家突然决定加入一套复杂的“累进税率算法”，你只需要新写一个类 ProgressiveTaxStrategy implements TaxStrategy，
        // 在 Main 入口一换即可。核心 TaxCalculator 逻辑完美免疫了外部需求的无常轰炸。
        double finalTax = calculator.compute(50000.0);
    }
}
```

🎯 Ultimate Summary (一句话总结)

- **Bad Design:** A house built with water pipes directly cast inside the thick concrete foundation walls. If a pipe cracks or you want to upgrade to a wider pipe, you have to smash down the entire foundation of the house. (把房屋的自来水管直接紧紧浇筑在钢筋混凝土的地基墙体里。一旦水管漏水或你想换一根粗管，你就必须把整座房子的承重墙全部炸掉。)
- **Good Design:** You install standard **Pipe Conduits and Adapter Valves (Protected Variations)**. The external walls are safe. If a water pipe fails or changes, you simply unscrew the valve, pull out the broken pipe through the conduit, and slide a new one in without moving a single brick. (你在墙里预留标准**管套和转接阀门（受保护变化）**。墙体稳如泰山，不管里面的水管怎么换、怎么坏，你只需要拧开阀门抽换管子，不需要动到房屋任何一块砖。)