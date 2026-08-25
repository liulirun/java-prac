
### How Features map to Design Patterns

| Pattern | Component | Purpose |
| --- | --- | --- |
| **Builder Pattern** | `SqlQuery` | Programmatically builds SELECT, JOIN, and WHERE clauses safely. |
| **Strategy Pattern** | `SqlGeneratorStrategy` | Isolates SQL logic for specific tables/joins into separate classes. |
| **Factory Pattern** | `SqlGeneratorFactory` | Instantiates the correct strategy based on a string name passed from Cucumber. |

---

### 1. The Gherkin Feature File

In the `Examples:` block, you pass the values as variables using `<variable_name>`. Cucumber will substitute these into the Data Table for each scenario run.

```gherkin
Scenario Outline: Generate SQL with dynamic parameters
  Given I generate SQL for level1 "<level1_val>" and level2 "<level2_val>" with options:
    | option1 | <opt1_val> |
    | option2 | <opt2_val> |
    | option3 | <opt3_val> |

  Examples:
    | level1_val | level2_val | opt1_val | opt2_val | opt3_val |
    | L1_ALPHA   | L2_BETA    | OPT_A    | OPT_B    | OPT_C    |
    | L1_GAMMA   | L2_DELTA   | OPT_X    |          | OPT_Z    |

```

*Note: If an example value in the table is left blank (like `opt2_val` in row 2), Cucumber passes an empty string `""` or `null` to the step.*

---

### 2. The Java Step Definition

The step definition implementation remains identical to a standard Data Table step. Cucumber injects the evaluated Gherkin values into the `Map<String, String>` automatically.

```java
import io.cucumber.java.en.Given;
import java.util.Map;

public class SqlSteps {

    @Given("I generate SQL for level1 {string} and level2 {string} with options:")
    public void generateSqlWithTableAndOutline(String level1, String level2, Map<String, String> options) {
        
        SqlGenerator.Builder builder = new SqlGenerator.Builder(level1, level2);

        // Process options from the Data Table
        if (hasValue(options.get("option1"))) {
            builder.option1(options.get("option1"));
        }
        if (hasValue(options.get("option2"))) {
            builder.option2(options.get("option2"));
        }
        if (hasValue(options.get("option3"))) {
            builder.option3(options.get("option3"));
        }

        String sql = builder.build().buildSql();
        System.out.println("Generated SQL: " + sql);
    }

    private boolean hasValue(String val) {
        return val != null && !val.trim().isEmpty();
    }
}

```

---

### Alternative: Pure Scenario Outline (Without Data Table)

If every execution takes the same set of options, you can also collapse the options directly into the step sentence without using a Data Table at all:

```gherkin
Scenario Outline: Generate SQL dynamically
  Given I generate SQL for level1 "<level1>" and level2 "<level2>" with option1 "<opt1>", option2 "<opt2>", and option3 "<opt3>"

  Examples:
    | level1 | level2 | opt1  | opt2  | opt3  |
    | L1_A   | L2_B   | OPT1  | OPT2  | OPT3  |
    | L1_A   | L2_B   | OPT1  |       |       |

```

### multiple classes

When your SQL generation logic grows beyond a single query structure to handle **different tables, dynamic JOINs, and flexible SELECT fields**, you should transition from a basic Builder to a combination of **Strategy Pattern** and **Fluent Query Builder**.

Here is an architectural design using Java interfaces and classes to keep your Cucumber step definitions clean and maintainable.

---

### Key Architectural Concepts

1. **`SqlQuery` (Model):** Encapsulates query state (SELECT columns, FROM table, JOIN clauses, WHERE conditions).
2. **`QueryBuilder` (Fluent Builder):** Provides a clean API to assemble parts of the query step-by-step.
3. **`SqlStrategy` (Interface):** Encapsulates specialized generation logic if different table structures require entirely distinct SQL formats (e.g., Aggregations vs. Standard Joins).

---

### 1. Core Query State & Fluent Builder

#### `SqlQuery.java`

```java
package com.example.sql;

import java.util.ArrayList;
import java.util.List;

public class SqlQuery {
    private final List<String> selectFields = new ArrayList<>();
    private String fromTable;
    private final List<String> joins = new ArrayList<>();
    private final List<String> conditions = new ArrayList<>();

    public SqlQuery select(String... fields) {
        for (String field : fields) {
            if (field != null && !field.isBlank()) {
                this.selectFields.add(field);
            }
        }
        return this;
    }

    public SqlQuery from(String table) {
        this.fromTable = table;
        return this;
    }

    public SqlQuery join(String joinType, String table, String condition) {
        if (table != null && !table.isBlank()) {
            this.joins.add(joinType + " JOIN " + table + " ON " + condition);
        }
        return this;
    }

    public SqlQuery where(String condition) {
        if (condition != null && !condition.isBlank()) {
            this.conditions.add(condition);
        }
        return this;
    }

    public String build() {
        StringBuilder sql = new StringBuilder("SELECT ");
        
        // Select Fields
        if (selectFields.isEmpty()) {
            sql.append("*");
        } else {
            sql.append(String.join(", ", selectFields));
        }

        // From Table
        sql.append(" FROM ").append(fromTable != null ? fromTable : "default_table");

        // Joins
        for (String joinClause : joins) {
            sql.append(" ").append(joinClause);
        }

        // Where Conditions
        if (!conditions.isEmpty()) {
            sql.append(" WHERE ").append(String.join(" AND ", conditions));
        }

        return sql.toString();
    }
}

```

