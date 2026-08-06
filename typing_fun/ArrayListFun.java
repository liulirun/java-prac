import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//aim for intermidete JAVA dev practise, I want to practise hands on programming functions in whiteboard. 
// target: Arraylist
// need to get fluent with Arraylist operations/methods 
// need to have questions + answers
// need to see common java traps
public class ArrayListFun {
  public static void main(String[] args) {
    ArrayList<Integer> dupList = new ArrayList<Integer>(List.of(10, 10, 11, 12, 12, 15, 15, 15, 11, 18));

    removeDup(dupList);

    // shallowCopy();
    // List<String> original = new ArrayList<String>(List.of("11", "12", "13", "14",
    // "15", "16"));
    // removeEveryNthElement(original, 2);
    // System.out.println(original); // Output: [11, 13, 15]
  }

  public static void removeDup(ArrayList<Integer> list) {
    // Task: remove duplicate integers from an ArrayList.
    // Constraints:Do not create a new list or allocate a large data structure
    // (e.g., no HashSet).
    // Must execute in O(N) time.Do not use removeIf or
    // indexed remove() inside a standard loop, as shifting repeatedly causes O(N²)
    // performance.
    if (list == null || list.size() < 1)
      return;

    int uIdx = 1;
    // 10, 10, 11, 12, 12, 15, 15, 15, 11, 18
    // O(N)
    for (int i = 1; i < list.size(); i++) {
      if (!list.get(i).equals(list.get(uIdx))) {
        list.set(uIdx, list.get(i));
        uIdx++;
        System.out.println("reset for uIdex:" + uIdx);
      } else {
        System.out.println("skip for i:" + i);
      }
    }
    System.out.println(list);
    list.subList(uIdx, list.size()).clear();
    System.out.println(list);
  }

  public static void shallowCopy() {
    List<String> originalList = new ArrayList<>(Arrays.asList("Delta", "Alpha", "Charlie", "Bravo"));
    List<String> subListView = originalList.subList(1, 3);
    subListView.set(0, "subView - Set");
    subListView.add(0, "subView - Add");
    originalList.set(1, "parentView - Set");
    originalList.set(2, "parentView - Add");

    // because modCount in heap for originalList changed, sublistView crash below
    // originalList.add("parentView - Add");

    System.out.println("subListView:" + subListView);
    System.out.println("originallist:" + originalList);
  }

  public static void removeEveryNthElement(List<String> list, int n) {
    // removes every n-th element from a List starting from index n-1.
    // Catch1: modify the list in-place without creating a second list
    // Catch2: must execute faster than O(k*m)
    if (list == null || n <= 0)
      return;
    // counter is Array and acts like FINAL in heap (memory address)
    // content of Array can change
    int[] counter = new int[] { 0 };
    list.removeIf(item -> {
      int currentIdx = counter[0]++;
      return (currentIdx + 1) % n == 0;
    });
  }
}
