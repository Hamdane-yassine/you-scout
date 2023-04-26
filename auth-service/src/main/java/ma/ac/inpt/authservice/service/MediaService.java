package ma.ac.inpt.authservice.service;

import org.springframework.web.multipart.MultipartFile;

public interface MediaService {
    String uploadFile(MultipartFile file);
    void deleteFile(String fileUrl);
}


