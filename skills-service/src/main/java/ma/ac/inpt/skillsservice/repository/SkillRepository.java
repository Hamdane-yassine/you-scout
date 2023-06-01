package ma.ac.inpt.skillsservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import ma.ac.inpt.skillsservice.model.Skill;

public interface SkillRepository extends MongoRepository<Skill, String> {
    List<Skill> findByName(String title);
}