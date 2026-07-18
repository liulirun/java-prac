import java.util.Arrays;

/**
 * Q20) Linked List Cycle
 *
 * AI-BEST:
 * Floyd's slow/fast pointers.
 * Time: O(n), Space: O(1)
 */
public class q20_linked_list_cycle_easy {
    static class ListNode {
        int val;
        ListNode next;

        ListNode(int val) {
            this.val = val;
        }
    }

    static boolean hasCycleBest(ListNode head) {
        // Step 1: Move slow by 1 and fast by 2.
        // Why: in a cycle, fast eventually catches slow.
        ListNode slow = head;
        ListNode fast = head;

        // Step 2: Stop on meet (cycle) or null (no cycle).
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) return true;
        }

        // Step 3: Reaching list end means no cycle.
        return false;
    }

    static ListNode buildListWithCycle(int[] values, int cyclePos) {
        if (values.length == 0) return null;
        ListNode[] nodes = new ListNode[values.length];
        for (int i = 0; i < values.length; i++) nodes[i] = new ListNode(values[i]);
        for (int i = 0; i < values.length - 1; i++) nodes[i].next = nodes[i + 1];
        if (cyclePos >= 0 && cyclePos < values.length) {
            nodes[values.length - 1].next = nodes[cyclePos];
        }
        return nodes[0];
    }

    public static void main(String[] args) {
        System.out.println("Q20: Linked List Cycle");
        int[][] valueCases = {
                {3, 2, 0, -4},
                {1, 2},
                {1}
        };
        int[] cyclePosCases = {1, 0, -1};

        for (int i = 0; i < valueCases.length; i++) {
            int[] values = valueCases[i];
            int cyclePos = cyclePosCases[i];
            ListNode head = buildListWithCycle(values, cyclePos);
            System.out.println("Input: values=" + Arrays.toString(values) + ", cyclePos=" + cyclePos);
            System.out.println("  BEST: " + hasCycleBest(head));
        }
    }
}
