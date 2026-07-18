import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class try_stream {
    public static void main(String[] args) {

    //   Method[] methods = Stream.class.getDeclaredMethods();
    //   System.out.println("Unique Stream Methods");
    //   // 2. Use a Stream to process the method names
    //   Stream.of(methods)
    //         .map(Method::getName)  // Extract just the name String
    //         .distinct()            // Remove duplicate names (overloads)
    //         .sorted()              // Put them in alphabetical order
    //         .forEach(System.out::println); // Print each one
    

        // Convert a String[] array into a List<String>.
        String[] arr = {"Apple", "Banana", "Cherry"};
        List<String> listStr = Arrays.stream(arr).collect(Collectors.toList());
        System.out.println(listStr);

        // Convert a String of digits (e.g., "123") into an int sum using chars().
        long sum = "1234".chars().map(Character::getNumericValue).sum();
        System.out.println(sum);

        // Convert a List of Objects into a Map<Integer, String> using an ID as the key.
        class User {
            int id;
            String name;
            User(int id, String name){ this.id = id; this.name=name; }
            int getId(){return id;}
            String getName(){return name;}
        }  
        List<User> users = List.of(new User(1, "a"), new User(2, "b"));
        Map<Integer, String> mapRes = users.stream().collect(Collectors.toMap(User::getId, User::getName));
        System.out.println(mapRes);
        
        // // Convert a primitive int[] into a List<Integer> (using boxing).
        int[] arrInt = {1, 2, 3, 4};
        List<Integer> listInt = IntStream.of(arrInt).boxed().collect(Collectors.toList());
        System.out.println(listInt);
        
        // // Convert a List<String> back into a standard String[] array.
        String[] arrRess = Arrays.asList("Tom", "Jerry").stream().toArray(String[]::new);
        System.out.println(arrRess);

        // Convert a Map<Integer, String> into a List<String> of its values only.
        Map<Integer, String> mapA = new HashMap<>(Map.of(1, "A", 2, "B"));
        System.out.println(mapA.values().stream().collect(Collectors.toList()));

        // Join a List<String> into a single formatted String separated by hyphens.
        List<String> strA = List.of("1","1","1","1");
        System.out.println(strA.stream().collect(Collectors.joining("->")));
        
        // Group a List<String> by their first character into a Map<Character, List<String>>.
        List<String> strA2 = List.of("abc","ab","az","bww","box");
        System.out.println(strA2.stream().collect(Collectors.groupingBy(s->s.charAt(0))));

        // // Group
        List<Integer> scores = List.of(1, 2, 3, 90);
        Map<String, List<Integer>> gradeGroups = scores.stream()
        .collect(Collectors.groupingBy(s -> s >= 60 ? "Pass" : "Fail"));
        System.out.println(gradeGroups);
    }
}
