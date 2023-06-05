package ma.ac.inpt.postservice.service.media;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.ac.inpt.postservice.exception.DeleteFileException;
import ma.ac.inpt.postservice.exception.UploadFileException;
import ma.ac.inpt.postservice.exception.VideoValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static ma.ac.inpt.postservice.config.VideoConfig.MAX_VIDEO_SIZE;

/**
 * Service implementation for handling media storage using Amazon S3.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class S3MediaServiceImpl implements MediaService {

    private static final String FOLDER_NAME = "PostVideos/";
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
        validateVideoFile(file);
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

    public boolean isSupportedVideoFormat(String contentType, String filename) {
        if (contentType != null) {
            // Check the content type
            if (!contentType.startsWith("video/")) {
                return false;
            }
        }

        if (filename != null) {
            String extension = StringUtils.getFilenameExtension(filename);
            if (extension != null) {
                // Define a list of supported video formats
                List<String> supportedFormats = Arrays.asList("mp4", "avi", "mov", "mkv");
                return supportedFormats.contains(extension.toLowerCase());
            }
        }

        return false;
    }

    private void validateVideoFile(MultipartFile videoFile) {
        // Check the file size limit
        if (videoFile.getSize() > MAX_VIDEO_SIZE) {
            throw new VideoValidationException("Video file size exceeds the limit.");
        }

        // Check the file format by its content type or file extension
        if (!isSupportedVideoFormat(videoFile.getContentType(), videoFile.getOriginalFilename())) {
            throw new VideoValidationException("Unsupported video file format.");
        }

    }

//    private void generateVideoThumbnails(MultipartFile videoFile, String keyName) {
//        // Save the uploaded video to a temporary file
//        Path tempVideoPath;
//        try {
//            tempVideoPath = Files.createTempFile("temp_video_", videoFile.getOriginalFilename());
//            Files.copy(videoFile.getInputStream(), tempVideoPath, StandardCopyOption.REPLACE_EXISTING);
//        } catch (IOException e) {
//            throw new VideoProcessingException("Error saving the uploaded video to a temporary file.", e);
//        }
//
//        // Generate thumbnails using FFmpeg
//        try {
//            avformat.AVFormatContext formatContext = new avformat.AVFormatContext(null);
//
//            // Open the video file
//            if (avformat.avformat_open_input(formatContext, tempVideoPath.toString(), null, null) < 0) {
//                throw new VideoProcessingException("Error opening the video file for thumbnail generation.");
//            }
//
//            // Read video streams and find the first video stream
//            if (avformat.avformat_find_stream_info(formatContext, (Pointer) null) < 0) {
//                throw new VideoProcessingException("Error finding video stream information.");
//            }
//
//            int videoStreamIndex = -1;
//            for (int i = 0; i < formatContext.nb_streams(); i++) {
//                avutil.AVStream stream = formatContext.streams(i);
//                avcodec.AVCodecParameters codecParameters = stream.codecpar();
//
//                if (codecParameters.codec_type() == avutil.AVMEDIA_TYPE_VIDEO) {
//                    videoStreamIndex = i;
//                    break;
//                }
//            }
//
//            if (videoStreamIndex == -1) {
//                throw new VideoProcessingException("No video stream found in the video file.");
//            }
//
//            avutil.AVStream videoStream = formatContext.streams(videoStreamIndex);
//
//            // Set the time to capture the thumbnail (e.g., 5 seconds)
//            long timestamp = 5 * avutil.AV_TIME_BASE;
//
//            // Seek to the specified timestamp
//            avformat.av_seek_frame(formatContext, videoStreamIndex, timestamp, 0);
//
//            // Read the frame at the specified timestamp
//            avutil.AVFrame frame = avutil.av_frame_alloc();
//            avcodec.AVPacket packet = avcodec.av_packet_alloc();
//            while (avformat.av_read_frame(formatContext, packet) >= 0) {
//                if (packet.stream_index() == videoStreamIndex) {
//                    // Decode the video frame
//                    avcodec.avcodec_send_packet(videoStream.codec(), packet);
//                    avcodec.avcodec_receive_frame(videoStream.codec(), frame);
//
//                    // Process the video frame (e.g., save it as a thumbnail image)
//                    processVideoFrame(frame, keyName);
//
//                    break;  // Stop after processing the first frame
//                }
//
//                avcodec.av_packet_unref(packet);
//            }
//
//            // Free allocated resources
//            avcodec.av_packet_free(packet);
//            avutil.av_frame_free(frame);
//            avformat.avformat_close_input(formatContext);
//        } catch (Exception e) {
//            throw new VideoProcessingException("Error generating video thumbnails.", e);
//        } finally {
//            // Delete the temporary video file
//            try {
//                Files.delete(tempVideoPath);
//            } catch (IOException e) {
//                // Handle the exception (e.g., log a warning) if the file deletion fails
//            }
//        }
//    }

//    private void processVideoFrame(avutil.AVFrame frame, String keyName) {
//        // Perform thumbnail processing on the video frame
//        // This could involve resizing, cropping, or saving the frame as an image
//
//        // Example: Save the frame as an image file
//        String thumbnailFileName = keyName + "_thumbnail.jpg";
//        String thumbnailFilePath = THUMBNAIL_FOLDER + thumbnailFileName;
//
//        avutil.AVFrameRGB frameRGB = avutil.av_frame_alloc();
//        avutil.av_image_alloc(frameRGB.data(), frameRGB.linesize(), frame.width(), frame.height(), avutil.AV_PIX_FMT_RGB24, 1);
//
//        // Convert the video frame to RGB format
//        avutil.SwsContext swsContext = avutil.sws_getContext(
//                frame.width(), frame.height(), frame.format(),
//                frame.width(), frame.height(), avutil.AV_PIX_FMT_RGB24,
//                avutil.SWS_BICUBIC, null, null, (DoublePointer) null);
//
//        avutil.sws_scale(swsContext, frame.data(), frame.linesize(), 0, frame.height(), frameRGB.data(), frameRGB.linesize());
//
//        // Save the RGB frame as an image file (e.g., JPEG)
//        saveFrameAsImage(frameRGB, thumbnailFilePath);
//
//        // Free allocated resources
//        avutil.av_frame_free(frameRGB);
//        avutil.sws_freeContext(swsContext);
//    }

//    private void saveFrameAsImage(avutil.AVFrameRGB frameRGB, String filePath) {
//        // Save the frame as an image file (e.g., JPEG) using an image processing library or tool
//        // You can use libraries like OpenCV, ImageIO, or ImageMagick to perform this task
//
//        // Example: Save the frame as a JPEG image using ImageIO
//        BufferedImage image = new BufferedImage(frameRGB.width(), frameRGB.height(), BufferedImage.TYPE_INT_RGB);
//
//        int[] rgbData = new int[frameRGB.width() * frameRGB.height()];
//        for (int y = 0; y < frameRGB.height(); y++) {
//            for (int x = 0; x < frameRGB.width(); x++) {
//                int index = y * frameRGB.width() + x;
//                int r = frameRGB.data(0).getByte(index * 3) & 0xFF;
//                int g = frameRGB.data(0).getByte(index * 3 + 1) & 0xFF;
//                int b = frameRGB.data(0).getByte(index * 3 + 2) & 0xFF;
//
//                rgbData[index] = (r << 16) | (g << 8) | b;
//            }
//        }
//
//        image.setRGB(0, 0, frameRGB.width(), frameRGB.height(), rgbData, 0, frameRGB.width());
//
//        try {
//            ImageIO.write(image, "JPEG", new File(filePath));
//        } catch (IOException e) {
//            throw new VideoProcessingException("Error saving the video frame as an image file.", e);
//        }
//    }
}

