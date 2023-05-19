package ma.ac.inpt.socialgraphservice.mapper;

import ma.ac.inpt.socialgraphservice.model.User;
import ma.ac.inpt.socialgraphservice.payload.UserEventPayload;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "following", ignore = true)
    @Mapping(target = "followers", ignore = true)
    @Mapping(target = "blockedUsers", ignore = true)
    User userEventPayloadToUser(UserEventPayload userEventPayload);

    UserEventPayload userToUserEventPayload(User user);
}


