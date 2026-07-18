package immutable;

import java.time.LocalDate;

// 1. This is the main public class. The file MUST be named ImmutableTest.java
public class ImmutableTest {
    public static void main(String[] args) {
        LocalDate myDate = LocalDate.now(); 
        
        UserUseLocalDate user = new UserUseLocalDate("Alice", myDate);
        
        myDate.plusDays(5); // This creates a new date but does not alter 'myDate'
        
        System.out.println("Original Date: " + myDate);
    }
}

final class UserUseLocalDate {
    private final String username;
    private final LocalDate registrationDate; 

    public UserUseLocalDate(String username, LocalDate registrationDate) {
        this.username = username;
        this.registrationDate = registrationDate; 
    }

    public LocalDate getRegistrationDate() {
        return registrationDate; 
    }
}
