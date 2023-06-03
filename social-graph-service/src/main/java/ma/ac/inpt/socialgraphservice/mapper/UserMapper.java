package ma.ac.inpt.socialgraphservice.mapper;

import ma.ac.inpt.socialgraphservice.model.User;
import ma.ac.inpt.socialgraphservice.payload.UserEventPayload;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Mapper interface for mapping User and UserEventPayload objects.
 */
@Mapper
public interface UserMapper {

    /**
     * The instance of the UserMapper.
     */
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    /**
     * Maps a UserEventPayload object to a User object.
     *
     * @param userEventPayload the UserEventPayload object
     * @return the mapped User object
     */
    @Mapping(target = "following", ignore = true)
    @Mapping(target = "followers", ignore = true)
    @Mapping(target = "blockedUsers", ignore = true)
    User userEventPayloadToUser(UserEventPayload userEventPayload);

    /**
     * Maps a User object to a UserEventPayload object.
     *
     * @param user the User object
     * @return the mapped UserEventPayload object
     */
    UserEventPayload userToUserEventPayload(User user);
}


