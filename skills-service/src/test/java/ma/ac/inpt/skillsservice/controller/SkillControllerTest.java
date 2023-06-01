package ma.ac.inpt.skillsservice.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ma.ac.inpt.skillsservice.model.Skill;
import ma.ac.inpt.skillsservice.service.SkillService;

@WebMvcTest(SkillController.class)
public class SkillControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SkillService skillService;

    // Object to String
    private static String asJsonString(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    // Test getAllSkills
    @Test
    public void testGetAllSkill() throws Exception {
        // Arrange
        Skill skill1 = new Skill("1", "Skill 1", 0, true);
        Skill skill2 = new Skill("2", "Skill 2", 0, true);
        List<Skill> skills = Arrays.asList(skill1, skill2);

        when(skillService.getAllSkills(null)).thenReturn(skills);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/skills"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is("1")))
                .andExpect(jsonPath("$[0].name", is("Skill 1")))
                .andExpect(jsonPath("$[0].usedCount", is(0)))
                .andExpect(jsonPath("$[0].isActivated", is(true)))
                .andExpect(jsonPath("$[1].id", is("2")))
                .andExpect(jsonPath("$[1].name", is("Skill 2")))
                .andExpect(jsonPath("$[1].usedCount", is(0)))
                .andExpect(jsonPath("$[1].isActivated", is(true)));

        // Verify
        verify(skillService, times(1)).getAllSkills(null);
    }

    // Test getSkillById
    @Test
    public void testGetSkillById_ExistingSkill() throws Exception {
        // Arrange
        String skillId = "1";
        Skill skill = new Skill(skillId, "Skill 1", 0, true);
        Optional<Skill> skillData = Optional.of(skill);

        when(skillService.getSkillById(skillId)).thenReturn(skillData);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/skills/{id}", skillId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(skillId)))
                .andExpect(jsonPath("$.name", is("Skill 1")))
                .andExpect(jsonPath("$.usedCount", is(0)))
                .andExpect(jsonPath("$.isActivated", is(true)));

        // Verify
        verify(skillService, times(1)).getSkillById(skillId);
    }
    @Test
    public void testGetSkillById_NonExistingSkill() throws Exception {
        // Arrange
        String skillId = "1";
        Optional<Skill> skillData = Optional.empty();

        when(skillService.getSkillById(skillId)).thenReturn(skillData);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/skills/{id}", skillId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Skill not found"));

        // Verify
        verify(skillService, times(1)).getSkillById(skillId);
    }

    // Test createSkill
    @Test
    public void testCreateSkill_ValidSkill() throws Exception {
        // Arrange
        Skill skill = new Skill("1", "Skill 1", 0, true);
        Skill createdSkill = new Skill("1", "Skill 1", 0, true);

        when(skillService.createSkill(skill)).thenReturn(createdSkill);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/skills")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(skill)))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.id", is("1")))
                        .andExpect(jsonPath("$.name", is("Skill 1")))
                        .andExpect(jsonPath("$.usedCount", is(0)))
                        .andExpect(jsonPath("$.isActivated", is(true)));

        // Verify
        verify(skillService, times(1)).createSkill(skill);
    }
    @Test
    public void testCreateSkill_InvalidSkill() throws Exception {
        // Arrange
        Skill skill = new Skill(); // Invalid skill with missing fields

        when(skillService.createSkill(skill)).thenThrow(new IllegalArgumentException());

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/skills")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(skill)))
                        .andExpect(status().isInternalServerError());

        // Verify
        verify(skillService, times(1)).createSkill(skill);
    }

    // Test incrementSkill

    // Test decrementSkill
}
