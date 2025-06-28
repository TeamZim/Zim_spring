package zim.tave.memory.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.*;

import zim.tave.memory.domain.DiaryImage;

@ExtendWith(MockitoExtension.class)
class ImageUploadIntegrationTest {

    @Test
    void S3_이미지_URL_테스트() {
        // given
        String s3ImageUrl = "https://test-bucket.s3.ap-northeast-2.amazonaws.com/images/test.jpg";

        // when
        DiaryImage image = new DiaryImage();
        image.setImageUrl(s3ImageUrl);
        image.setCameraType(DiaryImage.CameraType.FRONT);
        image.setRepresentative(true);

        // then
        assertThat(image.getImageUrl()).isEqualTo(s3ImageUrl);
        assertThat(image.getCameraType()).isEqualTo(DiaryImage.CameraType.FRONT);
        assertThat(image.isRepresentative()).isTrue();
        assertThat(image.getImageUrl()).contains("s3.ap-northeast-2.amazonaws.com");
    }

    @Test
    void 더미_이미지_URL_테스트() {
        // given
        String dummyUrl = "https://example.com/images/dummy.jpg";
        
        // when
        DiaryImage image = new DiaryImage();
        image.setImageUrl(dummyUrl);
        
        // then
        assertThat(image.getImageUrl()).isEqualTo(dummyUrl);
        assertThat(image.getImageUrl()).startsWith("https://");
    }

    @Test
    void 로컬_파일_URL_테스트() {
        // given
        String localUrl = "file:///tmp/test-image.jpg";
        
        // when
        DiaryImage image = new DiaryImage();
        image.setImageUrl(localUrl);
        
        // then
        assertThat(image.getImageUrl()).isEqualTo(localUrl);
        assertThat(image.getImageUrl()).startsWith("file://");
    }

    @Test
    void 상대_경로_URL_테스트() {
        // given
        String relativeUrl = "/images/test.jpg";
        
        // when
        DiaryImage image = new DiaryImage();
        image.setImageUrl(relativeUrl);
        
        // then
        assertThat(image.getImageUrl()).isEqualTo(relativeUrl);
        assertThat(image.getImageUrl()).startsWith("/");
    }
} 