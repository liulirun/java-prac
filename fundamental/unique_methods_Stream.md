# Unique Stream Methods

## Table 1: Data `-->` Java Stream

| From                 | Example                   | When to use this                                    | Protip                                                                       |
| -------------------- | ------------------------- | --------------------------------------------------- | ---------------------------------------------------------------------------- |
| **Individual Items** | `Stream.of("A", "B")`     | When you have a few loose variables to process.     | Great for creating a quick, temporary stream without a container.            |
| **Primitive Array**  | `IntStream.of(nums)`      | When working with `int[]`, `long[]`, or `double[]`. | Use specialized streams (`IntStream`) to avoid the "boxing" performance hit. |
| **Numeric Range**    | `IntStream.range(0, 10)`  | For loops or generating a sequence of numbers.      | `range` is exclusive (0-9), while `rangeClosed` is inclusive (0-10).         |
| **Empty Source**     | `Stream.empty()`          | To avoid returning `null` from a method.            | Returning an empty stream prevents `NullPointerExceptions` in the caller.    |
| **ArrayList / Set**  | `list.stream()`           | The standard way to filter or map any Collection.   | Use `parallelStream()` if the list is massive and tasks are independent.     |
| **Map**              | `map.entrySet().stream()` | When you need to process both keys and values.      | You can also use `map.keySet().stream()` if you only need the keys.          |
| **Object Array**     | `Arrays.stream(fruits)`   | When you have a `String[]` or `Integer[]` array.    | This is the most efficient way to start processing existing array data.      |
| **File / IO**        | `Files.lines(path)`       | When reading a text file line-by-line.              | This is "lazy"—it doesn't load the whole file into memory at once.           |

## Table 2: Stream `-->` Data Types

| To                  | Example                                | When to use this                                            | Protip                                                                    |
| ------------------- | -------------------------------------- | ----------------------------------------------------------- | ------------------------------------------------------------------------- |
| **List**            | `.collect(Collectors.toList())`        | The most common way to save results to an `ArrayList`.      | Since Java 16, you can use `.toList()` directly for an unmodifiable list. |
| **Set**             | `.collect(Collectors.toSet())`         | When you need to remove duplicates from your results.       | Order is not guaranteed unless you collect to a `LinkedHashSet`.          |
| **Map**             | `.collect(Collectors.toMap(...))`      | Converting a stream of objects into a Key-Value lookup.     | You must handle duplicate keys or it throws an `IllegalStateException`.   |
| **Grouping/Map**    | `.collect(Collectors.groupingBy(...))` | To categorize items into a Map of Lists.                    | This is basically the "SQL GROUP BY" equivalent for Java collections.     |
| **Single String**   | `.collect(Collectors.joining(", "))`   | To turn a stream of text into one formatted String.         | It can take a delimiter, prefix, and suffix (e.g., `[A, B, C]`).          |
| **Array**           | `.toArray(String[]::new)`              | When you need results in legacy or fixed-length structures. | Use the "constructor reference" to ensure the correct array type.         |
| **Numeric Value**   | `.count()` or `.sum()`                 | When you just need a single calculation (total items).      | For non-numeric streams, use `.reduce()` or `Collectors.summingInt()`.    |
| **Optional Object** | `.findFirst()` or `.max()`             | When you need a single result that might not exist.         | Use `.orElse()` to handle cases where the stream is empty.                |

## Table 2: Frequent & Intermediate Operations

These methods are the primary tools used to transform, filter, and shape your data before a result is produced.

| Method          | Description                          | Return Type | Typical Example                |
| --------------- | ------------------------------------ | ----------- | ------------------------------ |
| `filter`        | Keeps elements matching a condition  | `Stream<T>` | `.filter(n -> n > 10)`         |
| `map`           | Transforms each element              | `Stream<R>` | `.map(String::toUpperCase)`    |
| `flatMap`       | Flattens nested collections          | `Stream<R>` | `.flatMap(List::stream)`       |
| `distinct`      | Removes duplicate elements           | `Stream<T>` | `.distinct()`                  |
| `sorted`        | Sorts the stream                     | `Stream<T>` | `.sorted()`                    |
| `limit`         | Truncates stream to first N items    | `Stream<T>` | `.limit(5)`                    |
| `skip`          | Discards first N items               | `Stream<T>` | `.skip(2)`                     |
| `peek`          | Debugs/watches elements passing by   | `Stream<T>` | `.peek(System.out::println)`   |
| `mapToInt/Long` | Converts to a primitive stream       | `IntStream` | `.mapToInt(Integer::intValue)` |
| `of`            | Creates a stream from values         | `Stream<T>` | `Stream.of(1, 2, 3)`           |
| `takeWhile`     | Takes items until condition is false | `Stream<T>` | `.takeWhile(n -> n < 100)`     |
| `dropWhile`     | Skips items until condition is false | `Stream<T>` | `.dropWhile(n -> n < 10)`      |

## Table 3: Terminal, Matching & Specialized Methods

These methods trigger the processing of the stream and produce a final result (like a list, a count, or a boolean).

| Method          | Description                          | Return Type   | Typical Example                   |
| --------------- | ------------------------------------ | ------------- | --------------------------------- |
| `collect`       | Converts stream to List, Set, etc.   | `R`           | `.collect(Collectors.toList())`   |
| `toList`        | Direct conversion to List (Java 16+) | `List<T>`     | `.toList()`                       |
| `forEach`       | Performs action on each item         | `void`        | `.forEach(System.out::print)`     |
| `count`         | Returns total number of elements     | `long`        | `.count()`                        |
| `reduce`        | Combines elements into one value     | `Optional<T>` | `.reduce(Integer::sum)`           |
| `findFirst/Any` | Returns an element from the stream   | `Optional<T>` | `.findFirst()`                    |
| `any/allMatch`  | Checks if condition matches items    | `boolean`     | `.anyMatch(s -> s.isEmpty())`     |
| `max / min`     | Finds extreme values                 | `Optional<T>` | `.max(Comparator.natural())`      |
| `toArray`       | Converts stream to an array          | `Object[]`    | `.toArray()`                      |
| `mapMulti`      | One-to-many replacement for flatMap  | `Stream<R>`   | `.mapMulti((n, consumer) -> ...)` |
| `iterate`       | Generates an infinite sequence       | `Stream<T>`   | `Stream.iterate(0, n -> n + 2)`   |
| `concat`        | Joins two streams together           | `Stream<T>`   | `Stream.concat(s1, s2)`           |

**Note:** The `lambda$...` methods in your list are internal generated code used for mapping logic and are not intended to be called by developers directly.


