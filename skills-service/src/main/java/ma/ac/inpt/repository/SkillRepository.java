package ma.ac.inpt.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import ma.ac.inpt.model.Skill;

public interface SkillRepository extends MongoRepository<Skill, String> {
    List<Skill> findByName(String title);
}