package edu.school21.sockets.repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public interface CrudRepository<T> {
    // Create
    void createBase();
    void save(T entity);

    // Read
    Optional<T> findByName(String name);

    boolean verification(String name, String password);

    List<T> findAll(long id);

    // Update
    void update(T entity);

    // Delete
    void delete(Long id);

}
