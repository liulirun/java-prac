// WHEN TO USE: Use this when an important state event inside an object needs to automatically 
// trigger alerts across other unrelated background entities without hardcoding structural links.
//
// HOW IT WORKS: The publisher stores a list of subscriber interfaces. When an event fires, 
// it iterates over that list, calling the update method on all active listeners simultaneously.

import java.util.ArrayList;
import java.util.List;

interface AlertListener { void handleNotification(String criticalMessage); }

class TerminalLogger implements AlertListener {
  @Override
  public void handleNotification(String msg) { System.out.println("Log saved: " + msg); }
}

class EmailAlertService implements AlertListener {
  @Override
  public void handleNotification(String msg) { System.out.println("Email broadcast sent containing: " + msg); }
}

class EventManager {
  private final List<AlertListener> subscribers = new ArrayList<>();

  public void attach(AlertListener listener) { subscribers.add(listener); }
  public void notifyAll(String msg) {
    for (AlertListener sub : subscribers) {
      sub.handleNotification(msg);
    }
  }
}

public class ObserverPattern {
  public static void main(String[] args) {
    System.out.println("--- Running Observer Pattern ---");
    EventManager dispatcher = new EventManager();
    dispatcher.attach(new TerminalLogger());
    dispatcher.attach(new EmailAlertService());
    System.out.println("Broadcasting Event:");
    dispatcher.notifyAll("System Memory Warning at 92%");
  }
}
