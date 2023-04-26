package ma.ac.inpt.authservice.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import ma.ac.inpt.authservice.exception.DeleteFileException;
import ma.ac.inpt.authservice.exception.UploadFileException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service implements MediaService {

    private static final String FOLDER_NAME = "ProfilePictures/";
    private final AmazonS3 s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Override
    public String uploadFile(MultipartFile file) {
        String keyName = FOLDER_NAME + UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            PutObjectRequest request = new PutObjectRequest(bucketName, keyName, file.getInputStream(), metadata);
            s3Client.putObject(request);
            s3Client.setObjectAcl(bucketName, keyName, CannedAccessControlList.PublicRead);
            return s3Client.getUrl(bucketName, keyName).toString();
        } catch (IOException e) {
            throw new UploadFileException("Error uploading file to S3: " + e.getMessage());
        }
    }

    @Override
    public void deleteFile(String fileUrl) {
        try {
            String keyName = new URL(fileUrl).getPath().substring(1);
            s3Client.deleteObject(new DeleteObjectRequest(bucketName, keyName));
        } catch (MalformedURLException e) {
            throw new DeleteFileException("Error deleting file from S3: " + e.getMessage());
        }
    }

}

