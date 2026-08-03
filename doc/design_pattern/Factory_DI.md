# Dependency Injection (DI) Modes Summary
Direct Comparison

| Feature               | Mode 1: Constructor Injection      | Mode 2: Factory Injection           |
| --------------------- | ---------------------------------- | ----------------------------------- |
| **Injection Point**   | `__init__(self, items)`            | `add_item(self, ..., factory_func)` |
| **Order Lifecycle**   | Fixed / Immutable at creation      | Flexible / Changes dynamically      |
| **When Math Happens** | Only at the very end (`get_total`) | Instantly as items are created      |
| **Code Complexity**   | Low / Very Clean                   | Medium / Abstract                   |


## Mode 1: Constructor Injection

Dependencies are created first and injected all at once during the initialization of the parent object.

```
[ Items List ] ──► Injected via __init__() ──► [ Order Object ]
```

- **How it works:** You build your list of `OrderItem` objects outside the `Order` class, then pass the complete list into `Order(items=...)`.

- **Best Used For:** Bulk updates, loading a saved shopping cart from a database, or APIs receiving a complete JSON payload all at once.

- **Pros:**

  - Highly secure and immutable (prevents mid-process data changes).
  - Easiest to unit test.

- **Cons:**
  - Rigid; you cannot easily start with an empty order and build it up dynamically.

## Mode 2: Factory Injection (Dynamic)

An object-creation blueprint (a function or service) is injected into the class so dependencies can be generated on demand.

```
[ Factory Rule ] ──► Injected via method ──► [ Order Object ] ──► Creates [ OrderItem ]
```

- **How it works:** You pass a factory function into an order method. The `Order` uses that injected function to safely build and calculate `OrderItem` properties (like individual product discounts) on the fly.

- **Best Used For:** Interactive, live user sessions (e.g., a customer clicking "Add to Cart" one item at a time over a long period).

- **Pros:**

  - Highly flexible and dynamic.
  - Calculates item-specific rules (like discounts or tax tiers) instantly upon addition.

- **Cons:**
  - Increases architectural complexity by introducing an extra layer of abstraction.

