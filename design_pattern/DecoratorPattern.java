// WHEN TO USE: Use this when you want to dynamically stack or chain extra optional behaviors 
// onto an object at runtime without altering its original source class structure.
//
// HOW IT WORKS: The wrapper class implements the same interface as the target object, wraps it, 
// modifies the functional output, and then delegates the rest of the call back down the line.

interface TextProcessor {
    String process(String input);
}

class PlainText implements TextProcessor {
    @Override
    public String process(String input) { return input; }
}

abstract class TextDecorator implements TextProcessor {
    protected TextProcessor wrapper;
    public TextDecorator(TextProcessor wrapper) { this.wrapper = wrapper; }
}

class UpperCaseDecorator extends TextDecorator {
    public UpperCaseDecorator(TextProcessor wrapper) { super(wrapper); }
    @Override
    public String process(String input) { 
        return wrapper.process(input).toUpperCase(); 
    }
}

class ExclamationDecorator extends TextDecorator {
    public ExclamationDecorator(TextProcessor wrapper) { super(wrapper); }
    @Override
    public String process(String input) { 
        return wrapper.process(input) + "!!!"; 
    }
}

public class DecoratorPattern {
    public static void main(String[] args) {
        System.out.println("--- Running Decorator Pattern ---");

        TextProcessor simple = new PlainText();
        System.out.println(simple.process("hello")); // hello

        // Stacking layers dynamically at runtime
        TextProcessor customPipeline = new ExclamationDecorator(new UpperCaseDecorator(new PlainText()));
        System.out.println(customPipeline.process("hello")); // HELLO!!!
    }
}
