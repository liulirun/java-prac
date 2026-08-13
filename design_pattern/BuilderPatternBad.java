// Save this file as: BuilderPatternBad.java
// Run command: java BuilderPatternBad.java
// ============================================================================
// THE BAD CODE: The "Telescoping Constructor" Nightmare.
// To handle optional settings, you are forced to make a giant constructor 
// or write multiple confusing constructors that pass "null" everywhere.
// ============================================================================
class SystemProfileBad {
  private final String username; // Required
  private final String theme; // Optional
  private final boolean debug; // Optional

  // Constructor 1: If they only have the required username
  public SystemProfileBad(String username) {
    this(username, "DefaultLight", false); // Hardcoded fallbacks look messy
                                           // here
  }
  // Constructor 2: If they have username and theme
  public SystemProfileBad(String username, String theme) { this(username, theme, false); }
  // Constructor 3: The Giant "Master" Constructor
  public SystemProfileBad(String username, String theme, boolean debug) {
    this.username = username;
    this.theme = theme;
    this.debug = debug;
  }
  @Override
  public String toString() { return "User: " + username + " | Theme: " + theme + " | DebugMode: " + debug; }
}

public class BuilderPatternBad {
  public static void main(String[] args) {
    System.out.println("--- Running Bad Version (No Builder) ---");
    // Creating a simple profile requires you to know which constructor to pick
    SystemProfileBad simpleProfile = new SystemProfileBad("JuniorDev123");
    // ❌ CRITICAL PROBLEM 1: The "Argument Swamp"
    // To make a complex profile, you must type values blindly in a specific
    // order.
    // If you mistake the position of parameters, it breaks without warning.
    SystemProfileBad complexProfile = new SystemProfileBad("SeniorSysAdmin", "UltraDark", true);
    // ❌ CRITICAL PROBLEM 2: The "Null / Default Trap"
    // What if a user wants debug mode active, but wants the DEFAULT theme?
    // You are completely forced to pass manual default strings or 'null'
    // values!
    SystemProfileBad messyProfile = new SystemProfileBad("AdminTwo", "DefaultLight", true);
    SystemProfileBad dangerousProfile = new SystemProfileBad("AdminThree", null, true); // Dangerous
                                                                                        // null!
    System.out.println(simpleProfile);
    System.out.println(complexProfile);
    System.out.println(messyProfile);
  }
}
