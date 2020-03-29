package pl.strzelecki.spaceagency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.strzelecki.spaceagency.entity.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
