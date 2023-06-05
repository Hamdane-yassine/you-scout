package ma.ac.inpt.postservice;

import ma.ac.inpt.exceptions.UnableToGetUsersException;
import ma.ac.inpt.feignClient.AuthClient;
import ma.ac.inpt.payload.UserSummary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {
    @Mock
    private AuthClient authClient;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authService = new AuthService(authClient);
    }

    @Test
    void usersProfilePic_ReturnsUserProfilePicMap() {
        // Arrange
        List<String> usernames = Arrays.asList("user1", "user2", "user3");
        UserSummary user1 = UserSummary.builder()
                .username("user1")
                .profilePicture("pic1")
                .build();
        UserSummary user2 = UserSummary.builder()
                .username("user2")
                .profilePicture("pic2")
                .build();
        UserSummary user3 = UserSummary.builder()
                .username("user3")
                .profilePicture("pic3")
                .build();
        List<UserSummary> userSummaries = Arrays.asList(user1, user2, user3);
        ResponseEntity<List<UserSummary>> response =
                new ResponseEntity<>(userSummaries, HttpStatus.OK);

        when(authClient.findByUsernameIn(usernames)).thenReturn(response);

        // Act
        Map<String, String> result = authService.usersProfilePic(usernames);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("pic1", result.get("user1"));
        assertEquals("pic2", result.get("user2"));
        assertEquals("pic3", result.get("user3"));

        verify(authClient, times(1)).findByUsernameIn(usernames);
        verifyNoMoreInteractions(authClient);
    }

    @Test
    void usersProfilePic_ThrowsException_WhenStatusCodeIsNotSuccessful() {
        // Arrange
        List<String> usernames = Arrays.asList("user1", "user2", "user3");
        ResponseEntity<List<UserSummary>> response =
                new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        when(authClient.findByUsernameIn(usernames)).thenReturn(response);

        // Act & Assert
        UnableToGetUsersException exception = assertThrows(
                UnableToGetUsersException.class,
                () -> authService.usersProfilePic(usernames)
        );

        assertEquals("unable to get user summaries 500 INTERNAL_SERVER_ERROR", exception.getMessage());

        verify(authClient, times(1)).findByUsernameIn(usernames);
        verifyNoMoreInteractions(authClient);
    }
}
