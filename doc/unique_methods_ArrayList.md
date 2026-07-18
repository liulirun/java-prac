# Unique ArrayList Methods

## Table 1: Data `-->` ArrayList

| From                 | Example                                                   | When to use this                                             | Protip                                                                               |
| -------------------- | --------------------------------------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------------------------------ |
| **Array**            | `new ArrayList<>(Arrays.asList(arr))`                     | To turn a fixed array into a dynamic, growable list.         | Do not use `Arrays.asList(arr)` alone if you plan to `.add()` elements later.        |
| **Fixed List**       | `new ArrayList<>(List.of(a, b))`                          | Converting an immutable list into a modifiable one.          | `List.of` is faster for lookups, but `ArrayList` is required for structural changes. |
| **Other Collection** | `new ArrayList<>(hashSet)`                                | Converting a `Set` or `Queue` into a list for sorting.       | This effectively "freezes" the current state of the set into an ordered list.        |
| **2D Array**         | `new ArrayList<>(Arrays.deepAsList(matrix))`              | When converting a grid or matrix into nested lists.          | Changes to objects inside the list may still affect the original array.              |
| **Capacity Hint**    | `new ArrayList<>(1000)`                                   | Creating a blank list when you know the final size.          | This prevents the "growth" logic, making bulk additions much faster.                 |
| **Stream**           | `stream.collect(Collectors.toCollection(ArrayList::new))` | Saving filtered or mapped data into an `ArrayList`.          | Use `.toList()` (Java 16+) if you don't specifically need an `ArrayList` instance.   |
| **Iterable**         | `list.addAll(otherCollection)`                            | To merge elements from another source into an existing list. | This is more efficient than looping and calling `.add()` for every item.             |

## Table 2: ArrayList `-->` Data Types

| To                  | Example                                                    | When to use this                                              | Protip                                                                                                                                                                                                                                                                                                                                                                                                                       |
| ------------------- | ---------------------------------------------------------- | ------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Array**           | `list.toArray(new String[0])`                              | When an API requires a standard `String[]` instead of a List. | Modern Java (8+) optimized `new T[0]` to be as fast as pre-sized arrays.                                                                                                                                                                                                                                                                                                                                                     |
| **Immutable List**  | `List.copyOf(list)`                                        | To "lock" the list so it cannot be modified by other methods. | This creates a **deeply unmodifiable** copy; changes to the original won't affect it.                                                                                                                                                                                                                                                                                                                                        |
| **Set**             | `new HashSet<>(list)`                                      | When you need to strip all duplicate values from your list.   | Note that `HashSet` will lose the original insertion order of the ArrayList.                                                                                                                                                                                                                                                                                                                                                 |
| **Stack/Queue**     | `new LinkedList<>(list)`                                   | When you need efficient adding/removing from both ends.       | `ArrayList` is slow at deleting from the front; `LinkedList` is O(1) |
| **Sub-List**        | `list.subList(0, 5)`                                       | To get a "view" of a specific range (e.g., first 5 items).    | Changes to the `subList` **will** modify the original `ArrayList`.                                                                                                                                                                                                                                                                                                                                                           |
| **Primitive Array** | `list.stream().mapToInt(i->i).toArray()`                   | To convert `List<Integer>` into a high-performance `int[]`.   | Standard `.toArray()` only produces `Integer[]`, which is heavier on memory.                                                                                                                                                                                                                                                                                                                                                 |
| **Map**             | `list.stream().collect(Collectors.toMap(Obj::id, o -> o))` | Turning objects into a searchable ID-based lookup table.      | If keys duplicate, this crashes unless you provide a merge function.                                                                                                                                                                                                                                                                                                                                                         |
| **String**          | `String.join(", ", list)`                                  | Turning a list of Strings into a comma-separated format.      | Only works on `List<String>`. For numbers, map them to Strings first.                                                                                                                                                                                                                                                                                                                                                        |

## Table 2: Frequent & Public API Methods

These are the methods you use for data manipulation, searching, and list management.

| Method          | Description                  | Return Type  | Example                   |
| --------------- | ---------------------------- | ------------ | ------------------------- |
| `add`           | Appends or inserts element   | `boolean`    | `list.add("Java")`        |
| `get` / `set`   | Reads or updates by index    | `Element`          | `list.set(0, "val")`      |
| `remove`        | Deletes by index or object   | `Element` / `bool` | `list.remove(0)`          |
| `addFirst/Last` | Adds to start or end (J21+)  | `void`       | `list.addFirst("A")`      |
| `removeIf`      | Filters list by condition    | `boolean`    | `list.removeIf(n -> n<0)` |
| `contains`      | Checks if element exists     | `boolean`    | `list.contains("X")`      |
| `indexOf`       | Finds first position of item | `int`        | `list.indexOf("A")`       |
| `size`          | Returns number of elements   | `int`        | `list.size()`             |
| `clear`         | Removes all elements         | `void`       | `list.clear()`            |
| `sort`          | Sorts using a Comparator     | `void`       | `list.sort(null)`         |
| `subList`       | Gets a view of a range       | `List<E>`    | `list.subList(0, 5)`      |
| `toArray`       | Converts to an array         | `Object[]`   | `list.toArray()`          |

## Table 3: Performance, Internal & Specialized Methods

These handle memory resizing (`grow`), safety checks, and serialization.

| Method           | Description                    | Category | Usage Context               |
| ---------------- | ------------------------------ | -------- | --------------------------- |
| `trimToSize`     | Minimizes storage capacity     | Memory   | `list.trimToSize()`         |
| `ensureCapacity` | Manually expands capacity      | Memory   | `list.ensureCapacity(100)`  |
| `grow`           | Internal array expansion       | Internal | Triggered when list is full |
| `batchRemove`    | Helper for removeAll/retainAll | Internal | Bulk deletion logic         |
| `rangeCheck...`  | Validates index bounds         | Safety   | Prevents IndexOutOfBounds   |
| `checkForComod`  | Checks for concurrent edits    | Safety   | Used by Iterators           |
| `elementData`    | Accesses the raw array         | Internal | Transient storage field     |
| `clone`          | Shallow copy of the list       | Utility  | `list.clone()`              |
| `spliterator`    | Parallel iteration support     | Streams  | `list.spliterator()`        |
| `writeObject`    | Serializes the list            | Utility  | `java.io` operations        |
| `shiftTail...`   | Moves elements after removal   | Internal | Maintains array order       |
| `retainAll`      | Keeps only matching items      | Logic    | `list.retainAll(otherColl)` |
