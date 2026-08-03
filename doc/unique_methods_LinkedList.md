Here is the complete cheat sheet adapted specifically for **`LinkedList`** in Java 21.

Since a `LinkedList` is built using a chain of doubly-linked nodes rather than a contiguous array, the performance characteristics are very different. For example, index-based lookups (`get` / `set`) now require traversing the list node-by-node, making them **O(n)** instead of O(1). Conversely, adding or removing elements at the exact front or back is a pure pointer change, making them a guaranteed **O(1)** without any array resizing overhead.

Unique LinkedList Methods

Table 2: Frequent & Public API Methods

These are the methods you use for data manipulation, searching, and list management.

| Method        | Description                                | Return Type        | Example                   | Big O                                          |
| ------------- | ------------------------------------------ | ------------------ | ------------------------- | ---------------------------------------------- |
| `add`         | Appends or inserts element                 | `boolean`          | `list.add("Java")`        | O(1) for append, O(n) for insert by index      |
| `addFirst`    | Adds element to the start (J21+)           | `void`             | `list.addFirst("A")`      | O(1)                                           |
| `addLast`     | Adds element to the end (J21+)             | `void`             | `list.addLast("Z")`       | O(1)                                           |
| `get` / `set` | Reads or updates by index                  | `Element`          | `list.set(0, "val")`      | O(n) must traverse to index                    |
| `getFirst`    | Reads the first element (J21+)             | `Element`          | `list.getFirst()`         | O(1)                                           |
| `getLast`     | Reads the last element (J21+)              | `Element`          | `list.getLast()`          | O(1)                                           |
| `remove`      | Deletes by index or object                 | `Element` / `bool` | `list.remove(0)`          | O(n) must traverse to node                     |
| `removeFirst` | Deletes the first element (J21+)           | `Element`          | `list.removeFirst()`      | O(1)                                           |
| `removeLast`  | Deletes the last element (J21+)            | `Element`          | `list.removeLast()`       | O(1)                                           |
| `reversed`    | Returns a reverse-ordered view (J21+)      | `List<E>`          | `list.reversed()`         | O(1)                                           |
| `addAll`      | Appends a whole collection                 | `boolean`          | `list.addAll(otherColl)`  | O(m) where m is collection size                |
| `isEmpty`     | Checks if the list has zero items          | `boolean`          | `list.isEmpty()`          | O(1)                                           |
| `iterator`    | Returns a linear collection traversal tool | `Iterator<E>`      | `list.iterator()`         | O(1) to create, O(n) to fully loop             |
| `removeIf`    | Filters list by condition                  | `boolean`          | `list.removeIf(n -> n<0)` | O(n)                                           |
| `contains`    | Checks if element exists                   | `boolean`          | `list.contains("X")`      | O(n)                                           |
| `indexOf`     | Finds first position of item               | `int`              | `list.indexOf("A")`       | O(n)                                           |
| `lastIndexOf` | Finds last position of item                | `int`              | `list.lastIndexOf("A")`   | O(n)                                           |
| `size`        | Returns number of elements                 | `int`              | `list.size()`             | O(1) tracks counter internally                 |
| `clear`       | Removes all elements                       | `void`             | `list.clear()`            | O(n) must un-link every node for GC            |
| `sort`        | Sorts using a Comparator                   | `void`             | `list.sort(null)`         | O(n log n) copies to array, sorts, copies back |
| `subList`     | Gets a view of a range                     | `List<E>`          | `list.subList(0, 5)`      | O(1) to create, O(n) to navigate               |
| `toArray`     | Converts to an array                       | `Object[]`         | `list.toArray()`          | O(n)                                           |

Table 3: Performance, Internal & Specialized Methods

These handle node operations, queue/stack behavior, safety checks, and serialization.

