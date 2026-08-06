import java.util.HashMap;
import java.util.Map;

public class HashMapFun {

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
    // // containsValue: O(n)
    System.out.println("containsKey(Bob): " + scores.containsKey("Bob"));
  }

  public static void remove() {

    Map<String, Integer> scores = new HashMap<>(Map.of("Alice", 88, "Bob", 90));
    // // remove(key) / remove(key,value): average O(1)
    scores.remove("Bob");
    scores.remove("Chris", 100);
    System.out.println("after remove: " + scores);
  }

  public static void other() {
    Map<String, Integer> scores = new HashMap<>(Map.of("Alice", 88, "Bob", 90));
    // computeIfAbsent: average O(1)
    scores.computeIfAbsent("Diana", k -> 95);
    System.out.println("after computeIfAbsent(Diana): " + scores);
    // merge: average O(1)
    scores.merge("Alice", 5, Integer::sum);
    System.out.println("after merge(Alice,+5): " + scores);
  }

  public static void sets() {
    Map<String, Integer> scores = new HashMap<>(Map.of("Alice", 88, "Bob", 90));
    // // keySet / values / entrySet iteration: O(n)
    System.out.println("keys: " + scores.keySet());
    System.out.println("values: " + scores.values());

    for (Map.Entry<String, Integer> entry : scores.entrySet()) {
      System.out.println(entry.getKey() + " -> " + entry.getValue());
    }
  }

  public static void MultipleWayToChangeMapValues() {
    Map<String, Integer> scores = new HashMap<>(Map.of("Alice", 88, "Bob", 90));
    // way1
    scores.replaceAll((k, v) -> v - 100);
    // way2
    for (Map.Entry<String, Integer> entry : scores.entrySet()) {
      entry.setValue(entry.getValue() * 2);
    }
    scores.forEach((k, v) -> System.out.println(k + "->" + v));

    // way3
    scores.forEach((k, v) -> scores.put(k, v + 31));
    // way4
    for (String k : scores.keySet()) {
      scores.put(k, scores.get(k) + 3);
    }

    scores.forEach((k, v) -> System.out.println(k + "->" + v));
  }

  public static void main(String[] args) {
    // putOverRide();
    // getOverRide();
    // remove();
    // other();
    // sets();
    MultipleWayToChangeMapValues();
  }
}
