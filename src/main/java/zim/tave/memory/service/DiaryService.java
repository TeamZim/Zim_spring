package zim.tave.memory.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zim.tave.memory.domain.Diary;
import zim.tave.memory.domain.Trip;
import zim.tave.memory.repository.DiaryRepository;
import zim.tave.memory.repository.TripRepository;
import zim.tave.memory.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DiaryService {

    private final UserRepository userRepository;
    private final DiaryRepository diaryRepository;
    private final TripRepository tripRepository;

    @Transactional
    public void saveDiary(Diary diary) {
        diaryRepository.save(diary);
    }

    public List<Diary> findAll() {
        return diaryRepository.findAll();
    }

    public Diary findOne(Long diaryId) {
        return diaryRepository.findById(diaryId);
    }
}
