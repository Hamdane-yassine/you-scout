package ma.ac.inpt.skillsservice.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import ma.ac.inpt.skillsservice.model.Skill;

@ExtendWith(MockitoExtension.class)
@DataMongoTest
public class SkillsRepositoryTest {

    @Mock
    private SkillRepository skillRepositoryMock;

    @Autowired
    private SkillRepository skillRepository;

    @Test
    public void testFindByName() {
        // Arrange
        String name = "Java";
        Skill skill = new Skill("1", "Java", 0, true);
        List<Skill> skills = new ArrayList<>();
        skills.add(skill);

        when(skillRepositoryMock.findByName(name)).thenReturn(skills);

        // Act
        List<Skill> result = skillRepository.findByName(name);

        // Assert
        assertThat(result).isNotEmpty();
        assertThat(result).contains(skill);
    }

    @Test
    public void testFindByName_NotFound() {
        // Arrange
        String name = "Python";

        when(skillRepositoryMock.findByName(name)).thenReturn(new ArrayList<>());

        // Act
        List<Skill> result = skillRepository.findByName(name);

        // Assert
        assertThat(result).isEmpty();
    }
}
