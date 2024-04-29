package kr.co.ssalon.domain.service;


import kr.co.ssalon.domain.entity.Meeting;
import kr.co.ssalon.domain.entity.MemberMeeting;
import kr.co.ssalon.domain.repository.MeetingRepository;
import kr.co.ssalon.domain.repository.MemberMeetingRepository;
import kr.co.ssalon.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MemberMeetingService {

    private final MeetingRepository meetingRepository;
    private final MemberMeetingRepository memberMeetingRepository;
    private final MemberRepository memberRepository;


    public MemberMeeting findMemberMeeting(Long id) throws BadRequestException {
        Optional<MemberMeeting> findMemberMeeting = memberMeetingRepository.findById(id);
        MemberMeeting memberMeeting = validaitonMemberMeeting(findMemberMeeting);
        return memberMeeting;
    }

    private MemberMeeting validaitonMemberMeeting(Optional<MemberMeeting> memberMeeting) throws BadRequestException {
        if (memberMeeting.isPresent()) {
            return memberMeeting.get();
        }else
            throw new BadRequestException("해당 멤머미팅을 찾을 수 없습니다");
    }


}