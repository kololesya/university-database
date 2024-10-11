package com.laba.solvd.dao;

import java.util.List;

public interface GenericDao<T>{
    T findById(Long id) throws InterruptedException;
    List<T> findAll();
    void create(T t);
    void update(T t);
    void deleteById(Long id);
}
