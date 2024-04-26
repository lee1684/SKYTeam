package kr.co.ssalon.domain;

import kr.co.ssalon.domain.entity.Meeting;
import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.entity.MemberMeeting;
import kr.co.ssalon.domain.service.MeetingService;
import kr.co.ssalon.domain.service.MemberService;
import org.aspectj.lang.annotation.After;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class MeetingControllerTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MeetingService meetingService;

    List<Long> memberIds = new ArrayList<>();
    List<Long> meetingIds = new ArrayList<>();

    @BeforeEach
    public void setting() throws Exception {
        String role = "ROLE_USER";
        for (int i = 1; i < 11; i++) {
            Long registerId = memberService.register("test" + i, "test" + i + "@naver.com", role);
            memberIds.add(registerId);
        }
        for (int i = 1; i < 4; i++) {
            Long registerId = meetingService.register(memberIds.get(i - 1), "제목 테스트" + i, "설명 테스트" + i, "장소 테스트" + i, 5);
            meetingIds.add(registerId);
        }
        for (int i = 5; i < 8; i++) {
            meetingService.participant(memberIds.get(i - 1), meetingIds.get(0));
        }
        for (int i = 8; i < 10; i++) {
            meetingService.participant(memberIds.get(i - 1), meetingIds.get(1));
        }
        meetingService.participant(memberIds.get(9), meetingIds.get(2));

    }


    @AfterEach
    public void clear() {
        meetingService.clear();
        memberService.clear();
    }

    @DisplayName("모임 상세 정보 조회 테스트: 모임의 제목, 장소, 설명, 시간, 증표 조회")
    @Test
    public void 모임상세정보조회1() throws Exception {
        //given - when - then
        for (long meetingId = 1; meetingId < 4; meetingId++) {
            Meeting meeting = meetingService.find(meetingIds.get((int) meetingId - 1));
            LocalDateTime date = LocalDateTime.now();
            meeting.changeMeetingDate(date);
            assertThat(meeting).extracting("title").isEqualTo("제목 테스트" + meetingId);
            assertThat(meeting).extracting("location").isEqualTo("장소 테스트" + meetingId);
            assertThat(meeting).extracting("description").isEqualTo("설명 테스트" + meetingId);
            assertThat(meeting).extracting("capacity").isEqualTo(5);
            assertThat(meeting).extracting("meetingDate").isEqualTo(date);
            assertThat(meeting.getTicket().getDecoration()).isEqualTo("기본 설정");
        }
    }

    @DisplayName("모임 상세 정보 조회 테스트: 인원 조회")
    @Test
    public void 모임상세정보조회2() throws Exception {
        //given
        Meeting meeting1 = meetingService.find(meetingIds.get(0));
        Meeting meeting2 = meetingService.find(meetingIds.get(1));
        Meeting meeting3 = meetingService.find(meetingIds.get(2));
        List<Member> meeting1JoinMembers = new ArrayList<>();
        List<Member> meeting2JoinMembers = new ArrayList<>();
        List<Member> meeting3JoinMembers = new ArrayList<>();
        for (long memberId = 5; memberId < 8; memberId++) {
            Member member = memberService.find(memberIds.get((int) memberId - 1));
            meeting1JoinMembers.add(member);
        }
        for (long memberId = 8; memberId < 10; memberId++) {
            Member member = memberService.find(memberIds.get((int) memberId - 1));
            meeting2JoinMembers.add(member);
        }
        meeting3JoinMembers.add(memberService.find(memberIds.get(9)));

        //when
        List<MemberMeeting> participants1 = meeting1.getParticipants();
        List<MemberMeeting> participants2 = meeting2.getParticipants();
        List<MemberMeeting> participants3 = meeting3.getParticipants();
        //then
        assertThat(participants1.size()).isEqualTo(3);
        assertThat(participants1).extracting("member").containsExactlyElementsOf(meeting1JoinMembers);
        assertThat(participants2.size()).isEqualTo(2);
        assertThat(participants2).extracting("member").containsExactlyElementsOf(meeting2JoinMembers);
        assertThat(participants3.size()).isEqualTo(1);
        assertThat(participants3).extracting("member").containsExactlyElementsOf(meeting3JoinMembers);
    }
}
