package kr.co.ssalon.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import kr.co.ssalon.domain.entity.Category;
import kr.co.ssalon.domain.entity.Meeting;
import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.entity.MemberMeeting;
import kr.co.ssalon.domain.service.MeetingService;
import kr.co.ssalon.domain.service.MemberService;
import kr.co.ssalon.jwt.JWTUtil;
import kr.co.ssalon.web.dto.MeetingDTO;
import kr.co.ssalon.web.dto.MeetingForm;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class MeetingApiControllerTest {


    @Autowired
    private MockMvc mvc;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MemberService memberService;
    @Autowired
    private MeetingService meetingService;


    @BeforeEach
    public void clear() {
        meetingService.clear();
        memberService.clear();
    }


    @DisplayName("모임 개설 API (POST: /moims) 테스트")
    @Test
    public void 모임개설테스트() throws Exception {
        // given
        String role = "ROLE_USER";
        String name = "naver 2313124213";
        memberService.register(name, "test@naver.com", role);
        String accessToken = jwtUtil.createJwt("Authorization", name, role, 600000L);
        MeetingForm meetingForm = MeetingForm.builder()
                .categoryName("카테고리 이름")
                .title("모임 타이틀")
                .description("모임 설명")
                .location("모임 장소")
                .capacity(5)
                .meetingDate(LocalDateTime.now())
                .build();
        String json = objectMapper.writeValueAsString(meetingForm);

        // when
        mvc.perform(MockMvcRequestBuilders.post("/moims")
                        .cookie(createCookieAccess("Authorization", accessToken))
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Meeting meeting = meetingService.find(1L);
        Category category = meeting.getCategory();
        // then
        assertThat(meeting.getCategory()).isEqualTo(category);
        assertThat(meeting.getTitle()).isEqualTo(meeting.getTitle());
        assertThat(meeting.getDescription()).isEqualTo(meetingForm.getDescription());
        assertThat(meeting.getLocation()).isEqualTo(meetingForm.getLocation());
        assertThat(meeting.getCapacity()).isEqualTo(meetingForm.getCapacity());
        assertThat(meeting.getMeetingDate()).isEqualTo(meetingForm.getMeetingDate());
    }

    @DisplayName("모임 정보 조회 API (GET: /moims/{moimId})")
    @Test
    public void 모임정보조회테스트() throws Exception {
        //given
        String role = "ROLE_USER";
        String name1 = "naver 2313124213";
        String name2 = "naver 123324215243";
        MeetingForm meetingForm = MeetingForm.builder()
                .categoryName("카테고리 이름")
                .title("모임 타이틀")
                .description("모임 설명")
                .location("모임 장소")
                .capacity(5)
                .meetingDate(LocalDateTime.now())
                .build();
        Long memberId1 = memberService.register(name1, "test1@naver.com", role);
        Long memberId2 = memberService.register(name2, "test2@naver.com", role);
        String accessToken = jwtUtil.createJwt("Authorization", name1, role, 600000L);
        Long meetingId = meetingService.register(memberId1, meetingForm.getTitle(), meetingForm.getDescription(), meetingForm.getLocation(), meetingForm.getCapacity());
        Long participantId = meetingService.participant(memberId2, meetingId);
        Meeting meeting = meetingService.find(meetingId);

        //when - then
        mvc.perform(MockMvcRequestBuilders.get("/moims/{moimId}", meetingId)
                        .cookie(createCookieAccess("Authorization", accessToken))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(meetingForm.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(meetingForm.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.location").value(meetingForm.getLocation()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.capacity").value(meetingForm.getCapacity()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meetingDate").value(meetingForm.getMeetingDate()));
    }


    @DisplayName("모임 참가자 목록 조회 API (GET: /moims/{moimId}/users)")
    @Test
    public void 모임참가자목록조회() throws Exception{
        //given
        String role = "ROLE_USER";
        String name1 = "naver 2313124213";
        String name2 = "naver 123324215243";
        MeetingForm meetingForm = MeetingForm.builder()
                .categoryName("카테고리 이름")
                .title("모임 타이틀")
                .description("모임 설명")
                .location("모임 장소")
                .capacity(5)
                .meetingDate(LocalDateTime.now())
                .build();
        Long memberId1 = memberService.register(name1, "test1@naver.com", role);
        Long memberId2 = memberService.register(name2, "test2@naver.com", role);
        String accessToken = jwtUtil.createJwt("Authorization", name1, role, 600000L);
        Long meetingId = meetingService.register(memberId1, meetingForm.getTitle(), meetingForm.getDescription(), meetingForm.getLocation(), meetingForm.getCapacity());
        Long participantId = meetingService.participant(memberId2, meetingId);

        //when - then
        mvc.perform(MockMvcRequestBuilders.get("/moims/{moimId}/users", meetingId)
                        .cookie(createCookieAccess("Authorization", accessToken))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect() //리스트니까 {} -> 바꿔서 저장해라
                .andExpect()
                .andExpect();

    }
    @DisplayName("모임 참가 API (POST: /moims/{moimId}/users)")
    @Test
    public void 모임참가() throws Exception{
        //given
        String role = "ROLE_USER";
        String name1 = "naver 2313124213";
        String name2 = "naver 123324215243";
        MeetingForm meetingForm = MeetingForm.builder()
                .categoryName("카테고리 이름")
                .title("모임 타이틀")
                .description("모임 설명")
                .location("모임 장소")
                .capacity(5)
                .meetingDate(LocalDateTime.now())
                .build();
        Long memberId1 = memberService.register(name1, "test1@naver.com", role);
        Long memberId2 = memberService.register(name2, "test2@naver.com", role);
        String accessToken = jwtUtil.createJwt("Authorization", name2, role, 600000L);
        Long meetingId = meetingService.register(memberId1, meetingForm.getTitle(), meetingForm.getDescription(), meetingForm.getLocation(), meetingForm.getCapacity());
        //when
        mvc.perform(MockMvcRequestBuilders.post("/moims/{moimId}/users", meetingId)
                        .cookie(createCookieAccess("Authorization", accessToken))
                )
                .andExpect(MockMvcResultMatchers.status().isOk());

        Member creator = memberService.find(memberId1);
        Member member2 = memberService.find(memberId2);
        Meeting meeting = meetingService.find(meetingId);
        List<MemberMeeting> participants = meeting.getParticipants();
        //then
        assertThat(participants.size()).isEqualTo(2);
        assertThat(participants).extracting("member").containsExactly(creator, member2);
    }

    public Cookie createCookieAccess(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60 * 10);
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }


}
