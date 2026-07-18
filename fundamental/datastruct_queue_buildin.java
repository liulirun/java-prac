import java.util.ArrayDeque;
import java.util.Queue;

public class datastruct_queue_buildin {
    public static void main(String[] args) {
        Queue<String> queue = new ArrayDeque<>();

        // offer adds to tail: O(1)
        queue.offer("job-1");
        queue.offer("job-2");
        queue.offer("job-3");
        System.out.println("initial: " + queue);

        // add also inserts tail: O(1)
        queue.add("job-4");
        System.out.println("after add(job-4): " + queue);

        // peek/element read head: O(1)
        System.out.println("peek: " + queue.peek());
        System.out.println("element: " + queue.element());

        // poll/remove remove head: O(1)
        System.out.println("poll: " + queue.poll());
        System.out.println("remove: " + queue.remove());
        System.out.println("after poll: " + queue);

        // contains: O(n), size/isEmpty: O(1)
        System.out.println("contains job-3: " + queue.contains("job-3"));
        System.out.println("size: " + queue.size() + ", isEmpty: " + queue.isEmpty());

        // addAll: O(m)
        queue.addAll(java.util.List.of("job-5", "job-6"));
        System.out.println("after addAll: " + queue);

        // iterate: O(n)
        for (String job : queue) {
            System.out.println("next job: " + job);
        }

        // clear: O(n)
        queue.clear();
        System.out.println("after clear, isEmpty: " + queue.isEmpty());
    }
}
