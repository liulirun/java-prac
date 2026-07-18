package immutable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListProof {
    public static void main(String[] args) {

        final List<String> roles = List.of("Admin", "User"); // Truly immutable [2]

        // ✅ Check: Read data
        String first = roles.get(0); 

        // ❌ ERROR: Compiler blocks reassigning a final variable
        // roles = List.of("Guest"); 

        // ❌ RUNTIME ERROR: UnsupportedOperationException (Data is locked)
        // roles.set(0, "Hacker"); 
        // roles.add("Guest"); 

        /////////////////////////////
        List<String> dynamicList = new ArrayList<>();
        dynamicList.add("Apple");

        List<String> unmodifiable = Collections.unmodifiableList(dynamicList);

        // 1. Print memory identity hashcodes
        System.out.println("--- Memory Address Proof ---");
        System.out.println("dynamicList identity hash:    " + System.identityHashCode(dynamicList));
        
        // UnmodifiableList is a wrapper wrapper object, but let's look inside
        System.out.println("unmodifiable wrapper hash:   " + System.identityHashCode(unmodifiable));
        
        // 2. Print contents before change
        System.out.println("\n--- Before Modification ---");
        System.out.println("dynamicList:  " + dynamicList);
        System.out.println("unmodifiable: " + unmodifiable);

        // Modify the original list
        dynamicList.add("Banana");

        // 3. Print contents after change
        System.out.println("\n--- After Modification ---");
        System.out.println("dynamicList:  " + dynamicList);
        System.out.println("unmodifiable: " + unmodifiable);
        System.out.println("unmodifiable size: " + unmodifiable.size());
    }
}
