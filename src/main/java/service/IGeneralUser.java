package service;

import java.util.List;

public interface IGeneralUser<T> {
    void insert(T t);

    T select(int id);

    List<T> selectAll();

    boolean delete(int id);

    boolean update(T t);

    T findByCountry(String country);

    List<T> sort();

}
