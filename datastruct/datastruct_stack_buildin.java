import java.util.ArrayDeque;
import java.util.Deque;

public class datastruct_stack_buildin {
    public static void main(String[] args) {
        Deque<String> stack = new ArrayDeque<>();

        // push: O(1)
        stack.push("A");
        stack.push("B");
        stack.push("C");
        System.out.println("initial: " + stack);

        // peek/pop: O(1)
        System.out.println("peek: " + stack.peek());
        System.out.println("pop: " + stack.pop());
        System.out.println("after pop: " + stack);

        // offerFirst/pollFirst are stack-equivalent: O(1)
        stack.offerFirst("D");
        stack.offerFirst("E");
        System.out.println("after offerFirst: " + stack);
        System.out.println("pollFirst: " + stack.pollFirst());
        System.out.println("after pollFirst: " + stack);

        // contains: O(n), size/isEmpty: O(1)
        System.out.println("contains A: " + stack.contains("A"));
        System.out.println("size: " + stack.size() + ", isEmpty: " + stack.isEmpty());

        // iterate top to bottom (ArrayDeque iterator): O(n)
        for (String item : stack) {
            System.out.println("stack item: " + item);
        }

        // clear: O(n)
        stack.clear();
        System.out.println("after clear, isEmpty: " + stack.isEmpty());
    }
}
