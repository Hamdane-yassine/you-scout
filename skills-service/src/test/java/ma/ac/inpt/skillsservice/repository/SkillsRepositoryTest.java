//package ma.ac.inpt.skillsservice.repository;
//
//import ma.ac.inpt.skillsservice.model.Skill;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DataMongoTest
//@ExtendWith(SpringExtension.class)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@ContextConfiguration(classes = {SkillsRepositoryTest.class })
//public class SkillsRepositoryTest {
//
//    @Autowired
//    private SkillRepository skillRepository;
//
//    @MockBean
//    private SkillRepository mockSkillRepository;
//
//    @Test
//    public void testFindByName() {
//        // given
//        Skill skill1 = new Skill("1", "Java", 10, true);
//        Skill skill2 = new Skill("2", "Python", 5, false);
//        List<Skill> expectedSkills = Arrays.asList(skill1, skill2);
//
//        Mockito.when(mockSkillRepository.findByName("Java"))
//                .thenReturn(expectedSkills);
//
//        // when
//        List<Skill> skills = skillRepository.findByName("Java");
//
//        // then
//        assertThat(skills).isEqualTo(expectedSkills);
//    }
//
//    @Test
//    public void testSave() {
//        // given
//        Skill skill = new Skill("1", "Java", 10, true);
//
//        // when
//        skillRepository.save(skill);
//
//        // then
//        Optional<Skill> savedSkill = skillRepository.findById("1");
//        assertThat(savedSkill).isPresent();
//        assertThat(savedSkill.get()).isEqualTo(skill);
//    }
//}
