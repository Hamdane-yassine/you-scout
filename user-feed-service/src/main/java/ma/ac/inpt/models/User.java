package ma.ac.inpt.models;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;


@Data
@Builder
public class User {
    private UUID userId;
    private String username;
}