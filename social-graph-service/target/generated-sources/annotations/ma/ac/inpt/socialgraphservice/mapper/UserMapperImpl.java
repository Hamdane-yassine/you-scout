package ma.ac.inpt.socialgraphservice.mapper;

import javax.annotation.processing.Generated;
import ma.ac.inpt.socialgraphservice.model.User;
import ma.ac.inpt.socialgraphservice.payload.UserEventPayload;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-05-29T17:54:16+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
public class UserMapperImpl implements UserMapper {

    @Override
    public User userEventPayloadToUser(UserEventPayload userEventPayload) {
        if ( userEventPayload == null ) {
            return null;
        }

        User user = new User();

        user.setId( userEventPayload.getId() );
        user.setUsername( userEventPayload.getUsername() );

        return user;
    }

    @Override
    public UserEventPayload userToUserEventPayload(User user) {
        if ( user == null ) {
            return null;
        }

        UserEventPayload userEventPayload = new UserEventPayload();

        userEventPayload.setId( user.getId() );
        userEventPayload.setUsername( user.getUsername() );

        return userEventPayload;
    }
}
