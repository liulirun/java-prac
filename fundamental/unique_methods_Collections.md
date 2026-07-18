# Unique Collections Methods

## Table 1: Data `-->` Collections

| From                   | Example                              | When to use this                                          | Protip                                                                              |
| ---------------------- | ------------------------------------ | --------------------------------------------------------- | ----------------------------------------------------------------------------------- |
| **Loose Items**        | `List.of(1, 2, 3)`                   | Modern initialization of a small, constant set of data.   | These are **immutable**; wrap in `new ArrayList<>(...)` if you need to modify them. |
| **A Single Object**    | `Collections.singletonList(obj)`     | When an API requires a `List` but you only have one item. | Much more memory-efficient than creating a full `ArrayList` for one item.           |
| **Nothing**            | `Collections.emptyList()`            | To return a "safe" empty result instead of `null`.        | Returns a shared, cached object, saving memory compared to `new ArrayList<>()`.     |
| **Array**              | `Arrays.asList(arr)`                 | Turning an array into a List for searching/sorting.       | Creates a **fixed-size** list; you can't `.add()`, but you can `.set()` values.     |
| **Enumeration**        | `Collections.list(enum)`             | Converting legacy types (like `Vector`) to modern Lists.  | Essential bridge for working with very old Java libraries or network APIs.          |
| **Mutable List**       | `Collections.unmodifiableList(list)` | To "lock" a list before passing it to another API.        | If you change the **original** list, this view will also change!                    |
| **Thread-Unsafe List** | `Collections.synchronizedList(list)` | When multiple threads need to access the same list.       | For modern high-concurrency, use `CopyOnWriteArrayList` instead.                    |
| **Existing Map**       | `Collections.newSetFromMap(map)`     | Creating a Set that behaves like a specific Map.          | Great way to create a high-performance thread-safe Set from a `ConcurrentHashMap`.  |

## Table 2: Collections `-->` Data Types

| To                | Example                                        | When to use this                                              | Protip                                                                                         |
| ----------------- | ---------------------------------------------- | ------------------------------------------------------------- | ---------------------------------------------------------------------------------------------- |
| **Array**         | `list.toArray(new String)`                     | When you need to pass data to legacy fixed-length APIs.       | `new String` is modern practice; the JVM sizes it more efficiently than pre-allocating.        |
| **Stream**        | `list.stream()`                                | The entry point for filtering, mapping, or processing.        | Use `.parallelStream()` for massive collections to leverage multi-core CPUs.                   |
| **Map**           | `list.stream().collect(Collectors.toMap(...))` | When your items have IDs and you want a lookup table.         | Ensure IDs are unique or use a "merge function" to handle key collisions.                      |
| **Enumeration**   | `Collections.enumeration(list)`                | To interface with legacy code expecting an `Enumeration`.     | This is the exact "reverse" of `Collections.list()` for backward compatibility.                |
| **Single Value**  | `Collections.max(list)`                        | Finding the highest or latest item in a collection.           | For custom objects, provide a `Comparator` to define the sort logic.                           |
| **Reversed View** | `Collections.reverse(list)`                    | When you need to flip the order of items in place.            | This **mutates** the original list. Duplicate the list first if you need to keep the original. |
| **Checked View**  | `Collections.checkedList(list, String.class)`  | When using raw types to ensure only specific types are added. | Throws a `ClassCastException` the moment an invalid type is inserted at runtime.               |
| **String**        | `list.toString()`                              | For logging or debugging the contents.                        | Format is `[a, b]`. For custom separators, use `String.join()` or `Collectors.joining()`.      |


## Table 2: Frequent & General Purpose Methods

These are the standard tools used for sorting, modifying, and creating safe versions of your collections.

| Method            | Description                  | Return Type | Example                           |
| ----------------- | ---------------------------- | ----------- | --------------------------------- |
| `sort`            | Sorts a List                 | `void`      | `Collections.sort(list)`          |
| `reverse`         | Reverses list order          | `void`      | `Collections.reverse(list)`       |
| `shuffle`         | Randomizes elements          | `void`      | `Collections.shuffle(list)`       |
| `copy`            | Copies src to dest list      | `void`      | `Collections.copy(dest, src)`     |
| `fill`            | Overwrites all elements      | `void`      | `Collections.fill(list, val)`     |
| `max` / `min`     | Finds extreme values         | `T`         | `Collections.max(list)`           |
| `frequency`       | Counts occurrences           | `int`       | `Collections.frequency(l, x)`     |
| `unmodifiable...` | Makes list/map read-only     | `List/Map`  | `Collections.unmodifiableList(l)` |
| `empty...`        | Returns cached empty list    | `List/Map`  | `Collections.emptyList()`         |
| `singleton...`    | Creates a 1-item list        | `List/Map`  | `Collections.singletonList(x)`    |
| `binarySearch`    | Finds index (must be sorted) | `int`       | `Collections.binarySearch(l, x)`  |
| `addAll`          | Adds multiple elements       | `boolean`   | `Collections.addAll(list, 1, 2)`  |

## Table 3: Specialized, Thread-Safe & Internal Methods

These include synchronization wrappers, type-checking, and internal algorithm variations.

| Method                | Description                   | Category    | Usage Context                              |
| --------------------- | ----------------------------- | ----------- | ------------------------------------------ |
| `synchronized...`     | Makes collection thread-safe  | Concurrency | `Collections.synchronizedList(l)`          |
| `checked...`          | Forces type-safety at runtime | Safety      | `Collections.checkedList(l, String.class)` |
| `nCopies`             | Immutable list of N items     | Utility     | `Collections.nCopies(10, "A")`             |
| `disjoint`            | True if no common items       | Logic       | `Collections.disjoint(c1, c2)`             |
| `rotate`              | Shifts elements by distance   | Logic       | `Collections.rotate(list, 2)`              |
| `replaceAll`          | Replaces all old with new     | Logic       | `Collections.replaceAll(l, old, new)`      |
| `asLifoQueue`         | Treats Deque as a Stack       | Wrapper     | `Collections.asLifoQueue(deque)`           |
| `reverseOrder`        | Comparator for descending     | Utility     | `list.sort(Collections.reverseOrder())`    |
| `indexOfSubList`      | Finds start of a sequence     | Search      | `Collections.indexOfSubList(l, sub)`       |
| `indexed/iterator...` | Internal search logic         | Internal    | Helper for `binarySearch`                  |
| `rotate1 / rotate2`   | Internal rotation logic       | Internal    | Algorithmic implementations                |
| `newSetFromMap`       | Set backed by a Map           | Wrapper     | Creating a Set from a ConcurrentMap        |

**Note:** The `unmodifiable` and `synchronized` methods are actually wrappers. They don't copy the data; they just control access to the original collection.

Would you like to see why **unmodifiableList** is safer than just using a final variable?