package pl.strzelecki.spaceagency.service;

public interface DuplicateFinder<T> {
    boolean lookForDuplicateInDb(T t);
}
