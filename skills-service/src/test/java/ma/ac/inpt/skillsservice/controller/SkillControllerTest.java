package ma.ac.inpt.skillsservice.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ma.ac.inpt.skillsservice.config.SecurityTestConfig;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ma.ac.inpt.skillsservice.model.Skill;
import ma.ac.inpt.skillsservice.service.SkillService;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(SkillController.class)
@Import(SecurityTestConfig.class)
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
    public void testGetAllSkills_NoSkills() throws Exception {
        // Arrange
        when(skillService.getAllSkills(null)).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/skills"))
                .andExpect(status().isNoContent());

        // Verify
        verify(skillService, times(1)).getAllSkills(null);
    }
    @Test
    public void testGetAllSkills_SkillsExist() throws Exception {
        // Arrange
        List<Skill> skills = Arrays.asList(
                new Skill("1", "Skill 1", 0, true),
                new Skill("2", "Skill 2", 0, true)
        );
        when(skillService.getAllSkills(null)).thenReturn(skills);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/skills"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Skill 1")))
                .andExpect(jsonPath("$[0].usedCount", is(0)))
                .andExpect(jsonPath("$[0].isActivated", is(true)))
                .andExpect(jsonPath("$[1].name", is("Skill 2")))
                .andExpect(jsonPath("$[1].usedCount", is(0)))
                .andExpect(jsonPath("$[1].isActivated", is(true)));

        // Verify
        verify(skillService, times(1)).getAllSkills(null);
    }
    @Test
    public void testGetAllSkills_ExceptionThrown() throws Exception {
        // Arrange
        when(skillService.getAllSkills(null)).thenThrow(new RuntimeException());

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/skills"))
                .andExpect(status().isInternalServerError());

        // Verify
        verify(skillService, times(1)).getAllSkills(null);
    }

    // Test getSkillById
    @Test
    public void testGetSkillById_ExistingSkill() throws Exception {
        // Arrange
        String skillId = "13";
        Skill skill = new Skill(skillId, "Skill 1", 0, true);
        Optional<Skill> skillData = Optional.of(skill);

        when(skillService.getSkillById(skillId)).thenReturn(skillData);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/skills/{id}", skillId))
                .andExpect(status().isOk())
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
        mockMvc.perform(MockMvcRequestBuilders.get("/skills/{id}", skillId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Skill not found"));

        // Verify
        verify(skillService, times(1)).getSkillById(skillId);
    }

    // Test createSkill
    @Test
    public void createSkill_ValidSkill() throws Exception {
        // Arrange
        Skill skill = new Skill("1", "Passing", 0, true);

        Skill createdSkill = new Skill("1", "Passing", 0, true);

        when(skillService.createSkill(Mockito.any(Skill.class))).thenReturn(createdSkill);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/skills")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(skill)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Passing"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.usedCount").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isActivated").value(true));
    }

    // Test incrementSkill
    @Test
    public void testIncrementSkill_ValidId() throws Exception {
        // Arrange
        String skillId = "1";
        String message = "Skill not found";

        when(skillService.incrementSkill(skillId)).thenReturn(message);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/skills/{id}/increment", skillId))
                .andExpect(status().isNotFound())
                .andExpect(content().string(message));

        // Verify
        verify(skillService, times(1)).incrementSkill(skillId);
    }
    @Test
    public void testIncrementSkill_ExceptionThrown() throws Exception {
        // Arrange
        String skillId = "1";

        when(skillService.incrementSkill(skillId)).thenThrow(new RuntimeException());

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/skills/{id}/increment", skillId))
                .andExpect(status().isInternalServerError());

        // Verify
        verify(skillService, times(1)).incrementSkill(skillId);
    }

    // Test decrementSkill
    @Test
    public void testDecrementSkill_ValidId() throws Exception {
        // Arrange
        String skillId = "1";
        String message = "Skill not found";

        when(skillService.decrementSkill(skillId)).thenReturn(message);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/skills/{id}/decrement", skillId))
                .andExpect(status().isNotFound())
                .andExpect(content().string(message));

        // Verify
        verify(skillService, times(1)).decrementSkill(skillId);
    }

    @Test
    public void testDecrementSkill_ExceptionThrown() throws Exception {
        // Arrange
        String skillId = "1";

        when(skillService.decrementSkill(skillId)).thenThrow(new RuntimeException());

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/skills/{id}/decrement", skillId))
                .andExpect(status().isInternalServerError());

        // Verify
        verify(skillService, times(1)).decrementSkill(skillId);
    }

    // Test deleteSkill
    @Test
    public void testDeleteSkill_ValidId() throws Exception {
        // Arrange
        String skillId = "1";
        String message = "Skill not found";

        when(skillService.deleteSkill(skillId)).thenReturn(message);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/skills/{id}", skillId))
                .andExpect(status().isNotFound())
                .andExpect(content().string(message));

        // Verify
        verify(skillService, times(1)).deleteSkill(skillId);
    }

    @Test
    public void testDeleteSkill_ExceptionThrown() throws Exception {
        // Arrange
        String skillId = "1";

        when(skillService.deleteSkill(skillId)).thenThrow(new RuntimeException());

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/skills/{id}", skillId))
                .andExpect(status().isInternalServerError());

        // Verify
        verify(skillService, times(1)).deleteSkill(skillId);
    }
}
