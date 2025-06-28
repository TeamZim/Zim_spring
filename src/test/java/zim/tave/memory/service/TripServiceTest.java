package zim.tave.memory.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import zim.tave.memory.domain.Trip;
import zim.tave.memory.domain.TripTheme;
import zim.tave.memory.domain.User;
import zim.tave.memory.dto.CreateTripRequest;
import zim.tave.memory.dto.UpdateTripRequest;
import zim.tave.memory.repository.TripRepository;
import zim.tave.memory.repository.TripThemeRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TripServiceTest {

    @Mock
    private TripRepository tripRepository;

    @Mock
    private TripThemeRepository tripThemeRepository;

    @InjectMocks
    private TripService tripService;

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
    void 여행_생성_테스트() {
        // given
        CreateTripRequest request = new CreateTripRequest();
        request.setTripName("제주도 여행");
        request.setDescription("제주도 3박 4일 여행");
        request.setThemeId(1L);
        request.setUserId(1L);

        when(tripThemeRepository.findById(1L)).thenReturn(Optional.of(tripTheme));

        // when
        Trip result = tripService.createTrip(request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTripName()).isEqualTo("제주도 여행");
        verify(tripRepository).save(any(Trip.class));
    }

    @Test
    void 여행_생성_시_테마_없음_예외_테스트() {
        // given
        CreateTripRequest request = new CreateTripRequest();
        request.setThemeId(999L);

        when(tripThemeRepository.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> tripService.createTrip(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테마를 찾을 수 없습니다.");
    }

    @Test
    void 여행_수정_테스트() {
        // given
        UpdateTripRequest request = new UpdateTripRequest();
        request.setTripName("수정된 여행명");
        request.setDescription("수정된 설명");

        when(tripRepository.findOne(1L)).thenReturn(trip);

        // when
        tripService.updateTrip(1L, request);

        // then
        verify(tripRepository).findOne(1L);
        assertThat(trip.getTripName()).isEqualTo("수정된 여행명");
        assertThat(trip.getDescription()).isEqualTo("수정된 설명");
    }

    @Test
    void 여행_수정_시_테마_변경_테스트() {
        // given
        TripTheme newTheme = new TripTheme();
        newTheme.setId(2L);
        newTheme.setThemeName("겨울 테마");

        UpdateTripRequest request = new UpdateTripRequest();
        request.setThemeId(2L);

        when(tripRepository.findOne(1L)).thenReturn(trip);
        when(tripThemeRepository.findById(2L)).thenReturn(Optional.of(newTheme));

        // when
        tripService.updateTrip(1L, request);

        // then
        assertThat(trip.getTripTheme()).isEqualTo(newTheme);
    }

    @Test
    void 사용자별_여행_조회_테스트() {
        // given
        List<Trip> trips = Arrays.asList(trip);
        when(tripRepository.findByUserId(1L)).thenReturn(trips);

        // when
        List<Trip> result = tripService.findByUserId(1L);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(trip);
    }

    @Test
    void 여행_삭제_테스트() {
        // given
        when(tripRepository.findOne(1L)).thenReturn(trip);

        // when
        tripService.deleteTrip(1L);

        // then
        assertThat(trip.getIsDeleted()).isTrue();
    }

    @Test
    void 여행_종료날짜_업데이트_테스트() {
        // when
        tripService.updateTripEndDate(1L);

        // then
        verify(tripRepository).updateTripEndDate(1L);
    }

    @Test
    void 마지막_다이어리_날짜_조회_테스트() {
        // given
        LocalDate expectedDate = LocalDate.of(2024, 1, 15);
        when(tripRepository.findLastDiaryDateByTripId(1L)).thenReturn(expectedDate);

        // when
        LocalDate result = tripService.getLastDiaryDateByTripId(1L);

        // then
        assertThat(result).isEqualTo(expectedDate);
    }
}