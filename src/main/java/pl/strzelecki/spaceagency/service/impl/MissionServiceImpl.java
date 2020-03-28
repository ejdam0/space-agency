package pl.strzelecki.spaceagency.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pl.strzelecki.spaceagency.entity.Mission;
import pl.strzelecki.spaceagency.repository.MissionRepository;
import pl.strzelecki.spaceagency.service.AgencyService;
import pl.strzelecki.spaceagency.service.DuplicateFinder;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MissionServiceImpl implements AgencyService<Mission> {

    private static final Logger logger = LogManager.getLogger(MissionServiceImpl.class);

    private MissionRepository missionRepo;
    private DuplicateFinder<Mission> duplicateFinder;

    @Autowired
    public MissionServiceImpl(MissionRepository missionRepo,
                              DuplicateFinder<Mission> duplicateFinder) {
        this.missionRepo = missionRepo;
        this.duplicateFinder = duplicateFinder;
    }

    @Override
    public List<Mission> findAll() {
        logger.info("Find all missions");
        logger.trace("Calling missionRepo to find all missions");
        return missionRepo.findAll();
    }

    @Override
    public void save(Mission mission) {
        // need to check if mission id exists in the database
        logger.info("There is no duplicate mission in the database");
        logger.trace("Checking if mission set to edit is in the database");
        Optional<Mission> result = missionRepo.findById(mission.getId());
        if (mission.getId() != 0 && result.isEmpty()) {
            logger.info("Mission set to edit does not exist in the database");
            logger.error("Exception while searching for duplicate - mission does not exist");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This mission does not exist in the database.");
        }
        // need to check if mission name exists in the database
        logger.info("Save a mission");
        logger.trace("Checking if the mission already exists in the database");
        if (duplicateFinder.lookForDuplicateInDb(mission)) {
            logger.error("Exception while searching for duplicate - mission already exists");
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This mission name already exists in the database.");
        }
        logger.info("Mission exists in the database (and can be edited) or is new");
        logger.info("Saving the mission");
        missionRepo.save(mission);
    }

    @Override
    public void remove(long id) {
        logger.info("Remove a mission");
        logger.trace("Checking if the mission exists in the database");
        Optional<Mission> missionById = missionRepo.findById(id);
        logger.trace("Checking if result (Optional<Mission>) is empty");
        if (missionById.isEmpty()) {
            logger.error("Exception - Result is empty");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Mission with id: " + id + " not found.");
        }
        logger.info("Deleting the mission");
        missionRepo.deleteById(id);
    }
}
