package pl.strzelecki.spaceagency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.strzelecki.spaceagency.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
