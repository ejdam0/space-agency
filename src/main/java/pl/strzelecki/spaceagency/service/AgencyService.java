package pl.strzelecki.spaceagency.service;

import java.util.List;

public interface AgencyService<T> {
    List<T> findAll();

    void save(T t);

    void remove(long id);
}
