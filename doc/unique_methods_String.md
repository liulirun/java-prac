# Unique String Methods

## Table 1: Data `-->` String

| From                            | Example                                | When to use this                                | Protip                                                                           |
| ------------------------------- | -------------------------------------- | ----------------------------------------------- | -------------------------------------------------------------------------------- |
| **Any Primitive** (int, double) | `String.valueOf(10)`                   | General purpose conversion.                     | Prefer this over `"" + 10` because it’s clearer and slightly faster.             |
| **char\[]**                     | `String.valueOf(charArray)`            | Converting raw character data back to text.     | `new String(charArray)` does the same, but `valueOf` is generally more readable. |
| **Object** (User, Car)          | `obj.toString()`                       | Text representation of a custom class.          | Always override `toString()` in your classes or you'll get a memory address.     |
| **ArrayList**                   | `list.toString()`                      | To see contents in `[a, b, c]` format.          | This is already called automatically if you put a list in a `println`.           |
| **Array** (String\[], int\[])   | `Arrays.toString(arr)`                 | For debugging or logging small arrays.          | Use `deepToString()` for nested or multi-dimensional arrays.                     |
| **StringBuilder**               | `sb.toString()`                        | Finalizing a string built inside a loop.        | Never use `+` in a loop; always use `StringBuilder` for performance.             |
| **Collection of Strings**       | `String.join(", ", list)`              | When you need a specific separator (e.g., CSV). | Works directly on `List<String>` without needing a manual loop.                  |
| **Stream**                      | `stream.collect(Collectors.joining())` | Complex filtering/mapping before joining.       | Use the 3-argument version to add a prefix and suffix like `[A, B, C]`.          |

## Table 2: String `-->` Data Types

| To                   | Example                                | When to use this                                    | Protip                                                                         |
| -------------------- | -------------------------------------- | --------------------------------------------------- | ------------------------------------------------------------------------------ |
| **Integer / Double** | `Integer.parseInt(str)`                | Converting user input or web params into numbers.   | Throws `NumberFormatException` on invalid strings; always use a try-catch.     |
| **Boolean**          | `Boolean.parseBoolean(str)`            | Converting "true"/"false" flags.                    | Case-insensitive; returns `true` for "true" and `false` for everything else.   |
| **Date / Time**      | `LocalDate.parse(str)`                 | Converting ISO dates (YYYY-MM-DD) into objects.     | For custom formats (e.g. "MM/DD/YY"), you must use a `DateTimeFormatter`.      |
| **Enum**             | `Color.valueOf(str.toUpperCase())`     | Matching a string to a specific constant.           | Ensure the casing matches the Enum exactly; `toUpperCase()` makes this safer.  |
| **char\[] (Array)**  | `str.toCharArray()`                    | Manipulating individual characters (e.g., ciphers). | Modifying this array does **not** change the original immutable string.        |
| **byte\[] (Bytes)**  | `str.getBytes(StandardCharsets.UTF_8)` | Before network transmission or file saving.         | **Always** specify a Charset to avoid encoding bugs across different OS types. |
| **Stream**           | `str.chars()`                          | Functional logic to count or filter characters.     | Returns an `IntStream`; use `.mapToObj(c -> (char)c)` to work with characters. |
| **List / Array**     | `str.split(", ")`                      | Turning CSV strings into a collection of items.     | Use a limit like `split(",", 2)` to stop splitting after the first match.      |


## Table 2: Frequent & Public API Methods

These are the essential tools for text manipulation, searching, and validation.

| Method                | Description                   | Return Type | Example                 |
| --------------------- | ----------------------------- | ----------- | ----------------------- |
| `substring`           | Extracts part of the string   | `String`    | `s.substring(0, 5)`     |
| `contains`            | Checks for a sequence         | `boolean`   | `s.contains("abc")`     |
| `replace`             | Replaces characters/words     | `String`    | `s.replace("a", "b")`   |
| `split`               | Breaks string into array      | `String[]`  | `s.split(",")`          |
| `trim` / `strip`      | Removes whitespace            | `String`    | `s.trim()`              |
| `toLowerCase`         | Converts to lowercase         | `String`    | `s.toLowerCase()`       |
| `indexOf`             | Finds first position of char  | `int`       | `s.indexOf("x")`        |
| `isEmpty` / `isBlank` | Checks if string has text     | `boolean`   | `s.isBlank()`           |
| `startsWith`          | Checks prefix                 | `boolean`   | `s.startsWith("Hi")`    |
| `length`              | Returns character count       | `int`       | `s.length()`            |
| `charAt`              | Gets char at specific index   | `char`      | `s.charAt(0)`           |
| `join`                | Merges strings with delimiter | `String`    | `String.join("-", ...)` |

## Table 3: Specialized, Internal & Encoding Methods

These handle memory optimization, character encoding (UTF-8/ASCII), and JVM internal logic.

| Method             | Description                      | Category     | Usage Context                 |
| ------------------ | -------------------------------- | ------------ | ----------------------------- |
| `intern`           | Adds string to constant pool     | Optimization | `s.intern()`                  |
| `getBytes`         | Converts to byte array           | Encoding     | `s.getBytes("UTF-8")`         |
| `valueOf`          | Converts other types to String   | Utility      | `String.valueOf(123)`         |
| `matches`          | Validates against Regex          | Logic        | `s.matches("\\d+")`           |
| `decode / encode`  | Internal byte transformations    | Internal     | Handling different charsets   |
| `coder`            | Identifies encoding (Latin1/U16) | Internal     | JVM memory management         |
| `checkBounds`      | Validates index ranges           | Internal     | Bounds checking logic         |
| `repeat`           | Multiplies the string            | Utility      | `s.repeat(3)`                 |
| `indent / outdent` | Adjusts text margins             | Utility      | Formatting block text         |
| `codePointAt`      | Gets Unicode code point          | Unicode      | Handling emojis/special chars |
| `isLatin1`         | Checks internal storage type     | Internal     | Performance/Memory check      |
| `formatted`        | Template filling (Java 15+)      | Utility      | `"%s".formatted(v)`           |

**Note:** Methods like `decodeUTF8` and `isMalformed` are internal helpers that ensure your text doesn't become "garbage" characters when converting between different languages or file formats.

