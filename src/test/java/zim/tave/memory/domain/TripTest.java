package zim.tave.memory.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

class TripTest {

    private User user;
    private TripTheme tripTheme;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        
        tripTheme = new TripTheme();
        tripTheme.setId(1L);
        tripTheme.setThemeName("여름 테마");
    }

    @Test
    void 여행_생성_테스트() {
        // given
        String tripName = "제주도 여행";
        String description = "제주도 3박 4일 여행";

        // when
        Trip trip = Trip.createTrip(user, tripName, description, tripTheme);

        // then
        assertThat(trip.getTripName()).isEqualTo(tripName);
        assertThat(trip.getDescription()).isEqualTo(description);
        assertThat(trip.getUser()).isEqualTo(user);
        assertThat(trip.getTripTheme()).isEqualTo(tripTheme);
        assertThat(trip.getStartDate()).isEqualTo(LocalDate.now());
        assertThat(trip.getIsDeleted()).isFalse();
    }

    @Test
    void 다이어리_추가_시_종료날짜_업데이트_테스트() {
        // given
        Trip trip = Trip.createTrip(user, "제주도 여행", "제주도 여행", tripTheme);
        Diary diary = new Diary();
        diary.setCreatedAt(LocalDateTime.of(2024, 1, 15, 10, 0));

        // when
        trip.addDiary(diary);

        // then
        assertThat(trip.getEndDate()).isEqualTo(LocalDate.of(2024, 1, 15));
        assertThat(trip.getDiaries()).hasSize(1);
        assertThat(diary.getTrip()).isEqualTo(trip);
    }

    @Test
    void 종료날짜_수동_업데이트_테스트() {
        // given
        Trip trip = Trip.createTrip(user, "제주도 여행", "제주도 여행", tripTheme);
        LocalDate newEndDate = LocalDate.of(2024, 1, 20);

        // when
        trip.updateEndDate(newEndDate);

        // then
        assertThat(trip.getEndDate()).isEqualTo(newEndDate);
    }
} 