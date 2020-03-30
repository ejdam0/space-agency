package pl.strzelecki.spaceagency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.strzelecki.spaceagency.model.Mission;

public interface MissionRepository extends JpaRepository<Mission, Long> {
    boolean existsByName(String name);
}
