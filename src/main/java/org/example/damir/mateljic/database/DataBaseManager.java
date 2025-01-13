package org.example.damir.mateljic.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import java.util.logging.Logger;

public final class DataBaseManager {
    private static final String URL = "mongodb+srv://DamirNoviUser:DamirNoviUserPassword@cluster0.f00pa.mongodb.net/";
    private static final String DATABASE_NAME = "EmployeePayrollManagement";
    private static DataBaseManager instance;
    private final MongoClient mongoClient;
    private final MongoDatabase database;
    private final Logger logger = Logger.getLogger(DataBaseManager.class.getName());

    private  DataBaseManager() {
        try {
            mongoClient = MongoClients.create(URL);
            database = mongoClient.getDatabase(DATABASE_NAME);
            System.out.println("Connected to MongoDB successfully.");
        } catch (Exception e) {
            logger.info("Error connecting to MongoDB: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static synchronized DataBaseManager getInstance() {
        if (instance == null) {
            instance = new DataBaseManager();
        }
        return instance;
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    public void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
            logger.info("Connection to MongoDB closed.");
        }
    }
}