# The Template Method Pattern

## Principle Summary / 原则总结

The Template Method Pattern defines the fixed skeleton of an algorithm in a
base class and lets subclasses customize selected steps. The template controls
the order; extension points control the variable details.

Template Method 模式在基类中定义算法的固定骨架，并允许子类定制指定步骤。模板方法
控制顺序；扩展点控制变化细节。

### The Principle in One Sentence / 一句话原则

**Fix the test lifecycle order, and leave only approved steps customizable.**

**固定测试生命周期顺序，只开放经过批准的步骤进行定制。**

### Learning Model / 学习模型

`Template method → shared setup → subclass step → shared cleanup → report`

`模板方法 → 共享准备 → 子类步骤 → 共享清理 → 报告`

### Implementation Sequence / 实现顺序

1. **Find the invariant sequence / 找出固定顺序** — Identify steps every test suite must execute in the same order. / 找出所有测试套件都必须按相同顺序执行的步骤。
2. **Create a final template / 创建 final 模板** — Prevent subclasses from skipping or reordering critical lifecycle steps. / 防止子类跳过或重新排列关键生命周期步骤。
3. **Expose hooks / 暴露 hook** — Make variable steps abstract or safely overridable. / 将可变步骤定义为抽象方法或安全的可覆盖方法。
4. **Keep shared controls centralized / 集中共享控制** — Own retries, cleanup, reporting, and failure handling in the base workflow. / 在基类流程中统一管理重试、清理、报告和失败处理。
5. **Keep subclasses focused / 保持子类专注** — A subclass describes scenario-specific actions, not lifecycle policy. / 子类只描述场景动作，不负责生命周期策略。

### 0. Compact API/Playwright Testing Pseudo-code / 精简 API/Playwright 测试伪代码

```java
abstract class ApiContractSuite {
    // Fixed order / 固定顺序: subclasses cannot change the lifecycle.
    public final void run() {
        createClient();
        seedData();
        executeScenario();       // Extension point / 扩展点
        verifyContract();
        cleanupData();
    }

    protected abstract void executeScenario();
    protected abstract void verifyContract();
    protected void createClient() { /* shared setup / 共享准备 */ }
    protected void seedData() { /* shared fixture / 共享 fixture */ }
    protected void cleanupData() { /* guaranteed cleanup / 确保清理 */ }
}

class OrdersApiSuite extends ApiContractSuite {
    protected void executeScenario() { response = api.get("/orders"); }
    protected void verifyContract() { assertThat(response).hasStatus(200); }
}

new OrdersApiSuite().run();
```

**Essential implementation points / 核心实现要点**

| English | 中文 |
| --- | --- |
| Separate invariant lifecycle steps from variable test steps. | 将固定生命周期步骤与可变测试步骤分离。 |
| Make the template method `final` when order is a safety rule. | 当顺序是安全规则时，将模板方法设为 `final`。 |
| Keep hooks small and intentional. | 保持 hook 小而明确。 |
| Put cleanup in the template so every subclass receives it. | 将清理放在模板中，确保每个子类都执行。 |
| Use inheritance for a genuinely shared lifecycle, not merely for code reuse. | 只有生命周期确实共享时使用继承，不要仅为复用代码而使用。 |

## SDET Applications / SDET 应用

1. **API CRUD contract suites / API CRUD 契约套件** — Use a template when every resource suite must create an authenticated fixture, execute a resource scenario, validate the contract, and delete data in that order.

   ```java
   class ProductsSuite extends ApiContractSuite {
       executeScenario() { response = productsApi.get("/products"); }
       verifyContract() { assertThat(response).hasJsonPath("items[].sku"); }
   }
   ```

2. **Playwright smoke suites / Playwright 冒烟套件** — Use a template when every browser suite must start tracing, create an isolated context, run the scenario, capture evidence on failure, and close the context.

   ```java
   class LoginSmokeSuite extends BrowserSuiteTemplate {
       executeScenario() { loginPage.loginAs("standard-user"); }
       verifyOutcome() { expect(homePage.title()).toBe("Home"); }
   }
   ```

3. **API eventual-consistency suites / API 最终一致性套件** — Use a template when all tests must submit a command, poll within a timeout, assert the final state, and remove the test resource, while only the command and state assertion vary.

   ```java
   class ShipmentSuite extends EventualStateSuite {
       submitCommand() { shipmentApi.create(orderId); }
       assertFinalState() { assertThat(shipmentApi.get(id)).hasStatus("READY"); }
   }
   ```

## Good vs. Bad Comparison / 好的实现 vs. 糟糕的实现

| Area / 维度 | 🔴 Bad: duplicated lifecycle / 差：重复生命周期 | 🟢 Good: template lifecycle / 好：模板生命周期 |
| --- | --- | --- |
| **Order / 顺序** | Each test decides its own setup and cleanup order. **每个测试自行决定准备和清理顺序。** | The template enforces one safe order. **模板强制执行一个安全顺序。** |
| **Cleanup / 清理** | A failed test can skip cleanup or leave data behind. **测试失败可能跳过清理并留下数据。** | Cleanup belongs to the shared template. **清理属于共享模板。** |
| **Variation / 变化** | Subclasses or suites rewrite the entire workflow. **子类或测试套件重写整个流程。** | Subclasses override only approved hooks. **子类只覆盖允许的 hook。** |
| **Consistency / 一致性** | Reporting, tracing, and retries differ across suites. **不同套件的报告、追踪和重试不一致。** | Shared lifecycle behavior is applied uniformly. **共享生命周期行为统一应用。** |
| **Design risk / 设计风险** | An overridable lifecycle method can be reordered accidentally. **可覆盖的生命周期方法可能被误排序。** | A `final` template protects critical control flow. **`final` 模板保护关键控制流。** |

## When to Use / 何时使用

Use Template Method when the sequence is stable and the variable steps belong
naturally to a family of related suites. Prefer composition when variations are
independent algorithms or when inheritance would create a rigid hierarchy.

当流程顺序稳定，且变化步骤自然属于同一类测试套件时使用 Template Method。如果变化
是相互独立的算法，或继承会造成僵化层级，则优先使用组合。
