Here is the updated document. The public and internal tables now include the remaining Java 21+ sequenced collection operations (`addFirst`, `addLast`, `getFirst`, `getLast`, `removeFirst`, `removeLast`), along with their reversed view method (`reversed`), all complete with plain English Big O notations.

Unique ArrayList Methods

Table 2: Frequent & Public API Methods

These are the methods you use for data manipulation, searching, and list management.

| Method        | Description                                | Return Type        | Example                   | Big O                                      |
| ------------- | ------------------------------------------ | ------------------ | ------------------------- | ------------------------------------------ |
| `add`         | Appends or inserts element                 | `boolean`          | `list.add("Java")`        | O(1) amortized for append, O(n) for insert |
| `addFirst`    | Adds element to the start (J21+)           | `void`             | `list.addFirst("A")`      | O(n)                                       |
| `addLast`     | Adds element to the end (J21+)             | `void`             | `list.addLast("Z")`       | O(1) amortized                             |
| `get` / `set` | Reads or updates by index                  | `Element`          | `list.set(0, "val")`      | O(1)                                       |
| `getFirst`    | Reads the first element (J21+)             | `Element`          | `list.getFirst()`         | O(1)                                       |
| `getLast`     | Reads the last element (J21+)              | `Element`          | `list.getLast()`          | O(1)                                       |
| `remove`      | Deletes by index or object                 | `Element` / `bool` | `list.remove(0)`          | O(n)                                       |
| `removeFirst` | Deletes the first element (J21+)           | `Element`          | `list.removeFirst()`      | O(n)                                       |
| `removeLast`  | Deletes the last element (J21+)            | `Element`          | `list.removeLast()`       | O(1)                                       |
| `reversed`    | Returns a reverse-ordered view (J21+)      | `List<E>`          | `list.reversed()`         | O(1)                                       |
| `addAll`      | Appends a whole collection                 | `boolean`          | `list.addAll(otherColl)`  | O(n + m) where m is collection size        |
| `isEmpty`     | Checks if the list has zero items          | `boolean`          | `list.isEmpty()`          | O(1)                                       |
| `iterator`    | Returns a linear collection traversal tool | `Iterator<E>`      | `list.iterator()`         | O(1) to create, O(n) to fully loop         |
| `removeIf`    | Filters list by condition. *if requires to remove nTH element*                  | `boolean`          | `list.removeIf(n -> n<0)` | O(n)                                       |
| `contains`    | Checks if element exists                   | `boolean`          | `list.contains("X")`      | O(n)                                       |
| `indexOf`     | Finds first position of item               | `int`              | `list.indexOf("A")`       | O(n)                                       |
| `lastIndexOf` | Finds last position of item                | `int`              | `list.lastIndexOf("A")`   | O(n)                                       |
| `size`        | Returns number of elements                 | `int`              | `list.size()`             | O(1)                                       |
| `clear`       | Removes all elements                       | `void`             | `list.clear()`            | O(n)                                       |
| `sort`        | Sorts using a Comparator                   | `void`             | `list.sort(null)`         | O(n log n)                                 |
| `subList`     | Gets a view of a range                     | `List<E>`          | `list.subList(0, 5)`      | O(1)                                       |
| `toArray`     | Converts to an array                       | `Object[]`         | `list.toArray()`          | O(n)                                       |

Table 3: Performance, Internal & Specialized Methods

These handle memory resizing (`grow`), safety checks, and serialization.

| Method             | Description                          | Category | Usage Context               | Big O                                      |
| ------------------ | ------------------------------------ | -------- | --------------------------- | ------------------------------------------ |
| `trimToSize`       | Minimizes storage capacity           | Memory   | `list.trimToSize()`         | O(n)                                       |
| `ensureCapacity`   | Manually expands capacity            | Memory   | `list.ensureCapacity(100)`  | O(n)                                       |
| `grow`             | Internal array expansion             | Internal | Triggered when list is full | O(n)                                       |
| `batchRemove`      | Helper for removeAll/retainAll       | Internal | Bulk deletion logic         | O(n)                                       |
| `fastRemove`       | Internal skip-bounds-checking remove | Internal | Speeds up bulk deletes      | O(n)                                       |
| `elementData(idx)` | Raw index array lookup helper        | Internal | Internal un-checked reads   | O(1)                                       |
| `rangeCheck...`    | Validates index bounds               | Safety   | Prevents IndexOutOfBounds   | O(1)                                       |
| `checkForComod`    | Checks for concurrent edits          | Safety   | Used by Iterators           | O(1)                                       |
| `elementData`      | Accesses the raw array               | Internal | Transient storage field     | O(1)                                       |
| `clone`            | Shallow copy of the list             | Utility  | `list.clone()`              | O(n)                                       |
| `spliterator`      | Parallel iteration support           | Streams  | `list.spliterator()`        | O(1)                                       |
| `writeObject`      | Serializes the list                  | Utility  | `java.io` operations        | O(n)                                       |
| `readObject`       | Deserializes the list                | Utility  | Reading from a byte stream  | O(n)                                       |
| `shiftTail...`     | Moves elements after removal         | Internal | Maintains array order       | O(n)                                       |
| `retainAll`        | Keeps only matching items            | Logic    | `list.retainAll(otherColl)` | O(n \* m) where m is other collection size |

