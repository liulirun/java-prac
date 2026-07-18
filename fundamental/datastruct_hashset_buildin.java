import java.util.HashSet;
import java.util.Set;

public class datastruct_hashset_buildin {
    public static void main(String[] args) {
        Set<String> tags = new HashSet<>();

        // add: average O(1), worst O(n)
        tags.add("api");
        tags.add("java");
        tags.add("api");
        tags.addAll(Set.of("microservices", "testing")); // addAll: O(m)
        System.out.println("after add/addAll: " + tags);

        // contains/remove: average O(1)
        System.out.println("contains java: " + tags.contains("java"));
        tags.remove("java");
        System.out.println("after remove java: " + tags);

        // iteration: O(n)
        for (String tag : tags) {
            System.out.println("tag: " + tag);
        }

        // set operations using retainAll/removeAll/containsAll: O(n) to O(n+m)
        Set<String> target = new HashSet<>(Set.of("api", "testing", "ci"));
        Set<String> intersection = new HashSet<>(tags);
        intersection.retainAll(target);
        System.out.println("intersection: " + intersection);

        Set<String> difference = new HashSet<>(tags);
        difference.removeAll(target);
        System.out.println("difference: " + difference);

        System.out.println("tags containsAll intersection: " + tags.containsAll(intersection));

        // size/isEmpty/clear: O(1) / O(1) / O(n)
        System.out.println("size: " + tags.size() + ", isEmpty: " + tags.isEmpty());
        tags.clear();
        System.out.println("after clear, isEmpty: " + tags.isEmpty());
    }
}
