package zim.tave.memory.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import zim.tave.memory.domain.*;
import zim.tave.memory.dto.CreateDiaryRequest;
import zim.tave.memory.dto.UpdateDiaryOptionalFieldsRequest;
import zim.tave.memory.repository.DiaryRepository;
import zim.tave.memory.repository.TripRepository;
import zim.tave.memory.repository.UserRepository;
import zim.tave.memory.repository.EmotionRepository;
import zim.tave.memory.repository.WeatherRepository;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class DiaryServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private DiaryRepository diaryRepository;

    @Mock
    private TripRepository tripRepository;

    @Mock
    private EmotionRepository emotionRepository;

    @Mock
    private WeatherRepository weatherRepository;

    @InjectMocks
    private DiaryService diaryService;

    private User user;
    private Trip trip;
    private Diary diary;
    private Emotion emotion;
    private Weather weather;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        
        trip = new Trip();
        trip.setId(1L);
        
        diary = new Diary();
        diary.setId(1L);
        diary.setUser(user);
        diary.setTrip(trip);
        diary.setCity("서울");
        diary.setContent("테스트 내용");
        diary.setCreatedAt(LocalDateTime.of(2024, 1, 15, 10, 0));
        
        emotion = new Emotion("행복", "#FFD700");
        emotion.setId(1L);
        
        weather = new Weather();
        weather.setId(1L);
        weather.setName("맑음");
    }

    @Test
    void 다이어리_생성_테스트() {
        // given
        CreateDiaryRequest request = new CreateDiaryRequest();
        request.setUserId(1L);
        request.setTripId(1L);
        request.setCountryCode("KR");
        request.setCity("서울");
        request.setDateTime(LocalDateTime.of(2024, 1, 15, 10, 0));
        request.setContent("테스트 내용");
        
        CreateDiaryRequest.DiaryImageInfo imageInfo1 = new CreateDiaryRequest.DiaryImageInfo();
        imageInfo1.setImageUrl("front.jpg");
        imageInfo1.setCameraType(DiaryImage.CameraType.FRONT);
        imageInfo1.setRepresentative(true);
        
        CreateDiaryRequest.DiaryImageInfo imageInfo2 = new CreateDiaryRequest.DiaryImageInfo();
        imageInfo2.setImageUrl("back.jpg");
        imageInfo2.setCameraType(DiaryImage.CameraType.BACK);
        imageInfo2.setRepresentative(false);
        
        request.setImages(Arrays.asList(imageInfo1, imageInfo2));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(tripRepository.findOne(1L)).thenReturn(trip);

        // when
        Diary result = diaryService.createDiary(request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getCity()).isEqualTo("서울");
        assertThat(result.getContent()).isEqualTo("테스트 내용");
        verify(diaryRepository).save(any(Diary.class));
    }

    @Test
    void 다이어리_생성_시_사용자_없음_예외_테스트() {
        // given
        CreateDiaryRequest request = new CreateDiaryRequest();
        request.setUserId(999L);

        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> diaryService.createDiary(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("사용자를 찾을 수 없습니다.");
    }

    @Test
    void 선택적_필드_업데이트_테스트() {
        // given
        UpdateDiaryOptionalFieldsRequest request = new UpdateDiaryOptionalFieldsRequest();
        request.setDetailedLocation("강남역 1번 출구");
        request.setAudioUrl("audio/recording.mp3");
        request.setEmotionId(1L);
        request.setWeatherId(1L);

        when(diaryRepository.findById(1L)).thenReturn(diary);
        when(emotionRepository.findById(1L)).thenReturn(Optional.of(emotion));
        when(weatherRepository.findById(1L)).thenReturn(Optional.of(weather));

        // when
        diaryService.updateDiaryOptionalFields(1L, request);

        // then
        assertThat(diary.getDetailedLocation()).isEqualTo("강남역 1번 출구");
        assertThat(diary.getAudioUrl()).isEqualTo("audio/recording.mp3");
        assertThat(diary.getEmotion()).isEqualTo(emotion);
        assertThat(diary.getWeather()).isEqualTo(weather);
    }

    @Test
    void 대표사진_변경_테스트() {
        // given
        DiaryImage image1 = new DiaryImage();
        image1.setId(1L);
        image1.setRepresentative(true);
        
        DiaryImage image2 = new DiaryImage();
        image2.setId(2L);
        image2.setRepresentative(false);
        
        diary.addDiaryImage(image1);
        diary.addDiaryImage(image2);

        when(diaryRepository.findById(1L)).thenReturn(diary);

        // when
        diaryService.updateRepresentativeImage(1L, 2L);

        // then
        assertThat(image1.isRepresentative()).isFalse();
        assertThat(image2.isRepresentative()).isTrue();
    }

    @Test
    void 대표사진_변경_시_이미지_없음_예외_테스트() {
        // given
        when(diaryRepository.findById(1L)).thenReturn(diary);

        // when & then
        assertThatThrownBy(() -> diaryService.updateRepresentativeImage(1L, 999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미지를 찾을 수 없습니다.");
    }

    @Test
    void 다이어리_삭제_테스트() {
        // given
        when(diaryRepository.findById(1L)).thenReturn(diary);
        when(diaryRepository.findByTripId(1L)).thenReturn(Arrays.asList());

        // when
        diaryService.deleteDiary(1L);

        // then
        verify(diaryRepository).delete(diary);
        assertThat(trip.getEndDate()).isNull();
    }

    @Test
    void 다이어리_삭제_후_남은_다이어리_있을_때_종료날짜_업데이트_테스트() {
        // given
        Diary remainingDiary = new Diary();
        remainingDiary.setCreatedAt(LocalDateTime.of(2024, 1, 20, 10, 0));
        
        when(diaryRepository.findById(1L)).thenReturn(diary);
        when(diaryRepository.findByTripId(1L)).thenReturn(Arrays.asList(remainingDiary));

        // when
        diaryService.deleteDiary(1L);

        // then
        verify(diaryRepository).delete(diary);
        assertThat(trip.getEndDate()).isEqualTo(LocalDate.of(2024, 1, 20));
    }

    @Test
    void 사용자별_다이어리_조회_테스트() {
        // given
        List<Diary> diaries = Arrays.asList(diary);
        when(diaryRepository.findByUserId(1L)).thenReturn(diaries);

        // when
        List<Diary> result = diaryService.findByUserId(1L);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(diary);
    }

    @Test
    void 여행별_다이어리_조회_테스트() {
        // given
        List<Diary> diaries = Arrays.asList(diary);
        when(diaryRepository.findByTripId(1L)).thenReturn(diaries);

        // when
        List<Diary> result = diaryService.findByTripId(1L);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(diary);
    }
} 