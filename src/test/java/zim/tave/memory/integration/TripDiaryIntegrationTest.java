package zim.tave.memory.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import zim.tave.memory.domain.*;
import zim.tave.memory.dto.CreateTripRequest;
import zim.tave.memory.dto.CreateDiaryRequest;
import zim.tave.memory.service.TripService;
import zim.tave.memory.service.DiaryService;
import zim.tave.memory.repository.TripRepository;
import zim.tave.memory.repository.DiaryRepository;
import zim.tave.memory.repository.UserRepository;
import zim.tave.memory.repository.TripThemeRepository;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TripDiaryIntegrationTest {

    @Mock
    private TripRepository tripRepository;

    @Mock
    private DiaryRepository diaryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TripThemeRepository tripThemeRepository;

    @InjectMocks
    private TripService tripService;

    @InjectMocks
    private DiaryService diaryService;

    private User user;
    private TripTheme tripTheme;
    private Trip trip;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        
        tripTheme = new TripTheme();
        tripTheme.setId(1L);
        tripTheme.setThemeName("여름 테마");
        
        trip = new Trip();
        trip.setId(1L);
        trip.setTripName("제주도 여행");
        trip.setDescription("제주도 3박 4일 여행");
        trip.setUser(user);
        trip.setTripTheme(tripTheme);
    }

    @Test
    void 여행_생성_후_다이어리_추가_시_종료날짜_자동_업데이트_테스트() {
        // given - 여행 생성
        CreateTripRequest tripRequest = new CreateTripRequest();
        tripRequest.setTripName("제주도 여행");
        tripRequest.setDescription("제주도 3박 4일 여행");
        tripRequest.setThemeId(1L);
        tripRequest.setUserId(1L);

        when(tripThemeRepository.findById(1L)).thenReturn(Optional.of(tripTheme));

        Trip createdTrip = tripService.createTrip(tripRequest);
        assertThat(createdTrip.getStartDate()).isEqualTo(LocalDate.now());
        assertThat(createdTrip.getEndDate()).isNull(); // 아직 다이어리가 없음

        // when - 다이어리 생성
        CreateDiaryRequest diaryRequest = new CreateDiaryRequest();
        diaryRequest.setUserId(1L);
        diaryRequest.setTripId(1L);
        diaryRequest.setCountryCode("KR");
        diaryRequest.setCity("제주시");
        diaryRequest.setDateTime(LocalDateTime.of(2024, 1, 15, 10, 0));
        diaryRequest.setContent("제주도 첫째 날");
        
        CreateDiaryRequest.DiaryImageInfo imageInfo1 = new CreateDiaryRequest.DiaryImageInfo();
        imageInfo1.setImageUrl("front.jpg");
        imageInfo1.setCameraType(DiaryImage.CameraType.FRONT);
        imageInfo1.setRepresentative(true);
        
        CreateDiaryRequest.DiaryImageInfo imageInfo2 = new CreateDiaryRequest.DiaryImageInfo();
        imageInfo2.setImageUrl("back.jpg");
        imageInfo2.setCameraType(DiaryImage.CameraType.BACK);
        imageInfo2.setRepresentative(false);
        
        diaryRequest.setImages(Arrays.asList(imageInfo1, imageInfo2));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(tripRepository.findOne(1L)).thenReturn(createdTrip);

        Diary createdDiary = diaryService.createDiary(diaryRequest);

        // then
        assertThat(createdDiary.getTrip()).isEqualTo(createdTrip);
        assertThat(createdTrip.getEndDate()).isEqualTo(LocalDate.of(2024, 1, 15));
        assertThat(createdTrip.getDiaries()).hasSize(1);
    }

    @Test
    void 다이어리_삭제_시_여행_종료날짜_재계산_테스트() {
        // given - 여행과 다이어리들 생성
        when(tripThemeRepository.findById(1L)).thenReturn(Optional.of(tripTheme));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(tripRepository.findOne(1L)).thenReturn(trip);

        // 첫 번째 다이어리 생성
        CreateDiaryRequest diaryRequest1 = new CreateDiaryRequest();
        diaryRequest1.setUserId(1L);
        diaryRequest1.setTripId(1L);
        diaryRequest1.setCountryCode("KR");
        diaryRequest1.setCity("제주시");
        diaryRequest1.setDateTime(LocalDateTime.of(2024, 1, 15, 10, 0));
        diaryRequest1.setContent("제주도 첫째 날");
        diaryRequest1.setImages(createImageInfo("front1.jpg", "back1.jpg"));

        Diary diary1 = diaryService.createDiary(diaryRequest1);

        // 두 번째 다이어리 생성
        CreateDiaryRequest diaryRequest2 = new CreateDiaryRequest();
        diaryRequest2.setUserId(1L);
        diaryRequest2.setTripId(1L);
        diaryRequest2.setCountryCode("KR");
        diaryRequest2.setCity("서귀포시");
        diaryRequest2.setDateTime(LocalDateTime.of(2024, 1, 20, 10, 0));
        diaryRequest2.setContent("제주도 마지막 날");
        diaryRequest2.setImages(createImageInfo("front2.jpg", "back2.jpg"));

        Diary diary2 = diaryService.createDiary(diaryRequest2);

        // when - 마지막 다이어리 삭제
        when(diaryRepository.findById(2L)).thenReturn(diary2);
        when(diaryRepository.findByTripId(1L)).thenReturn(Arrays.asList(diary1));

        diaryService.deleteDiary(2L);

        // then - 종료날짜가 첫 번째 다이어리 날짜로 변경됨
        assertThat(trip.getEndDate()).isEqualTo(LocalDate.of(2024, 1, 15));
    }

    @Test
    void 모든_다이어리_삭제_시_종료날짜_null_테스트() {
        // given
        when(tripThemeRepository.findById(1L)).thenReturn(Optional.of(tripTheme));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(tripRepository.findOne(1L)).thenReturn(trip);

        CreateDiaryRequest diaryRequest = new CreateDiaryRequest();
        diaryRequest.setUserId(1L);
        diaryRequest.setTripId(1L);
        diaryRequest.setCountryCode("KR");
        diaryRequest.setCity("제주시");
        diaryRequest.setDateTime(LocalDateTime.of(2024, 1, 15, 10, 0));
        diaryRequest.setContent("제주도 여행");
        diaryRequest.setImages(createImageInfo("front.jpg", "back.jpg"));

        Diary diary = diaryService.createDiary(diaryRequest);

        // when - 마지막 다이어리 삭제
        when(diaryRepository.findById(1L)).thenReturn(diary);
        when(diaryRepository.findByTripId(1L)).thenReturn(Arrays.asList());

        diaryService.deleteDiary(1L);

        // then
        assertThat(trip.getEndDate()).isNull();
    }

    private List<CreateDiaryRequest.DiaryImageInfo> createImageInfo(String frontUrl, String backUrl) {
        CreateDiaryRequest.DiaryImageInfo imageInfo1 = new CreateDiaryRequest.DiaryImageInfo();
        imageInfo1.setImageUrl(frontUrl);
        imageInfo1.setCameraType(DiaryImage.CameraType.FRONT);
        imageInfo1.setRepresentative(true);
        
        CreateDiaryRequest.DiaryImageInfo imageInfo2 = new CreateDiaryRequest.DiaryImageInfo();
        imageInfo2.setImageUrl(backUrl);
        imageInfo2.setCameraType(DiaryImage.CameraType.BACK);
        imageInfo2.setRepresentative(false);
        
        return Arrays.asList(imageInfo1, imageInfo2);
    }
} 