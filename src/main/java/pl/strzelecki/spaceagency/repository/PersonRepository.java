package pl.strzelecki.spaceagency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.strzelecki.spaceagency.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
