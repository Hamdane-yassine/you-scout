package inpt.ac.ma.chatservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.repository.config.EnableReactiveCouchbaseRepositories;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableReactiveCouchbaseRepositories(basePackages = "inpt.ac.ma.chatservice.repo")
public class couchbaseConfig extends AbstractCouchbaseConfiguration {



    @Override
    public String getConnectionString() {
        return "localhost";
    }

    @Override
    public String getUserName() {
        return "aseds";
    }

    @Override
    public String getPassword() {
        return "asedsaseds";
    }

    @Override
    public String getBucketName() {
        return "chat";
    }


}
