
# The Strategy Pattern - Behaviour Pattern!

## The Principle in One Sentence / 一句话原则

**Keep the workflow stable; move the changing algorithm into an interchangeable strategy.**
**业务目的不变、但执行方式会变化**时使用 Strategy.
**保持流程稳定；把经常变化的算法移入可替换的策略。**


## What This Pattern Achieves / 此模式解决什么问题

- **Low coupling / 低耦合：** The stable test flow does not depend on every algorithm. / 稳定的测试流程不依赖所有算法的细节。
- **Reuse / 复用：** One API or Playwright flow can run with multiple strategies. / 一套 API 或 Playwright 流程可以搭配多个策略运行。
- **Safe extension / 安全扩展：** Add a new strategy instead of editing a large conditional block. / 通过新增策略扩展，而不是修改庞大的条件分支。


## API Testing Application / API 测试应用

> **Real API Framework Use Case:** When one API test suite must call endpoints protected by different authentication methods, such as OAuth2, API keys, and mutual TLS.
>
> **真实 API 框架使用场景：** 当同一套 API 测试需要访问不同认证方式保护的端点，例如 OAuth2、API Key 和双向 TLS（mTLS）时使用。

📊 **Implementation Summary / 实现总结**

| Step / 步骤 | What to do / 做什么 | Real-World Application in API Testing / API 测试中的实际应用 |
| --- | --- | --- |
| **1. Interface / 接口** | Define one `apply(request)` contract. / 定义统一的 `apply(request)` 契约。 | Every authentication type adds credentials in the same way. / 所有认证方式都用同一种方式为请求添加凭证。 |
| **2. Strategies / 策略** | Put each authentication algorithm in its own class. / 每种认证算法独立成一个类。 | Implement `OAuth2Strategy`, `ApiKeyStrategy`, and `MtlsStrategy`. / 实现三个具体认证策略。 |
| **3. Context / 上下文** | Inject the strategy into the client; keep the client blind. / 将策略注入客户端，让客户端保持无感知。 | The HTTP client sends requests without `if/else` for authentication. / HTTP 客户端发送请求时不写认证 `if/else`。 |

### 0. 💻 Compact API Testing Pseudo-code / 精简 API 测试伪代码

```java
// 1) Stable contract: the Context only knows this interface.
//    稳定契约：Context（上下文）只依赖这个接口。
interface AuthStrategy { void apply(Request req); }

// 2) One concrete strategy = one algorithm or behavior variation.
//    一个具体策略 = 一种算法或行为变体。
class OAuth2Strategy implements AuthStrategy {
    void apply(Request r) { r.header("Authorization", "Bearer " + token); }
}
class ApiKeyStrategy implements AuthStrategy {
    void apply(Request r) { r.header("x-api-key", key); }
}
class MtlsStrategy implements AuthStrategy {
    void apply(Request r) { r.useCertificate(clientCert); }
}

// 3) Context receives the interface, not a concrete class.
//    Context 注入接口，而不是依赖某个具体策略类。
class ApiClient {
    ApiClient(AuthStrategy auth) { this.auth = auth; }
    Response send(Request req) {
        auth.apply(req);                 // 4) Delegate; no auth if/else in Context.
                                         //    委托执行；Context 中不写认证 if/else。
        return http.execute(req);        // 5) Keep assertions in the test layer.
                                         //    将断言保留在测试层。
    }
}

// 6) Select the strategy at test setup; keep the test flow unchanged.
//    在测试准备阶段选择策略；保持测试流程不变。
client = new ApiClient(new OAuth2Strategy(token));
response = client.send(request);
```

**Essential implementation points / 核心实现要点**

