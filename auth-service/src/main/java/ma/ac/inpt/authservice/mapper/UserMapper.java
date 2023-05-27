package ma.ac.inpt.authservice.mapper;

import ma.ac.inpt.authservice.dto.*;
import ma.ac.inpt.authservice.model.Profile;
import ma.ac.inpt.authservice.model.Role;
import ma.ac.inpt.authservice.model.User;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * The INSTANCE of the UserMapper which will be used to access the mapper methods.
     */
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);


    /**
     * Method used to map from Set of Role to List of role names (String).
     *
     * @param roles Set of Role entities
     * @return List of role names
     */
    @Named("rolesToRoleNames")
    static List<String> rolesToRoleNames(Collection<Role> roles) {
        return roles.stream()
                .map(Role::getRoleName)
                .collect(Collectors.toList());
    }

    /**
     * Map from User entity to UserDetailsDto.
     *
     * @param user User entity
     * @return UserDetailsDto mapped from the User entity
     */
    @Mapping(source = "profile.fullName", target = "fullName")
    @Mapping(source = "profile.profilePicture", target = "profilePicture")
    @Mapping(source = "profile.dateOfBirth", target = "dateOfBirth")
    @Mapping(source = "profile.gender", target = "gender")
    @Mapping(source = "profile.country", target = "country")
    @Mapping(source = "profile.cityOrRegion", target = "cityOrRegion")
    @Mapping(source = "profile.bio", target = "bio")
    @Mapping(source = "profile.socialMediaLinks", target = "socialMediaLinks")
    @Mapping(source = "roles", target = "roles", qualifiedByName = "rolesToRoleNames")
    @Mapping(target = "isEnabled", expression = "java(user.isEnabled())") // Map isEnabled property
    UserDetailsDto userToUserDetailsDto(User user);

    /**
     * Map from User entity to UserUpdateResponse.
     *
     * @param user User entity
     * @return UserUpdateResponse mapped from the User entity
     */
    UserUpdateResponse userToUserUpdateResponse(User user);

    /**
     * Map from Profile entity to ProfileUpdateResponse.
     *
     * @param profile Profile entity
     * @return ProfileUpdateResponse mapped from the Profile entity
     */
    ProfileUpdateResponse profileToProfileUpdateResponse(Profile profile);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "profile.profilePicture", target = "profilePictureUrl")
    ProfilePictureUpdateResponse userToProfilePictureUpdateResponse(User user);
}


