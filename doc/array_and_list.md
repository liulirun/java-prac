## Array
| Collection Type         | Can change items (`arr[0] = "X"` or `.set()`)? | Can change size (`.add()` or `.remove()`)? | Java Runtime Exception on Size Change?    |
| ----------------------- | ---------------------------------------------- | ------------------------------------------ | ----------------------------------------- |
| **`String[] arr`**      | **Yes**                                        | **No**                                     | *No method exists (Compiler Error)*       |
| **`Arrays.asList()`**   | **Yes**                                        | **No**                                     | **Yes** (`UnsupportedOperationException`) |
| **`List.of()`**         | **No**                                         | **No**                                     | **Yes** (`UnsupportedOperationException`) |
| **`new ArrayList<>()`** | **Yes**                                        | **Yes**                                    | *No (Works perfectly)*                    |


```java
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListCheatSheet {
    public static void main(String[] args) {

        // 1. STANDARD ARRAY (Fixed size, items can change)
        String[] arr = {"A", "B"};
        arr[0] = "X";       // ✅ ALLOWED: Modifies item
        // arr.add("C");    // ❌ ERROR: Arrays do not have add/remove methods

        // 2. ARRAYS.ASLIST (Fixed size, items can change)
        List<String> hybridList = Arrays.asList("A", "B");
        hybridList.set(0, "X");    // ✅ ALLOWED: Modifies item
        // hybridList.add("C");    // ❌ CRASH: UnsupportedOperationException

        // 3. LIST.OF (Completely locked, read-only)
        List<String> lockedList = List.of("A", "B");
        // lockedList.set(0, "X"); // ❌ CRASH: UnsupportedOperationException
        // lockedList.add("C");    // ❌ CRASH: UnsupportedOperationException

        // 4. NEW ARRAYLIST (Fully flexible, standard list)
        List<String> flexibleList = new ArrayList<>(List.of("A", "B"));
        flexibleList.set(0, "X");   // ✅ ALLOWED: Modifies item
        flexibleList.add("C");      // ✅ ALLOWED: Grows list size
        flexibleList.remove(0);   // ✅ ALLOWED: Shrinks list size
    }
}
```

## LinkedList
| Operation                                                            | `ArrayList` Time Complexity | `LinkedList` Time Complexity | Explanation                                                                                                              |
| -------------------------------------------------------------------- | --------------------------- | ---------------------------- | ------------------------------------------------------------------------------------------------------------------------ |
| **Get / Set by Index** *(e.g., `.get(5)`, `.set(2, "X")`)*           | **O(1)** *(Instant)*        | **O(N)** *(Slow)*            | `ArrayList` jumps straight to the memory address. `LinkedList` must crawl through node pointers one-by-one.              |
| **Add / Remove at Front** *(e.g., `.add(0, "X")`, `.removeFirst()`)* | **O(N)** *(Slow)*           | **O(1)** *(Instant)*         | `ArrayList` has to shift every single remaining element over. `LinkedList` just changes a couple of pointer connections. |
| **Add at End** *(e.g., `.add("X")`, `.addLast("X")`)*                | **O(1)** *(Instant)*        | **O(1)** *(Instant)*         | `ArrayList` appends to the next open slot. `LinkedList` uses its internal `tail` pointer to attach it instantly.         |
| **Remove from End** *(e.g., `.remove(l.size()-1)`, `.removeLast()`)* | **O(1)** *(Instant)*        | **O(1)** *(Instant)*         | `ArrayList` clears the last index (no shifting needed). `LinkedList` unlinks the tail node instantly.                    |


```java
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class LinkedListCheatSheet {
    public static void main(String[] args) {

        // 1. DECLARED AS LIST (Restricted View)
        // ❌ BAD: You lose access to head/tail optimizations like removeFirst()
        List<String> restrictedView = new LinkedList<>(Arrays.asList("A", "B"));
        restrictedView.add("C");             // ✅ ALLOWED: Standard end-of-list add
        // restrictedView.removeFirst();     // ❌ ERROR: Method does not exist on List interface


        // 2. DECLARED AS LINKEDLIST (Full Access - Recommended)
        // ✅ GOOD: Unlocks O(1) performance methods for front/back operations
        LinkedList<String> linkedList = new LinkedList<>(List.of("A", "B", "C"));

        // Fast Stack/Queue Operations
        linkedList.addFirst("START");        // ✅ O(1): Instantly inserts at the beginning
        linkedList.addLast("END");           // ✅ O(1): Instantly inserts at the end

        // Fast Removal Operations
        linkedList.removeFirst();            // ✅ O(1): Instantly deletes the first item
        linkedList.removeLast();             // ✅ O(1): Instantly deletes the last item

        // Inspecting Items
        String first = linkedList.peekFirst(); // ✅ O(1): Reads first item without deleting
        String last = linkedList.peekLast();   // ✅ O(1): Reads last item without deleting


        // 3. THE TRAP OPERATIONS (Slow Index Lookups)
        // ⚠️ CAUTION: Avoid using numeric positions with LinkedList
        linkedList.set(1, "CHANGED");        // ⚠️ O(N): Java must crawl through links to find position 1
        String item = linkedList.get(2);     // ⚠️ O(N): Java must crawl through links to find position 2        
    }
}

