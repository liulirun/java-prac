Java Stream, Collectors, and Comparator Summary

Here is the complete Java Stream guide combined into quick-scan markdown tables, including intermediate operations, terminal operations, Collectors, and Comparator utilities.

1\. Main Stream Functions (Intermediate & Terminal)

Intermediate Operations (Lazy)

| Function       | Purpose                                         | Common Use Case                                   |
| -------------- | ----------------------------------------------- | ------------------------------------------------- |
| **`filter`**   | Discards elements that fail a boolean condition | Removing inactive users from a list               |
| **`map`**      | Transforms each element into another object     | Extracting IDs from a list of user objects        |
| **`flatMap`**  | Flattens nested streams/collections into one    | Merging lists of orders from multiple customers   |
| **`distinct`** | Removes duplicate elements using `equals()`     | Finding unique tags in a blog database            |
| **`sorted`**   | Orders elements naturally or via `Comparator`   | Sorting products by price high-to-low             |
| **`limit`**    | Truncates stream to a specific maximum size     | Fetching only the top 5 highest scores            |
| **`skip`**     | Discards the first *N* elements                 | Implementing pagination (skipping the first page) |

Terminal Operations (Eager)

| Function        | Purpose                                         | Common Use Case                                |
| --------------- | ----------------------------------------------- | ---------------------------------------------- |
| **`forEach`**   | Performs an action on every element             | Printing results to the console log            |
| **`reduce`**    | Combines elements into a single summary value   | Calculating the total sum of item prices       |
| **`count`**     | Returns the total number of elements            | Checking how many items matched a filter       |
| **`anyMatch`**  | Checks if at least one element fits a condition | Verifying if an email list contains admins     |
| **`findFirst`** | Retrieves the first element found               | Finding the oldest employee in a sorted stream |
| **`collect`**   | Converts the stream into a new structure        | Packing stream results back into a `List`      |

2\. `.collect(Collectors)` Functions

| Category                 | Collector Function               | Main Purpose                                         |
| ------------------------ | -------------------------------- | ---------------------------------------------------- |
| **Data Accumulation**    | `Collectors.toList()`            | Gathers elements into a standard `List`              |
|                          | `Collectors.toSet()`             | Gathers elements into a `Set` to remove duplicates   |
|                          | `Collectors.toCollection(...)`   | Gathers elements into a specific type like `TreeSet` |
| **Key-Value Pairs**      | `Collectors.toMap(...)`          | Converts elements into an indexed key-value `Map`    |
| **Grouping / Splitting** | `Collectors.groupingBy(...)`     | Groups elements into a `Map` based on a property     |
|                          | `Collectors.partitioningBy(...)` | Splits elements into two groups: `true` and `false`  |
| **Aggregation**          | `Collectors.joining(...)`        | Concatenates string elements into a single string    |
|                          | `Collectors.counting()`          | Counts elements (often used inside `groupingBy`)     |
|                          | `Collectors.summarizingInt(...)` | Computes count, sum, min, average, and max at once   |

3\. Comparator Utilities (For sorting streams)

These methods from `java.util.Comparator` are passed directly into `.sorted()` or specific collectors to control data order.

| Function                        | Purpose                                              | Common Use Case                                             |
| ------------------------------- | ---------------------------------------------------- | ----------------------------------------------------------- |
| **`Comparator.naturalOrder()`** | Sorts elements in standard ascending order           | Sorting a stream of integers from lowest to highest         |
| **`Comparator.reverseOrder()`** | Sorts elements in standard descending order          | Sorting a stream of strings alphabetically from Z to A      |
| **`Comparator.comparing(...)`** | Sorts objects by a specific property/getter          | Sorting a list of `User` objects by `User::getAge`          |
| **`thenComparing(...)`**        | Chains a second sorting rule if the first rule ties  | Sorting by `LastName`, then breaking ties by `FirstName`    |
| **`reversed()`**                | Reverses the logic of an existing custom comparator  | Changing a custom "low-to-high" sort into "high-to-low"     |
| **`nullsFirst(...)`**           | Puts `null` values at the start of the sorted stream | Sorting data safely without throwing `NullPointerException` |
| **`nullsLast(...)`**            | Puts `null` values at the end of the sorted stream   | Keeping rows with missing values at the bottom of a list    |
