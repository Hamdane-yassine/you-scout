package ma.ac.inpt.postservice.service.media;

import org.springframework.web.multipart.MultipartFile;

/**
 * An interface for services that handle media files such as images, videos, and audio.
 */
public interface MediaService {

    /**
     * Uploads a file to the storage system and returns the file URL.
     *
     * @param file The file to be uploaded
     * @return The URL of the uploaded file
     */
    String uploadFile(MultipartFile file);

    /**
     * Deletes a file from the storage system.
     *
     * @param fileUrl The URL of the file to be deleted
     */
    void deleteFile(String fileUrl);
}



