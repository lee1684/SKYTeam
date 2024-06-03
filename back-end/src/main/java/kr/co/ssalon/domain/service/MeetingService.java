package kr.co.ssalon.domain.service;

import com.google.gson.Gson;
import kr.co.ssalon.domain.dto.MeetingDomainDTO;
import kr.co.ssalon.domain.entity.*;
import kr.co.ssalon.domain.repository.*;
import kr.co.ssalon.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
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
    private final MeetingOutRepository meetingOutRepository;
    private final RecommendService recommendService;
    private final AwsLambdaService awsLambdaService;
    private final ValidationService validationService;

    // 모임 개설
    @Transactional
    public Long createMoim(String username, MeetingDomainDTO meetingDomainDTO) throws BadRequestException {

        // 회원 찾기
        Member currentUser = findMember(username);
        // 카테고리 찾기
        Category category = findCategory(meetingDomainDTO.getCategory());

        // 모임 생성 { 카테고리, 회원, 모임 이미지, 모임 제목, 모임 설명, 모임 장소, 모임 수용인원, 모임 날짜, 공유 여부 }
        Meeting meeting = Meeting.createMeeting(
                category,
                currentUser,
                meetingDomainDTO.getMeetingPictureUrls(),
                meetingDomainDTO.getTitle(),
                meetingDomainDTO.getDescription(),
                meetingDomainDTO.getLocation(),
                meetingDomainDTO.getCapacity(),
                meetingDomainDTO.getPayment(),
                meetingDomainDTO.getMeetingDate(),
                meetingDomainDTO.getIsSharable()
        );

        // 모임 참가 생성 및 나의 가입된 모임에 추가
        MemberMeeting memberMeeting = MemberMeeting.createMemberMeeting(currentUser, meeting);
        memberMeeting.changeAttendanceTrue();
        memberMeetingRepository.save(memberMeeting);
        Meeting savedMeeting = meetingRepository.save(meeting);

        // 티켓 초기 정보 설정
        ticketService.initTicket(savedMeeting.getId(), "N");

        // 모임 정보 임베딩
        recommendService.updateMoimEmbedding(savedMeeting);

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
        if (memberMeetingRepository.existsByMemberIdAndMeetingId(currentUser.getId(), moimId)) {
            throw new BadRequestException("이미 모임에 참가하고 있습니다.");
        }
        ;
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

    public Boolean isCreator(String username, Long moimId) throws BadRequestException {
        Member member = findMember(username);
        Meeting meeting = findMeeting(moimId);

        if(!isParticipant(moimId, member)) {
            throw new BadRequestException("모임의 참여자가 아닙니다.");
        }

        return member.equals(meeting.getCreator());
    }
    // 모임 목록 조회
    public Page<Meeting> getMoims(MeetingSearchCondition meetingSearchCondition, String username, Pageable pageable) {
        Page<Meeting> meetings = meetingRepository.searchMoims(meetingSearchCondition, username, pageable);
        return meetings;
    }

    public Page<Meeting> searchByKeyword(String keyword, Pageable pageable){
        if(keyword == null) keyword = "";

        Page<Meeting> byKeywordContaining = meetingRepository.searchByTitleOrDescription(keyword, pageable);
        return byKeywordContaining;
    }

    // 모임 정보 업데이트
    @Transactional
    public Long editMoim(String username, Long moimId, MeetingInfoDTO meetingInfoDTO) throws BadRequestException {

        // 모임 개최자 찾기
        Member currentUser = findMember(username);

        // 모임 찾기
        Meeting currentMeeting = findMeeting(moimId);

        // 개최자 검증
        if (!currentMeeting.getCreator().equals(currentUser)) {
            throw new BadRequestException("모임을 개설한 회원이 아닙니다.");
        }

        // 카테고리 찾기
        Category category = findCategory(meetingInfoDTO.getCategory());
        currentMeeting.updateMeeting(category, meetingInfoDTO.getPayment(), meetingInfoDTO.getMeetingPictureUrls(), meetingInfoDTO.getTitle(), meetingInfoDTO.getDescription(), meetingInfoDTO.getLocation(), meetingInfoDTO.getCapacity(), meetingInfoDTO.getMeetingDate(), meetingInfoDTO.getIsSharable());

        // 임베딩 업데이트
        Meeting updatedMeeting = findMeeting(moimId);
        recommendService.updateMoimEmbedding(updatedMeeting);

        return currentMeeting.getId();
    }

    // 모임 정보 업데이트
    @Transactional
    public Long editMoim(Long moimId, MeetingInfoDTO meetingInfoDTO) throws BadRequestException {
        Meeting currentMeeting = findMeeting(moimId);
        Category category = findCategory(meetingInfoDTO.getCategory());
        currentMeeting.updateMeeting(category, meetingInfoDTO.getPayment(), meetingInfoDTO.getMeetingPictureUrls(), meetingInfoDTO.getTitle(), meetingInfoDTO.getDescription(), meetingInfoDTO.getLocation(), meetingInfoDTO.getCapacity(), meetingInfoDTO.getMeetingDate(), meetingInfoDTO.getIsSharable());

        Meeting updatedMeeting = findMeeting(moimId);
        recommendService.updateMoimEmbedding(updatedMeeting);

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

    @Transactional
    public Long deleteMoim(Long moimId) throws BadRequestException {
        Meeting currentMeeting = findMeeting(moimId);
        List<MemberMeeting> participants = currentMeeting.getParticipants();
        participants.forEach(participant -> participant.getMember().deleteMemberMeeting(participant));
        memberMeetingRepository.deleteByMeetingId(moimId);
        meetingRepository.deleteById(moimId);
        return moimId;
    }

    // 모임에 참여한 유저 조회
    public List<ParticipantDTO> getUsers(Long moimId) throws BadRequestException {
        Meeting meeting = findMeeting(moimId);
        List<MemberMeeting> participants = meeting.getParticipants();
        List<ParticipantDTO> participantsDTO = participants.stream().map(ParticipantDTO::new).collect(Collectors.toList());
        return participantsDTO;
    }

    @Transactional
    public MeetingOut deleteUserFromMoim(String username, Long moimId, Long userId, String reason) throws BadRequestException {
        Meeting meeting = findMeeting(moimId);
        Member currentUser = findMember(username);
        Member targetUser = findMember(userId);
        MemberMeeting targetMemberMeeting = findMemberMeeting(targetUser, meeting);

        // 요청자가 모임에 포함되어 있는지 검증
        if(!isParticipant(moimId, currentUser)) {throw new BadRequestException("요청자가 모임에 참여자 목록에 존재하지 않습니다.");}

        targetUser.deleteMemberMeeting(targetMemberMeeting);
        meeting.deleteMemberMeeting(targetMemberMeeting);

        // 요청자가 모임 개최자인 경우 -> 강퇴
        if(currentUser.equals(meeting.getCreator()) || !currentUser.equals(targetUser)) {

            MeetingOut meetingOut = MeetingOut.createMeetingOutReason(targetUser, meeting, "강퇴", reason);
            meetingOutRepository.save(meetingOut);
            memberMeetingRepository.delete(targetMemberMeeting);
            return meetingOut;
        }

        // 요청자가 모임 개최자가 아닌 경우 -> 탈퇴
        else if (!currentUser.equals(meeting.getCreator()) || currentUser.equals(targetUser)) {

            MeetingOut meetingOut = MeetingOut.createMeetingOutReason(targetUser, meeting, "탈퇴", reason);
            meetingOutRepository.save(meetingOut);
            memberMeetingRepository.delete(targetMemberMeeting);
            return meetingOut;
        }

        else {
            throw new BadRequestException("요청자와 타겟의 관계가 잘못 설정되었습니다.");
        }
    }

    public void updateMoimEmbeddingAll() {

        List<Meeting> allMeetings = meetingRepository.findAll();

        for (Meeting meeting : allMeetings) {
            Long moimId = meeting.getId();
            String moimTitle = meeting.getTitle();
            StringBuilder prompt = new StringBuilder();

            prompt.append("우리 모임은 ").append(meeting.getCategory().getName()).append(" 모임입니다. ");
            prompt.append("우리 모임은 ").append(meeting.getLocation()).append("에서 열립니다. ");
            prompt.append(meeting.getDescription());

            awsLambdaService.updateMoimEmbedding(moimId, moimTitle, prompt.toString());
        }
    }

    private Member findMember(String username) throws BadRequestException {
        Optional<Member> findMember = memberRepository.findByUsername(username);
        Member member = ValidationService.validationMember(findMember);
        return member;
    }

    private Member findMember(Long userId) throws BadRequestException {
        Optional<Member> findMember = memberRepository.findById(userId);
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

    private MemberMeeting findMemberMeeting(Member member, Meeting meeting) throws BadRequestException {
        Optional<MemberMeeting> findMemberMeeting = memberMeetingRepository.findByMemberAndMeeting(member, meeting);
        MemberMeeting memberMeeting = ValidationService.validationMemberMeeting(findMemberMeeting);
        return memberMeeting;
    }
}