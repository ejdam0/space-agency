package pl.strzelecki.spaceagency.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.strzelecki.spaceagency.entity.Mission;
import pl.strzelecki.spaceagency.repository.MissionRepository;
import pl.strzelecki.spaceagency.service.DuplicateFinder;

import java.util.Optional;

@Service
public class MissionDuplicateFinder implements DuplicateFinder<Mission> {

    private static final Logger logger = LogManager.getLogger(MissionDuplicateFinder.class);

    private MissionRepository missionRepo;

    @Autowired
    public MissionDuplicateFinder(MissionRepository missionRepo) {
        this.missionRepo = missionRepo;
    }

    @Override
    public boolean lookForDuplicateInDb(Mission mission) {
        logger.info("Looking for duplicate mission in database");
        logger.trace("Calling missionRepo to find mission by its name");
        Optional<Mission> missionFromDb = missionRepo.findByName(mission.getName());
        logger.trace("Checking if result (Optional<Mission>) is empty");
        if (missionFromDb.isEmpty()) {
            logger.info("Result is empty - no duplicate exists in database");
            return false;
        }
        logger.info("There is duplicate data in the database");
        return true;
    }
}
