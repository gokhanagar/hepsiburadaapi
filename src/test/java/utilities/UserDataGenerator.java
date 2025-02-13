package utilities;

import java.util.UUID;

import models.User;

public class UserDataGenerator {
    private static User lastGeneratedUser;

    public static User generateUser() {
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        
        User user = new User(
            "user_" + uniqueId,
            "FirstName_" + uniqueId,
            "LastName_" + uniqueId,
            "email_" + uniqueId + "@test.com",
            "555" + uniqueId.substring(0, 7),
            1
        );
        
        lastGeneratedUser = user;
        return user;
    }

    public static String getLastGeneratedUsername() {
        return lastGeneratedUser != null ? lastGeneratedUser.getUsername() : null;
    }

    public static User getLastGeneratedUser() {
        return lastGeneratedUser;
    }
} 