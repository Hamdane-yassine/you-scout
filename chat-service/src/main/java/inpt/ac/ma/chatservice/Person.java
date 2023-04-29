package inpt.ac.ma.chatservice;

import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;

import java.util.UUID;

@Document
public class Person {
    @Id
    private UUID id;
    private String lastName;

    //getters and setters
}