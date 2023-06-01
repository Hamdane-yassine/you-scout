package ma.ac.inpt.authservice.mapper;

import ma.ac.inpt.authservice.dto.ProfilePictureUpdateResponse;
import ma.ac.inpt.authservice.dto.ProfileUpdateResponse;
import ma.ac.inpt.authservice.dto.UserDetailsDto;
import ma.ac.inpt.authservice.dto.UserUpdateResponse;
import ma.ac.inpt.authservice.model.Profile;
import ma.ac.inpt.authservice.model.Role;
import ma.ac.inpt.authservice.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    private final UserMapper mapper = UserMapper.INSTANCE;

    @Test
    @DisplayName("Should correctly map from User to UserDetailsDto")
    void userToUserDetailsDtoTest() {
        //given
        Role role = new Role(1L, "USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        User user = User.builder()
                .id(1L)
                .username("testUser")
                .email("test@test.com")
                .password("password")
                .isEnabled(true)
                .roles(roles)
                .build();
        //when
        UserDetailsDto userDetailsDto = mapper.userToUserDetailsDto(user);
        //then
        assertEquals(user.getEmail(), userDetailsDto.getEmail());
        assertEquals(user.getUsername(), userDetailsDto.getUsername());
        assertTrue(userDetailsDto.isEnabled());
        assertTrue(userDetailsDto.getRoles().contains("USER"));
    }

    @Test
    @DisplayName("Should correctly map from User to UserUpdateResponse")
    void userToUserUpdateResponseTest() {
        //given
        User user = User.builder()
                .id(1L)
                .username("testUser")
                .email("test@test.com")
                .password("password")
                .build();
        //when
        UserUpdateResponse response = mapper.userToUserUpdateResponse(user);
        //then
        assertEquals(user.getEmail(), response.getEmail());
        assertEquals(user.getUsername(), response.getUsername());
    }

    @Test
    @DisplayName("Should correctly map from Profile to ProfileUpdateResponse")
    void profileToProfileUpdateResponseTest() {
        //given
        Profile profile = Profile.builder()
                .fullName("John Doe")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .gender("Male")
                .country("USA")
                .cityOrRegion("New York")
                .bio("Developer")
                .socialMediaLinks(new HashMap<>())
                .build();
        //when
        ProfileUpdateResponse response = mapper.profileToProfileUpdateResponse(profile);
        //then
        assertEquals(profile.getFullName(), response.getFullName());
        assertEquals(profile.getDateOfBirth(), response.getDateOfBirth());
        assertEquals(profile.getGender(), response.getGender());
        assertEquals(profile.getCountry(), response.getCountry());
        assertEquals(profile.getCityOrRegion(), response.getCityOrRegion());
        assertEquals(profile.getBio(), response.getBio());
        assertEquals(profile.getSocialMediaLinks(), response.getSocialMediaLinks());
    }

    @Test
    @DisplayName("Should correctly map from User to ProfilePictureUpdateResponse")
    void userToProfilePictureUpdateResponseTest() {
        //given
        Profile profile = Profile.builder()
                .profilePicture("pictureUrl")
                .build();
        User user = User.builder()
                .id(1L)
                .username("testUser")
                .profile(profile)
                .build();
        //when
        ProfilePictureUpdateResponse response = mapper.userToProfilePictureUpdateResponse(user);
        //then
        assertEquals(user.getUsername(), response.getUsername());
        assertEquals(profile.getProfilePicture(), response.getProfilePictureUrl());
    }

    @Test
    @DisplayName("Should correctly map from Set of Role to List of role names")
    void rolesToRoleNamesTest() {
        //given
        Role role1 = new Role(1L, "USER");
        Role role2 = new Role(2L, "ADMIN");
        Set<Role> roles = new HashSet<>();
        roles.add(role1);
        roles.add(role2);
        //when
        List<String> roleNames = UserMapper.rolesToRoleNames(roles);
        //then
        assertTrue(roleNames.contains("USER"));
        assertTrue(roleNames.contains("ADMIN"));
    }
}