---

### 2. Strategy Pattern for Specific Query Types

Define an interface for specific SQL generators, and implement concrete strategies for different business domains or report types.

#### `SqlGeneratorStrategy.java` (Interface)

```java
package com.example.sql;

import java.util.Map;

public interface SqlGeneratorStrategy {
    String generate(String level1, String level2, Map<String, String> options);
}

```

#### Strategy 1: User Details Query (With Table Joins)

```java
package com.example.sql;

import java.util.Map;

public class UserDetailsSqlStrategy implements SqlGeneratorStrategy {

    @Override
    public String generate(String level1, String level2, Map<String, String> options) {
        SqlQuery query = new SqlQuery()
            .select("u.id", "u.name", "p.profile_data", "r.role_name")
            .from("users u")
            .join("LEFT", "profiles p", "u.id = p.user_id")
            .join("INNER", "roles r", "u.role_id = r.id")
            .where("u.level1 = '" + level1 + "'")
            .where("u.level2 = '" + level2 + "'");

        if (options.containsKey("option1")) {
            query.where("p.status = '" + options.get("option1") + "'");
        }
        if (options.containsKey("option2")) {
            query.where("r.region = '" + options.get("option2") + "'");
        }

        return query.build();
    }
}

```

#### Strategy 2: Summary/Aggregation Query

```java
package com.example.sql;

import java.util.Map;

public class SummaryMetricsSqlStrategy implements SqlGeneratorStrategy {

    @Override
    public String generate(String level1, String level2, Map<String, String> options) {
        SqlQuery query = new SqlQuery()
            .select("m.category", "COUNT(m.id) AS total", "AVG(m.score) AS avg_score")
            .from("metrics m")
            .join("LEFT", "category_ref c", "m.cat_id = c.id")
            .where("m.dept_level1 = '" + level1 + "'")
            .where("m.dept_level2 = '" + level2 + "'");

        if (options.containsKey("option1")) {
            query.where("c.type = '" + options.get("option1") + "'");
        }

        return query.build();
    }
}

```

---

### 3. Strategy Factory

Use a Factory class so Cucumber steps can pick the right generator using a string key from Gherkin.

#### `SqlGeneratorFactory.java`

```java
package com.example.sql;

import java.util.HashMap;
import java.util.Map;

public class SqlGeneratorFactory {

    private static final Map<String, SqlGeneratorStrategy> STRATEGIES = new HashMap<>();

    static {
        STRATEGIES.put("USER_DETAILS", new UserDetailsSqlStrategy());
        STRATEGIES.put("SUMMARY_METRICS", new SummaryMetricsSqlStrategy());
    }

    public static SqlGeneratorStrategy getStrategy(String queryType) {
        SqlGeneratorStrategy strategy = STRATEGIES.get(queryType.toUpperCase());
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown query type: " + queryType);
        }
        return strategy;
    }
}

```

---

### 4. Gherkin Feature File & Java Cucumber Step

Now, you can pass the **`queryType`** dynamically from Gherkin.

#### `sql_generation.feature`

```gherkin
Feature: Multi-Table SQL Generation

  Scenario Outline: Generate query based on query type and parameter matrix
    Given I generate "<query_type>" SQL for level1 "<level1_val>" and level2 "<level2_val>" with options:
      | option1 | <opt1_val> |
      | option2 | <opt2_val> |

    Examples:
      | query_type      | level1_val | level2_val | opt1_val | opt2_val |
      | USER_DETAILS    | L1_ALPHA   | L2_BETA    | ACTIVE   | REGION_US|
      | SUMMARY_METRICS | L1_GAMMA   | L2_DELTA   | FINANCE  |          |

```

#### `SqlSteps.java`

```java
package com.example.steps;

import com.example.sql.SqlGeneratorFactory;
import com.example.sql.SqlGeneratorStrategy;
import io.cucumber.java.en.Given;

import java.util.Map;

public class SqlSteps {

    @Given("I generate {string} SQL for level1 {string} and level2 {string} with options:")
    public void generateSqlForType(String queryType, String level1, String level2, Map<String, String> options) {

        // Get strategy via factory
        SqlGeneratorStrategy strategy = SqlGeneratorFactory.getStrategy(queryType);

        // Generate SQL
        String generatedSql = strategy.generate(level1, level2, options);

        System.out.println("[" + queryType + "] Generated SQL:\n" + generatedSql);
    }
}

```

---

