# Other Design Patterns / 其他设计模式

This file is now an index. Each pattern has its own bilingual learning
document with the same structure as the Strategy pattern: principle summary,
implementation points, SDET applications, compact pseudocode, and a Good vs.
Bad comparison.

本文件现在作为索引。每个模式都有独立的双语学习文档，并采用与 Strategy 模式相同的
结构：原则总结、实现要点、SDET 应用、精简伪代码，以及好的实现与糟糕实现对比。

## Pattern Map / 模式地图

| Pattern / 模式 | Type / 类型 | Best testing use / 典型测试用途 |
| --- | --- | --- |
| [Singleton](Design_Patterns_Creator_Singleton.md) | Creational / 创建型 | Scoped browser sessions, worker configuration, and test-resource lifecycle. / 作用域浏览器会话、worker 配置和测试资源生命周期。 |
| [Facade](Design_Patterns_Structural_Facade.md) | Structural / 结构型 | Hide multi-service setup and repeated API/UI workflows. / 隐藏多服务准备和重复 API/UI 流程。 |
| [Decorator](Design_Patterns_Structural_Decorator.md) | Structural / 结构型 | Add metrics, tracing, failure evidence, or fault injection. / 增加指标、追踪、失败证据或故障注入。 |
| [Template Method](Design_Patterns_Behaviour_Template_Method.md) | Behavioral / 行为型 | Enforce a shared setup–execute–verify–cleanup lifecycle. / 强制统一的准备–执行–校验–清理生命周期。 |
| [Page Object Model](Design_Patterns_Testing_Page_Object_Model.md) | Testing domain / 测试领域 | Keep Playwright selectors and UI actions out of business tests. / 将 Playwright selector 和 UI 操作隔离出业务测试。 |

## How to Choose / 如何选择

- **Singleton / 单例** — One correctly scoped shared resource is required. / 需要一个作用域正确的共享资源。
- **Facade / 外观** — Many low-level calls form one repeated business action. / 多个底层调用组成一个重复的业务动作。
- **Decorator / 装饰者** — Optional responsibilities should be stacked around an existing component. / 需要在现有组件外叠加可选职责。
- **Template Method / 模板方法** — The lifecycle order is fixed, but selected steps vary. / 生命周期顺序固定，但部分步骤变化。
- **Page Object Model / 页面对象模型** — UI interaction details must be isolated from test intent. / 需要将 UI 交互细节与测试意图隔离。

## Framework Evolution Summary / 框架演进总结

### Fragile test design / 脆弱的测试设计

`One test script → hard-coded selectors + repeated setup + mixed diagnostics + uncontrolled cleanup`

`一个测试脚本 → 硬编码 selector + 重复准备 + 混杂诊断代码 + 不受控清理`

### Pattern-based test design / 基于模式的测试设计

`Test intent → Page Object/Facade → shared lifecycle → decorated client → scoped resource`

`测试意图 → Page Object/Facade → 共享生命周期 → 装饰后的客户端 → 有作用域的资源`

The patterns solve different problems and can be composed, but each should have
one clear responsibility. The goal is not to use every pattern; the goal is to
make change, reuse, and test failure diagnosis easier.

这些模式解决不同问题，也可以组合使用，但每个模式都应保持清晰的单一职责。目标不是
使用所有模式，而是让变化、复用和测试失败定位更容易。
