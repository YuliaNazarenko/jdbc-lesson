package com.eurotechstudy.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<E, K> {

    E save(E e);

    void update(E e);

    boolean delete(K id);

    Optional<E> findById(K id);

    List<E> findAll();



}
