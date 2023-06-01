package ma.ac.inpt.skillsservice.service;

import ma.ac.inpt.skillsservice.model.Skill;
import ma.ac.inpt.skillsservice.repository.SkillRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SkillServiceTest {
    @Mock
    private SkillRepository skillRepository;

    private SkillService skillService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        skillService = new SkillService(skillRepository);
    }

    // Test getAllSkills
    @Test
    void testGetAllSkills_WithNameIsNull() {
        // Arrange
        List<Skill> expectedSkills = new ArrayList<>();
        expectedSkills.add(new Skill("1", "Dribbling", 0, true));
        expectedSkills.add(new Skill("2", "Shooting", 3, true));
        when(skillRepository.findAll()).thenReturn(expectedSkills);

        // Act
        List<Skill> actualSkills = skillService.getAllSkills(null);

        // Assert
        assertEquals(expectedSkills, actualSkills);
        verify(skillRepository, times(1)).findAll();
        verify(skillRepository, never()).findByName(anyString());
    }
    @Test
    void testGetAllSkills_WithNameIsNotNull() {
        // Arrange
        String name = "Dribbling";
        List<Skill> expectedSkills = new ArrayList<>();
        expectedSkills.add(new Skill("1", "Dribbling", 0, true));
        when(skillRepository.findByName(name)).thenReturn(expectedSkills);

        // Act
        List<Skill> actualSkills = skillService.getAllSkills(name);

        // Assert
        assertEquals(expectedSkills, actualSkills);
        verify(skillRepository, never()).findAll();
        verify(skillRepository, times(1)).findByName(name);
    }

    // Test getSkillById
    @Test
    void testGetSkillById_ShouldReturnSkillById() {
        // Arrange
        String id = "123";
        Skill expectedSkill = new Skill(id, "Dribbling", 0, true);
        when(skillRepository.findById(id)).thenReturn(Optional.of(expectedSkill));

        // Act
        Optional<Skill> actualSkill = skillService.getSkillById(id);

        // Assert
        assertEquals(Optional.of(expectedSkill), actualSkill);
        verify(skillRepository, times(1)).findById(id);
    }
    @Test
    void testGetSkillById_WithInvalidId_ShouldReturnEmptyOptional() {
        // Arrange
        String id = "456";
        when(skillRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<Skill> actualSkill = skillService.getSkillById(id);

        // Assert
        assertEquals(Optional.empty(), actualSkill);
        verify(skillRepository, times(1)).findById(id);
    }

    // Test createSkill
    @Test
    void testCreateSkill() {
        // Arrange
        String skillName = "Dribbling";
        Skill originalSkill = new Skill(skillName, "Description", 0, true);
        Skill savedSkill = new Skill("123", skillName, 0, true);
        when(skillRepository.save(any(Skill.class))).thenReturn(savedSkill);

        // Act
        Skill createdSkill = skillService.createSkill(originalSkill);

        // Assert
        assertEquals(savedSkill, createdSkill);
        verify(skillRepository, times(1)).save(any(Skill.class));
    }

    // Test incrementSkill
    @Test
    void testIncrementSkill_ExistingSkill() {
        // Arrange
        String skillId = "123";
        Skill existingSkill = new Skill(skillId, "Dribbling", 5, true);
        when(skillRepository.findById(skillId)).thenReturn(Optional.of(existingSkill));

        // Act
        String result = skillService.incrementSkill(skillId);

        // Assert
        assertEquals("Skill Count incremented successfully", result);
        assertEquals(6, existingSkill.getUsedCount());
        verify(skillRepository, times(1)).findById(skillId);
        verify(skillRepository, times(1)).save(existingSkill);
    }
    @Test
    void testIncrementSkill_NonExistingSkill() {
        // Arrange
        String skillId = "456";
        when(skillRepository.findById(skillId)).thenReturn(Optional.empty());

        // Act
        String result = skillService.incrementSkill(skillId);

        // Assert
        assertEquals("Skill not found", result);
        verify(skillRepository, times(1)).findById(skillId);
        verify(skillRepository, never()).save(any(Skill.class));
    }

    // Test decrementSkill
    @Test
    void testDecrementSkill_ExistingSkillWithPositiveUsedCount() {
        // Arrange
        String skillId = "123";
        Skill existingSkill = new Skill(skillId, "Dribbling", 5, true);
        when(skillRepository.findById(skillId)).thenReturn(Optional.of(existingSkill));

        // Act
        String result = skillService.decrementSkill(skillId);

        // Assert
        assertEquals("Skill Count decremented successfully", result);
        assertEquals(4, existingSkill.getUsedCount());
        verify(skillRepository, times(1)).findById(skillId);
        verify(skillRepository, times(1)).save(existingSkill);
    }
    @Test
    void testDecrementSkill_ExistingSkillWithZeroUsedCount() {
        // Arrange
        String skillId = "456";
        Skill existingSkill = new Skill(skillId, "Shooting", 0, true);
        when(skillRepository.findById(skillId)).thenReturn(Optional.of(existingSkill));

        // Act
        String result = skillService.decrementSkill(skillId);

        // Assert
        assertEquals("Skill cannot be decremented", result);
        assertEquals(0, existingSkill.getUsedCount());
        verify(skillRepository, times(1)).findById(skillId);
        verify(skillRepository, never()).save(any(Skill.class));
    }
    @Test
    void testDecrementSkill_NonExistingSkill() {
        // Arrange
        String skillId = "789";
        when(skillRepository.findById(skillId)).thenReturn(Optional.empty());

        // Act
        String result = skillService.decrementSkill(skillId);

        // Assert
        assertEquals("Skill not found", result);
        verify(skillRepository, times(1)).findById(skillId);
        verify(skillRepository, never()).save(any(Skill.class));
    }

    // Test activateSkill
    @Test
    void testActivateSkill_ExistingSkill() {
        // Arrange
        String skillId = "123";
        Skill existingSkill = new Skill(skillId, "Dribbling", 5, false);
        when(skillRepository.findById(skillId)).thenReturn(Optional.of(existingSkill));

        // Act
        String result = skillService.activateSkill(skillId);

        // Assert
        assertEquals("Skill Activated successfully", result);
        assertTrue(existingSkill.getIsActivated());
        verify(skillRepository, times(1)).findById(skillId);
        verify(skillRepository, times(1)).save(existingSkill);
    }

    @Test
    void testActivateSkill_NonExistingSkill() {
        // Arrange
        String skillId = "456";
        when(skillRepository.findById(skillId)).thenReturn(Optional.empty());

        // Act
        String result = skillService.activateSkill(skillId);

        // Assert
        assertEquals("Skill not found", result);
        verify(skillRepository, times(1)).findById(skillId);
        verify(skillRepository, never()).save(any(Skill.class));
    }

    // Test deactivateSkill
    @Test
    void testDeactivateSkill_ExistingSkill_Success() {
        // Arrange
        String skillId = "123";
        Skill skill = new Skill(skillId, "Dribbling", 0, true);
        when(skillRepository.findById(skillId)).thenReturn(Optional.of(skill));

        // Act
        String result = skillService.deactivateSkill(skillId);

        // Assert
        verify(skillRepository, times(1)).findById(skillId);
        verify(skillRepository, times(1)).save(skill);
        assertEquals("Skill Deactivated successfully", result);
        assertEquals(false, skill.getIsActivated());
    }
    @Test
    void testDeactivateSkill_NonExistingSkill() {
        // Arrange
        String skillId = "123";
        when(skillRepository.findById(skillId)).thenReturn(Optional.empty());

        // Act
        String result = skillService.deactivateSkill(skillId);

        // Assert
        verify(skillRepository, times(1)).findById(skillId);
        verify(skillRepository, never()).save(any());
        assertEquals("Skill not found", result);
    }

    // Test deleteSkill
    @Test
    void testDeleteSkill_ExistingSkill() {
        // Arrange
        String skillId = "123";
        Skill existingSkill = new Skill(skillId, "Dribbling", 5, true);
        when(skillRepository.findById(skillId)).thenReturn(Optional.of(existingSkill));

        // Act
        String result = skillService.deleteSkill(skillId);

        // Assert
        assertEquals("Skill deleted successfully", result);
        verify(skillRepository, times(1)).findById(skillId);
        verify(skillRepository, times(1)).deleteById(skillId);
    }

    @Test
    void testDeleteSkill_NonExistingSkill() {
        // Arrange
        String skillId = "456";
        when(skillRepository.findById(skillId)).thenReturn(Optional.empty());

        // Act
        String result = skillService.deleteSkill(skillId);

        // Assert
        assertEquals("No skill found", result);
        verify(skillRepository, times(1)).findById(skillId);
        verify(skillRepository, never()).deleteById(any());
    }
}
