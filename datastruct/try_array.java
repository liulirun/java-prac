import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class try_array {
  public static void main(String[] args){   
    //Create Array    
    String[] fruits = {"Apple", "Banana", "Date", "Cherry"};
    // 2. Array Literal (Best when you know values upfront)
    int[] byLiteral = {10, 20, 30};
    // 3. Anonymous / Explicit Initialization (Good for passing to methods)
    int[] byExplicit = new int[]{1, 2, 3};
    // 4. Multidimensional (Array of arrays)
    int[][] matrix = {{1, 2}, {3, 4}};
    String[] fruits2 = Stream.of("Apple", "Banana", "Date", "Cherry").toArray(String[]::new);
    System.out.println(byLiteral.toString() + byExplicit.toString() + matrix.toString() + fruits2.toString());
    
    // 1. Declare size first (elements default to 0, false, or null)
    int[] fills = new int[5];
    Arrays.fill(fills, 10);
    System.out.println("2. Fills Array: " + Arrays.toString(fills)); // [10, 10, 10, 10, 10]
    
    // Arrays.sort sorts alphabetically: Apple, Banana, Cherry, Date
    fruits[0] = "SB";
    Arrays.sort(fruits);
    System.out.println("1. Sorted Fruits: " + Arrays.toString(fruits));    
    
    // 3. Binary Search
    int[] numbers = {5, 1, 9, 3};
    // SORT before search. Unsorted search results in undefined behavior (often negative or wrong index)
    System.out.println("3. Search Unsorted: " + Arrays.binarySearch(numbers, 3)); 
    Arrays.sort(numbers); // numbers is now [1, 3, 5, 9]
    System.out.println("3. Search Sorted (index of 3): " + Arrays.binarySearch(numbers, 3)); // 1
    
    // 4. Copy a range (Index 1 inclusive to 3 exclusive)
    String[] b = Arrays.copyOfRange(fruits, 1, 3);
    System.out.println("4. Range Copy: " + Arrays.toString(b)); // [Banana, Cherry]

    // 5. Convert Array to List (Fixed-size)    
    List<String> listA = Arrays.asList(fruits);
    System.out.println("5. Fixed List: " + listA + " | Type: " + listA.getClass().getSimpleName());
    // Type: Arrays$ArrayList
    
    // 6. Convert Array to ArrayList (Modifiable)
    ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(fruits));
    System.out.println("6. Modifiable List: " + arrayList + " | Type: " + arrayList.getClass().getSimpleName());
    // Type: ArrayList
    
    System.out.println("6. Memory Reference Match (==): " + (arrayList == listA)); // false
    System.out.println("6. Content Match (.equals): " + arrayList.equals(listA));   // true

    arrayList.add("new");
    arrayList.add(0, "new2");
    System.out.println("6. List after Additions: " + arrayList); 
    // [new2, Apple, Banana, Cherry, Date, new]
    
    // 7. Joining
    String joined = String.join("->", fruits);
    System.out.println("7. Joined String: " + joined); // Apple->Banana->Cherry->Date
    
    // 8. Convert Array to a Set (Removes duplicates)
    String[] c = {"A", "A", "B", "C", "A"};
    Set<String> setC = new HashSet<>(Arrays.asList(c));
    System.out.println("8. Set (Unique): " + setC); // [A, B, C] (Order may vary)

    // 9. Convert Array to a Stream
    String[] s = {"A", "A", "B", "C", "A"};
    Stream<String> streamC = Arrays.stream(s);
    long counts = streamC.filter(f -> f.startsWith("A")).count();
    System.out.println("9. Count of 'A' starts: " + counts); // 3
  }
}
