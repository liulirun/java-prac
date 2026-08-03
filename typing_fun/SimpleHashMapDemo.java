public class SimpleHashMapDemo {

  // 1. The Node structure containing the 4 essential fields
  static class Node<K, V> {
    final int hash;
    final K key;
    V value;
    Node<K, V> next; // Pointer to the next node in case of a collision

    Node(int hash, K key, V value, Node<K, V> next) {
      this.hash = hash;
      this.key = key;
      this.value = value;
      this.next = next;
    }
  }

  static class MyHashMap<K, V> {
    // Fixed small capacity of 4 to easily trigger and demonstrate collisions
    private final int CAPACITY = 4;
    @SuppressWarnings("unchecked")
    private final Node<K, V>[] table = new Node[CAPACITY];

    // Simple hash function to calculate the array index
    private int getBucketIndex(K key) {
      int hash = key.hashCode();
      // Use Math.abs to handle negative hash codes, then use remainder math (%)
      int index = Math.abs(hash) % CAPACITY;
      System.out.printf("   [System Math] Key '%s' has HashCode %d -> Maps to Index [%d]\n", key, hash, index);
      return index;
    }

    // INSERT OPERATION (Handles Success and Collisions)
    public void put(K key, V value) {
      System.out.printf("\n>>> Inserting Pair: {%s = %s}\n", key, value);
      int hash = key.hashCode();
      int index = getBucketIndex(key);

      Node<K, V> head = table[index];

      // Scenario A: Bucket is empty (No Collision - Pure Success)
      if (head == null) {
        table[index] = new Node<>(hash, key, value, null);
        System.out.printf("   [Success] Slot [%d] was completely empty. Placed directly.\n", index);
        return;
      }

      // Scenario B: Bucket is occupied (Collision Imminent)
      System.out.printf("   [Collision Warning] Slot [%d] is already occupied by key '%s'!\n", index, head.key);
      Node<K, V> current = head;

      while (current != null) {
        // If the key already exists, update its value
        if (current.key.equals(key)) {
          System.out.printf("   [Update] Key '%s' already exists in the chain. Updating value to '%s'.\n", key, value);
          current.value = value;
          return;
        }
        // If we reached the end of the chain, stop looping
        if (current.next == null) {
          break;
        }
        current = current.next;
      }

      // Chain the new node to the end of the existing linked list
      current.next = new Node<>(hash, key, value, null);
      System.out.printf("   [Separate Chaining] Appended key '%s' to the end of the chain at index [%d].\n", key,
          index);
    }

    // RETRIEVAL OPERATION (Loops through chains if necessary)
    public V get(K key) {
      System.out.printf("\n<<< Fetching Value for Key: '%s'\n", key);
      int index = getBucketIndex(key);
      Node<K, V> current = table[index];

      int step = 1;
      while (current != null) {
        System.out.printf("   [Step %d] Checking node at index [%d] with key '%s'...\n", step, index, current.key);
        if (current.key.equals(key)) {
          System.out.printf("   [Match Found!] Key matches. Returning value: '%s'\n", current.value);
          return current.value;
        }
        // Move to the next linked node in the chain
        current = current.next;
        step++;
      }

      System.out.println("   [Not Found] Traversed the entire bucket/chain. Key does not exist.");
      return null;
    }

    // Helper method to visually print the internal array structure
    public void printInternalStructure() {
      System.out.println("\n================ INTERNAL MAP STRUCTURE ================");
      for (int i = 0; i < CAPACITY; i++) {
        System.out.print("[" + i + "] -> ");
        Node<K, V> current = table[i];
        if (current == null) {
          System.out.print("null");
        } else {
          while (current != null) {
            System.out.print("Node{" + current.key + "=" + current.value + "} -> ");
            current = current.next;
          }
          System.out.print("null");
        }
        System.out.println();
      }
      System.out.println("========================================================");
    }
  }

  public static void main(String[] args) {
    MyHashMap<String, String> map = new MyHashMap<>();

    // 1. Insert items that land on different indexes (Clean Success)
    // For standard Java strings on a 4-capacity map:
    // "A" maps to Index 1
    // "B" maps to Index 2
    map.put("A", "Apple");
    map.put("B", "Banana");

    // Print structure after clean insertions
    map.printInternalStructure();

    // 2. Insert items that cause a collision
    // "E" also maps to Index 1 (Collides with "A")
    // "I" also maps to Index 1 (Collides with "A" and "E")
    map.put("E", "Eggplant");
    map.put("I", "Ice Cream");

    // Print structure showing the built chains
    map.printInternalStructure();

    // 3. Fetch data to see how it navigates the chains
    map.get("B"); // Fast lookup (Index 2 has no chain)
    map.get("I"); // Chain lookup (Traverses Index 1: A -> E -> I)
    map.get("Z"); // Non-existent item lookup
  }
}
