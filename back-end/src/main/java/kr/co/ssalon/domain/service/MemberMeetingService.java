package kr.co.ssalon.domain.service;

import kr.co.ssalon.domain.entity.Meeting;
import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.entity.MemberMeeting;
import kr.co.ssalon.domain.repository.MemberMeetingRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberMeetingService {

    private final MemberMeetingRepository memberMeetingRepository;

    public MemberMeeting findByMemberAndMeeting(Member member, Meeting meeting) throws BadRequestException {

        Optional<MemberMeeting> findMemberMeeting = memberMeetingRepository.findByMemberAndMeeting(member, meeting);
        MemberMeeting memberMeeting = validationMemberMeeting(findMemberMeeting);
        return memberMeeting;
    }

    private MemberMeeting validationMemberMeeting(Optional<MemberMeeting> findMemberMeeting) throws BadRequestException {
        if (findMemberMeeting.isPresent()) {
            return findMemberMeeting.get();
        } else
            throw new BadRequestException("회원이 참여하고 있는 모임을 찾을 수 없습니다.");
    }
}
