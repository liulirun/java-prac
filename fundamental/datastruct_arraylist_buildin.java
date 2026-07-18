import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class datastruct_arraylist_buildin {
    public static void main(String[] args) {
        Method[] methods = ArrayList.class.getDeclaredMethods();

        System.out.println("Unique ArrayList Methods");
        Stream.of(methods)
              .map(Method::getName)
              .distinct()
              .sorted()
              .forEach(System.out::println);
       
        ArrayList<String> skills = new ArrayList<>();
        skills.ensureCapacity(10); // reserve internal capacity: O(1) amortized

        // add at end: O(1) amortized
        skills.add("Java");
        skills.add("SQL");
        skills.add("REST");
        System.out.println("after add: " + skills);

        // add at index: O(n) due to shift
        skills.add(1, "Testing");
        System.out.println("after add(1, Testing): " + skills);

        // addAll: O(m) to append m items
        skills.addAll(List.of("JUnit", "Docker"));
        System.out.println("after addAll: " + skills);

        // get/set: get O(1), set O(1)
        System.out.println("get(0): " + skills.get(0));
        skills.set(2, "API");
        System.out.println("after set(2, API): " + skills);

        // contains/indexOf/lastIndexOf: O(n)
        skills.add("API");
        System.out.println("contains API: " + skills.contains("API"));
        System.out.println("indexOf API: " + skills.indexOf("API"));
        System.out.println("lastIndexOf API: " + skills.lastIndexOf("API"));

        // remove by value/index: O(n)
        skills.remove("Docker");
        skills.remove(0);
        System.out.println("after remove: " + skills);

        // removeIf: O(n)
        skills.removeIf(s -> s.length() <= 4);
        System.out.println("after removeIf(length <= 4): " + skills);

        // sort: O(n log n)
        Collections.sort(skills);
        System.out.println("sorted: " + skills);

        // reverse: O(n)
        Collections.reverse(skills);
        System.out.println("reversed: " + skills);

        // replaceAll: O(n)
        skills.replaceAll(String::toUpperCase);
        System.out.println("replaceAll(upper): " + skills);

        // subList view creation: O(1), traversal/use: depends on operation
        List<String> top2 = skills.subList(0, Math.min(2, skills.size()));
        System.out.println("subList top2: " + top2);

        // toArray conversion: O(n)
        String[] asArray = skills.toArray(new String[0]);
        System.out.println("toArray: " + java.util.Arrays.toString(asArray));

        // size/isEmpty/clear: O(1) / O(1) / O(n)
        System.out.println("size: " + skills.size() + ", isEmpty: " + skills.isEmpty());
        skills.clear();
        skills.trimToSize(); // shrink backing array to current size: O(n)
        System.out.println("after clear, isEmpty: " + skills.isEmpty());
    }
}
