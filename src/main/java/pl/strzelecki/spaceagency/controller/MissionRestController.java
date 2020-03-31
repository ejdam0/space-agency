package pl.strzelecki.spaceagency.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.strzelecki.spaceagency.model.Mission;
import pl.strzelecki.spaceagency.service.AgencyService;

import java.util.List;

@RestController
@RequestMapping("/missions")
public class MissionRestController {

    private static final Logger logger = LogManager.getLogger(MissionRestController.class);

    private AgencyService<Mission> missionService;

    @Autowired
    public MissionRestController(@Qualifier("missionServiceImpl") AgencyService<Mission> missionService) {
        this.missionService = missionService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody Mission mission) {
        logger.info("Add a mission");
        logger.trace("Setting mission ID to 0, to force saving");
        // set id to force saving
        mission.setId(0);
        try {
            logger.trace("Calling missionService to try to save the mission");
            missionService.save(mission);
        } catch (ResponseStatusException e) {
            logger.error("Exception while trying to save the mission: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), e.getStatus());
        }
        logger.info("Mission added successfully");
        return new ResponseEntity<>("Added new mission.", HttpStatus.CREATED);
    }

    @PutMapping("/edit")
    public ResponseEntity<?> edit(@RequestBody Mission mission) {
        logger.info("Edit a mission");
        try {
            logger.trace("Calling missionService to try to edit the mission");
            missionService.save(mission);
        } catch (ResponseStatusException e) {
            logger.error("Exception while trying to edit the mission: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), e.getStatus());
        }
        logger.info("Mission edited successfully");
        return new ResponseEntity<>(mission, HttpStatus.OK);
    }

    @DeleteMapping("/remove/{missionId}")
    public ResponseEntity<?> remove(@PathVariable("missionId") long id) {
        logger.info("Remove a mission");
        try {
            logger.trace("Calling missionService to try to remove the mission");
            missionService.remove(id);
        } catch (ResponseStatusException e) {
            logger.error("Exception while trying to remove the mission: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), e.getStatus());
        }
        logger.info("Mission removed successfully");
        return new ResponseEntity<>("Deleted mission with id: " + id, HttpStatus.OK);
    }
}
