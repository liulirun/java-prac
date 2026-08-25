// Save this file as: ChainOfResponsibilityPatternBad.java
// Run command: java ChainOfResponsibilityPatternBad.java
// ============================================================================
// THE BAD CODE: The Caller has to act like a micromanager.
// It must run all the step operations inside a giant, messy nesting block.
// ============================================================================
class UserValidatorBad {
  public boolean isValidUser(String username) {
    if (username == null || username.isBlank()) {
      System.out.println("[FAILED] Invalid Username.");
      return false;
    }
    System.out.println("[PASSED] User exists check.");
    return true;
  }
}

class SecurityValidatorBad {
  public boolean isAuthorized(String accessLevel) {
    if (!"ADMIN".equalsIgnoreCase(accessLevel)) {
      System.out.println("[FAILED] Security match rejected.");
      return false;
    }
    System.out.println("[PASSED] Access level verified.");
    return true;
  }
}

public class ChainOfResponsibilityPatternBad {
  public static void main(String[] args) {
    System.out.println("--- Running Bad Version (No Chain) ---");
    UserValidatorBad userCheck = new UserValidatorBad();
    SecurityValidatorBad securityCheck = new SecurityValidatorBad();
    System.out.println("Attempt 1 (Guest User):");
    runEntirePipelineManual("JohnDoe", "GUEST", userCheck, securityCheck);
    System.out.println("\nAttempt 2 (Admin User):");
    runEntirePipelineManual("RootAdmin", "ADMIN", userCheck, securityCheck);
  }
  // ❌ CRITICAL PROBLEM BLOCK:
  // Look at this messy, nested routing logic.
  // Your main app is now forced to track exactly who runs first, who runs
  // second,
  // and manually write the 'return' commands to drop out early.
  public static void runEntirePipelineManual(String username, String accessLevel, UserValidatorBad userCheck,
      SecurityValidatorBad securityCheck) {
    // Step 1 Check
    if (userCheck.isValidUser(username)) {
      // Step 2 Check (Permanently hardcoded right inside Step 1's success path)
      if (securityCheck.isAuthorized(accessLevel)) {
        System.out.println("Success! All validation blocks passed.");
      }
    }
  }
}
