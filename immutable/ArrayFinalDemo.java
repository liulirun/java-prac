package immutable;

public class ArrayFinalDemo {
    public static void main(String[] args) {

        String[] standardRoles = {"Admin", "User"};
        final String[] finalRoles = {"Admin", "User"};

        standardRoles[1] = "Hacker";            // ✅ Mutate internal contents
        finalRoles[1] = "Hacker";               // ✅ Mutate internal contents (final allows this)
        // standardRoles = new String[]{"1"};
        standardRoles = new String[]{"Guest"};  // ✅ Reassign variable pointer to a new array
        // finalRoles = new String[]{"Guest"};  // ❌ ERROR: Compiler blocks reassigning final variable

        try {
            standardRoles[1] = "NewSlot";       // ❌ RUNTIME ERROR: Index 1 is dead (size shrunk to 1 above)
            finalRoles[2] = "Guest";            // ❌ RUNTIME ERROR: Fixed size check (Index 2 does not exist)
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Caught Expected Error: Out of bounds!");
        }
    }
}
