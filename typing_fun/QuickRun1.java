import java.util.ArrayList;
import java.util.List;

public class QuickRun1 {
  public static void main(String[] args) {
    // List<String> l1 = new ArrayList<String>(List.of("E1", "2", "3", "4"));
    // l1.set(1, "CHANGED!");

    // l1.subList(0, 2).clear();
    // System.out.println(l1);
    // }

    List<String> list = new ArrayList<>(List.of("A", "B", "C"));
    for (String item : list) {
      if (item.equals("B")) {
        list.remove(item);
      }
    }
    System.out.println(list);
    List<String> original = new ArrayList<>(List.of("Red", "Green", "Blue"));
    List<String> sub = original.subList(0, 2);
    System.out.println(original);
    System.out.println(sub.get(0));
  }
}