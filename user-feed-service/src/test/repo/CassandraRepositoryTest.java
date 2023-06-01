package repo;

import lombok.RequiredArgsConstructor;
import ma.ac.inpt.FeedServiceApplication;
import ma.ac.inpt.UserFeedEntity;
import ma.ac.inpt.repo.Cassandra;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.cassandra.DataCassandraTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataCassandraTest
@RequiredArgsConstructor
@ContextConfiguration
public class CassandraRepositoryTest {

    private final Cassandra cassandraRepository;

    @Test
    public void testFindByUsername() {
        // Create some test data
        String username = "testUser";
        UserFeedEntity entity1 = UserFeedEntity.builder()
                .userId(UUID.randomUUID())
                .username(username)
                .postId("postId1")
                .createdAt(Instant.now())
                .build();

        UserFeedEntity entity2 = UserFeedEntity.builder()
                .userId(UUID.randomUUID())
                .username(username)
                .postId("postId2")
                .createdAt(Instant.now())
                .build();
        cassandraRepository.save(entity1);
        cassandraRepository.save(entity2);

        // Perform the query
        Pageable pageable = PageRequest.of(0, 10);
        Slice<UserFeedEntity> result = cassandraRepository.findByUsername(username, pageable);

        // Verify the results
        assertTrue(result.hasContent());
        assertEquals(2, result.getContent().size());
        assertEquals(entity1, result.getContent().get(0));
        assertEquals(entity2, result.getContent().get(1));
    }
}
