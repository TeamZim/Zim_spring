package zim.tave.memory.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import zim.tave.memory.domain.Trip;
import zim.tave.memory.repository.TripRepository;

import java.lang.reflect.Member;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TripServiceTest {

    @Autowired EntityManager em;
    @Autowired TripRepository tripRepository;
    @Autowired TripService tripService;

    @Test
    public void 여행추가() throws Exception {
        // given

        //when

        //then
    }

}