| English | 中文 |
| --- | --- |
| Identify one goal with multiple interchangeable behaviors. | 找出一个目标，以及实现该目标的多种可替换行为。 |
| Define the smallest common interface for those behaviors. | 为这些行为定义最小且统一的接口。 |
| Put one complete behavior in each concrete strategy. | 每个具体策略只负责一种完整行为。 |
| Inject the interface into the Context from the test setup. | 在测试准备阶段把接口注入 Context。 |
| Let the Context delegate; do not put strategy-selection `if/else` inside it. | 让 Context 负责委托；不要在 Context 内部写策略选择 `if/else`。 |
| Reuse the same test flow and change only the injected strategy. | 复用同一套测试流程，只替换注入的策略。 |

### Three Real-Life Scenarios / 三个真实使用场景

Each example below has a different changing algorithm. The test's business
assertion stays the same while the injected strategy changes the test behavior.
下面每个示例都有不同的可变算法；测试的业务断言保持不变，只替换注入的策略。

1. **API reliability test: retry semantics / API 可靠性测试：重试语义**

   Use Strategy when a test client calls APIs with different retry rules. A
   read-only inventory request may retry a `429` or transient `503`, but a
   payment `POST` must not retry automatically because it could create a
   duplicate charge. The endpoint test should not contain these transport
   decisions.

   当测试客户端访问不同接口、而每个接口的重试规则不同时使用策略：库存查询可以对
   `429` 或临时 `503` 重试，但支付 `POST` 不能自动重试，以免重复扣款。

   ```java
   interface RetryStrategy {
       Response execute(Supplier<Response> request);
   }

   class ExponentialBackoff implements RetryStrategy {
       Response execute(Supplier<Response> request) {
           // Retry only transient responses, with a bounded attempt count.
           return retry429Or503(request, maxAttempts = 3, backoff = EXPONENTIAL);
       }
   }

   class NoRetry implements RetryStrategy {
       Response execute(Supplier<Response> request) { return request.get(); }
   }

   class ApiTestClient {
       ApiTestClient(RetryStrategy retry) { this.retry = retry; }
       Response get(String path) { return retry.execute(() -> http.get(path)); }
       Response post(String path) { return retry.execute(() -> http.post(path)); }
   }

   inventoryTest = new ApiTestClient(new ExponentialBackoff());
   assertThat(inventoryTest.get("/inventory")).hasStatus(200);

   paymentTest = new ApiTestClient(new NoRetry());
   assertThat(paymentTest.post("/payments")).hasStatus(201);
   ```

2. **Playwright checkout test: payment flows / Playwright 结账测试：支付流程**

   Use Strategy when the checkout journey is stable but the payment interaction
   varies by card, PayPal, or bank transfer. The test keeps one end-to-end flow
   and injects the payment behavior selected by the scenario data; adding a new
   payment provider does not create another copy of the checkout test.

   当结账主流程稳定、但银行卡、PayPal 和银行转账的支付操作不同时使用策略。
   测试只保留一份端到端流程，通过场景数据注入支付行为。

   ```java
   interface PaymentFlow {
       void complete(Page page, TestOrder order);
   }

   class CardPayment implements PaymentFlow {
       void complete(Page page, TestOrder order) {
           page.getByLabel("Card number").fill(order.cardNumber());
           page.getByRole(BUTTON, name = "Pay").click();
       }
   }

   class PayPalPayment implements PaymentFlow {
       void complete(Page page, TestOrder order) {
           page.getByRole(BUTTON, name = "PayPal").click();
           page.getByRole(BUTTON, name = "Approve").click();
       }
   }

   class CheckoutTest {
       void completesOrder(PaymentFlow payment) {
           page.goto("/checkout");
           page.getByTestId("shipping-address").fill("10 Main Street");
           payment.complete(page, order);
           expect(page.getByTestId("order-confirmation")).toBeVisible();
       }
   }

   new CheckoutTest().completesOrder(new CardPayment());
   new CheckoutTest().completesOrder(new PayPalPayment());
   ```

