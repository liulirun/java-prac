# The Page Object Model (POM)

## Principle Summary / 原则总结

The Page Object Model represents a page or reusable UI component as an object
that owns its locators and user actions. Tests express business intent through
that object instead of coupling assertions and workflows to selector details.

Page Object Model 将页面或可复用 UI 组件表示为对象，由对象管理定位器和用户操作。
测试通过对象表达业务意图，而不是让断言和流程依赖具体 selector 细节。

### The Principle in One Sentence / 一句话原则

**Put UI knowledge in page/component objects and keep business intent in tests.**

**将 UI 知识放入页面/组件对象，将业务意图保留在测试中。**

### Learning Model / 学习模型

`Test intent → Page Object action → Locator interaction → Observable UI state → Test assertion`

`测试意图 → Page Object 操作 → Locator 交互 → 可观察 UI 状态 → 测试断言`

### Implementation Sequence / 实现顺序

1. **Identify a page boundary / 确定页面边界** — Group locators and actions belonging to one page or reusable component. / 将同一页面或可复用组件的定位器和操作分组。
2. **Hide locators / 隐藏定位器** — Keep selectors private so locator changes do not spread into tests. / 将 selector 设为私有，避免变化扩散到测试中。
3. **Expose user-level actions / 暴露用户级操作** — Use methods such as `loginAs`, `addToCart`, or `submitSearch`. / 使用 `loginAs`、`addToCart` 或 `submitSearch` 等用户层操作。
4. **Return the next page/component / 返回下一个页面/组件** — Model navigation and composition explicitly. / 明确建模页面跳转和组件组合。
5. **Keep assertions intentional / 保持断言有意图** — Page objects may expose state, but tests own business expectations. / Page Object 可以暴露状态，但业务期望由测试负责。

### 0. Compact Playwright Testing Pseudo-code / 精简 Playwright 测试伪代码

```java
class LoginPage {
    private final Page page;
    private final Locator username;
    private final Locator password;
    private final Locator submit;

    LoginPage(Page page) {
        this.page = page;
        this.username = page.getByLabel("Username");
        this.password = page.getByLabel("Password");
        this.submit = page.getByRole(BUTTON, name = "Sign in");
    }

    HomePage loginAs(String user, String pass) {
        username.fill(user);
        password.fill(pass);
        submit.click();
        return new HomePage(page); // Model navigation / 建模页面跳转
    }
}

// Test states business intent, not CSS selectors.
// 测试表达业务意图，而不是 CSS selector。
home = new LoginPage(page).loginAs("standard-user", secret);
expect(home.welcomeMessage()).toBeVisible();
```

**Essential implementation points / 核心实现要点**

| English | 中文 |
| --- | --- |
| Keep locators and low-level UI actions inside the page/component object. | 将定位器和底层 UI 操作放在页面/组件对象内部。 |
| Name methods after user or business actions, not clicks and selectors. | 方法使用用户或业务动作命名，而不是 click 或 selector。 |
| Keep test assertions outside the Page Object unless they are reusable state checks. | 除非是可复用状态检查，否则将测试断言放在 Page Object 外部。 |
| Return page/component objects when navigation occurs. | 发生跳转时返回对应页面/组件对象。 |
| Use stable, user-facing locators and centralize selector changes. | 使用稳定的面向用户定位器，并集中管理 selector 变化。 |

## SDET Applications / SDET 应用

1. **Authentication journeys / 身份认证流程** — Use a Login Page Object when password login, SSO, and MFA tests share the same business outcome but the page selectors and transitions must stay out of test cases.

   ```java
   home = loginPage.loginWithPassword(user, pass);
   expect(home.accountMenu()).toBeVisible();
   ```

2. **Shopping cart components / 购物车组件** — Use a reusable Cart component when many tests add, remove, and update products using the same controls but different test data.

   ```java
   cart.addProduct("SKU-100");
   cart.changeQuantity("SKU-100", 2);
   expect(cart.total()).toHaveText("$20.00");
   ```

3. **Search and filtering / 搜索与筛选** — Use a Search Results Page Object when product, customer, and order searches share filter behavior but expose different result assertions.

   ```java
   results = ordersPage.searchByCustomer("C-100");
   expect(results.row("O-200")).toBeVisible();
   ```

## Good vs. Bad Comparison / 好的实现 vs. 糟糕的实现

| Area / 维度 | 🔴 Bad: selectors in tests / 差：selector 散落在测试中 | 🟢 Good: Page Objects / 好：使用 Page Object |
| --- | --- | --- |
| **Readability / 可读性** | Tests describe clicks and CSS instead of user intent. **测试描述 click 和 CSS，而不是用户意图。** | Tests read like business workflows. **测试代码像业务流程。** |
| **Locator changes / 定位器变化** | A selector change requires edits across many tests. **selector 变化需要修改大量测试。** | The page/component object owns the selector change. **页面/组件对象集中处理 selector 变化。** |
| **Reuse / 复用** | Login, cart, or search steps are copied. **登录、购物车或搜索步骤被复制。** | One object exposes reusable actions. **一个对象提供可复用操作。** |
| **Assertions / 断言** | Low-level helpers hide or perform unrelated business assertions. **底层 helper 隐藏或执行无关业务断言。** | Tests own scenario-specific expectations. **测试负责场景特有的期望。** |
| **Failure diagnosis / 失败诊断** | Failures expose implementation noise and brittle selectors. **失败信息充满实现噪声和脆弱 selector。** | Failures point to a business action or page boundary. **失败定位到业务动作或页面边界。** |

## When to Use / 何时使用

Use POM when UI selectors and interaction details are repeated or likely to
change independently from business scenarios. Keep objects cohesive; do not
create one giant page object for the entire application.

当 UI selector 和交互细节被重复使用，或可能独立于业务场景变化时使用 POM。保持对象
内聚；不要为整个应用创建一个巨型 Page Object。
