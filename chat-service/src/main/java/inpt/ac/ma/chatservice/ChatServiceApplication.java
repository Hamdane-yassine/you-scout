package inpt.ac.ma.chatservice;

import inpt.ac.ma.chatservice.repo.RoomRepo;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class ChatServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatServiceApplication.class, args);
    }

}
