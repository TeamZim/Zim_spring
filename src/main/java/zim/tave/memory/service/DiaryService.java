package zim.tave.memory.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zim.tave.memory.repository.DiaryRepository;
import zim.tave.memory.repository.TripRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DiaryService {

//    private final MemberRepository memberRepository;
    private final DiaryRepository diaryRepository;
    private final TripRepository tripRepository;
}
