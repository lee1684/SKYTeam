package kr.co.ssalon.domain.service;

import kr.co.ssalon.domain.dto.MeetingDomainDTO;
import kr.co.ssalon.domain.entity.Category;
import kr.co.ssalon.domain.entity.Meeting;
import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.entity.MemberMeeting;
import kr.co.ssalon.domain.repository.CategoryRepository;
import kr.co.ssalon.web.dto.MeetingSearchCondition;
import org.apache.coyote.BadRequestException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class MeetingServiceIntegrationTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MeetingService meetingService;
    @Autowired
    MemberMeetingService memberMeetingService;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    CategoryService categoryService;
    List<Category> categorys = new ArrayList<>();
    String role = "ROLE_USER";

    @BeforeEach
    public void set() {
        Category exercise = Category.builder()
                .name("운동")
                .description("운동 설명")
                .imageUrl(null)
                .build();
        Category study = Category.builder()
                .name("공부")
                .description("공부 설명")
                .imageUrl(null)
                .build();
        Category music = Category.builder()
                .name("음악")
                .description("음악 설명")
                .imageUrl(null)
                .build();
        Category game = Category.builder()
                .name("게임")
                .description("게임 설명")
                .imageUrl(null)
                .build();


        categoryRepository.save(exercise);
        categoryRepository.save(study);
        categoryRepository.save(music);
        categoryRepository.save(game);
        categorys.add(exercise);
        categorys.add(study);
        categorys.add(music);
        categorys.add(game);


    }

    @Test
    @WithMockUser(username = "test") // 테스트 유저 인증 정보
    public void 모임개설() throws Exception {
        //given
        String creatorName = "creator";
        Long memberId = memberService.register("creator", "create@test.com", role).getId();
        Member member = memberService.findMember(memberId);

        MeetingDomainDTO meetingDomainDTO = MeetingDomainDTO.builder()
                .category("운동")
                .meetingPictureUrls(new ArrayList<>(new ArrayList<>(Arrays.asList("http:picture1", "http:picture2"))))
                .title("모임 제목")
                .description("모임 내용")
                .location("서울 신림")
                .capacity(8)
                .meetingDate(LocalDateTime.now()).build();
        //when
        Long moimId = meetingService.createMoim(creatorName, meetingDomainDTO);
        Meeting meeting = meetingService.findMeeting(moimId);
        //then
        assertThat(meeting.getCategory()).isEqualTo(categorys.get(0));
        assertThat(meeting.getMeetingPictureUrls()).containsExactly(meetingDomainDTO.getMeetingPictureUrls().get(0), meetingDomainDTO.getMeetingPictureUrls().get(1));
        assertThat(meeting.getTitle()).isEqualTo(meetingDomainDTO.getTitle());
        assertThat(meeting.getDescription()).isEqualTo(meetingDomainDTO.getDescription());
        assertThat(meeting.getLocation()).isEqualTo(meetingDomainDTO.getLocation());
        assertThat(meeting.getCapacity()).isEqualTo(meetingDomainDTO.getCapacity());
        assertThat(meeting.getMeetingDate()).isEqualTo(meetingDomainDTO.getMeetingDate());
        assertThat(meeting.getCreator()).isEqualTo(member);
        assertThat(meeting.getParticipants()).extracting("member").contains(member);
        assertThat(meeting.getParticipants().size()).isEqualTo(1);
    }

    @Test
    public void 모임참가() throws Exception {
        //given
        String creatorName = "creator";
        String testName1 = "test1";
        String testName2 = "test2";
        memberService.register("creator", "create@test.com", role);
        Long createMoimId1 = createMoim(creatorName, "운동", "운동 제목", "운동 설명", "운동 장소", 2);
        Long createMoimId2 = createMoim(creatorName, "공부", "공부 제목", "공부 설명", "공부 장소", 4);

        Long memberId = memberService.register(testName1, "test1@test.com", role).getId();
        memberService.register(testName2, "test2@test.com", role).getId();
        Member member = memberService.findMember(memberId);
        Long moimId1 = meetingService.findMeeting(createMoimId1).getId();
        Long moimId2 = meetingService.findMeeting(createMoimId2).getId();
        //when
        Long joinId1 = meetingService.join(testName1, moimId1);
        Meeting joinMeeting1 = meetingService.findMeeting(moimId1);

        Long joinId2 = meetingService.join(testName1, moimId2);
        Meeting joinMeeting2 = meetingService.findMeeting(moimId2);

        Member joinMember = memberService.findMember(memberId);
        Member creator = memberService.findMember(creatorName);
        MemberMeeting joinMemberMeeting1 = memberMeetingService.findMemberMeeting(joinId1);
        MemberMeeting joinMemberMeeting2 = memberMeetingService.findMemberMeeting(joinId2);

        //then
        assertThat(creator.getJoinedMeetings().size()).isEqualTo(2);
        assertThat(joinMeeting1.getParticipants()).extracting("member").contains(member);
        assertThat(joinMeeting1.getParticipants().size()).isEqualTo(2);
        assertThrows(BadRequestException.class, () -> {
            meetingService.join(testName1, moimId1);
        });

        assertThrows(BadRequestException.class,()->{
            meetingService.join(testName2, moimId1);
        });

        assertThat(joinMeeting2.getParticipants()).extracting("member").contains(member);
        assertThat(joinMeeting2.getParticipants().size()).isEqualTo(2);

        assertThat(joinMember.getJoinedMeetings().size()).isEqualTo(2);
        assertThat(joinMember.getJoinedMeetings()).contains(joinMemberMeeting1, joinMemberMeeting2);
    }

    @Test
    public void 모임목록조회() throws Exception {
        //given
        String creatorName = "creator";
        memberService.register("creator", "create@test.com", role);
        for (int i = 0; i < 35; i++) {
            if (i < 15)
                createMoim(creatorName, "운동", "운동 제목", "운동 설명", "운동 장소", 3);
            if (14 < i && i < 30)
                createMoim(creatorName, "공부", "공부 제목", "공부 설명", "공부 장소", 2);
            if (30 < i)
                createMoim(creatorName, "음악", "음악 제목", "음악 설명", "음악 장소", 10);
        }

        //when
        MeetingSearchCondition msc1 = MeetingSearchCondition.builder()
                .category("운동")
                .build();
        MeetingSearchCondition msc2 = MeetingSearchCondition.builder()
                .category("공부")
                .build();
        MeetingSearchCondition msc3 = MeetingSearchCondition.builder()
                .category("음악")
                .build();
        PageRequest pageRequest1 = PageRequest.of(0, 10);
        PageRequest pageRequest2 = PageRequest.of(1, 10);
        Page<Meeting> exerciseMoims = meetingService.getMoims(msc1, pageRequest1);
        Page<Meeting> studyMoims = meetingService.getMoims(msc2, pageRequest2);
        Page<Meeting> musicMoims = meetingService.getMoims(msc3, pageRequest1);

        //then
        assertThat(exerciseMoims.getTotalElements()).isEqualTo(15);
        assertThat(exerciseMoims.getTotalPages()).isEqualTo(2);
        assertThat(exerciseMoims.getSize()).isEqualTo(10);
        assertThat(exerciseMoims.getNumber()).isEqualTo(0);
        assertThat(exerciseMoims.getNumberOfElements()).isEqualTo(10);
        assertThat(exerciseMoims.getContent().size()).isEqualTo(10);

        assertThat(studyMoims.getTotalElements()).isEqualTo(15);
        assertThat(studyMoims.getTotalPages()).isEqualTo(2);
        assertThat(studyMoims.getSize()).isEqualTo(10);
        assertThat(studyMoims.getNumber()).isEqualTo(1);
        assertThat(studyMoims.getNumberOfElements()).isEqualTo(5);
        assertThat(studyMoims.getContent().size()).isEqualTo(5);
    }

    @Test
    public void 모임수정() throws Exception {
        //given
        String creatorName = "creator";
        memberService.register("creator", "create@test.com", role);
        Long moimId = createMoim(creatorName, "운동", "운동 제목", "운동 설명", "운동 장소", 3);

        //when
        MeetingDomainDTO meetingDomainDTO = MeetingDomainDTO.builder()
                .category("음악")
                .meetingPictureUrls(new ArrayList<>(new ArrayList<>(Arrays.asList("http:picture1", "http:picture2"))))
                .title("음악 제목")
                .description("음악 내용")
                .location("강남")
                .capacity(12)
                .meetingDate(LocalDateTime.now()).build();


        Long editMoimId = meetingService.editMoim(creatorName, moimId, meetingDomainDTO);

        Meeting editedMoim = meetingService.findMeeting(editMoimId);

        //then
        assertThat(editedMoim.getCategory().getName()).isEqualTo("음악");
        assertThat(editedMoim.getTitle()).isEqualTo("음악 제목");
        assertThat(editedMoim.getDescription()).isEqualTo("음악 내용");
        assertThat(editedMoim.getLocation()).isEqualTo("강남");
        assertThat(editedMoim.getCapacity()).isEqualTo(12);
        assertThrows(BadRequestException.class,()->{
            meetingService.editMoim("notCreatorName", moimId, meetingDomainDTO);
        });
    }
    @Test
    public void 모임삭제() throws Exception{
        //given
        String creatorName = "creator";
        String name = "participant";
        memberService.register("creator", "create@test.com", role);
        memberService.register("participant1", "p1@naver.com", role);
        memberService.register("participant2", "p1@naver.com", role);
        memberService.register("participant3", "p1@naver.com", role);
        memberService.register("participant4", "p1@naver.com", role);
        Long moimId1 = createMoim(creatorName, "운동", "운동 제목", "운동 설명", "운동 장소", 5);
        Long moimId2 = createMoim(creatorName, "게임", "게임 제목", "게임 설명", "게임 장소", 5);
        meetingService.join(name + 1, moimId1);
        meetingService.join(name + 1, moimId2);
        meetingService.join(name + 2, moimId1);
        meetingService.join(name + 3, moimId1);
        meetingService.join(name + 4, moimId1);
        //when
        meetingService.deleteMoim(creatorName, moimId1);
        Member creator = memberService.findMember(creatorName);
        Member member1 = memberService.findMember(name + 1);
        Member member2 = memberService.findMember(name + 2);
        Member member3 = memberService.findMember(name + 3);
        Member member4 = memberService.findMember(name + 4);
        //then
        assertThrows(BadRequestException.class, () -> {
            meetingService.findMeeting(moimId1);
        });
        assertThat(creator.getJoinedMeetings().size()).isEqualTo(1);
        assertThat(member1.getJoinedMeetings().size()).isEqualTo(1);
        assertThat(member2.getJoinedMeetings().size()).isEqualTo(0);
        assertThat(member3.getJoinedMeetings().size()).isEqualTo(0);
        assertThat(member4.getJoinedMeetings().size()).isEqualTo(0);
    }


    public Long createMoim(String creatorName, String category, String title, String description, String location, Integer capacity) throws Exception {
        MeetingDomainDTO meetingDomainDTO = MeetingDomainDTO.builder()
                .category(category)
                .meetingPictureUrls(new ArrayList<>(new ArrayList<>(Arrays.asList("http:picture1", "http:picture2"))))
                .title(title)
                .description(description)
                .location(location)
                .capacity(capacity)
                .meetingDate(LocalDateTime.now()).build();
        return meetingService.createMoim(creatorName, meetingDomainDTO);
    }
}
