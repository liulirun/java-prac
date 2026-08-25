# Abstract Classes vs Interfaces
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

# Java 21 Added Value: Sealed Interfaces & Pattern Matching
Java 21 Sealed Types & Pattern Matching

Part 1: Original Context & Chinese Explanation

- **The Modern Shift:** Java 21 allows you to combine `sealed` interfaces (closed hierarchies) and `record` types (immutable data carriers) with pattern-matching `switch` expressions.

- **中文概念翻译：** 密封接口 + 记录类型 + 模式匹配。这套组合拳允许你构建一个**彻底封闭**的数据分类。编译器在编译时就能百分之百确定整个系统中“只存在这几种特定的子类”。

- **What it solves (它解决的核心痛点)：**

  - **Eliminates Unsafe Defaults (干掉不安全的默认兜底分支)：** Traditional switches always need a `default` case because the compiler doesn't know if new subclasses were added. Java 21 enforces completeness at compile-time. (传统的类型判断必须写 `default` 兜底。Java 21 让编译器直接化身保安，少写任何一个子类的情况直接拒绝编译，彻底在编译期砸碎潜在的漏写 Bug。)
  - **Removes Manual Typecasting (消除臃肿的手动强转)：** It merges type checking and object casting into a single atomic action. (它把“判断类型”和“强制类型转换”合并成一步完成，代码极度清爽。)

Part 2: Java Code Comparison

❌ The Old Way (Without Java 21 Features)

```java
import java.util.Objects;

// ❌ Traditional open interface - any developer can create a new implementation anywhere
interface OrderState {}

class Processing implements OrderState {}
class Shipped implements OrderState {
    private final String trackingId;
    public Shipped(String trackingId) { this.trackingId = trackingId; }
    public String getTrackingId() { return trackingId; }
}

public class OrderHandler {
    public String handle(OrderState state) {
        // WHY: 必须痛苦地使用手动类型检查 (instanceof) 以及丑陋的向下强制类型转换。
        if (state instanceof Processing) {
            return "Order is being packed.";
        } 
        else if (state instanceof Shipped) {
            // WHY: 必须手动强转才能拿到 Shipped 的专属属性，代码冗长难看。
            Shipped s = (Shipped) state; 
            return "Order shipped! Tracking: " + s.getTrackingId();
        }

        // WHY: 致命缺陷！因为接口是开放的，编译器根本不知道未来还会不会有其他子类。
        // 你必须被迫写一个完全多余或者只能抛异常的 else 块来应付编译器。
        // 如果下个月同事偷偷加了一个 Cancelled 类，这里不会报任何编译错误，直接进入 else 导致生产事故！
        else {
            throw new IllegalArgumentException("Unknown state!");
        }
    }
}
```

✅ The Modern Way (With Java 21 Sealed & Patterns)

```java
// ✅ Good: Explicitly close the boundary. Only these two states can ever exist!
public sealed interface OrderState permits Processing, Shipped {}

// ✅ Good: Use records to instantly eliminate constructors and getters boilerplate
public record Processing() implements OrderState {}
public record Shipped(String trackingId) implements OrderState {}

public class OrderHandler {
    public String handle(OrderState state) {

        // WHY: 使用 Java 21 模式匹配 switch。类型检查与变量绑定合二为一。
        // 看到 `Shipped s` 了吗？判断通过后，变量 s 已经直接是强转好的具体对象，直接调用 s.trackingId()！
        return switch (state) {
            case Processing p -> "Order is being packed.";
            case Shipped s    -> "Order shipped! Tracking: " + s.trackingId();

            // WHY: 100% 免疫漏写 Bug！因为接口使用了 sealed，Java 明确知道除了这两者绝无第三方。
            // 这里不需要写任何 `default` 兜底分支！代码干净利落。
            // 奇迹发生在这里：如果未来有人在 sealed interface 后面追加了 `permits ..., Cancelled`，
            // 这个 switch 块会【瞬间报错拒绝编译】，逼迫你必须把 Cancelled 的情况补齐才能运行。
        };
    }
}
```

Part 3: UI/Runner Interaction with Modern Hierarchy

```java
public class Main {
    public static void main(String[] args) {
        OrderHandler handler = new OrderHandler();

        // 模拟接收到不同的轻量级状态记录
        OrderState packedState = new Processing();
        OrderState dispatchedState = new Shipped("TRACK12345");

        // WHY: 外部调用及其安全、优雅。
        // 业务数据（Record）只管清清白白地装载数据，处理逻辑（switch）完美内聚在 Handler 中。
        // 彻底实现了数据流与控制流的完美类型安全分离。
        System.out.println(handler.handle(packedState));
        System.out.println(handler.handle(dispatchedState));
    }
}
```

🎯 Ultimate Summary (一句话总结)

- **Without Java 21:** You are sorting mail, but anyone can invent a custom envelope type at any moment. You have to open every letter manually to guess what it is, and constantly prepare a trash bin (`default -> throw`) for unexpected mail shapes. (以前就像分发信件，任何人随时都能发明新信封，你必须肉眼检查、手动裁剪，还要时刻准备一个垃圾桶处理未知形状的诡异信件。)
- **With Java 21:** The post office issues a strict rule: only two legal sizes of envelopes exist (`sealed`). The sorter instantly routes them based on their exact shape, without needing any manual opening, and the machine physically refuses to accept any unknown envelope shape at the door. (现在邮局下了铁律：全天下只准有这两种规格的信封。分拣机瞬间按外形精准分流，不需任何拆箱拆包动作，外来未知规格的信封在投递的第一秒就会被大门彻底卡死。)