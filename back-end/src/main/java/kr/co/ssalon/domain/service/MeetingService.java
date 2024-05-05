package kr.co.ssalon.domain.service;

import kr.co.ssalon.domain.dto.MeetingDomainDTO;
import kr.co.ssalon.domain.entity.*;
import kr.co.ssalon.domain.repository.*;
import kr.co.ssalon.oauth2.CustomOAuth2Member;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import kr.co.ssalon.web.dto.MeetingSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final MemberMeetingRepository memberMeetingRepository;
    private final MemberRepository memberRepository;
    private final TicketService ticketService;
    private final CategoryRepository categoryRepository;


    // 모임 개설
    @Transactional
    public Long createMoim(String username, MeetingDomainDTO meetingDomainDTO) throws BadRequestException {

        // 회원 찾기
        Member currentUser = findMember(username);
        // 카테고리 찾기
        Category category = findCategory(meetingDomainDTO.getCategory());
        // 모임 생성 { 카테고리, 회원, 모임 이미지, 모임 제목, 모임 설명, 모임 장소, 모임 수용인원, 모임 날짜 }
        Meeting meeting = Meeting.createMeeting(category, currentUser, meetingDomainDTO.getMeetingPictureUrls(), meetingDomainDTO.getTitle(), meetingDomainDTO.getDescription(), meetingDomainDTO.getLocation(), meetingDomainDTO.getCapacity(), meetingDomainDTO.getMeetingDate());

        // 모임 참가 생성 및 나의 가입된 모임에 추가
        MemberMeeting memberMeeting = MemberMeeting.createMemberMeeting(currentUser, meeting);
        memberMeetingRepository.save(memberMeeting);
        Meeting savedMeeting = meetingRepository.save(meeting);

        // 티켓 초기 정보 설정
        ticketService.initTicket(savedMeeting.getId());

        return savedMeeting.getId();
    }


    // 모임 참가
    @Transactional
    public Long join(String username, Long moimId) throws BadRequestException {
        Optional<Member> findMember = memberRepository.findByUsername(username);
        // 회원 검증
        Member currentUser = ValidationService.validationMember(findMember);
        // 모임 찾기
        Meeting meeting = findMeeting(moimId);
        if (memberMeetingRepository.existsByMemberId(currentUser.getId())) {
            throw new BadRequestException("이미 모임에 참가하고 있습니다.");
        };
        // 모임 참가 생성 및 나의 가입된 모임에 추가
        MemberMeeting memberMeeting = MemberMeeting.createMemberMeeting(currentUser, meeting);
        MemberMeeting joinedMemberMeeting = memberMeetingRepository.save(memberMeeting);
        return joinedMemberMeeting.getId();
    }

    // 모임 참가자 확인
    public Boolean isParticipant(Long moimId, Member member) throws BadRequestException {
        Meeting meeting = findMeeting(moimId);
        List<MemberMeeting> participants = meeting.getParticipants();

        for (MemberMeeting memberMeeting : participants) {
            if (memberMeeting.getMember().equals(member)) {
                return true;
            }
        }
        return false;
    }

    // 모임 목록 조회
    public Page<Meeting> getMoims(MeetingSearchCondition meetingSearchCondition, Pageable pageable) {
        Page<Meeting> meetings = meetingRepository.searchMoims(meetingSearchCondition, pageable);
        return meetings;
    }


    // 모임 정보 업데이트
    @Transactional
    public Long editMoim(String username, Long moimId, MeetingDomainDTO meetingDomainDTO) throws BadRequestException {

        // 모임 개최자 찾기
        Member currentUser = findMember(username);

        // 모임 찾기
        Meeting currentMeeting = findMeeting(moimId);

        // 개최자 검증
        if (!currentMeeting.getCreator().equals(currentUser)) {
            throw new BadRequestException("모임을 개설한 회원이 아닙니다.");
        }

        // 카테고리 찾기
        Category category = findCategory(meetingDomainDTO.getCategory());

        // Dirty Checking
        currentMeeting.updateMeeting(category, meetingDomainDTO.getMeetingPictureUrls(), meetingDomainDTO.getTitle(), meetingDomainDTO.getDescription(), meetingDomainDTO.getLocation(), meetingDomainDTO.getCapacity(), meetingDomainDTO.getMeetingDate());
        return currentMeeting.getId();
    }


    // 모임 삭제
    @Transactional
    public Long deleteMoim(String username, Long moimId) throws BadRequestException {
        // 멤버 찾기
        Member currentUser = findMember(username);

        // 삭제할 미팅 찾기
        Meeting currentMeeting = findMeeting(moimId);

        // 개최자 검증
        if (!currentMeeting.getCreator().equals(currentUser)) {
            throw new BadRequestException("모임을 개설한 회원이 아닙니다.");
        }

        // 모임 참여자 찾기
        List<MemberMeeting> participants = currentMeeting.getParticipants();


        // 연관 관계 제거
        // 내가 참여한 모임 중에서 해당 모임을 삭제
        participants.forEach(participant -> participant.getMember().deleteMemberMeeting(participant));

        // 해당 모임 참가자 삭제
        memberMeetingRepository.deleteByMeetingId(moimId);

        // 해당 모임 삭제
        meetingRepository.deleteById(moimId);

        return moimId;
    }

    // 모임에 참여한 유저 조회
    public List<Long> getUsers(Long moimId) throws BadRequestException {
        Meeting meeting = findMeeting(moimId);
        List<MemberMeeting> participants = meeting.getParticipants();
        List<Long> participantsId = participants.stream().map(MemberMeeting::getId).collect(Collectors.toList());
        return participantsId;
    }


    private Member findMember(String username) throws BadRequestException {
        Optional<Member> findMember = memberRepository.findByUsername(username);
        Member member = ValidationService.validationMember(findMember);
        return member;
    }

    public Meeting findMeeting(Long moimId) throws BadRequestException {
        Optional<Meeting> findMeeting = meetingRepository.findById(moimId);
        Meeting meeting = ValidationService.validationMeeting(findMeeting);
        return meeting;
    }

    private Category findCategory(String categoryName) throws BadRequestException {
        Optional<Category> findCategory = categoryRepository.findByName(categoryName);
        Category category = ValidationService.validationCategory(findCategory);
        return category;
    }
}