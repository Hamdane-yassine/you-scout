package ma.ac.inpt.service;

import ma.ac.inpt.model.Skill;
import ma.ac.inpt.repository.SkillRepository;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SkillService {
    final private SkillRepository skillRepository;

    @Autowired
    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public List<Skill> getAllSkills(String name) {
        List<Skill> skills = new ArrayList<>();
        if (name == null)
            skillRepository.findAll().forEach(skills::add);
        else
            skillRepository.findByName(name).forEach(skills::add);
        return skills;
    }

    public Optional<Skill> getSKillById(String id) {
        Optional<Skill> skillData = skillRepository.findById(id);
        return skillData;
    }

    public Skill createSkill(@NotNull Skill skill) {
        ObjectId objectId = new ObjectId();
        String ID = objectId.toHexString();
        Skill newSkill = skillRepository.save(new Skill(ID, skill.getName(), 0, true));
        return newSkill;
    }

    public String incrementSkill(String id) {
        Optional<Skill> skillData = skillRepository.findById(id);

        if (skillData.isPresent()) {
            Skill skill = skillData.get();
            skill.setUsedCount(skill.getUsedCount() + 1);
            skillRepository.save(skill);
            return "Skill Count incremented successfully";
        } else {
            return "Skill not found";
        }
    }

    public String decrementSkill(String id) {
        Optional<Skill> skillData = skillRepository.findById(id);

        if (skillData.isPresent()) {
            Skill skill = skillData.get();
            if (skill.getUsedCount() > 0) {
                skill.setUsedCount(skill.getUsedCount() - 1);
                skillRepository.save(skill);
                return "Skill Count decremented successfully";
            } else {
                return "Skill cannot be decremented";
            }
        } else {
            return "Skill not found";
        }
    }

    public String activateSkill(String id) {
        Optional<Skill> skillData = skillRepository.findById(id);

        if (skillData.isPresent()) {
            Skill skill = skillData.get();
            skill.setIsActivated(true);
            skillRepository.save(skill);
            return "Skill Activated successfully";
        } else {
            return "Skill not found";
        }
    }

    public String deactivateSkill(String id) {
        Optional<Skill> skillData = skillRepository.findById(id);

        if (skillData.isPresent()) {
            Skill skill = skillData.get();
            skill.setIsActivated(false);
            skillRepository.save(skill);
            return "Skill Deactivated successfully";
        } else {
            return "Skill not found";
        }
    }

    public String deleteSkill(String id) {
        Optional<Skill> skillData = skillRepository.findById(id);
        if (skillData.isEmpty()) {
            return "No skill found";
        } else {
            skillRepository.deleteById(id);
            return "Skill deleted successfully";
        }
    }
}
