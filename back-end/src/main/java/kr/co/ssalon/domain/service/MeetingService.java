package kr.co.ssalon.domain.service;

import kr.co.ssalon.domain.entity.Meeting;
import kr.co.ssalon.domain.repository.MeetingRepository;
import kr.co.ssalon.web.dto.MeetingSearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;

    // 모임 목록 조회
    public Page<Meeting> getMoims(MeetingSearchCondition meetingSearchCondition, Pageable pageable) {
        Page<Meeting> meetings = meetingRepository.searchMoims(meetingSearchCondition, pageable);
        return meetings;

    }
}
