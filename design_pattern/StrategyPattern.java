// WHEN TO USE: Use this when you have an operation that can be done in multiple different ways, 
// and you want to choose or swap the algorithm at runtime without using dirty if-else blocks.
//
// HOW IT WORKS: The execution class holds a reference to an interface. At runtime, you inject 
// whichever concrete implementation behavior you want into that interface slot.

interface MathOperation {
  int execute(int a, int b);
}

class AddOperation implements MathOperation {
  @Override
  public int execute(int a, int b) {
    return a + b;
  }
}

class MultiplyOperation implements MathOperation {
  @Override
  public int execute(int a, int b) {
    return a * b;
  }
}

class ContextExecutor {
  private MathOperation operation;

  // Factory Injection Point: Swap behavior dynamically here
  public void setOperation(MathOperation operation) {
    this.operation = operation;
  }

  public void runMath(int a, int b) {
    int result = operation.execute(a, b);
    System.out.println("Result: " + result);
  }
}

public class StrategyPattern {
  public static void main(String[] args) {
    ContextExecutor context = new ContextExecutor();

    System.out.println("--- Running Strategy Pattern ---");

    // Dynamic Behavior Switch 1
    context.setOperation(new AddOperation());
    context.runMath(10, 5); // Prints: 15

    // Dynamic Behavior Switch 2
    context.setOperation(new MultiplyOperation());
    context.runMath(10, 5); // Prints: 50
  }
}
