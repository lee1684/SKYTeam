package kr.co.ssalon.domain.service;

import kr.co.ssalon.domain.entity.Meeting;
import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.entity.MemberMeeting;
import kr.co.ssalon.domain.repository.MeetingRepository;
import kr.co.ssalon.domain.repository.MemberMeetingRepository;
import kr.co.ssalon.web.dto.AttendanceDTO;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AttendanceService {


    private final MeetingRepository meetingRepository;
    private final MeetingService meetingService;
    private final MemberService memberService;
    private final MemberMeetingService memberMeetingService;
    private final QrService qrService;
    private final MemberMeetingRepository memberMeetingRepository;

    public List<AttendanceDTO> getAttendancesTrue(Long moimId) throws BadRequestException {
        Meeting currentMeeting = meetingService.findMeeting(moimId);
        List<MemberMeeting> participants = currentMeeting.getParticipants();

        List<AttendanceDTO> attendances = new ArrayList<>();

        for (MemberMeeting memberMeeting : participants) {
            AttendanceDTO attendanceDTO = new AttendanceDTO(memberMeeting);
            if (memberMeeting.isAttendance()) {
                attendances.add(attendanceDTO);
            }
        }
        return attendances;
    }

    @Transactional
    public boolean changeAttendance(Long moimId, Long userId, boolean attendance) throws BadRequestException {
        Meeting currentMeeting = meetingService.findMeeting(moimId);
        Member currentMember = memberService.findMember(userId);
        MemberMeeting currentMemberMeeting = memberMeetingService.findByMemberAndMeeting(currentMember, currentMeeting);

        currentMemberMeeting.changeAttendance(attendance);

        return currentMemberMeeting.isAttendance();
    }
}
