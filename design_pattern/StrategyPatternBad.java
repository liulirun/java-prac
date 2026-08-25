// THE BAD CODE: The Hardcoded Algorithm Swamp.
// Your execution class holds all the formulas inside itself. It forces 
// the user to pass a dirty string flag to choose the math path.
// ============================================================================
class ContextExecutorBad {
  private String currentOperationType; // ❌ Dirty flag indicator

  public void setOperationType(String operationType) { this.currentOperationType = operationType; }
  public void runMath(int a, int b) {
    int result = 0;
    // ❌ CRITICAL PROBLEM BLOCK:
    // This execution block is tightly coupled to every single math rule.
    // If you add a "Divide" or "Subtract" calculation tomorrow, you must
    // break open this core file and add more 'else if' or switch cases!
    if ("ADD".equalsIgnoreCase(currentOperationType)) {
      result = a + b;
    } else if ("MULTIPLY".equalsIgnoreCase(currentOperationType)) {
      result = a * b;
    } else {
      System.out.println("[ERROR] Unknown operation style!");
      return;
    }
    System.out.println("Result: " + result);
  }
}

public class StrategyPatternBad {
  public static void main(String[] args) {
    ContextExecutorBad context = new ContextExecutorBad();
    System.out.println("--- Running Bad Version (No Strategy) ---");
    // Switch 1: Requires typing exact matching magic strings
    context.setOperationType("ADD");
    context.runMath(10, 5);
    // Switch 2: High risk of typos breaking the application
    context.setOperationType("MULTIPLY");
    context.runMath(10, 5);
  }
}
