# Unique HashMap Methods

## Table 1: Data `-->` HashMap

| From                 | Example                                         | When to use this                                      | Protip                                                                                     |
| -------------------- | ----------------------------------------------- | ----------------------------------------------------- | ------------------------------------------------------------------------------------------ |
| **Loose Pairs**      | `Map.of("K1", "V1")`                            | Quick, small lookups (up to 10 pairs).                | Creates an **immutable** map; wrap in `new HashMap<>(...)` if you need to edit it.         |
| **Another Map**      | `new HashMap<>(oldMap)`                         | Creating a "shallow copy" of an existing map.         | Structural changes won't affect the old map, but changes to *objects* inside will.         |
| **Properties File**  | `new HashMap<Object, Object>(props)`            | Loading config settings into a searchable map.        | Use `map.putAll(properties)` on an existing map to merge multiple sources.                 |
| **A List/Stream**    | `list.stream().collect(Collectors.toMap(...))`  | Turning a list of objects into a lookup table.        | If keys might repeat, use the 3-argument version `(old, new) -> new` to handle collisions. |
| **Two Lists/Arrays** | `IntStream.range(0, size).collect(...)`         | When you have one list of keys and another of values. | Ensure both lists are the same length to avoid `IndexOutOfBoundsException`.                |
| **CSV / File**       | `Files.lines(p).collect(Collectors.toMap(...))` | Parsing a simple 2-column text file into memory.      | Always validate the split length before collecting to avoid `ArrayIndexOutOfBounds`.       |
| **JSON String**      | `mapper.readValue(json, HashMap.class)`         | Receiving data from a Web API or config file.         | Requires **Jackson** or **Gson**; Java doesn't parse JSON natively.                        |

## Table 2: HashMap `-->` Data Types

| To                   | Example                            | When to use this                                      | Protip                                                                        |
| -------------------- | ---------------------------------- | ----------------------------------------------------- | ----------------------------------------------------------------------------- |
| **List of Keys**     | `new ArrayList<>(map.keySet())`    | When you only need the unique identifiers.            | Perfect for creating a "directory" or index of available data.                |
| **List of Values**   | `new ArrayList<>(map.values())`    | When you need the data objects, ignoring the keys.    | The list order matches the map's internal iteration order (usually random).   |
| **Sorted Map**       | `new TreeMap<>(map)`               | When keys must be in alphabetical or numerical order. | Note that `TreeMap` is slower for inserts but keeps everything sorted.        |
| **Stream (Entries)** | `map.entrySet().stream()`          | To filter, sort, or transform map logic.              | Mapping to `entry.getKey()` or `entry.getValue()` lets you pivot data easily. |
| **Single Value**     | `map.getOrDefault(key, "Default")` | Safely retrieving one item without `null` checks.     | Use this to prevent `NullPointerExceptions` when a key might be missing.      |
| **JSON String**      | `mapper.writeValueAsString(map)`   | Sending data to a Web UI or saving to a database.     | Handles complex nested objects automatically if using Jackson or Gson.        |
| **Properties**       | `props.putAll(map)`                | Exporting settings back into a `.properties` file.    | Keys and values should be `String` types for the best compatibility.          |
| **String (Debug)**   | `map.toString()`                   | Quick debugging to see what's inside.                 | Hard to read for large maps; use a `forEach` loop for "pretty" printing.      |

## Table 2: Frequent & Public API Methods

These are the standard methods you call when interacting with a `HashMap`.

| Method         | Description                  | Return Type     | Example                   |
| -------------- | ---------------------------- | --------------- | ------------------------- |
| `put`          | Adds/Updates a pair          | `V`             | `map.put("k", v)`         |
| `get`          | Gets value by key            | `V`             | `map.get("k")`            |
| `remove`       | Deletes by key               | `V`             | `map.remove("k")`         |
| `containsKey`  | Checks if key exists         | `boolean`       | `map.containsKey("k")`    |
| `size`         | Count of entries             | `int`           | `map.size()`              |
| `clear`        | Wipes all data               | `void`          | `map.clear()`             |
| `isEmpty`      | Checks if empty              | `boolean`       | `map.isEmpty()`           |
| `keySet`       | Returns set of keys          | `Set<K>`        | `map.keySet()`            |
| `values`       | Returns collection of values | `Collection<V>` | `map.values()`            |
| `entrySet`     | Returns set of entries       | `Set<Entry>`    | `map.entrySet()`          |
| `getOrDefault` | Get value or default         | `V`             | `map.getOrDefault(k, 0)`  |
| `forEach`      | Functional iteration         | `void`          | `map.forEach((k,v)->...)` |

## Table 3: Advanced, Internal & Utility Methods

These include functional computing methods and internal logic like tree management and serialization.

| Method         | Description               | Category   | Usage Context                |
| -------------- | ------------------------- | ---------- | ---------------------------- |
| `compute`      | Computes a new value      | Functional | `map.compute(k, remapping)`  |
| `merge`        | Merges old/new values     | Functional | `map.merge(k, v, bifunc)`    |
| `putIfAbsent`  | Adds if key is missing    | Logic      | `map.putIfAbsent(k, v)`      |
| `replace`      | Replaces if present       | Logic      | `map.replace(k, oldV, newV)` |
| `resize`       | Grows internal table      | Internal   | Triggered by `loadFactor`    |
| `hash`         | Calculates hash code      | Internal   | `static final int hash()`    |
| `treeifyBin`   | Converts bins to Trees    | Internal   | High collision handling      |
| `newNode`      | Creates a basic Node      | Internal   | Entry creation               |
| `readObject`   | Deserializes map          | Utility    | `java.io` operations         |
| `clone`        | Shallow copy              | Utility    | `(HashMap) map.clone()`      |
| `tableSizeFor` | Power-of-two sizing       | Internal   | Initial capacity logic       |
| `afterNode...` | Callbacks (LinkedHashMap) | Internal   | Access/Removal hooks         |

**Note:** Methods like `putVal`, `getNode`, and `removeNode` are the internal workhorses that the public `put`, `get`, and `remove` methods call to do the actual data heavy lifting.

Would you like to see a code snippet showing how **computeIfAbsent** can replace a 5-line `if` statement?