package com.bootcamp.bankapi.dao;

import java.io.Serializable;
import java.util.List;

public interface DAO <T, ID extends Serializable> {

    T get(ID id);

    List<T> getAll();

    void save(T t);

    void delete (T t);
}
