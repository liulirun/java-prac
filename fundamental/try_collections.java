import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class try_collections {

//   The Collections class is a powerful utility toolkit in Java that performs operations on collections (like ArrayList, Set, or Map). Unlike ArrayList methods which you call on the object (e.g., list.add()), Collections methods are static, so you pass the list into them.

    public static void main(String[] args){
      // Method[] methods = Collections.class.getDeclaredMethods();

      //   System.out.println("Unique Collections Methods");
      //   // 2. Stream, extract names, remove duplicates, and sort
      //   Stream.of(methods)
      //         .map(Method::getName)
      //         .distinct()
      //         .sorted()
      //         .forEach(System.out::println);

      System.out.println("Collections Start");
      // Q2. Turn a String[] array into a fixed-size List using Arrays.
      String[] arr = {"Alpha", "Beta"};
      List<String> fixedList = Arrays.asList(arr);
      System.out.println(fixedList);

      // Q3. Initialize a read-only (unmodifiable) List containing three specific strings.
      List<String> listImmu = List.of("1", "2", "3");
      System.out.println(listImmu.getClass().getSimpleName());
      // Q4. Find the maximum and minimum values in a List of Integers using Collections.
      System.out.println(Collections.max(Arrays.asList(10, 50, 2, 99)));

      // Q5. Randomly shuffle the elements of an ArrayList.
      List<Integer> a = Arrays.asList(10, 50, 2, 99);
      Collections.shuffle(a);
      System.out.println(a);
// Q6. Create a high-performance Set from an existing ConcurrentHashMap using Collections.
// Q7. Reverse the order of elements in a List in-place.
// Q8. Return an empty, immutable List (to avoid returning null in a method).
// Q9. Create a singleton list containing exactly one specific object.
// Q10. Wrap a List in a Checked View to ensure only String types can be added at runtime.

      // Sorting: How to sort a list in natural order.
      // Reversing: How to flip the order of elements.
      // Shuffling: How to randomize the order (great for games).
      // Frequency: How to count how many times an object appears.
      // Min/Max: How to find the smallest or largest value.
      // Disjoint: How to check if two lists have zero common elements.
      // Fill: How to replace every element in a list with one specific value.
      // ReplaceAll: How to swap all instances of "A" with "B".
      // Rotate: How to shift elements by a specific distance.
      // Unmodifiable: How to create a "read-only" version of your list.
    }
  }