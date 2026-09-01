# The Singleton Pattern

## Principle Summary / 原则总结

The Singleton Pattern controls access to one shared instance through a single
creation point. In test automation, use it only for genuinely process-wide or
worker-wide resources, and prefer a thread-local instance when tests run in
parallel.

Singleton 模式通过统一的创建入口控制一个共享实例。在测试自动化中，只应将它用于
真正需要进程级或 worker 级共享的资源；并行测试时通常应优先使用线程隔离实例。

### The Principle in One Sentence / 一句话原则

**Create one controlled resource per required scope, and expose one access point to it.**

**在需要的作用域内只创建一个受控资源，并提供统一访问入口。**

### Learning Model / 学习模型

`Test worker asks for resource → Singleton creates it once → Tests reuse the scoped resource → Lifecycle closes it`

`测试 worker 请求资源 → Singleton 只创建一次 → 测试复用该作用域资源 → 生命周期结束时关闭资源`

### Implementation Sequence / 实现顺序

1. **Confirm the scope / 确认作用域** — Decide whether the resource is process-wide, worker-wide, or thread-wide. / 确认资源是进程级、worker 级还是线程级。
2. **Hide construction / 隐藏构造过程** — Make direct construction impossible and centralize creation. / 禁止外部直接创建，并集中管理实例创建。
3. **Make lifecycle explicit / 明确生命周期** — Define when the instance is initialized, reused, reset, and closed. / 明确实例何时初始化、复用、重置和关闭。
4. **Protect parallel tests / 保护并行测试** — Use `ThreadLocal` or a test-worker scope for mutable browser and test state. / 对可变浏览器及测试状态使用 `ThreadLocal` 或 worker 作用域。
5. **Keep it narrow / 保持职责单一** — The Singleton owns access and lifecycle, not test assertions or business behavior. / Singleton 只负责访问和生命周期，不负责测试断言或业务行为。

### 0. Compact Playwright/API Testing Pseudo-code / 精简 Playwright/API 测试伪代码

```java
// Stable access contract / 稳定的访问契约
interface BrowserSessionProvider {
    BrowserSession current();
}

final class WorkerBrowser implements BrowserSessionProvider {
    // One browser context per parallel worker, not one mutable browser for every test.
    // 每个并行 worker 一个浏览器上下文，而不是所有测试共享一个可变浏览器。
    private static final WorkerBrowser INSTANCE = new WorkerBrowser();
    private static final ThreadLocal<BrowserSession> SESSION = new ThreadLocal<>();

    private WorkerBrowser() {} // Prevent uncontrolled construction / 禁止随意 new

    static WorkerBrowser instance() { return INSTANCE; }

    public BrowserSession current() {
        if (SESSION.get() == null) {
            SESSION.set(startIsolatedSession()); // Create lazily / 延迟创建
        }
        return SESSION.get();
    }

    public void close() {
        if (SESSION.get() != null) SESSION.get().close();
        SESSION.remove();
    }
}

// Context depends on the provider, not on browser construction details.
// Context 依赖 provider，而不是依赖浏览器的创建细节。
class CheckoutTestContext {
    CheckoutTestContext(BrowserSessionProvider sessions) { this.sessions = sessions; }
    void openCheckout() { sessions.current().page().goto("/checkout"); }
}
```

**Essential implementation points / 核心实现要点**

| English | 中文 |
| --- | --- |
| Choose the smallest valid sharing scope. | 选择最小且正确的共享作用域。 |
| Prevent callers from creating competing instances. | 防止调用方创建多个相互竞争的实例。 |
| Separate resource access from resource lifecycle. | 将资源访问与资源生命周期分离。 |
| Never share mutable test state across parallel workers. | 永远不要让并行 worker 共享可变测试状态。 |
| Provide reset/close behavior so one test run cannot poison the next. | 提供重置/关闭机制，避免一次测试污染下一次测试。 |

## SDET Applications / SDET 应用

1. **Parallel Playwright workers / 并行 Playwright worker** — Use a thread-local browser-session provider when each worker needs one reusable session, while preventing pages and cookies from leaking between workers.

   ```java
   sessions = WorkerBrowser.instance();
   beforeEach(() -> sessions.current().newPage());
   afterWorker(() -> sessions.close());
   ```

2. **API environment configuration / API 环境配置** — Use one immutable configuration snapshot per test worker when every API client needs the same base URL, timeout, and environment flags.

   ```java
   config = TestConfiguration.forWorker();
   usersApi = new UsersApi(config);
   ordersApi = new OrdersApi(config);
   assertThat(config.baseUrl()).isEqualTo(usersApi.baseUrl());
   ```

3. **Test-run resource registry / 测试运行资源注册表** — Use a scoped registry when setup creates temporary tenants or containers and teardown must find each resource exactly once.

   ```java
   registry = TestResourceRegistry.forCurrentRun();
   tenant = tenantApi.create("strategy-test");
   registry.track(tenant.id(), () -> tenantApi.delete(tenant.id()));
   afterRun(() -> registry.cleanup());
   ```

## Good vs. Bad Comparison / 好的实现 vs. 糟糕的实现

| Area / 维度 | 🔴 Bad: uncontrolled global / 差：无控制的全局对象 | 🟢 Good: scoped Singleton / 好：有作用域的 Singleton |
| --- | --- | --- |
| **Scope / 作用域** | One mutable driver or client is shared by every thread. **所有线程共享一个可变 driver 或 client。** | The instance matches the required process, worker, or thread scope. **实例作用域与进程、worker 或线程需求匹配。** |
| **Lifecycle / 生命周期** | Construction and cleanup are hidden in random test code. **创建和清理散落在测试代码中。** | Creation, reuse, reset, and close are explicit. **创建、复用、重置和关闭都很明确。** |
| **Parallel safety / 并行安全** | Cookies, pages, or mutable configuration leak across tests. **Cookie、页面或可变配置在测试间泄漏。** | Mutable state is isolated per worker or thread. **可变状态按 worker 或线程隔离。** |
| **Testability / 可测试性** | Tests hard-code static calls and cannot substitute a fake resource. **测试硬编码静态调用，无法替换 fake 资源。** | Tests depend on a provider interface that can be substituted. **测试依赖 provider 接口，可以替换实现。** |
| **When adding behavior / 增加行为** | The Singleton becomes a god object containing clients, assertions, and business rules. **Singleton 变成包含客户端、断言和业务规则的上帝对象。** | The Singleton only controls one resource's access and lifecycle. **Singleton 只控制一种资源的访问和生命周期。** |

## When to Use / 何时使用

Use Singleton only when duplicate instances would be incorrect, expensive, or
unsafe and the sharing scope is explicit. Do not use it merely to avoid passing
dependencies into tests; dependency injection is usually clearer.

只有在重复创建实例会导致错误、成本过高或不安全，并且共享作用域明确时才使用
Singleton。不要仅仅为了避免向测试传递依赖而使用它；依赖注入通常更清晰。
