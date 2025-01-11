package com.example.socialfood.model.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;

/**
 * Singleton class for managing database access. Provides centralized access to Room database
 * instance and handles database migrations.
 */
public class DatabaseClient {
    private static DatabaseClient instance;
    private final AppDatabase database;
    private static final String DATABASE_NAME = "social_food_database";
    private static final String TAG = "DatabaseClient";

    /**
     * Private constructor to initialize the database. Sets up the Room database with migration
     * strategies.
     *
     * @param context Application context used to create the database
     */
    private DatabaseClient(Context context) {

        database = Room.databaseBuilder(
                context.getApplicationContext(),
                AppDatabase.class,
                "social_food_database").fallbackToDestructiveMigration().build();

        Log.d(TAG, "Database initialized: " + database.isOpen());

    }

    /**
     * Gets the singleton instance of DatabaseClient. Creates a new instance if none exists.
     *
     * @param context Application context
     * @return The singleton DatabaseClient instance
     * @throws IllegalArgumentException if context is null
     */
    public static synchronized DatabaseClient getInstance(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }
        if (instance == null) {
            instance = new DatabaseClient(context);
        }
        return instance;
    }

    public DatabaseClient(AppDatabase appDatabase) {
        this.database = appDatabase;
    }

    /**
     * Gets the Room database instance.
     *
     * @return The AppDatabase instance
     */
    public AppDatabase getDatabase() {
        return database;
    }

    /**
     * Clears the singleton instance (for testing purposes)
     */
    public static void clearInstance() {
        instance = null;
    }

}
