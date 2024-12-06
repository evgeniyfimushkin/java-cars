package edu.evgen.service;

import java.util.List;

public interface CrudService<T> {
    public void saveOrUpdate(T entity);
    public void deleteById(Long id);
    public List<T> findAll();
    public T findById(Long id);
}
