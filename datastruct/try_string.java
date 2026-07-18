import java.lang.reflect.Method;
import java.util.stream.Stream;

public class try_string {
    public static void main(String[] args) {
      Method[] methods = String.class.getDeclaredMethods();
      System.out.println("Unique String Methods");
      // 2. Use a Stream to process the method names
      Stream.of(methods)
            .map(Method::getName)  // Extract just the name String
            .distinct()            // Remove duplicate names (overloads)
            .sorted()              // Put them in alphabetical order
            .forEach(System.out::println); // Print each one

      final String strA = new String("yes, yes, hello");
      // get char e's count inside string
      long countE = strA.chars().filter(ch->ch == 'e').count();      
      System.out.println("e Count:"+ countE);
      // some words to be uppper
      final String strUpper = strA.replace("yes", "YES");
      System.out.println("strUpper:"+ strUpper);
      // find in string
      System.out.println("index1:"+ strA.indexOf("yes"));
      System.out.println("index2:"+ strA.indexOf("yes", 2));
      // strA.count("")
      // The correct way to print an array's contents
      int countYes = strA.split("yes", -1).length -1;
      System.out.println("index2:"+ countYes);

      // String[] parts = "yes: yes: hello".split("yes");
      // System.out.println(parts.length);
      // System.out.println(Arrays.toString(parts));
      // System.out.println(parts.getClass().getSimpleName()); // Prints: String[]
      // // 2. Get the full internal JVM name
      // System.out.println(parts.getClass().getName()Pr);       // Prints: [Ljava.lang.String;
      
      // String[] parts2 = "yes: yes: hello".split("yes",-1);
      // System.out.println(Arrays.toString(parts2));
      // System.out.println(parts2.length);
      System.out.println(" ".isBlank());
      System.out.println(" ".isEmpty());

      //convert to char HashMap
    }
}