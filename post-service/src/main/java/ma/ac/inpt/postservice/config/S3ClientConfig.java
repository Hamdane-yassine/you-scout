package ma.ac.inpt.postservice.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up the Amazon S3 client used for storing and retrieving files.
 */
@Configuration
public class S3ClientConfig {

    /**
     * The access key for the AWS account used for connecting to S3.
     */
    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    /**
     * The secret key for the AWS account used for connecting to S3.
     */
    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    /**
     * The region where the S3 bucket is located.
     */
    @Value("${cloud.aws.region.static}")
    private String region;

    /**
     * Creates a new instance of the Amazon S3 client.
     *
     * @return the S3 client instance
     */
    @Bean
    public AmazonS3 s3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region)
                .build();
    }
}


