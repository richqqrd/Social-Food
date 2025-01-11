package com.example.socialfood.model.database;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.socialfood.model.database.AppDatabase;
import com.example.socialfood.model.database.DatabaseClient;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Unit tests for the DatabaseClient singleton class.
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class DatabaseClientTest {
    /**
     * Mock context for testing database initialization
     */
    @Mock
    private Context mockContext;

    /**
     * Mock database instance for verifying database operations
     */
    @Mock
    private AppDatabase mockDatabase;

    /**
     * DatabaseClient instance under test
     */
    private DatabaseClient databaseClient;

    /**
     * Sets up the test environment before each test. Initializes mocks and creates a DatabaseClient
     * instance with mocked dependencies. Returns application context when requested to simulate
     * Android environment.
     */
    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(mockContext.getApplicationContext()).thenReturn(mockContext);
        databaseClient = DatabaseClient.getInstance(mockContext);
    }

    /**
     * Tests singleton pattern implementation of DatabaseClient. Verifies that: - getInstance()
     * returns non-null instance - Multiple calls return same instance
     */
    @Test
    public void testGetInstance() {
        DatabaseClient client = DatabaseClient.getInstance(mockContext);
        assertNotNull("DatabaseClient instance should not be null", client);

        DatabaseClient secondClient = DatabaseClient.getInstance(mockContext);
        assertSame("Should return same singleton instance", client, secondClient);
    }

    /**
     * Tests error handling when getting instance with null context. Expects
     * IllegalArgumentException to be thrown.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetInstanceWithNullContext() {
        DatabaseClient.getInstance(null);
    }

    /**
     * Tests retrieving database instance from DatabaseClient. Verifies that: - getDatabase()
     * returns non-null instance - Database is properly initialized
     */
    @Test
    public void testGetDatabase() {
        AppDatabase db = databaseClient.getDatabase();
        assertNotNull("Database instance should not be null", db);
    }

    /**
     * Cleans up test environment after each test. Resets singleton instance to ensure tests are
     * isolated.
     */
    @After
    public void tearDown() {
        DatabaseClient.clearInstance();
    }
}
