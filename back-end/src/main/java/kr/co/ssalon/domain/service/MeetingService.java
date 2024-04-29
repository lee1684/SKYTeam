package kr.co.ssalon.domain.service;

import kr.co.ssalon.domain.entity.Meeting;
import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.entity.MemberMeeting;
import kr.co.ssalon.domain.repository.MeetingRepository;
import kr.co.ssalon.domain.repository.MemberMeetingRepository;
import kr.co.ssalon.domain.repository.MemberRepository;
import kr.co.ssalon.oauth2.CustomOAuth2Member;
import kr.co.ssalon.web.dto.MeetingDTO;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import kr.co.ssalon.web.dto.MeetingSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final MemberMeetingRepository memberMeetingRepository;
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    // 모임 참가
    @Transactional
    public MeetingDTO joinMoim(CustomOAuth2Member customOAuth2Member, Long moimId) throws BadRequestException {
        String username = customOAuth2Member.getUsername();
        Member currentUser = memberService.getByUsername(username);

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

    @Transactional
    public Long createMoim(CustomOAuth2Member customOAuth2Member, MeetingDTO meetingDTO) throws BadRequestException {
        String username = customOAuth2Member.getUsername();
        Member currentUser = memberService.getByUsername(username);

        Meeting meeting = Meeting.createMeeting(meetingDTO);
        MemberMeeting memberMeeting = MemberMeeting.createMemberMeeting(currentUser, meeting);

        // member entity 업데이트
        currentUser.getJoinedMeetings().add(memberMeeting);

        // meeting entity 업데이트
        meeting.getParticipants().add(memberMeeting);

        // memberMeeting entity 업데이트
        memberMeeting.setMeeting(meeting);
        memberMeeting.setMember(currentUser);
        memberMeetingRepository.save(memberMeeting);

        return meeting.getId();
    }

    public Boolean isParticipant(Long moimId, Member member) {
        Meeting meeting = meetingRepository.getReferenceById(moimId);
        List<MemberMeeting> participants = meeting.getParticipants();

        for (MemberMeeting memberMeeting : participants) {
            if (memberMeeting.getMember().equals(member)) {
                return true;
            }
        }
        return false;
    }

    @Transactional
    public MeetingDTO getMoim(CustomOAuth2Member customOAuth2Member, Long moimId) throws BadRequestException {
        return new MeetingDTO(meetingRepository.getReferenceById(moimId));
    }

    @Transactional
    public Meeting updateMoim(CustomOAuth2Member customOAuth2Member, Long moimId, MeetingDTO meetingDTO) throws BadRequestException {
        String username = customOAuth2Member.getUsername();
        Member currentUser = memberService.getByUsername(username);

        if (!meetingRepository.getReferenceById(moimId).getCreator().equals(currentUser)) {
            throw new BadRequestException();
        }

        meetingDTO.setId(moimId);
        Meeting meeting = Meeting.createMeeting(meetingDTO);
        return meetingRepository.save(meeting);
    }

    @Transactional
    public Long deleteMoim(CustomOAuth2Member customOAuth2Member, Long moimId) throws  BadRequestException {
        String username = customOAuth2Member.getUsername();
        Member currentUser = memberService.getByUsername(username);

        Meeting meeting = meetingRepository.getReferenceById(moimId);
        if (!meeting.getCreator().equals(currentUser)) {
            throw new BadRequestException();
        }

        List<MemberMeeting> participants = meeting.getParticipants();
        for (MemberMeeting memberMeeting : participants) {
            Member roopMember = memberRepository.getReferenceById(memberMeeting.getMember().getId());
            roopMember.getJoinedMeetings().remove(memberMeeting);
            memberRepository.save(roopMember);
            memberMeetingRepository.deleteById(memberMeeting.getId());
        }
        meetingRepository.deleteById(moimId);

        return moimId;
    }

    @Transactional
    public List<Member> getUsers(CustomOAuth2Member customOAuth2Member, Long moimId) throws BadRequestException {
        Meeting meeting = meetingRepository.getReferenceById(moimId);
        List<MemberMeeting> participants = meeting.getParticipants();
        List<Member> memberList = new ArrayList<>();

        for (MemberMeeting memberMeeting : participants) {
            Member member = memberMeeting.getMember();
            memberList.add(member);
        }

        return memberList;
    }
}
