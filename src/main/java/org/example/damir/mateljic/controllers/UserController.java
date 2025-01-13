package org.example.damir.mateljic.controllers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.example.damir.mateljic.database.DataBaseManager;
import org.example.damir.mateljic.models.User;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Random;
import java.util.logging.Logger;

public final class UserController {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(UserController.class);
    private final MongoDatabase database;
    private final MongoCollection<Document> userCollection;
    private final Logger logger = Logger.getLogger(UserController.class.getName());

    public UserController() {
        this.database = DataBaseManager.getInstance().getDatabase();
        this.userCollection = database.getCollection("users");
    }

    public boolean register(User user) {
        if (userExists(user.getUsername(),user.getPassword())) {
            logger.info("User already exists: " + user.getUsername());
            return false;
        }
        user.setId(user.getUsername()+ new Random().nextInt(1000));
        Document newUser = new Document("id", user.getId())
                .append("username", user.getUsername())
                .append("password", user.getPassword())
                .append("role", user.getRole());

        userCollection.insertOne(newUser);
        logger.info("User registered successfully.");
        return true;
    }
    public boolean createSuperAdmin() {
        Document existingSuperAdmin = userCollection.find(new Document("role", "superadmin")).first();
        if (existingSuperAdmin != null) {
            logger.info("Superadmin already exists. No action taken.");
            return false;
        }
        Document superAdmin = new Document("id", "superadmin"+ new Random().nextInt(1000))
                .append("username", "superadmin")
                .append("password", "12345")
                .append("role", "superadmin");

        userCollection.insertOne(superAdmin);
        logger.info("Superadmin created successfully.");
        return true;
    }

    public Optional<User> login(String username, String password) {
        Document query = new Document("username", username).append("password", password);
        Document userDoc = userCollection.find(query).first();

        if (userDoc != null) {
            User user = new User(
                    userDoc.getString("id"),
                    userDoc.getString("username"),
                    userDoc.getString("password"),
                    userDoc.getString("role")
            );
            logger.info("User logged in successfully: " + user.getUsername());
            return Optional.of(user);
        } else {
            System.out.println("Invalid username or password.");
            return Optional.empty();
        }
    }

    // Delete user by username
    public boolean delete(String username) {
        Document query = new Document("username", username);
        Document userDoc = userCollection.find(query).first();

        if (userDoc != null) {
            userCollection.deleteOne(query);
            logger.info("User deleted successfully: " + username);
            return true;
        } else {
            logger.info("User not found: " + username);
            return false;
        }
    }

    private boolean userExists(String username,String password) {
        Document query = new Document("username", username ).append("password", password);
        return userCollection.find(query).first() != null;
    }
}
