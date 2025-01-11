package com.example.socialfood.model.handler;

import java.util.List;

/**
 * Interface for data access operations on entities. Provides basic CRUD operations that all entity
 * handlers must implement.
 * 
 * @param <T> The type of entity being handled
 */
public interface EntityHandlerInterface<T> {

    /**
     * Inserts a new entity into the database
     * 
     * @param entity The entity to insert
     * @return true if insertion was successful, false otherwise
     * @throws IllegalArgumentException if entity is invalid
     */
    boolean insert(T entity);

    /**
     * Retrieves all entities from the database
     * 
     * @return List of all entities, empty list if none found
     */
    List<T> getAll();

    /**
     * Updates an existing entity in the database
     * 
     * @param entity The entity to update
     * @return true if update was successful, false otherwise
     * @throws IllegalArgumentException if entity is invalid
     */
    boolean update(T entity);

    /**
     * Deletes an entity from the database
     * 
     * @param entity The entity to delete
     * @return true if deletion was successful, false otherwise
     */
    boolean delete(T entity);
}
