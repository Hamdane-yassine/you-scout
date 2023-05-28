package ma.ac.inpt.controller;

import ma.ac.inpt.model.Skill;
import ma.ac.inpt.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class SkillController {
    final private SkillService skillService;

    @Autowired
    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping("/skills")
    public ResponseEntity<List<Skill>> getAllSkills(@RequestParam(required = false) String skill) {
        try {
            List<Skill> skills = skillService.getAllSkills(skill);
            if (skills.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(skills, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //
    @GetMapping("/skills/{id}")
    public ResponseEntity<?> getSkillById(@PathVariable("id") String id) {
        Optional<Skill> skillData = skillService.getSKillById(id);

        if (skillData.isPresent()) {
            return new ResponseEntity<>(skillData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Skill not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/skills")
    public ResponseEntity<Skill> createSkill(@RequestBody Skill skill) {
        try {
            Skill newSkill = skillService.createSkill(skill);
            return new ResponseEntity<>(newSkill, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/skills/{id}/increment")
    public ResponseEntity<String> incrementSkill(@PathVariable("id") String id) {
        try {
            String message = skillService.incrementSkill(id);
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/skills/{id}/decrement")
    public ResponseEntity<String> decrementSkill(@PathVariable("id") String id) {
        try {
            String message = skillService.decrementSkill(id);
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/skills/{id}/activate")
    public ResponseEntity<String> activateSkill(@PathVariable("id") String id) {
        try {
            String message = skillService.activateSkill(id);
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/skills/{id}/deactivate")
    public ResponseEntity<String> deactivateSkill(@PathVariable("id") String id) {
        try {
            String message = skillService.deactivateSkill(id);
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/skills/{id}")
    public ResponseEntity<String> deleteSkill(@PathVariable("id") String id) {
        try {
            String message = skillService.deleteSkill(id);
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
