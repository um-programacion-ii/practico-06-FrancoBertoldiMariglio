package dao;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class GenericDAO<T> {
    protected static final Logger LOGGER = Logger.getLogger(GenericDAO.class.getName());
    protected HashMap<Integer, T> tabla = new HashMap<>();

    public void save(T entity, Integer id) throws NoSuchElementException {
        try {
            tabla.put(id, entity);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding entity", e);
            throw new NoSuchElementException("Error adding entity", e);
        }
    }

    public T getById(Integer id) throws NoSuchElementException {
        try {
            T entity = tabla.get(id);
            if (entity == null) {
                throw new NoSuchElementException("No entity with ID " + id);
            }
            return entity;
        } catch (NoSuchElementException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding entity by ID", e);
            throw new NoSuchElementException("Error finding entity by ID", e);
        }
    }

    public HashMap<Integer, T> getAll() throws NoSuchElementException {
        try {
            return tabla;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding all entities", e);
            throw new NoSuchElementException("Error finding all entities", e);
        }
    }

    public void update(T entity, Integer id) throws NoSuchElementException {
        try {
            if (tabla.get(id) == null) {
                throw new NoSuchElementException("No entity with ID " + id);
            }
            tabla.put(id, entity);
        } catch (NoSuchElementException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating entity", e);
            throw new NoSuchElementException("Error updating entity", e);
        }
    }

    public void delete(Integer id) throws NoSuchElementException {
        try {
            T removedEntity = tabla.remove(id);
            if (removedEntity == null) {
                throw new NoSuchElementException("No entity with ID " + id);
            }
        } catch (NoSuchElementException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting entity", e);
            throw new NoSuchElementException("Error deleting entity", e);
        }
    }
}