### Completely Immutable Core Java Datatypes

* **`java.lang.String`**
* **All Primitive Wrappers:** 
  * `Integer`, `Long`, `Double`, `Float`, `Short`, `Byte`, `Character`, `Boolean`
* **Modern Date/Time API (`java.time`):** 
  * `LocalDate`, `LocalTime`, `LocalDateTime`, `ZonedDateTime`, `Instant`
* **Math Types:** 
  * `BigDecimal`, `BigInteger`
* **Unmodifiable Collections:** 
  * Collections created via `List.of()`, `Set.of()`, or `Map.of()`

### 1. The Cache Pollution Trap
**Question:** What does this code print? Is the code safe?
```java
Integer a = 127;
Integer b = 127;
Integer c = 128;
Integer d = 128;

System.out.println((a == b) + " " + (c == d));
```
* **The Wrong Answer:** It prints `true true` because `Integer` is immutable and Java reuses the references.
* **The Tricky Catch:** It prints `true false`. 
* **Why it is wrong:** Java caches `Integer` objects, but *only* for values between `-128` and `127`. For `128`, Java creates two entirely separate objects in memory. Using `==` compares memory addresses, not values. 
* **The Interview Fix:** Never use `==` to compare object values, even if they are immutable wrappers. Always use `a.equals(b)`.

---

### 2. The Unmodifiable List Mirage
**Question:** Will this code throw an exception or execute successfully?
```java
List<String> dynamicList = new ArrayList<>();
dynamicList.add("Apple");

List<String> unmodifiable = Collections.unmodifiableList(dynamicList);
dynamicList.add("Banana"); 

System.out.println(unmodifiable.size());
```
* **The Wrong Answer:** It throws an `UnsupportedOperationException` when we try to add "Banana" because the list is unmodifiable.
* **The Tricky Catch:** It executes successfully and prints `2`.
* **Why it is wrong:** `Collections.unmodifiableList()` creates an unmodifiable *view*, not a truly immutable list. It prevents changes if you call `.add()` directly on the `unmodifiable` variable. However, it still points directly to the original `dynamicList`. If the original list changes, the "unmodifiable" list changes with it!
* **The Interview Fix:** To make it truly immutable, use `List.copyOf(dynamicList)` or `List.of()`, which copy the data into a brand-new, disconnected structure.

---

### 3. The `final` Arrays Deception
**Question:** Does this class guarantee that the configuration settings cannot be changed?
```java
public final class AppConfig {
    public static final String[] ALLOWED_ROLES = {"Admin", "User"};
    // below will break it. I do not do RE-ASSIGN, but change the value of content
    AppConfig.ALLOWED_ROLES[1] = "Hacker";
}
```
* **The Wrong Answer:** Yes, because it is `public static final`, making it a global, unchangeable constant.
* **The Tricky Catch:** It is completely insecure. Anyone can change the roles.
* **Why it is wrong:** The `final` keyword only stops you from reassigning the variable (e.g., `ALLOWED_ROLES = new String[5]`). However, arrays in Java are **always mutable**. An attacker can easily overwrite the values inside the array.
* **How to break it:** `AppConfig.ALLOWED_ROLES[1] = "Hacker";` (This works without any compilation errors!).
* **The Interview Fix:** Use an unmodifiable List instead: `public static final List<String> ALLOWED_ROLES = List.of("Admin", "User");`.

---

### 4. The Subclassing Loophole
**Question:** Is this `Contract` class strictly immutable?
```java
public class Contract {
    private final String title;

    public Contract(String title) {
        this.title = title;
    }
    public String getTitle() { return title; }
}
```
* **The Wrong Answer:** Yes, because the field is `private final` and there are no setter methods.
* **The Tricky Catch:** No, because the class itself is not marked `final`.
* **Why it is wrong:** An interviewer can break the immutability by creating a mutable subclass that overrides your methods to behave dynamically.
* **How to break it:**
  ```java
  class BrokenContract extends Contract {
      private String fakeTitle;
      public BrokenContract(String title) { super(title); }
      @Override public String getTitle() { return this.fakeTitle; }
      public void setTitle(String newTitle) { this.fakeTitle = newTitle; }
  }
  ```
* **The Interview Fix:** Always mark your immutable classes as `public final class` so they cannot be extended.

---

### 5. The Stream Identity Illusion
**Question:** What does this code print?
```java
List<String> words = List.of("hello", "world");
words.stream().map(String::toUpperCase);
System.out.println(words.get(0));
```
* **The Wrong Answer:** It prints `HELLO`.
* **The Tricky Catch:** It prints `hello`.
* **Why it is wrong:** Java streams do not modify the underlying data source. Because `String` is immutable, `.toUpperCase()` generates entirely new strings. Because the stream was never collected into a new variable using `.collect(Collectors.toList())`, the transformed strings are simply dropped.
* **The Interview Fix:** Capture the result of the stream operation: `List<String> upperWords = words.stream().map(String::toUpperCase).toList();`.
