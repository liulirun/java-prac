// Interface 1: Demonstrates Java 8+ and Java 9+ interface features
interface Playable {
    // =========================================================================
    // WHAT IS NOT SUPPORTED IN AN INTERFACE (Java 8, 9, and beyond)
    // =========================================================================

    // ❌ NOT SUPPORTED: Instance fields / state variables.
    // Interface variables are implicitly 'public static final' (constants).
    // You cannot have variables that track individual object state.
    // Un-commenting this line will cause a compilation error: "Variable 'volume'
    // might not have been initialized"
    // int volume;

    // ❌ NOT SUPPORTED: Constructors.
    // Interfaces cannot be instantiated on their own and cannot hold instance
    // state,
    // so they are not allowed to have constructors.
    // Un-commenting this will cause a compilation error: "Interfaces cannot have
    // constructors"
    /*
     * public Playable() {
     * System.out.println("Interfaces cannot have constructors!");
     * }
     */

    // ❌ NOT SUPPORTED: Final or Protected Abstract Methods.
    // Abstract methods in an interface must be overridden by implementing classes,
    // so they cannot be 'final' (prevents overriding) or 'protected' (restricts
    // visibility).
    // Un-commenting these will cause compilation errors:
    // final void pause();
    // protected void fastForward();

    // =========================================================================
    // SUPPORTED FEATURES
    // =========================================================================

    // 1. Abstract method (Must be implemented by the class)
    void play();

    // 2. Static method (Belongs to the interface, called via
    // Playable.checkSystem())
    static void checkSystem() {
        System.out.println("[System Static Log] Checking media player capabilities...");
    }

    // 3. Default method 1 (Inherited by the class)
    default void startPlayback() {
        logAction("START"); // Calls the private helper method
        play();
    }

    // 4. Default method 2 (Inherited by the class)
    default void stopPlayback() {
        logAction("STOP"); // Calls the same private helper method
        System.out.println("Playback stopped safely.");
    }

    // 5. Private method (Java 9+ feature: Used to share code between default
    // methods)
    private void logAction(String action) {
        System.out.println("[Media Private Log] Initiating action: " + action);
    }
}

// Interface 2: A secondary interface to demonstrate multiple inheritance
interface Encryptable {
    void encrypt();
}

// The main public class matching your filename "InterfaceFun1.java"
public class InterfaceFun1 implements Playable, Encryptable {
    private String title;

    // Constructor (Supported in classes, NOT in interfaces)
    public InterfaceFun1(String title) {
        this.title = title;
    }

    // Implementing abstract method from Playable
    @Override
    public void play() {
        System.out.println("Now playing video: " + title);
    }

    // Implementing abstract method from Encryptable
    @Override
    public void encrypt() {
        System.out.println("Securing '" + title + "' with AES-256 encryption.");
    }

    // Main execution point
    public static void main(String[] args) {
        // Create an instance of our class
        InterfaceFun1 movie = new InterfaceFun1("Inception.mp4");

        System.out.println("--- 1. Testing Interface Static Method ---");
        // Static methods are called directly on the Interface name
        Playable.checkSystem();

        System.out.println("\n--- 2. Testing Default & Private Methods ---");
        // Calls default method, which internally triggers the private log and abstract
        // play()
        movie.startPlayback();

        System.out.println();
        // Calls the second default method, sharing the same private log logic
        movie.stopPlayback();

        System.out.println("\n--- 3. Testing Multiple Inheritance Method ---");
        // Calls method from the second interface
        movie.encrypt();
    }
}
