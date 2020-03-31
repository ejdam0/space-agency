package pl.strzelecki.spaceagency.service;

public interface AgencyService<T> {
    void save(T t);

    void remove(long id);
}
