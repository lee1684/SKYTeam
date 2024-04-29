package kr.co.ssalon.domain.service;

import kr.co.ssalon.domain.entity.Meeting;
import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.entity.MemberMeeting;
import kr.co.ssalon.domain.repository.MemberMeetingRepository;
import org.apache.coyote.BadRequestException;

import java.util.Optional;

public class MemberMeetingService {

    private MemberMeetingRepository memberMeetingRepository;

    public MemberMeeting findByMemberAndMeeting(Member member, Meeting meeting) throws BadRequestException {

        Optional<MemberMeeting> findMemberMeeting = memberMeetingRepository.findByMemberAndMeeting(member, meeting);
        MemberMeeting memberMeeting = validationMemberMeeting(findMemberMeeting);
        return memberMeeting;
    }

    private MemberMeeting validationMemberMeeting(Optional<MemberMeeting> findMemberMeeting) throws BadRequestException {
        if (findMemberMeeting.isPresent()) {
            return findMemberMeeting.get();
        } else
            throw new BadRequestException("멤버 미팅을 찾을 수 없습니다.");
    }
}
