package com.example.auction;

import java.util.ArrayList;
import java.util.List;

public class AuthenticationService {
    private List<User> users;
    private User loggedInUser;

    public AuthenticationService() {
        users = new ArrayList<>();
        // Add some default users for testing
        users.add(new User("admin", "admin123", UserType.ADMIN));
        users.add(new User("user", "user123", UserType.USER));
    }

    public boolean authenticateAdmin(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password) &&
                    user.getUserType() == UserType.ADMIN) {
                loggedInUser = user; // Update the loggedInUser
                return true;
            }
        }
        return false;
    }

    public boolean authenticateUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password) &&
                    user.getUserType() == UserType.USER) {
                loggedInUser = user; // Update the loggedInUser
                return true;
            }
        }
        return false;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void addUser(String username, String password, UserType userType) {
        users.add(new User(username, password, userType));
    }

    public enum UserType {
        ADMIN,
        USER
    }

    public static class User {
        private String username;
        private String password;
        private UserType userType;
        private List<Bid> bids;

        public User(String username, String password, UserType userType) {
            this.username = username;
            this.password = password;
            this.userType = userType;
            this.bids = new ArrayList<>();
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public UserType getUserType() {
            return userType;
        }

        public List<Bid> getBids() {
            return bids;
        }
    }
}
