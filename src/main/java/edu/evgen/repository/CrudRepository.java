package edu.evgen.repository;

import java.util.List;

public interface CrudRepository<T> {
    public void saveOrUpdate(T entity);
    public void deleteById(Long id);
    public List<T> findAll();
    public T findById(Long id);
}
