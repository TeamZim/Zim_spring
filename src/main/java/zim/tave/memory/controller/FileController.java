package zim.tave.memory.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

@RestController
@RequiredArgsConstructor
public class FileController {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @GetMapping("/api/files")
    public ResponseEntity<InputStreamResource> getFile(@RequestParam String key) {
        try {
            System.out.println("FileController - Requested key: " + key);
            System.out.println("FileController - Bucket: " + bucket);
            
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();

            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
            GetObjectResponse response = s3Object.response();

            HttpHeaders headers = new HttpHeaders();
            String contentType = response.contentType();
            String fileName = key.substring(key.lastIndexOf("/") + 1);
            
            // 오디오 파일의 경우 브라우저에서 바로 재생되도록 Content-Type 설정
            if (key.toLowerCase().contains("audio/") || key.toLowerCase().endsWith(".m4a")) {
                headers.setContentType(MediaType.parseMediaType("audio/mp4"));
                headers.add("Content-Disposition", "inline; filename=\"" + fileName + "\"");
                System.out.println("FileController - Audio file detected, setting Content-Type: audio/mp4");
            }
            // HEIF 파일의 경우 브라우저 호환성을 위해 적절한 Content-Type 설정
            else if (key.toLowerCase().contains(".heic") || 
                (contentType != null && contentType.contains("heif"))) {
                headers.setContentType(MediaType.parseMediaType("image/heic"));
                // HEIF는 브라우저 지원이 제한적이므로 다운로드 유도
                headers.add("Content-Disposition", "inline; filename=\"" + fileName + "\"");
            } else if (contentType != null) {
                headers.setContentType(MediaType.parseMediaType(contentType));
            } else {
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            }
            
            headers.setContentLength(response.contentLength());

            System.out.println("FileController - File found successfully: " + key);
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new InputStreamResource(s3Object));

        } catch (Exception e) {
            System.out.println("FileController - Error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
} 