package ma.ac.inpt.authservice.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import ma.ac.inpt.authservice.service.media.S3MediaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class S3MediaServiceTest {

    @InjectMocks
    private S3MediaServiceImpl s3MediaService;

    @Mock
    private AmazonS3 s3Client;

    private static final String BUCKET_NAME = "bucket";
    private static final String FOLDER_NAME = "test/";

    @BeforeEach
    void setUp() {
        // Set the bucket name on the S3MediaService instance using reflection
        ReflectionTestUtils.setField(s3MediaService, "bucketName", BUCKET_NAME);
    }

    @Test
    @DisplayName("Test File Upload")
    void testUploadFile() throws IOException {
        // Create a mock file to be uploaded
        MultipartFile mockFile = new MockMultipartFile("file", "Hello, World!".getBytes());

        // Define a mock URL that the S3 client should return after file upload
        String mockFileUrl = "https://bucket.s3.region.amazonaws.com/" + FOLDER_NAME + UUID.randomUUID() + "_file";

        // When the S3 client is asked to put a file, return a successful result
        when(s3Client.putObject(any(PutObjectRequest.class))).thenReturn(new PutObjectResult());

        // When the S3 client is asked to get the URL of the uploaded file, return the mock URL
        when(s3Client.getUrl(eq(BUCKET_NAME), any())).thenReturn(new URL(mockFileUrl));

        // Call the method under test
        String uploadedUrl = s3MediaService.uploadFile(mockFile);

        // Verify that the S3 client was asked to put the file, get its URL, and set its access control list
        verify(s3Client, times(1)).putObject(any(PutObjectRequest.class));
        verify(s3Client, times(1)).getUrl(anyString(), anyString());
        verify(s3Client, times(1)).setObjectAcl(anyString(), anyString(), any(CannedAccessControlList.class));

        // Assert that the returned URL matches the mock URL
        assertEquals(mockFileUrl, uploadedUrl);
    }

    @Test
    @DisplayName("Test File Deletion")
    void testDeleteFile() {
        // Define a mock URL that the S3 client should delete
        String mockFileUrl = "https://bucket.s3.region.amazonaws.com/" + FOLDER_NAME + UUID.randomUUID() + "_file";

        // Call the method under test
        s3MediaService.deleteFile(mockFileUrl);

        // Verify that the S3 client was asked to delete the file
        verify(s3Client, times(1)).deleteObject(any(DeleteObjectRequest.class));
    }
}


