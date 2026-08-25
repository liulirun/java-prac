// WHEN TO USE: Use this when you have a sequence of processing checks or step operations 
// that a request must pass through, where any step has the authority to stop the process.
//
// HOW IT WORKS: Every validation step link contains a pointer reference to the next link. 
// A link completes its custom work and forwards the request, or drops out early by raising an issue.

abstract class ProcessingLink {
  protected ProcessingLink next;

  public void setNext(ProcessingLink next) {
    this.next = next;
  }

  public abstract void processRequest(String username, String accessLevel);
}

class UserCheckLink extends ProcessingLink {
  @Override
  public void processRequest(String username, String accessLevel) {
    if (username == null || username.isBlank()) {
      System.out.println("[FAILED] Invalid Username.");
      return;
    }
    System.out.println("[PASSED] User exists check.");
    if (next != null)
      next.processRequest(username, accessLevel);
  }
}

class SecurityLevelLink extends ProcessingLink {
  @Override
  public void processRequest(String username, String accessLevel) {
    if (!"ADMIN".equalsIgnoreCase(accessLevel)) {
      System.out.println("[FAILED] Security match rejected.");
      return;
    }
    System.out.println("[PASSED] Access level verified.");
    if (next != null)
      next.processRequest(username, accessLevel);
  }
}

public class ChainOfResponsibilityPattern {
  public static void main(String[] args) {
    System.out.println("--- Running Chain of Responsibility Pattern ---");

    ProcessingLink chainHead = new UserCheckLink();
    ProcessingLink secondLink = new SecurityLevelLink();
    chainHead.setNext(secondLink);

    System.out.println("Attempt 1 (Guest User):");
    chainHead.processRequest("JohnDoe", "GUEST"); // Fails at link 2

    System.out.println("\nAttempt 2 (Admin User):");
    chainHead.processRequest("RootAdmin", "ADMIN"); // Passes fully
  }
}