| Method                       | Description                               | Category | Usage Context              | Big O                           |
| ---------------------------- | ----------------------------------------- | -------- | -------------------------- | ------------------------------- |
| `peek` / `peekFirst`         | Looks at head element without removal     | Queue    | `list.peek()`              | O(1)                            |
| `peekLast`                   | Looks at tail element without removal     | Queue    | `list.peekLast()`          | O(1)                            |
| `poll` / `pollFirst`         | Retrieves and removes head element        | Queue    | `list.poll()`              | O(1)                            |
| `pollLast`                   | Retrieves and removes tail element        | Queue    | `list.pollLast()`          | O(1)                            |
| `offer` / `offerLast`        | Appends element to tail                   | Queue    | `list.offer("A")`          | O(1)                            |
| `offerFirst`                 | Prepends element to head                  | Queue    | `list.offerFirst("A")`     | O(1)                            |
| `push`                       | Pushes element onto stack (head)          | Stack    | `list.push("A")`           | O(1)                            |
| `pop`                        | Pops element off stack (head)             | Stack    | `list.pop()`               | O(1)                            |
| `linkFirst` / `linkLast`     | Internal node linkage helpers             | Internal | Architecture wiring        | O(1)                            |
| `unlinkFirst` / `unlinkLast` | Internal node removal helpers             | Internal | Architecture wiring        | O(1)                            |
| `node(index)`                | Internal helper to fetch node at position | Internal | Search optimization logic  | O(n) traverses from closest end |
| `checkForComod`              | Checks for concurrent edits               | Safety   | Used by Iterators          | O(1)                            |
| `clone`                      | Shallow copy of the list                  | Utility  | `list.clone()`             | O(n)                            |
| `spliterator`                | Parallel iteration support                | Streams  | `list.spliterator()`       | O(1)                            |
| `writeObject`                | Serializes the list nodes                 | Utility  | `java.io` operations       | O(n)                            |
| `readObject`                 | Deserializes the list nodes               | Utility  | Reading from a byte stream | O(n)                            |

Table 1: Data `-->` LinkedList

| From                 | Example                                                    | When to use this                                                 | Protip                                                                                |
| -------------------- | ---------------------------------------------------------- | ---------------------------------------------------------------- | ------------------------------------------------------------------------------------- |
| **Array**            | `new LinkedList<>(Arrays.asList(arr))`                     | To turn a fixed array into a node chain.                         | Prefer `ArrayList` unless you need frequent front inserts or deletions.               |
| **Fixed List**       | `new LinkedList<>(List.of(a, b))`                          | Converting an immutable list into a modifiable linked structure. | Avoid if you intend to do a lot of indexed random access later.                       |
| **Other Collection** | `new LinkedList<>(hashSet)`                                | Converting a `Set` or `Queue` into a sequential chain.           | This preserves collection data while adding head/tail insertion advantages.           |
| **2D Array**         | `new LinkedList<>(Arrays.deepAsList(matrix))`              | When converting a grid into nested linked collections.           | Yields a list where rows can be detached or manipulated in constant time.             |
| **Stream**           | `stream.collect(Collectors.toCollection(LinkedList::new))` | Saving filtered or mapped data into a `LinkedList`.              | Handy if the resulting stream output is going to feed straight into a Queue pipeline. |
| **Iterable**         | `list.addAll(otherCollection)`                             | To append elements from another source into the chain.           | Runs in linear time relative to the incoming data size, stitching links at the end.   |

Table 2: LinkedList `-->` Data Types

| To                  | Example                                                    | When to use this                                             | Protip                                                                                          |
| ------------------- | ---------------------------------------------------------- | ------------------------------------------------------------ | ----------------------------------------------------------------------------------------------- |
| **Array**           | `list.toArray(new String)`                                 | When an API requires a standard sequential `String[]`.       | Elements are unlinked and poured into contiguous memory; takes linear time.                     |
| **Immutable List**  | `List.copyOf(list)`                                        | To snapshot the linked sequence into a locked container.     | The resulting `List` uses a compact array structure behind the scenes, shedding node overhead.  |
| **Set**             | `new HashSet<>(list)`                                      | When you need to strip all duplicate values from your chain. | Drops pointer sequences entirely in favor of hash buckets; insertion sequence is lost.          |
| **ArrayList**       | `new ArrayList<>(list)`                                    | When you need efficient random index lookups instead.        | Highly recommended if you are done building the list dynamically and now need to read it often. |
| **Sub-List**        | `list.subList(0, 5)`                                       | To get a window view of a specific node range.               | Structural changes to this window will re-wire nodes in the master `LinkedList`.                |
| **Primitive Array** | `list.stream().mapToInt(i->i).toArray()`                   | To unpack wrapped `Integer` nodes into a primitive `int[]`.  | Bypasses boxed references completely, shrinking memory usage drastically.                       |
| **Map**             | `list.stream().collect(Collectors.toMap(Obj::id, o -> o))` | Turning node payloads into a searchable key-lookup graph.    | Speeds up item discovery from sequential node hopping to instantaneous hash-table lookup.       |
| **String**          | `String.join(", ", list)`                                  | Turning a list of Strings into a single text sequence.       | Traverses nodes sequentially from head to tail to stitch the final text block.                  |
