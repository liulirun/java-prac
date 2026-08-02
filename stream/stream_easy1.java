import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class stream_easy1 {

  // Java 21 Record: Automatically creates fields, constructor, and getters
  record Order(int id, String customer, String category, double price, boolean shipped) {
    public String toJsonString() {
      return String.format(
          "{\"id\": %d, \"customer\": \"%s\", \"category\": \"%s\", \"price\": %.2f, \"shipped\": %b}",
          id, customer, category, price, shipped);
    }
  }

  public static Order[] returnOdrder() {
    return new Order[] {
        new Order(101, "Alice", "Electronics", 1200.50, true),
        new Order(102, "Bob", "Books", 45.00, false),
        new Order(103, "Charlie", "Electronics", 150.00, true),
        new Order(104, "David", "Clothing", 85.20, true),
        new Order(105, "Eve", "Electronics", 899.99, false),
        new Order(106, "Frank", "Books", 22.10, true)
    };
  }

  public static void main(String[] args) {
    Order[] ordersArray = returnOdrder();
    // filterUnshippedThenSort(ordersArray);
    // groupAndPrintByCategory(ordersArray);

    findCheapestBook(ordersArray);
    // getFlattenedUniqueCategories(ordersArray);
  }

  static void filterUnshippedThenSort(Order[] orders) {
    System.out.println("\n--- 1. FILTER: Unshipped Orders Only ---");
    Arrays.stream(orders)
        .filter(o -> o.shipped && o.price > 10)
        .sorted(Comparator.<Order>comparingDouble(o -> o.price).reversed())
        .forEach(o -> System.out.println(o.customer + ": " + o.price));
  }

  static void groupAndPrintByCategory(Order[] orders) {
    System.out.println("\n--- 4. COLLECT: Grouped JSON Rows By Category ---");

    // G1: Group by Category -> Uppercase Customer Names
    Map<String, List<String>> g1 = Arrays.stream(orders)
        .collect(Collectors.groupingBy(
            o -> o.category.equals("Books") ? "Books" : "Other",
            Collectors.mapping(
                o -> o.customer.toUpperCase(),
                Collectors.toList())));
    g1.forEach((category, list) -> System.out.println("g1:\"" + category + "\": " + list));

    // G3: Group by Category -> Extract List of IDs in forEach
    Map<String, List<Order>> g3 = Arrays.stream(orders)
        .collect(Collectors.groupingBy(o -> o.category));

    g3.forEach((category, list) -> {
      List<Integer> ids = list.stream()
          .map(o -> o.id)
          .collect(Collectors.toList());
      System.out.println("g3:" + category + ": " + ids);
    });
  }

  static void findCheapestBook(Order[] orders) {
    System.out.println("\n--- 7. MIN/MAX: Find Specific Element Safely ---");

    boolean hasSuperExpensive = Arrays.stream(orders)
        .anyMatch(o -> o.price > 1000.00);
    System.out.printf("Has order > $1000? %s%n", hasSuperExpensive);
    boolean allOverTenDollars = Arrays.stream(orders)
        .allMatch(o -> o.price > 10.00);
    System.out.printf("Are all orders > $10? %s%n", allOverTenDollars);

    Optional<Order> cheapestBook = Arrays.stream(orders)
        .filter(o -> "Books".equals(o.category))
        .min(Comparator.comparingDouble(o -> o.price));

    cheapestBook.ifPresentOrElse(
        b -> System.out.println("Cheapest Book: " + b.toJsonString()),
        () -> System.out.println("No books found."));

    Long count = Arrays.stream(orders)
        .filter(o -> "Books".equals(o.category)).count();
    System.out.println(count);
  }

  static void getFlattenedUniqueCategories(Order[] orders) {
    System.out.println("\n--- 8. DISTINCT: Unique Category Tags ---");
    List<String> cleanTags = Arrays.stream(orders)
        .map(o -> o.category.toLowerCase())
        .distinct()
        .collect(Collectors.toList());
    System.out.println("Unique Categories on Platform: " + cleanTags);
  }
}