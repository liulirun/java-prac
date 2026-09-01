# The Decorator Pattern

## Principle Summary / 原则总结

The Decorator Pattern adds responsibilities to an object by wrapping it with
another object that implements the same interface. Each decorator delegates to
the wrapped component and adds one focused behavior before or after the call.

Decorator 模式通过使用实现相同接口的包装对象，为原对象动态添加职责。每个装饰器先
或后委托给被包装组件，并增加一种专注的行为。

### The Principle in One Sentence / 一句话原则

**Add behavior by composition, so the core client stays unchanged.**

**通过组合增加行为，使核心客户端保持不变。**

### Learning Model / 学习模型

`Core component → Decorator A → Decorator B → Test uses the same interface`

`核心组件 → 装饰器 A → 装饰器 B → 测试使用相同接口`

### Implementation Sequence / 实现顺序

1. **Define a component contract / 定义组件契约** — The base object and decorators must expose the same interface. / 基础对象和装饰器必须实现相同接口。
2. **Implement the core behavior / 实现核心行为** — Keep the underlying API client or UI action focused. / 保持底层 API 客户端或 UI 操作专注于核心职责。
3. **Wrap the component / 包装组件** — Store the same interface inside each decorator. / 在每个装饰器中保存相同接口类型的组件。
4. **Add one responsibility / 增加一种职责** — Add metrics, tracing, redaction, screenshots, or fault injection in a separate decorator. / 将指标、追踪、脱敏、截图或故障注入分别放入独立装饰器。
5. **Compose the required stack / 组合所需层次** — Order decorators deliberately because wrapping order can affect behavior. / 明确装饰器顺序，因为包装顺序可能影响行为。

### 0. Compact API/Playwright Testing Pseudo-code / 精简 API/Playwright 测试伪代码

```java
interface ApiTransport {
    Response send(Request request);
}

class HttpTransport implements ApiTransport {
    public Response send(Request request) {
        return http.execute(request); // Core responsibility / 核心职责
    }
}

class TimingTransport implements ApiTransport {
    private final ApiTransport inner;
    TimingTransport(ApiTransport inner) { this.inner = inner; }

    public Response send(Request request) {
        startTimer();
        Response response = inner.send(request);
        recordLatency(response); // Added responsibility / 增加的职责
        return response;
    }
}

class RedactingTransport implements ApiTransport {
    private final ApiTransport inner;
    RedactingTransport(ApiTransport inner) { this.inner = inner; }

    public Response send(Request request) {
        Response response = inner.send(request);
        return redactSecrets(response); // Added responsibility / 增加的职责
    }
}

// Compose without changing HttpTransport / 组合时不修改 HttpTransport
transport = new RedactingTransport(new TimingTransport(new HttpTransport()));
response = transport.send(request);
assertThat(response).hasStatus(200);
```

**Essential implementation points / 核心实现要点**

| English | 中文 |
| --- | --- |
| Keep the component and decorator behind the same interface. | 让组件和装饰器实现相同接口。 |
| Make every decorator responsible for one cross-cutting concern. | 每个装饰器只负责一种横切关注点。 |
| Always delegate to the wrapped component when the behavior requires the original operation. | 需要原始操作时必须委托给被包装组件。 |
| Compose decorators at test setup or dependency-configuration time. | 在测试准备或依赖配置阶段组合装饰器。 |
| Test decorator behavior separately and test the composed stack for ordering. | 分别测试装饰器行为，并测试组合栈的执行顺序。 |

## SDET Applications / SDET 应用

1. **API observability / API 可观测性** — Wrap the shared transport with timing and correlation-ID decorators so every test receives diagnostics without modifying endpoint clients.

   ```java
   api = new TimingTransport(new CorrelationIdTransport(new HttpTransport()));
   response = api.send(request);
   assertThat(response).hasStatus(200);
   ```

2. **Playwright failure evidence / Playwright 失败证据** — Decorate a page-action interface to capture a screenshot and trace when an action fails, while preserving the same action calls used by the test.

   ```java
   actions = new FailureTraceActions(new PlaywrightActions(page));
   actions.click("submit");
   expect(page.getByTestId("success")).toBeVisible();
   ```

3. **API fault-injection testing / API 故障注入测试** — Wrap a real transport with a deterministic fault decorator to simulate `503`, latency, or malformed responses without changing production client code.

   ```java
   api = new FaultInjectionTransport(realApi, failNext = 1, status = 503);
   response = api.send(get("/inventory"));
   assertThat(response).hasStatus(503);
   ```

## Good vs. Bad Comparison / 好的实现 vs. 糟糕的实现

| Area / 维度 | 🔴 Bad: modify the core / 差：修改核心组件 | 🟢 Good: compose decorators / 好：组合装饰器 |
| --- | --- | --- |
| **Adding diagnostics / 增加诊断** | Timing, logging, and redaction are hard-coded into every client method. **计时、日志和脱敏硬编码在每个客户端方法中。** | Cross-cutting behavior is added by wrapping the component. **通过包装组件增加横切行为。** |
| **Reuse / 复用** | Each API client or page action reimplements the same concern. **每个 API 客户端或页面操作重复实现相同职责。** | One decorator works with every implementation of the contract. **一个装饰器适用于契约的所有实现。** |
| **Core stability / 核心稳定性** | The core client changes whenever observability or test tooling changes. **可观测性或测试工具变化就要修改核心客户端。** | The core client remains focused and unchanged. **核心客户端保持专注且不变。** |
| **Composition / 组合** | Features are enabled through flags and nested conditionals. **通过 flag 和嵌套条件启用功能。** | Tests compose only the decorators needed for that scenario. **测试只组合当前场景需要的装饰器。** |
| **Ordering / 顺序** | Order is implicit and difficult to reason about. **执行顺序隐式且难以理解。** | The wrapper chain makes order visible in setup. **包装链让 setup 中的顺序清晰可见。** |

## When to Use / 何时使用

Use Decorator when optional responsibilities should be mixed and matched around
an existing object without changing its contract. Use Strategy when choosing one
alternative algorithm; use Decorator when stacking additional responsibilities.

当需要围绕现有对象灵活叠加可选职责、且不改变其契约时使用 Decorator。选择一种替代
算法使用 Strategy；叠加多种额外职责使用 Decorator。
