**Composition over Inheritance** is a design principle that states classes should achieve polymorphic behavior and code reuse by **containing other objects (Composition)** rather than **extending a parent class (Inheritance)** \[1].

It changes your mindset from thinking about what an object **is** (Inheritance: a `Car` *is a* `Vehicle`) to what an object **has** or **does** (Composition: a `Car` *has an* `Engine`).

Why It Modifies Standard Architecture

1. **Dynamic at Runtime:** Inheritance is rigid and locked at compile time. Composition allows you to swap out behaviors instantly while the application is running.
2. **Avoids the Fragile Base Class Problem:** In deep inheritance hierarchies, changing one method in a top-level parent class can accidentally break dozens of subclasses. Composition isolates classes.

Java Code Comparison

The Bad Way: Deep Inheritance (Rigid Architecture)

If you want to create a `RobotDog`, inheritance forces you into a corner. Does it extend `Robot` or `Animal`?

```java
class Animal { void breathe() {} }
class Dog extends Animal { void bark() {} }

class Robot { void chargeBattery() {} }
// ❌ ARCHITECTURE TRAP: Java blocks multiple inheritance. This will not compile!
// class RobotDog extends Dog, Robot { } 
```

The Better Way: Composition Architecture (Flexible)

Instead of forcing a class into a family tree, we pass behaviors into it as interchangeable components.

```java
package composition;

// 1. Define tiny, modular behavioral contracts
interface SoundBehavior { void makeNoise(); }
interface PowerSource { void supplyPower(); }

// 2. Create concrete reusable behaviors
class Barking implements SoundBehavior { public void makeNoise() { System.out.println("Bark!"); } }
class ElectronicBeep implements SoundBehavior { public void makeNoise() { System.out.println("Beep Boop!"); } }
class BatteryPower implements PowerSource { public void supplyPower() { System.out.println("Drawing 12V Battery."); } }

// 3. Assemble the class using Composition ("HAS-A")
class RobotDog {
    private SoundBehavior sound; // ✅ Check: Composed behavior reference
    private PowerSource power;   // ✅ Check: Composed behavior reference

    // Dependencies are injected via the constructor
    public RobotDog(SoundBehavior sound, PowerSource power) {
        this.sound = sound;
        this.power = power;
    }

    // Dynamic Swapping: We can change behaviors at runtime!
    public void setSoundBehavior(SoundBehavior newSound) { this.sound = newSound; }

    public void operate() {
        power.supplyPower();
        sound.makeNoise();
    }
}

// 4. Execution Class
public class CompositionDemo {
    public static void main(String[] args) {
        // Build a standard mechanical robot dog
        RobotDog myRoboPup = new RobotDog(new ElectronicBeep(), new BatteryPower());
        myRoboPup.operate(); // Prints: Drawing 12V Battery. Beep Boop!

        System.out.println("--- Swapping behavior at runtime ---");
        // Swap behavior instantly on the fly without changing the class structure
        myRoboPup.setSoundBehavior(new Barking());
        myRoboPup.operate(); // Prints: Drawing 12V Battery. Bark!
    }
}
```

Does Composition Violate the Diamond Problem?

**No. Composition is the ultimate cure for the Diamond Problem.**

The Diamond Problem happens when a class inherits the same method signature from two different parents, leaving the compiler confused about which version to execute:

```text
       ┌───────────────┐
       │   A (work())  │
       └───────────────┘
          ▲         ▲
          │         │
   ┌──────────┐ ┌──────────┐
   │B (work())│ │C (work())│
   └──────────┘ └──────────┘
          ▲         ▲
          │         │
       ┌───────────────┐
       │       D       │  <-- Which work() does D inherit? Compiler panics!
       └───────────────┘
```

Composition completely bypasses this issue. Because class `D` does not inherit from `B` or `C`, it contains instances of them instead. There is absolutely no ambiguity because you must explicitly specify which object's method you are invoking:

```java
class D {
    private B objB = new B();
    private C objC = new C();

    public void executeWork() {
        objB.work(); // ✅ Explicit: No compiler confusion
        objC.work(); // ✅ Explicit: No compiler confusion
    }
}
```

Cheat Sheet Summary

| Aspect             | Inheritance Architecture (`extends`)                  | Composition Architecture (`has-a`)                          |
| ------------------ | ----------------------------------------------------- | ----------------------------------------------------------- |
| **Relationship**   | "IS-A" type coupling                                  | "HAS-A" modular coupling                                    |
| **Binding**        | Static (Compile-time)                                 | Dynamic (Runtime swapping)                                  |
| **Encapsulation**  | Breaks it (Subclass exposes Parent internals)         | Preserves it (Components interact through clean interfaces) |
| **Diamond Threat** | Highly vulnerable if using complex interface defaults | **100% immune**                                             |

Would you like to explore how modern architectural frameworks like **Spring use Dependency Injection** to automate this exact composition process across enterprise applications?