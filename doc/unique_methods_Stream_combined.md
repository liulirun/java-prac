# Unique Stream Methods - Combined Guide

Use it to remember how to create streams, transform them, finish them, collect results, and sort with comparators.

## Quick Mental Model

| Step | What happens | Common methods |
| ---- | ------------ | -------------- |
| **1. Source** | Create a stream from data | `stream`, `Stream.of`, `Arrays.stream`, `Files.lines`, `IntStream.range` |
| **2. Intermediate operations** | Shape the data lazily | `filter`, `map`, `flatMap`, `distinct`, `sorted`, `limit`, `skip` |
| **3. Terminal operation** | Trigger processing and produce a result | `collect`, `toList`, `forEach`, `count`, `reduce`, `findFirst`, `anyMatch` |

## 3. Intermediate Operations

Intermediate operations are lazy: they describe work, but nothing runs until a terminal operation is called.

| Method | Purpose | Return type | Example |
| ------ | ------- | ----------- | ------- |
| `filter` | Keep elements matching a condition. | `Stream<T>` | `.filter(n -> n > 10)` |
| `map` | Transform each element. | `Stream<R>` | `.map(String::toUpperCase)` |
| `flatMap` | Flatten nested streams or collections. | `Stream<R>` | `.flatMap(List::stream)` |
| `mapMulti` | Emit zero, one, or many values per input. | `Stream<R>` | `.mapMulti((item, out) -> ...)` |
| `distinct` | Remove duplicates using `equals()`. | `Stream<T>` | `.distinct()` |
| `sorted` | Sort naturally or with a `Comparator`. | `Stream<T>` | `.sorted(Comparator.reverseOrder())` |
| `limit` | Keep only the first N items. | `Stream<T>` | `.limit(5)` |
| `skip` | Drop the first N items. | `Stream<T>` | `.skip(20)` |
| `peek` | Inspect values while debugging. | `Stream<T>` | `.peek(System.out::println)` |
| `mapToInt` / `mapToLong` / `mapToDouble` | Convert to a primitive stream. | `IntStream`, `LongStream`, `DoubleStream` | `.mapToInt(User::age)` |
| `takeWhile` | Take items until the predicate becomes false. | `Stream<T>` | `.takeWhile(n -> n < 100)` |
| `dropWhile` | Skip items until the predicate becomes false. | `Stream<T>` | `.dropWhile(n -> n < 10)` |

## 4. Terminal Operations

Terminal operations are eager: they start the stream pipeline and return the final result.

| Method | Purpose | Return type | Example |
| ------ | ------- | ----------- | ------- |
| `collect` | Convert stream output into a collection, map, string, or summary. | `R` | `.collect(Collectors.toList())` |
| `toList` | Convert directly to an unmodifiable list. | `List<T>` | `.toList()` |
| `forEach` | Run an action for each element. | `void` | `.forEach(System.out::println)` |
| `count` | Count elements. | `long` | `.count()` |
| `reduce` | Combine elements into one value. | `Optional<T>` or `T` | `.reduce(Integer::sum)` |
| `findFirst` | Return the first element if present. | `Optional<T>` | `.findFirst()` |
| `findAny` | Return any element if present. | `Optional<T>` | `.findAny()` |
| `anyMatch` | Check whether at least one item matches. | `boolean` | `.anyMatch(String::isEmpty)` |
| `allMatch` | Check whether all items match. | `boolean` | `.allMatch(n -> n > 0)` |
| `noneMatch` | Check whether no items match. | `boolean` | `.noneMatch(Objects::isNull)` |
| `max` / `min` | Find the largest or smallest item. | `Optional<T>` | `.max(Comparator.naturalOrder())` |
| `toArray` | Convert to an array. | `Object[]` or `T[]` | `.toArray(String[]::new)` |

## 5. Common Collectors

Collectors are used inside `.collect(...)`. They are especially useful when building lists, maps, groups, and summary values.

| Category | Collector | Main purpose |
| -------- | --------- | ------------ |
| **Data accumulation** | `Collectors.toList()` | Gather elements into a list. |
| **Data accumulation** | `Collectors.toSet()` | Gather unique elements into a set. |
| **Data accumulation** | `Collectors.toCollection(TreeSet::new)` | Gather elements into a specific collection type. |
| **Key-value pairs** | `Collectors.toMap(...)` | Convert elements into a `Map`. |
| **Grouping** | `Collectors.groupingBy(...)` | Group elements by a property. |
| **Grouping** | `Collectors.partitioningBy(...)` | Split elements into `true` and `false` buckets. |
| **Aggregation** | `Collectors.joining(", ")` | Join strings into one string. |
| **Aggregation** | `Collectors.counting()` | Count elements, often inside `groupingBy`. |
| **Aggregation** | `Collectors.summingInt(...)` | Sum integer values. |
| **Aggregation** | `Collectors.averagingInt(...)` | Average integer values. |
| **Aggregation** | `Collectors.summarizingInt(...)` | Get count, sum, min, average, and max at once. |

## 6. Stream Methods vs Collector Versions

Use stream methods for the whole pipeline. Use collector versions when the operation should happen inside each group or partition.

| Stream method | Stream return type | Collector version | Collector result | Best use |
| ------------- | ------------------ | ----------------- | ---------------- | -------- |
| `.map(function)` | `Stream<R>` | `Collectors.mapping(function, downstream)` | Downstream result | Transform values inside a group. |
| `.filter(predicate)` | `Stream<T>` | `Collectors.filtering(predicate, downstream)` | Downstream result | Filter values inside a group. |
| `.flatMap(function)` | `Stream<R>` | `Collectors.flatMapping(function, downstream)` | Downstream result | Flatten nested values inside a group. |
| `.count()` | `long` | `Collectors.counting()` | `Long` | Count items per group. |
| `.distinct()` | `Stream<T>` | Usually `Collectors.toSet()` | `Set<T>` | Remove duplicates per group. |
| `.reduce(...)` | `Optional<T>` or `T` | `Collectors.reducing(...)` | `Optional<T>` or `T` | Reduce values per group. |
| `.toList()` | `List<T>` | `Collectors.toList()` | `List<T>` | Collect values per group. |

