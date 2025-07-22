package zim.tave.memory.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import zim.tave.memory.service.S3Uploader;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class FileUploadController {

    private final S3Uploader s3Uploader;
    private static final long MAX_FILE_SIZE = 500 * 1024 * 1024; // 500MB
    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
            "image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp", "image/heic"
    );
    private static final List<String> ALLOWED_AUDIO_TYPES = Arrays.asList(
            "audio/mpeg", "audio/mp3", "audio/wav", "audio/m4a", "audio/mp4", "audio/x-m4a", "audio/aac", "audio/ogg"
    );
    
    private static final List<String> ALLOWED_IMAGE_EXTENSIONS = Arrays.asList(
            ".jpg", ".jpeg", ".png", ".gif", ".webp", ".heic"
    );
    private static final List<String> ALLOWED_AUDIO_EXTENSIONS = Arrays.asList(
            ".mp3", ".wav", ".m4a", ".aac", ".ogg"
    );

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @RequestParam("type") String type) {
        try {
            // 파일 존재 확인
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("파일이 비어있습니다.");
            }
            
            // 파일 크기 검증
            if (file.getSize() > MAX_FILE_SIZE) {
                return ResponseEntity.badRequest()
                        .body("파일 크기가 500MB를 초과합니다. 현재 파일 크기: " + 
                              (file.getSize() / 1024 / 1024) + "MB");
            }
            
            // 파일 타입 검증 (Content-Type과 확장자 모두 확인)
            String contentType = file.getContentType();
            String fileName = file.getOriginalFilename();
            String fileExtension = "";
            
            if (fileName != null && fileName.contains(".")) {
                fileExtension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
            }
            
            if ("images".equals(type)) {
                boolean validContentType = contentType != null && ALLOWED_IMAGE_TYPES.contains(contentType);
                boolean validExtension = ALLOWED_IMAGE_EXTENSIONS.contains(fileExtension);
                
                if (!validContentType && !validExtension) {
                    return ResponseEntity.badRequest()
                            .body("지원하지 않는 이미지 형식입니다. 지원 형식: JPG, PNG, GIF, WebP, HEIC (현재: " + contentType + ", " + fileExtension + ")");
                }
            }
            
            if ("audio".equals(type)) {
                boolean validContentType = contentType != null && ALLOWED_AUDIO_TYPES.contains(contentType);
                boolean validExtension = ALLOWED_AUDIO_EXTENSIONS.contains(fileExtension);
                
                if (!validContentType && !validExtension) {
                    return ResponseEntity.badRequest()
                            .body("지원하지 않는 오디오 형식입니다. 지원 형식: MP3, WAV, M4A, AAC, OGG (현재: " + contentType + ", " + fileExtension + ")");
                }
            }
            
            String url = s3Uploader.upload(file, type);
            return ResponseEntity.ok(url);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("업로드 실패: " + e.getMessage());
        }
    }
}