Table 1: Data `-->` ArrayList

| From                 | Example                                                   | When to use this                                             | Protip                                                                               |
| -------------------- | --------------------------------------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------------------------------ |
| **Array**            | `new ArrayList<>(Arrays.asList(arr))`                     | To turn a fixed array into a dynamic, growable list.         | Do not use `Arrays.asList(arr)` alone if you plan to `.add()` elements later.        |
| **Fixed List**       | `new ArrayList<>(List.of(a, b))`                          | Converting an immutable list into a modifiable one.          | `List.of` is faster for lookups, but `ArrayList` is required for structural changes. |
| **Other Collection** | `new ArrayList<>(hashSet)`                                | Converting a `Set` or `Queue` into a list for sorting.       | This effectively "freezes" the current state of the set into an ordered list.        |
| **2D Array**         | `new ArrayList<>(Arrays.deepAsList(matrix))`              | When converting a grid or matrix into nested lists.          | Changes to objects inside the list may still affect the original array.              |
| **Capacity Hint**    | `new ArrayList<>(1000)`                                   | Creating a blank list when you know the final size.          | This prevents the "growth" logic, making bulk additions much faster.                 |
| **Stream**           | `stream.collect(Collectors.toCollection(ArrayList::new))` | Saving filtered or mapped data into an `ArrayList`.          | Use `.toList()` (Java 16+) if you don't specifically need an `ArrayList` instance.   |
| **Iterable**         | `list.addAll(otherCollection)`                            | To merge elements from another source into an existing list. | This is more efficient than looping and calling `.add()` for every item.             |

Table 2: ArrayList `-->` Data Types

| To                  | Example                                                    | When to use this                                              | Protip                                                                                |
| ------------------- | ---------------------------------------------------------- | ------------------------------------------------------------- | ------------------------------------------------------------------------------------- |
| **Array**           | `list.toArray(new String)`                                 | When an API requires a standard `String[]` instead of a List. | Modern Java (8+) optimized `new T` to be as fast as pre-sized arrays.                 |
| **Immutable List**  | `List.copyOf(list)`                                        | To "lock" the list so it cannot be modified by other methods. | This creates a **deeply unmodifiable** copy; changes to the original won't affect it. |
| **Set**             | `new HashSet<>(list)`                                      | When you need to strip all duplicate values from your list.   | Note that `HashSet` will lose the original insertion order of the ArrayList.          |
| **Stack/Queue**     | `new LinkedList<>(list)`                                   | When you need efficient adding/removing from both ends.       | `ArrayList` is slow at deleting from the front; `LinkedList` is O(1)                  |
| **Sub-List**        | `list.subList(0, 5)`                                       | To get a "view" of a specific range (e.g., first 5 items).    | Changes to the `subList` **will** modify the original `ArrayList`.                    |
| **Primitive Array** | `list.stream().mapToInt(i->i).toArray()`                   | To convert `List<Integer>` into a high-performance `int[]`.   | Standard `.toArray()` only produces `Integer[]`, which is heavier on memory.          |
| **Map**             | `list.stream().collect(Collectors.toMap(Obj::id, o -> o))` | Turning objects into a searchable ID-based lookup table.      | If keys duplicate, this crashes unless you provide a merge function.                  |
| **String**          | `String.join(", ", list)`                                  | Turning a list of Strings into a comma-separated format.      | Only works on `List<String>`. For numbers, map them to Strings first.                 |

If you want to fine-tune this cheat sheet further, let me know:

- Should we add **SequencedCollection syntax examples** for the Java 21 methods?
- Do you want to explicitly **highlight which methods throw exceptions** when the list is empty?