# The Facade Pattern

## Principle Summary / 原则总结

The Facade Pattern provides one simple, task-focused interface over several
complex subsystem operations. The caller expresses a business action once;
the Facade coordinates ordering, data passing, and low-level dependencies.

Facade 模式在多个复杂子系统操作之上提供一个简单、面向任务的接口。调用方只表达
一次业务动作；Facade 负责协调步骤顺序、数据传递和底层依赖。

### The Principle in One Sentence / 一句话原则

**Hide subsystem choreography behind one intention-revealing operation.**

**将子系统之间复杂的编排隐藏在一个表达意图的操作之后。**

### Learning Model / 学习模型

`Test asks for one business action → Facade coordinates subsystems → Test receives a usable result → Test asserts outcome`

`测试请求一个业务动作 → Facade 编排子系统 → 测试获得可用结果 → 测试断言结果`

### Implementation Sequence / 实现顺序

1. **Find a repeated workflow / 找出重复流程** — Identify a multi-step action repeated by several tests. / 找到多个测试重复执行的多步骤动作。
2. **Name the business intent / 命名业务意图** — Expose a method that describes what the test wants, not how each subsystem works. / 暴露描述测试意图的方法，而不是暴露子系统细节。
3. **Compose existing services / 组合现有服务** — Keep API clients, database helpers, and page objects behind the Facade. / 将 API 客户端、数据库 helper 和页面对象隐藏在 Facade 后面。
4. **Control the workflow / 控制流程** — Centralize ordering, translations, and cleanup while preserving useful failures. / 集中管理顺序、数据转换和清理，同时保留有价值的错误信息。
5. **Keep the Facade thin / 保持 Facade 精简** — It orchestrates; it should not become a new business-rule monolith. / Facade 负责编排，不应变成新的业务规则单体。

### 0. Compact API/Playwright Testing Pseudo-code / 精简 API/Playwright 测试伪代码

```java
class UserOnboardingFacade {
    UserOnboardingFacade(AuthApi auth, ProfileApi profile, MailApi mail) {
        this.auth = auth;
        this.profile = profile;
        this.mail = mail;
    }

    UserHandle createVerifiedUser(UserData data) {
        // Facade hides ordering and token hand-off / Facade 隐藏顺序和 token 传递
        Registration registration = auth.register(data.email(), data.password());
        String verificationCode = mail.readVerificationCode(data.email());
        auth.verify(registration.userId(), verificationCode);
        profile.create(registration.userId(), data.profile());
        return new UserHandle(registration.userId());
    }
}

// Test expresses intent, not subsystem choreography.
// 测试表达业务意图，而不是编排底层子系统。
user = onboarding.createVerifiedUser(testData);
assertThat(profileApi.get(user.id())).hasStatus(200);
```

**Essential implementation points / 核心实现要点**

| English | 中文 |
| --- | --- |
| Start with a workflow that appears in multiple tests. | 从多个测试都会出现的流程开始。 |
| Give the Facade a business-level name and operation. | 为 Facade 使用业务层面的名称和操作。 |
| Keep subsystem clients private to the Facade. | 将子系统客户端限制在 Facade 内部。 |
| Make data hand-offs and ordering explicit inside the Facade. | 在 Facade 内明确数据传递和步骤顺序。 |
| Return a useful domain result so tests can assert meaningful outcomes. | 返回有意义的领域结果，便于测试进行有效断言。 |

## SDET Applications / SDET 应用

1. **API user onboarding / API 用户开户** — Use a Facade when registration, email verification, role assignment, and profile creation are required before the test can exercise a user endpoint.

   ```java
   user = onboarding.createVerifiedUser(customerData);
   response = usersApi.getProfile(user.id());
   assertThat(response).hasStatus(200);
   ```

2. **Playwright checkout setup / Playwright 结账准备** — Use a Facade when a UI test needs API-created inventory, a seeded customer, and a ready cart before opening the browser.

   ```java
   checkoutData = checkoutSetup.prepareCart(product, customer);
   page.goto("/checkout/" + checkoutData.cartId());
   expect(page.getByTestId("cart-total")).toHaveText(checkoutData.total());
   ```

3. **API order lifecycle / API 订单生命周期** — Use a Facade when creating an order requires stock reservation, payment authorization, and shipment creation, but the test only cares that the order reaches a target state.

   ```java
   order = orderLifecycle.createReadyToShip(orderRequest);
   response = ordersApi.get(order.id());
   assertThat(response).hasJsonPath("status", "READY_TO_SHIP");
   ```

## Good vs. Bad Comparison / 好的实现 vs. 糟糕的实现

| Area / 维度 | 🔴 Bad: exposed choreography / 差：暴露编排细节 | 🟢 Good: focused Facade / 好：专注的 Facade |
| --- | --- | --- |
| **Test readability / 测试可读性** | Tests list every low-level API, database, or UI step. **测试列出每个底层 API、数据库或 UI 步骤。** | Tests call one intention-revealing operation. **测试调用一个表达意图的操作。** |
| **Reuse / 复用** | The same sequence is copied across suites. **相同顺序在多个测试套件中复制。** | One Facade workflow is reused consistently. **一个 Facade 流程被一致复用。** |
| **Change impact / 变更影响** | A token field or endpoint change requires editing many tests. **token 字段或 endpoint 变化需要修改大量测试。** | Only the Facade's subsystem coordination changes. **只需修改 Facade 的子系统编排。** |
| **Failure diagnosis / 失败诊断** | Setup noise hides the business assertion that failed. **准备代码噪声掩盖真正失败的业务断言。** | Facade failures can identify the failed subsystem step. **Facade 可明确指出失败的子系统步骤。** |
| **Design boundary / 设计边界** | One giant helper contains unrelated workflows. **一个巨型 helper 包含互不相关的流程。** | Each Facade represents one cohesive business capability. **每个 Facade 代表一个内聚的业务能力。** |

## When to Use / 何时使用

Use Facade when many callers need the same subsystem choreography or when a
low-level integration is too noisy for the test's intent. Do not use it to hide
important assertions or to merge unrelated workflows into one helper.

当多个调用方需要相同的子系统编排，或底层集成细节太多、影响测试意图时使用 Facade。
不要用它隐藏重要断言，也不要把无关流程合并到一个 helper 中。
