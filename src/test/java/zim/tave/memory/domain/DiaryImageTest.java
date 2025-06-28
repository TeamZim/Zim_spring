package zim.tave.memory.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.assertj.core.api.Assertions.*;

class DiaryImageTest {

    private Diary diary;
    private DiaryImage frontImage;
    private DiaryImage backImage;

    @BeforeEach
    void setUp() {
        diary = new Diary();
        diary.setId(1L);
        
        frontImage = new DiaryImage();
        frontImage.setId(1L);
        frontImage.setImageUrl("front.jpg");
        frontImage.setCameraType(DiaryImage.CameraType.FRONT);
        frontImage.setImageOrder(1);
        
        backImage = new DiaryImage();
        backImage.setId(2L);
        backImage.setImageUrl("back.jpg");
        backImage.setCameraType(DiaryImage.CameraType.BACK);
        backImage.setImageOrder(2);
    }

    @Test
    void 카메라_타입_테스트() {
        // given & when
        DiaryImage.CameraType frontType = DiaryImage.CameraType.FRONT;
        DiaryImage.CameraType backType = DiaryImage.CameraType.BACK;

        // then
        assertThat(frontType).isEqualTo(DiaryImage.CameraType.FRONT);
        assertThat(backType).isEqualTo(DiaryImage.CameraType.BACK);
        assertThat(frontType).isNotEqualTo(backType);
    }

    @Test
    void 전면_카메라_이미지_생성_테스트() {
        // given & when
        frontImage.setDiary(diary);
        frontImage.setRepresentative(true);

        // then
        assertThat(frontImage.getCameraType()).isEqualTo(DiaryImage.CameraType.FRONT);
        assertThat(frontImage.getImageUrl()).isEqualTo("front.jpg");
        assertThat(frontImage.getImageOrder()).isEqualTo(1);
        assertThat(frontImage.isRepresentative()).isTrue();
        assertThat(frontImage.getDiary()).isEqualTo(diary);
    }

    @Test
    void 후면_카메라_이미지_생성_테스트() {
        // given & when
        backImage.setDiary(diary);
        backImage.setRepresentative(false);

        // then
        assertThat(backImage.getCameraType()).isEqualTo(DiaryImage.CameraType.BACK);
        assertThat(backImage.getImageUrl()).isEqualTo("back.jpg");
        assertThat(backImage.getImageOrder()).isEqualTo(2);
        assertThat(backImage.isRepresentative()).isFalse();
        assertThat(backImage.getDiary()).isEqualTo(diary);
    }

    @Test
    void 대표사진_변경_테스트() {
        // given
        frontImage.setRepresentative(true);
        backImage.setRepresentative(false);

        // when
        frontImage.setRepresentative(false);
        backImage.setRepresentative(true);

        // then
        assertThat(frontImage.isRepresentative()).isFalse();
        assertThat(backImage.isRepresentative()).isTrue();
    }

    @Test
    void 이미지_순서_테스트() {
        // given & when
        frontImage.setImageOrder(1);
        backImage.setImageOrder(2);

        // then
        assertThat(frontImage.getImageOrder()).isEqualTo(1);
        assertThat(backImage.getImageOrder()).isEqualTo(2);
        assertThat(frontImage.getImageOrder()).isLessThan(backImage.getImageOrder());
    }
} 