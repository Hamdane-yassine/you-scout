package ma.ac.inpt.authservice.service.media;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.ac.inpt.authservice.exception.file.DeleteFileException;
import ma.ac.inpt.authservice.exception.file.UploadFileException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

/**
 * Service implementation for handling media storage using Amazon S3.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class S3MediaServiceImpl implements MediaService {

    private static final String FOLDER_NAME = "ProfilePictures/";
    private final AmazonS3 s3Client; // Amazon S3 client for managing storage operations

    @Value("${aws.s3.bucket}")
    private String bucketName; // AWS S3 bucket name

    /**
     * Uploads a file to the S3 bucket and returns the file URL.
     *
     * @param file the file to be uploaded
     * @return the URL of the uploaded file
     */
    @Override
    public String uploadFile(MultipartFile file) {
        String keyName = FOLDER_NAME + UUID.randomUUID() + "_" + file.getOriginalFilename();
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());
            PutObjectRequest request = new PutObjectRequest(bucketName, keyName, file.getInputStream(), metadata);
            s3Client.putObject(request);
            s3Client.setObjectAcl(bucketName, keyName, CannedAccessControlList.PublicRead);
            String fileUrl = s3Client.getUrl(bucketName, keyName).toString();
            log.info("File uploaded to S3: {}", fileUrl);
            return fileUrl;
        } catch (IOException e) {
            log.error("Error uploading file to S3: {}", e.getMessage(), e);
            throw new UploadFileException("Error uploading file to S3: " + e.getMessage());
        }
    }

    /**
     * Deletes a file from the S3 bucket using the provided file URL.
     *
     * @param fileUrl the URL of the file to be deleted
     */
    @Override
    public void deleteFile(String fileUrl) {
        try {
            String keyName = new URL(fileUrl).getPath().substring(1);
            s3Client.deleteObject(new DeleteObjectRequest(bucketName, keyName));
            log.info("File deleted from S3: {}", fileUrl);
        } catch (MalformedURLException e) {
            log.error("Error deleting file from S3: {}", e.getMessage(), e);
            throw new DeleteFileException("Error deleting file from S3: " + e.getMessage());
        }
    }
}

