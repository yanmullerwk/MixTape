package br.com.mixtape.dao;

import java.util.List;

public interface IDao<T> {
    void save(T entity);
    void update(T entity);
    T findById(int id);
    List<T> findAll();
    void delete(int id);
}
