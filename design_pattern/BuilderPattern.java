// WHEN TO USE: Use this when you have an object with many optional setup parameters or complex 
// steps, and you want to prevent giant constructors filled with dirty parameters and null values.
//
// HOW IT WORKS: An isolated inner configuration builder class sets properties step-by-step 
// via chain methods, returning the final built immutable data target instance when you call build().
// TODO: use **Lombok's `@Builder` annotation** to automatically generate this entire static inner class
class SystemProfile {
  private final String username; // Required
  private final String theme; // Optional
  private final boolean debug; // Optional

  private SystemProfile(Builder builder) {
    this.username = builder.username;
    this.theme = builder.theme;
    this.debug = builder.debug;
  }
  @Override
  public String toString() { return "User: " + username + " | Theme: " + theme + " | DebugMode: " + debug; }

  public static class Builder {
    private final String username;
    private String theme = "DefaultLight"; // Default fallback
    private boolean debug = false; // Default fallback

    public Builder(String username) { this.username = username; }
    public Builder withTheme(String theme) {
      this.theme = theme;
      return this;
    }
    public Builder withDebug(boolean debug) {
      this.debug = debug;
      return this;
    }
    public SystemProfile build() { return new SystemProfile(this); }
    @Override
    public String toString() { return "Builder Data -> User: " + username + ", Theme: " + theme + ", Debug: " + debug; }
  }
}

public class BuilderPattern {
  public static void main(String[] args) {
    System.out.println("--- Running Builder Pattern ---");
    // Step-by-Step object construction
    SystemProfile simpleProfile = new SystemProfile.Builder("JuniorDev123").build();
    SystemProfile complexProfile = new SystemProfile.Builder("SeniorSysAdmin").withTheme("UltraDark").withDebug(true)
        .build();
    System.out.println(simpleProfile);
    System.out.println(complexProfile);
  }
}
