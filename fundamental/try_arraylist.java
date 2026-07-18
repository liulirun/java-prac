import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class try_arraylist {
  public static void main(String[] args){
    System.out.println("start try_arrayList");
    List<Integer> l = new ArrayList<Integer>(List.of(1, 2, 3, 4, 5, 1, 2, 3));
    
    // Category 1: Basic Manipulation
    l.add(1, 1000); // [1, 1000, 2, 3, 4, 5, 1, 2, 3]
    l.remove(2);    // Removes '2' at index 2 -> [1, 1000, 3, 4, 5, 1, 2, 3]
    l.set(2, 200);  // Replaces '3' with '200'
    System.out.println("1-3. Modified List: " + l); 
    // Output: [1, 1000, 200, 4, 5, 1, 2, 3]

    // Q10: Wipe data
    List<Integer> l1 = new ArrayList<Integer>(List.of(1, 2, 3, 4, 5, 1, 2, 3));
    l1.clear();
    System.out.println("10. Cleared List: " + l1); 
    // Output: []

    // Q12: Merge lists
    List<Integer> l2 = new ArrayList<Integer>(List.of(1, 2, 3, 4, 5, 1, 2, 3));
    l.addAll(0, l2);
    System.out.println("12. Merged List: " + l); 
    // Output: [1, 2, 3, 4, 5, 1, 2, 3, 1, 1000, 200, 4, 5, 1, 2, 3]
    
    // Category 2: Searching & Inspection
    System.out.println("4. Contains 200: " + l.contains(200));   // true
    System.out.println("4. Contains 2300: " + l.contains(2300)); // false
    System.out.println("5. Index of 200: " + l.indexOf(200));   // 10
    
    // Q11: removeIf (Removes all numbers <= 3)
    System.out.println("11. Any removed? " + l.removeIf(i -> i <= 3)); 
    System.out.println("11. Filtered List: " + l); 
    // Output: [4, 5, 4, 5, 1000, 200, 4, 5]
    
    // Category 3: Ordering & Subsets
    List<String> l3 = new ArrayList<String>(List.of("Python", "Java", "Sql"));
    l3.sort(null); 
    System.out.println("6. Alphabetical: " + l3); 
    // Output: [Java, Python, Sql]

    l3.sort(Comparator.naturalOrder());
    System.out.println("6. Natural Order: " + l3); 
    // Output: [Java, Python, Sql]
    
    // Q9: SubList
    List<String> l4 = l3.subList(0, 2);
    System.out.println("9. SubList (0-2): " + l4); 
    // Output: [Java, Python]
    
    // Q14: Reverse
    Collections.reverse(l3);
    System.out.println("14. Reversed List: " + l3); 
    // Output: [Sql, Python, Java]

    // Category 4: Type Conversions
    Object[] objArray = l3.toArray();
    System.out.println("7. Object Array: " + objArray.getClass().getSimpleName() + " | Value: " + Arrays.toString(objArray));
    
    String[] strArray = l3.toArray(new String[0]);
    System.out.println("8. String Array: " + strArray.getClass().getSimpleName() + " | Value: " + Arrays.toString(strArray));
    
    // Q13: toString
    String listAsString = l3.toString();
    System.out.println("13. String Type: " + listAsString.getClass().getSimpleName() + " | Value: " + listAsString);
  }  
}