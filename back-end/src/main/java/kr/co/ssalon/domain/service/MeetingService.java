package kr.co.ssalon.domain.service;

import kr.co.ssalon.domain.entity.Meeting;
import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.entity.MemberMeeting;
import kr.co.ssalon.domain.repository.MeetingRepository;
import kr.co.ssalon.domain.repository.MemberMeetingRepository;
import kr.co.ssalon.oauth2.CustomOAuth2Member;
import kr.co.ssalon.web.dto.MeetingDTO;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import kr.co.ssalon.web.dto.MeetingSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final MemberMeetingRepository memberMeetingRepository;
    private final MemberService memberService;

    // 모임 참가
    @Transactional
    public MeetingDTO join(CustomOAuth2Member customOAuth2Member, Long moimId) throws BadRequestException {
        String username = customOAuth2Member.getUsername();
        Member currentUser = memberService.findMember(username);
        Meeting meeting = meetingRepository.findById(moimId)
                .orElseThrow(() -> new BadRequestException("해당 모임을 찾을 수 없습니다. ID: " + moimId));
        MemberMeeting memberMeeting = MemberMeeting.createMemberMeeting(currentUser, meeting);

        // member entity 업데이트
        currentUser.getJoinedMeetings().add(memberMeeting);

        // meeting entity 업데이트
        meeting.getParticipants().add(memberMeeting);

        // memberMeeting entity 업데이트
        memberMeeting.setMeeting(meeting);
        memberMeeting.setMember(currentUser);
        memberMeetingRepository.save(memberMeeting);

        // 참가한 모임 정보 반환
        return new MeetingDTO(meeting);
    }

    // 모임 목록 조회
    public Page<Meeting> getMoims(MeetingSearchCondition meetingSearchCondition, Pageable pageable) {
        Page<Meeting> meetings = meetingRepository.searchMoims(meetingSearchCondition, pageable);
        return meetings;
    }

    public Meeting findMeeting(Long id) throws BadRequestException {
        Optional<Meeting> findMeeting = meetingRepository.findById(id);
        Meeting meeting = validationMeeting(findMeeting);
        return meeting;
    }

    private Meeting validationMeeting(Optional<Meeting> meeting) throws BadRequestException {
        if (meeting.isPresent()) {
            return meeting.get();
        }else
            throw new BadRequestException("해당 회원을 찾을 수 없습니다");
    }
}
