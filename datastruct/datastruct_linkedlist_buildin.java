import java.util.LinkedList;

public class datastruct_linkedlist_buildin {
    public static void main(String[] args) {
        LinkedList<String> tasks = new LinkedList<>();

        // addFirst/addLast: O(1)
        tasks.addFirst("Design");
        tasks.addLast("Implement");
        tasks.addLast("Test");
        System.out.println("initial: " + tasks);

        // offerFirst/offerLast: O(1)
        tasks.offerFirst("Plan");
        tasks.offerLast("Deploy");
        System.out.println("after offerFirst/offerLast: " + tasks);

        // peekFirst/peekLast: O(1)
        System.out.println("peekFirst: " + tasks.peekFirst());
        System.out.println("peekLast: " + tasks.peekLast());

        // add(index, value): O(n) to walk + relink
        tasks.add(1, "Review");
        System.out.println("after add at index: " + tasks);

        // get/set by index: O(n)
        System.out.println("get(2): " + tasks.get(2));
        tasks.set(2, "Code");
        System.out.println("after set(2, Code): " + tasks);

        // contains/indexOf/lastIndexOf: O(n)
        tasks.add("Code");
        System.out.println("contains Code: " + tasks.contains("Code"));
        System.out.println("indexOf Code: " + tasks.indexOf("Code"));
        System.out.println("lastIndexOf Code: " + tasks.lastIndexOf("Code"));

        // removeFirst/removeLast: O(1)
        String firstDone = tasks.removeFirst();
        String lastDone = tasks.removeLast();
        System.out.println("removed: " + firstDone + ", " + lastDone);
        System.out.println("remaining: " + tasks);

        // remove by index/value: O(n)
        tasks.remove(1);
        tasks.remove("Code");
        System.out.println("after remove index/value: " + tasks);

        // queue-like poll/peek: O(1)
        System.out.println("peek(): " + tasks.peek());
        System.out.println("poll(): " + tasks.poll());
        System.out.println("after poll: " + tasks);

        // stack-like push/pop: O(1)
        tasks.push("Hotfix");
        System.out.println("after push: " + tasks);
        System.out.println("pop: " + tasks.pop());
        System.out.println("after pop: " + tasks);

        // size/isEmpty/clear: O(1) / O(1) / O(n)
        System.out.println("size: " + tasks.size() + ", isEmpty: " + tasks.isEmpty());
        tasks.clear();
        System.out.println("after clear, isEmpty: " + tasks.isEmpty());
    }
}
