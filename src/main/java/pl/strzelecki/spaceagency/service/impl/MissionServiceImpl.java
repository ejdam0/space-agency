package pl.strzelecki.spaceagency.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Change;
import org.javers.core.diff.Diff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pl.strzelecki.spaceagency.model.Mission;
import pl.strzelecki.spaceagency.repository.MissionRepository;
import pl.strzelecki.spaceagency.service.AgencyService;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MissionServiceImpl implements AgencyService<Mission> {

    private static final Logger logger = LogManager.getLogger(MissionServiceImpl.class);

    private MissionRepository missionRepo;

    @Autowired
    public MissionServiceImpl(MissionRepository missionRepo) {
        this.missionRepo = missionRepo;
    }

    @Override
    public void save(Mission mission) {
        // need to check if mission id exists in the database
        logger.info("Save mission");
        Optional<Mission> optMissionInDb = missionRepo.findById(mission.getId());
        boolean checkIfExistsInDb = missionRepo.existsByName(mission.getName());

        logger.info("Checking provided data against the database");
        if (mission.getId() != 0 && optMissionInDb.isEmpty()) {
            logger.info("Mission set to edit does not exist in the database");
            logger.error("Exception while searching for mission - mission does not exist");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This mission does not exist in the database.");
        } else if (mission.getId() == 0 && checkIfExistsInDb) {
            logger.trace("Mission with that name exists in the database");
            logger.error("Exception while adding mission - mission exists");
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This mission exists in the database.");
        } else if (mission.getId() != 0 && !checkIfExistsInDb) {
            logger.trace("Mission does not exist in the database");
            logger.info("Saving new mission");
            missionRepo.save(mission);
        } else {
            // mission id != 0, checkIfExistsInDb == false
            logger.info("Mission id is other than 0 and does exist in the database");
            logger.trace("Checking what's changed");
            Javers javers = JaversBuilder.javers().build();
            logger.trace("Getting result from optional mission");
            Mission missionFromDb = null;
            if (optMissionInDb.isPresent()) {
                missionFromDb = optMissionInDb.get();
            }
            logger.trace("Comparing mission names");
            Diff diff = javers.compare(missionFromDb, mission);
            List<Change> changes = diff.getChanges();
            logger.trace("Checking changes list size");
            if (changes.size() != 0 && checkIfExistsInDb) {
                logger.trace("Mission with that name exists in the database");
                logger.error("Exception while editing mission - mission exists");
                throw new ResponseStatusException(HttpStatus.CONFLICT, "This mission exists in the database.");
            } else {
                logger.info("Changes applied to other properties than name");
                logger.info("Editing mission");
                missionRepo.save(mission);
            }
        }
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
