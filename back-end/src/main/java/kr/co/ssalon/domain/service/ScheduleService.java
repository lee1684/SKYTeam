package kr.co.ssalon.domain.service;

import kr.co.ssalon.domain.entity.Meeting;
import kr.co.ssalon.domain.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ScheduleService {

    private final MeetingRepository meetingRepository;

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void isMoimFinished() {
        List<Meeting> meetingList = meetingRepository.findAllUnfinishedMeetings();

        for (Meeting meeting : meetingList) {
            if(meeting.getMeetingDate().isBefore(LocalDateTime.now())) {
                meeting.changeIsFinished();
            }
        }
    }
}
