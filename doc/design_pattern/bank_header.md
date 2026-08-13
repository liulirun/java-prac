To manage dynamic HTTP REST requests cleanly, you can combine the **Builder Pattern** (for step-by-step construction) with the **Strategy Pattern** (to isolate how requests are actually executed).

Here is exactly how to structure your class to support default headers, adding new headers, deleting base headers, and handling query parameters.

🛠️ The Architecture Blueprint

```java
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class RestRequest {
    private final String baseUrl;
    private final Map<String, String> headers;
    private final Map<String, String> queryParams;

    // Private constructor: Only accessible by the Builder
    private RestRequest(Builder builder) {
        this.baseUrl = builder.baseUrl;
        this.headers = new HashMap<>(builder.headers);
        this.queryParams = new HashMap<>(builder.queryParams);
    }

    // Getters for execution logic
    public String getFullUrl() {
        if (queryParams.isEmpty()) return baseUrl;
        String queryString = queryParams.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("&"));
        return baseUrl + "?" + queryString;
    }

    public Map<String, String> getHeaders() { return headers; }

    // --- THE BUILDER NESTED CLASS ---
    public static class Builder {
        private final String baseUrl;
        private final Map<String, String> headers = new HashMap<>();
        private final Map<String, String> queryParams = new HashMap<>();

        public Builder(String baseUrl) {
            this.baseUrl = baseUrl;
            // 1. DATA STRUCTURE SELECTION: Automatically load base headers
            this.headers.put("Content-Type", "application/json");
            this.headers.put("User-Agent", "JavaRestApp/1.0");
        }

        // SCENARIO: Ability to add more headers
        public Builder addHeader(String key, String value) {
            this.headers.put(key, value);
            return this;
        }

        // SCENARIO: Delete from base headers
        public Builder removeHeader(String key) {
            this.headers.remove(key);
            return this;
        }

        // SCENARIO: Consider query parameters
        public Builder addQueryParam(String key, String value) {
            this.queryParams.put(key, value);
            return this;
        }

        public RestRequest build() {
            return new RestRequest(this);
        }
    }
}
```

📋 Pattern & Data Structure Breakdown

1\. Data Structure Choice: `HashMap<String, String>`

- **Why:** Headers and query parameters are key-value pairs.
- **The Benefit:** `HashMap` permits `O(1)` constant-time lookups, additions (`.put()`), and removals (`.remove()`). This satisfies your exact requirement to cleanly delete base headers on the fly.

2\. Design Pattern: Fluent Builder

- **Why:** Building HTTP requests is inherently incremental. You need to configure paths, tokens, and query flags step-by-step.
- **The Benefit:** It prevents messy parameter lists and allows you to establish default base headers inside the `Builder` constructor immediately upon instantiation.

💻 How to Use It (Client Code Example)

Here is how you can use this configuration class to build distinct HTTP request targets:

```java
public class Main {
    public static void main(String[] args) {
        // Example 1: Standard call using default base headers
        RestRequest standardRequest = new RestRequest.Builder("https://example.com")
                .addQueryParam("limit", "10")
                .addQueryParam("page", "1")
                .build();

        // Example 2: Specialized call changing/deleting base headers
        RestRequest authorizedRequest = new RestRequest.Builder("https://example.com")
                .removeHeader("User-Agent")              // Wipes out default user agent
                .addHeader("Authorization", "Bearer XYZ") // Appends new custom header
                .addQueryParam("export", "true")
                .build();

        // Verification Prints
        System.out.println("Standard Request URL: " + standardRequest.getFullUrl());
        System.out.println("Standard Headers: " + standardRequest.getHeaders());

        System.out.println("\nAuthorized Request URL: " + authorizedRequest.getFullUrl());
        System.out.println("Authorized Headers: " + authorizedRequest.getHeaders());
    }
}
```

Would you like to expand this system into a multi-method layout by integrating the **Strategy Pattern** to execute `GET`, `POST`, or `DELETE` commands seamlessly?