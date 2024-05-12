package kr.co.ssalon.domain.service;

import kr.co.ssalon.domain.dto.MemberDomainDTO;
import kr.co.ssalon.domain.entity.Meeting;
import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.entity.MemberMeeting;
import kr.co.ssalon.domain.entity.Report;
import kr.co.ssalon.domain.repository.MeetingRepository;
import kr.co.ssalon.domain.repository.MemberRepository;
import kr.co.ssalon.web.dto.BlackListSearchCondition;
import kr.co.ssalon.web.dto.MeetingListSearchDTO;
import kr.co.ssalon.web.dto.ReportSearchCondition;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MeetingRepository meetingRepository;

    @Transactional
    public Member register(String username, String email, String role) throws Exception {
        validationUsername(username);
        Member member = Member.createMember(username, email, role);
        member = memberRepository.save(member);
        return member;
    }

    @Transactional
    public Member oauthUpdate(String username, String email, String role) throws BadRequestException {
        Optional<Member> findMember = memberRepository.findByUsername(username);
        Member member = ValidationService.validationMember(findMember);
        member.changeEmail(email);
        member.changeRole(role);
        if (member.getMemberDates() != null) {
            member.changeLastLoginDate();
        }
        return member;
    }


    public Member findMember(String username) throws BadRequestException {
        Optional<Member> findMember = memberRepository.findByUsername(username);
        Member member = ValidationService.validationMember(findMember);
        return member;
    }

    public Member findMember(Long id) throws BadRequestException {
        Optional<Member> findMember = memberRepository.findById(id);
        Member member = ValidationService.validationMember(findMember);
        return member;
    }

    public List<Member> findAllMember() {
        return memberRepository.findAll();
    }


    public void validationUsername(String username) throws Exception {
        Optional<Member> findMember = memberRepository.findByUsername(username);
        if (findMember.isPresent()) {
            throw new Exception("이미 해당 소셜 로그인을 한 회원이 DB에 존재합니다. 업데이트를 진행합니다.");
        }
    }

    @Transactional
    public Member signup(String username, MemberDomainDTO additionalInfo) throws BadRequestException {
        Member currentUser = findMember(username);
        boolean isRealSignup = currentUser.getNickname() == null;

        currentUser.initDetailSignInfo(
                additionalInfo.getNickname(), additionalInfo.getProfilePictureUrl(),
                additionalInfo.getGender(), additionalInfo.getAddress(), additionalInfo.getIntroduction(),
                additionalInfo.getInterests()
        );
        if (isRealSignup) {
            currentUser.initMemberDates();
        }
        return currentUser;
    }

    @Transactional
    public Member updateUserInfoForAdmin(Long userId, MemberDomainDTO additionalInfo) throws BadRequestException {
        Member updateUser = findMember(userId);

        updateUser.initDetailSignInfo(
                additionalInfo.getNickname(), additionalInfo.getProfilePictureUrl(),
                additionalInfo.getGender(), additionalInfo.getAddress(), additionalInfo.getIntroduction(),
                additionalInfo.getInterests()
        );
        return updateUser;
    }

    @Transactional
    public void withdraw(String username) throws BadRequestException {
        // 멤버 찾기
        Member currentUser = findMember(username);

        // 개설한 모임 제거
        List<MemberMeeting> joinedMeetings = currentUser.getJoinedMeetings();
        for (MemberMeeting memberMeeting : joinedMeetings) {
            Meeting joinedMeeting = memberMeeting.getMeeting();
            if (joinedMeeting.getCreator().equals(currentUser)) {
                meetingRepository.delete(joinedMeeting);
            }
        }

        // 멤버 제거
        memberRepository.delete(currentUser);
    }

    public List<MeetingListSearchDTO> getJoinedMeetingList(String username) throws BadRequestException {
        Member currentUser = findMember(username);

        // 참여한 모임 목록 조회
        List<Meeting> joinedMeetingList = currentUser.getJoinedMeetings().stream().map(MemberMeeting::getMeeting).toList();
        List<MeetingListSearchDTO> meetingListSearchList = joinedMeetingList.stream().map(MeetingListSearchDTO::new).toList();
        return meetingListSearchList;
    }

    public List<MeetingListSearchDTO> getCreatedMeetingLIst(String username) throws BadRequestException {
        Member currentUser = findMember(username);

        // 참여한 모임 중 내가 개최자인 모임 조회
        List<MeetingListSearchDTO> meetingListSearchList = currentUser.getJoinedMeetings().stream()
                .map(MemberMeeting::getMeeting)
                .filter(meeting -> meeting.getCreator().getId().equals(currentUser.getId()))
                .map(MeetingListSearchDTO::new)
                .collect(Collectors.toList());

        return meetingListSearchList;
    }

    public List<MeetingListSearchDTO> getUnfinishedMeetingList(String username) throws BadRequestException {
        Member currentUser = findMember(username);

        List<MeetingListSearchDTO> meetingListSearchList = currentUser.getJoinedMeetings().stream()
                .map(MemberMeeting::getMeeting)
                .filter(meeting -> !meeting.getIsFinished())
                .map(MeetingListSearchDTO::new)
                .collect(Collectors.toList());
        return meetingListSearchList;
    }

    public List<MeetingListSearchDTO> getFinishedMeetingList(String username) throws BadRequestException {
        Member currentUser = findMember(username);

        List<MeetingListSearchDTO> meetingListSearchList = currentUser.getJoinedMeetings().stream()
                .map(MemberMeeting::getMeeting)
                .filter(Meeting::getIsFinished)
                .map(MeetingListSearchDTO::new)
                .collect(Collectors.toList());
        return meetingListSearchList;
    }



    @Transactional
    public String changeBlackReason(Long userId, String reason) throws BadRequestException {
        Member currentUser = findMember(userId);
        currentUser.changeBlackReasonState(reason);
        return reason;
    }

    public Page<Member> getBlackList(BlackListSearchCondition blackListSearchCondition, Pageable pageable) throws BadRequestException {
        Page<Member> members = memberRepository.getBlackList(blackListSearchCondition, pageable);
        return members;
    }
}
