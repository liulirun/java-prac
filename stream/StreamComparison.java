import java.util.List;

record User(String name, List<String> addresses) {
}

public class StreamComparison {
  public static void main(String[] args) {
    // Setup data using the Record constructor
    List<User> users = List.of(
        new User("Alice", List.of("123 Main St", "456 Oak Ave")),
        new User("Bob", List.of()),
        new User("Charlie", List.of("789 Pine Rd")));

    // --- METHOD 1: FLATMAP ---
    // Records generate getter methods automatically using the component name:
    // user.addresses()
    List<String> flatMapResults = users.stream()
        .flatMap(user -> user.addresses().stream())
        .toList();

    // --- METHOD 2: MAPMULTI ---
    List<String> mapMultiResults = users.stream()
        .<String>mapMulti((user, consumer) -> {
          for (String address : user.addresses()) {
            consumer.accept(address);
          }
        })
        .toList();

    System.out.println("FlatMap:  " + flatMapResults);
    System.out.println("MapMulti: " + mapMultiResults);
    // Both print: [123 Main St, 456 Oak Ave, 789 Pine Rd]
  }
}
