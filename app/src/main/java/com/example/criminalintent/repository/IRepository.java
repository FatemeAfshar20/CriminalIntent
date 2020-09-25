package com.example.criminalintent.repository;

import com.example.criminalintent.model.Crime;

import java.util.List;
import java.util.UUID;

public interface IRepository<E> {
    List<E> getList();
    E get(UUID id);
    void insert(E e);
    void update(E e);
    void delete(E e);
    int getPosition(UUID id);
}
