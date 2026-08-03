import java.util.HashMap;
import java.util.Map;

public class HashMap_fun {

  public static void putOverRide() {
    Map<String, Integer> scores = new HashMap<>(Map.of("Alice", 88, "Bob", 90));
    // put: average O(1), worst O(n) with heavy collisions
    scores.put("Alice", 81);
    scores.put("Alice", 82);
    // // putIfAbsent: average O(1)
    scores.putIfAbsent("Alice", 90);
    scores.put("Bob", 90);
    for (Map.Entry<String, Integer> entry : scores.entrySet()) {
      System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
    }
  }

  public static void getOverRide() {

    Map<String, Integer> scores = new HashMap<>(Map.of("Alice", 88, "Bob", 90));
    // get/getOrDefault/containsKey: average O(1)
    System.out.println("get(Alice): " + scores.get("Alice"));
    System.out.println("get(Alice1): " + scores.get("Alice1"));
    System.out.println("getOrDefault(Chris): " + scores.getOrDefault("Chris",
        0));
    System.out.println("containsKey(Bob): " + scores.containsKey("Bob"));
  }

  public static void main(String[] args) {
    putOverRide();
    getOverRide();
    // Map<String, Integer> map = new HashMap<>(Map.of("Alice", 88, "Bob", 90));
    Map<String, Integer> scores = new HashMap<>();

    // // containsValue: O(n)
    // System.out.println("containsValue(90): " + scores.containsValue(90));

    // // replace: average O(1)
    // scores.replace("Alice", 88);
    // scores.replace("Chris", 77, 80);
    // System.out.println("after replace: " + scores);

    // // computeIfAbsent: average O(1)
    // scores.computeIfAbsent("Diana", k -> 95);
    // System.out.println("after computeIfAbsent(Diana): " + scores);

    // // merge: average O(1)
    // scores.merge("Alice", 5, Integer::sum);
    // System.out.println("after merge(Alice,+5): " + scores);

    // // remove(key) / remove(key,value): average O(1)
    // scores.remove("Bob");
    // scores.remove("Chris", 100);
    // System.out.println("after remove: " + scores);

    // // keySet / values / entrySet iteration: O(n)
    // System.out.println("keys: " + scores.keySet());
    // System.out.println("values: " + scores.values());
    // for (Map.Entry<String, Integer> entry : scores.entrySet()) {
    // System.out.println(entry.getKey() + " -> " + entry.getValue());
    // }

    // // forEach traversal: O(n)
    // scores.forEach((name, score) -> System.out.println("forEach: " + name + "=" +
    // score));

    // // size/isEmpty/clear: O(1) / O(1) / O(n)
    // System.out.println("size: " + scores.size() + ", isEmpty: " +
    // scores.isEmpty());
    // scores.clear();
    // System.out.println("after clear, isEmpty: " + scores.isEmpty());
  }
}