3. **API contract test: response-version rules / API 契约测试：响应版本规则**

   Use Strategy when `/orders` serves API v1 and v2. Both versions must return
   the same business data, but their field names and required metadata differ.
   Inject the version-specific contract validator so the test does not become a
   growing block of `if (version == ...)` assertions.

   当 `/orders` 同时提供 API v1 和 v2、但字段名及必需元数据不同时使用策略。注入
   对应版本的契约校验器，避免测试中不断增加 `if (version == ...)` 断言。

   ```java
   interface OrdersContract {
       void assertValid(Response response);
   }

   class OrdersV1Contract implements OrdersContract {
       void assertValid(Response response) {
           assertThat(response).hasJsonPath("data[].id");
           assertThat(response).hasJsonPath("data[].total");
       }
   }

   class OrdersV2Contract implements OrdersContract {
       void assertValid(Response response) {
           assertThat(response).hasJsonPath("orders[].orderId");
           assertThat(response).hasJsonPath("orders[].amount");
           assertThat(response).hasHeader("X-Api-Version", "2");
       }
   }

   class OrdersContractTest {
       void verifiesOrders(OrdersContract contract, String acceptVersion) {
           response = api.get("/orders", header("Accept-Version", acceptVersion));
           assertThat(response).hasStatus(200);
           contract.assertValid(response);
       }
   }

   verifiesOrders(new OrdersV1Contract(), "1");
   verifiesOrders(new OrdersV2Contract(), "2");
   ```

## 📊 **Good vs. Bad Comparison / 好的实现 vs. 糟糕的实现**

| Area / 维度 | 🔴 Bad: conditional design / 差：条件分支设计 | 🟢 Good: Strategy design / 好：Strategy 设计 |
| --- | --- | --- |
| **Responsibility / 职责** | The API client or UI test knows every variation: retry rules, payment providers, or API versions. **客户端或 UI 测试知道所有变化：**重试规则、支付提供商或 API 版本。 | The Context owns the stable workflow; each strategy owns one variation. **Context 负责稳定流程；**每个策略负责一种变化。 |
| **Decision logic / 决策逻辑** | The core test grows `if/else` or `switch` branches. **核心测试不断增加** `if/else` 或 `switch` 分支。 | The test selects an implementation once and calls the common interface. **测试只选择一次实现，**然后调用统一接口。 |
| **Adding behavior / 增加行为** | Modify the existing client/test and risk breaking unrelated scenarios. **修改现有客户端/测试，**容易影响无关场景。 | Add a new strategy class without changing the Context. **新增策略类，**无需修改 Context。 |
| **Reuse / 复用** | Duplicate the whole API or Playwright test for every variation. **每种变化复制整套** API 或 Playwright 测试。 | Reuse one test flow and inject a different strategy. **复用一套测试流程，**只注入不同策略。 |
| **Testability / 可测试性** | Business assertions are mixed with setup and variation logic, making failures harder to diagnose. **业务断言与准备代码、变化逻辑混在一起，**难以定位失败原因。 | Strategies can be unit-tested independently, while the Context test verifies delegation. **策略可独立单测，**Context 测试只验证委托关系。 |
| **Change risk / 变更风险** | Every new rule increases coupling and regression risk. **每条新规则都会增加耦合和回归风险。** | Variations are isolated behind the contract; adding one has a smaller blast radius. **变化被接口隔离，**新增策略的影响范围更小。 |

🕒 **When to Use This Pattern / 何时使用此模式**

Use Strategy when an SDET test has **one stable goal** but **multiple interchangeable ways** to achieve it—for example, retrying an API request, completing checkout with different providers, or validating different API versions. Choose it when the variation is likely to grow or change independently.

当 SDET 测试只有**一个稳定目标**，但有**多种可替换的实现方式**时使用 Strategy，例如 API 请求重试、不同支付提供商的结账流程，或不同 API 版本的校验。当这些变化可能独立增加或修改时，使用此模式最有价值。
