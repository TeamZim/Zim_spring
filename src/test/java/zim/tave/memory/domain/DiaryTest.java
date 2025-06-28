package zim.tave.memory.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

class DiaryTest {

    private User user;
    private Trip trip;
    private Country country;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        
        trip = new Trip();
        trip.setId(1L);
        
        country = new Country();
        country.setCountryCode("KR");
        country.setCountryName("대한민국");
    }

    @Test
    void 다이어리_생성_테스트() {
        // given
        String city = "서울";
        LocalDateTime dateTime = LocalDateTime.of(2024, 1, 15, 10, 0);
        String content = "서울 여행 첫째 날";

        // when
        Diary diary = Diary.createDiary(user, trip, country, city, dateTime, content);

        // then
        assertThat(diary.getUser()).isEqualTo(user);
        assertThat(diary.getTrip()).isEqualTo(trip);
        assertThat(diary.getCountry()).isEqualTo(country);
        assertThat(diary.getCity()).isEqualTo(city);
        assertThat(diary.getDateTime()).isEqualTo(dateTime);
        assertThat(diary.getContent()).isEqualTo(content);
        assertThat(diary.getCreatedAt()).isNotNull();
    }

    @Test
    void 선택적_필드_설정_테스트() {
        // given
        Diary diary = Diary.createDiary(user, trip, country, "서울", 
                                       LocalDateTime.now(), "테스트 내용");
        
        String detailedLocation = "강남역 1번 출구";
        String audioUrl = "audio/recording.mp3";
        Emotion emotion = new Emotion("행복", "#FFD700");
        Weather weather = new Weather();
        weather.setName("맑음");

        // when
        diary.setOptionalFields(detailedLocation, audioUrl, emotion, weather);

        // then
        assertThat(diary.getDetailedLocation()).isEqualTo(detailedLocation);
        assertThat(diary.getAudioUrl()).isEqualTo(audioUrl);
        assertThat(diary.getEmotion()).isEqualTo(emotion);
        assertThat(diary.getWeather()).isEqualTo(weather);
    }

    @Test
    void 이미지_추가_테스트() {
        // given
        Diary diary = Diary.createDiary(user, trip, country, "서울", 
                                       LocalDateTime.now(), "테스트 내용");
        DiaryImage image1 = new DiaryImage();
        image1.setImageUrl("image1.jpg");
        image1.setCameraType(DiaryImage.CameraType.FRONT);
        
        DiaryImage image2 = new DiaryImage();
        image2.setImageUrl("image2.jpg");
        image2.setCameraType(DiaryImage.CameraType.BACK);

        // when
        diary.addDiaryImage(image1);
        diary.addDiaryImage(image2);

        // then
        assertThat(diary.getDiaryImages()).hasSize(2);
        assertThat(image1.getDiary()).isEqualTo(diary);
        assertThat(image2.getDiary()).isEqualTo(diary);
    }
} 