### Example: Global vs Per-Group

```java
// Global: filter the whole list, then group.
Map<String, List<User>> activeByRole = users.stream()
    .filter(User::active)
    .collect(Collectors.groupingBy(User::role));

// Per-group: group everyone, but keep only active users inside each bucket.
Map<String, List<User>> activeInsideEachRole = users.stream()
    .collect(Collectors.groupingBy(
        User::role,
        Collectors.filtering(User::active, Collectors.toList())
    ));
```

## 7. Comparator Utilities

Comparator methods are commonly passed to `.sorted(...)`, `.max(...)`, and `.min(...)`.

| Method | Purpose | Example |
| ------ | ------- | ------- |
| `Comparator.naturalOrder()` | Sort ascending by natural order. | `.sorted(Comparator.naturalOrder())` |
| `Comparator.reverseOrder()` | Sort descending by natural order. | `.sorted(Comparator.reverseOrder())` |
| `Comparator.comparing(...)` | Sort objects by one property. | `.sorted(Comparator.comparing(User::age))` |
| `thenComparing(...)` | Add another sort rule for ties. | `.sorted(comparing(User::lastName).thenComparing(User::firstName))` |
| `reversed()` | Reverse an existing comparator. | `.sorted(comparing(User::age).reversed())` |
| `Comparator.nullsFirst(...)` | Put `null` values first safely. | `.sorted(nullsFirst(naturalOrder()))` |
| `Comparator.nullsLast(...)` | Put `null` values last safely. | `.sorted(nullsLast(naturalOrder()))` |

## 8. Small Warnings

| Topic | Remember |
| ----- | -------- |
| **Streams are single-use** | After a terminal operation, create a new stream if you need another pipeline. |
| **`peek` is mostly for debugging** | Avoid using it for important side effects. |
| **`parallelStream` is not automatically faster** | Use it only for large data and independent, CPU-heavy work. |
| **Duplicate map keys fail by default** | `Collectors.toMap(...)` throws unless you provide a merge function. |
| **Internal `lambda$...` methods** | These are compiler-generated implementation details, not methods developers call directly. |


## 1. Data to Stream

| From | Example | When to use this | Tip |
| ---- | ------- | ---------------- | --- |
| **Individual items** | `Stream.of("A", "B")` | You have a few values, not a collection. | Good for quick temporary streams. |
| **Collection** | `list.stream()` | Standard way to process a `List`, `Set`, or other `Collection`. | Use `parallelStream()` only when work is large and independent. |
| **Object array** | `Arrays.stream(fruits)` | You have `String[]`, `Integer[]`, or another object array. | Best general array-to-stream option. |
| **Primitive array** | `IntStream.of(nums)` | You have `int[]`, `long[]`, or `double[]`. | Primitive streams avoid boxing overhead. |
| **Numeric range** | `IntStream.range(0, 10)` | You need loop-like number generation. | `range` excludes the end; `rangeClosed` includes it. |
| **Map entries** | `map.entrySet().stream()` | You need keys and values together. | Use `keySet()` or `values()` if you only need one side. |
| **Text file** | `Files.lines(path)` | You want to read a file line by line. | Lazy, so use try-with-resources to close it. |
| **Empty source** | `Stream.empty()` | You want to return "no data" safely. | Prefer this over returning `null`. |
| **Generated values** | `Stream.iterate(0, n -> n + 2)` | You need a generated sequence. | Limit infinite streams with `limit` or a predicate. |
| **Two streams** | `Stream.concat(s1, s2)` | You need one stream after another. | Avoid deep chains of `concat`; collect or flatten instead. |

## 2. Stream to Data

| To | Example | When to use this | Tip |
| -- | ------- | ---------------- | --- |
| **List** | `.toList()` | You want a simple result list. | Java 16+ `toList()` returns an unmodifiable list. |
| **Mutable list** | `.collect(Collectors.toList())` | You need collector style or older Java. | If you require `ArrayList`, use `toCollection(ArrayList::new)`. |
| **Set** | `.collect(Collectors.toSet())` | You want duplicates removed. | Use `toCollection(LinkedHashSet::new)` to keep encounter order. |
| **Map** | `.collect(Collectors.toMap(User::id, u -> u))` | You want a key-value lookup. | Duplicate keys need a merge function. |
| **Grouped map** | `.collect(Collectors.groupingBy(User::role))` | You want categories like SQL `GROUP BY`. | Result is usually `Map<K, List<T>>`. |
| **Partitioned map** | `.collect(Collectors.partitioningBy(User::active))` | You want two groups: `true` and `false`. | Useful for pass/fail or active/inactive splits. |
| **String** | `.collect(Collectors.joining(", "))` | You want one formatted string. | Supports delimiter, prefix, and suffix. |
| **Array** | `.toArray(String[]::new)` | An API needs an array. | Constructor reference keeps the correct array type. |
| **Primitive array** | `.mapToInt(Integer::intValue).toArray()` | You need `int[]`, `long[]`, or `double[]`. | Avoids boxed wrapper arrays like `Integer[]`. |
| **Number** | `.count()`, `.sum()`, `.average()` | You need one numeric result. | `sum` and `average` are on primitive streams. |
| **Optional value** | `.findFirst()`, `.max(...)`, `.min(...)` | A single result may or may not exist. | Handle empty results with `orElse`, `orElseGet`, or `ifPresent`. |
