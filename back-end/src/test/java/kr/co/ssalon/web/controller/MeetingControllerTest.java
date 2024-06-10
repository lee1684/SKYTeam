package kr.co.ssalon.web.controller;

<<<<<<< HEAD
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import kr.co.ssalon.config.JacksonConfig;
import kr.co.ssalon.domain.dto.MeetingDomainDTO;
=======
>>>>>>> develop
import kr.co.ssalon.domain.entity.*;
import kr.co.ssalon.domain.repository.CategoryRepository;
import kr.co.ssalon.domain.repository.MeetingRepository;
import kr.co.ssalon.domain.service.CategoryService;
import kr.co.ssalon.domain.service.MeetingService;
import kr.co.ssalon.domain.service.MemberService;
<<<<<<< HEAD
import kr.co.ssalon.domain.service.RecommendService;
import kr.co.ssalon.oauth2.CustomOAuth2Member;
import kr.co.ssalon.web.controller.annotation.WithCustomMockUser;
import kr.co.ssalon.web.dto.*;
=======
import kr.co.ssalon.oauth2.CustomOAuth2Member;
import kr.co.ssalon.web.controller.annotation.WithCustomMockUser;
import kr.co.ssalon.web.dto.MeetingSearchCondition;
>>>>>>> develop
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
<<<<<<< HEAD
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.*;
import org.springframework.http.converter.HttpMessageNotWritableException;
=======
import org.springframework.data.domain.*;
>>>>>>> develop
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
<<<<<<< HEAD
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
=======
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
>>>>>>> develop
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MeetingController.class)
<<<<<<< HEAD
@Import(JacksonConfig.class)
=======
>>>>>>> develop
public class MeetingControllerTest {

    @MockBean
    private MeetingService meetingService;

    @MockBean
    private MemberService memberService;

    @MockBean
    private MeetingRepository meetingRepository;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private CategoryService categoryService;

<<<<<<< HEAD
    @MockBean
    private RecommendService recommendService;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("모욈 참가 API(POST /api/moims/{moimId}/users) 테스트")
    @WithCustomMockUser(username = "test")
    public void 모임참가API() throws Exception {
        // given
        Long moimId = 1L;

        Meeting meeting = mock(Meeting.class);

        Category category = mock(Category.class);
        when(meeting.getCategory()).thenReturn(category);

        Member creator = mock(Member.class);
        when(meeting.getCreator()).thenReturn(creator);

        when(meetingService.findMeeting(moimId)).thenReturn(meeting);

        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/moims/"+moimId+"/users").with(csrf()));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(meeting.getId().intValue())))
                .andExpect(jsonPath("$.categoryId", is(meeting.getCategory().getId().intValue())))
                .andExpect(jsonPath("$.creatorId", is(meeting.getCreator().getId().intValue())))
        ;
    }



=======
    @Autowired
    private MockMvc mockMvc;

>>>>>>> develop
    @Test
    @DisplayName("모임 목록 조회 API(GET /api/moims) 테스트")
    @WithCustomMockUser(username = "test")
    public void 모임목록조회API() throws Exception {
        // given

        String username = "test";

        // "운동" 카테고리 Mock 객체 생성
        Category category = mock(Category.class);
        when(category.getName()).thenReturn("운동");

        // Payment, Member(creator), Ticket Mock 객체 생성
        Payment payment = mock(Payment.class);
        Member creator = mock(Member.class);
        when(creator.getUsername()).thenReturn("testCreator");
        Ticket ticket = mock(Ticket.class);

        // "운동", "서울특별시" 모임 Mock 객체 생성
        Meeting meeting = mock(Meeting.class);
        when(meeting.getCategory()).thenReturn(category);
        when(meeting.getLocation()).thenReturn(Region.SEOUL.getLocalName());
        when(ticket.getMeeting()).thenReturn(meeting);

        // @NotNull 통과를 위해 payment, creator, ticket 빈 객체(mock) 추가
        // when(meeting.getPayment()).thenReturn(payment);
        when(meeting.getCreator()).thenReturn(creator);
        when(meeting.getTicket()).thenReturn(ticket);

        // 모임 목록 필터("운동", "서울특별시") 객체 생성
        MeetingSearchCondition meetingSearchCondition = MeetingSearchCondition.builder()
                .category("운동")
                .build();

        // Pageable 객체 생성
        Pageable pageable = PageRequest.of(0, 10);

        // "운동", "서울특별시" 모임 Mock 객체에 대한 Page 객체 생성
        Page<Meeting> meetingsPage = new PageImpl<>(Collections.singletonList(meeting));
        when(meetingService.getMoims(meetingSearchCondition, username, pageable)).thenReturn(meetingsPage);
        System.out.println(meetingsPage);


        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/moims")
                .param("category", "운동")
                .param("page", "0")
                .param("size", "10"));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].categoryName",is("운동")));
    }
<<<<<<< HEAD

    @Test
    @DisplayName("모임 개설 API(POST /api/moims) 테스트")
    @WithCustomMockUser(username = "test")
    public void 모임개설API() throws Exception {


        // give
        String username = "test";
        Member member = mock(Member.class);

        List<String> meetingPictureUrls = Arrays.asList("testMeetingPictureUrl1.com", "testMeetingPictureUrl2.com");
        String title = "testTitle";
        String description = "testDescription";
        String location = "testLocation";
        Integer capacity = 10;
        LocalDateTime meetingDate = LocalDateTime.now();
        Integer payment = 1000;
        Boolean isSharable = false;

        Long moimId = 1L;

        // Category 객체 생성
        Category category = Category.builder()
                .id(1L)
                .name("testCategory")
                .description("testCategoryDescription")
                .imageUrl("testCategoryImageUrl")
                .build();

        MeetingDomainDTO meetingDomainDTO = MeetingDomainDTO.builder()
                .category(category.getName())
                .meetingPictureUrls(meetingPictureUrls)
                .title(title)
                .description(description)
                .location(location)
                .capacity(capacity)
                .meetingDate(meetingDate)
                .payment(payment)
                .isSharable(isSharable)
                .build();

        Meeting meeting = Meeting.createMeeting(
                category,
                member,
                meetingDomainDTO.getMeetingPictureUrls(),
                meetingDomainDTO.getTitle(),
                meetingDomainDTO.getDescription(),
                meetingDomainDTO.getLocation(),
                meetingDomainDTO.getCapacity(),
                meetingDomainDTO.getPayment(),
                meetingDomainDTO.getMeetingDate(),
                meetingDomainDTO.getIsSharable()
        );

        when(meetingService.createMoim(username, meetingDomainDTO)).thenReturn(moimId);
        when(meetingService.findMeeting(moimId)).thenReturn(meeting);

        // When
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/moims")
                .contentType("application/json")  // Set the content type to application/json
                .content(objectMapper.writeValueAsString(meetingDomainDTO))
                .with(csrf());

        ResultActions resultActions = mockMvc.perform(requestBuilder);

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryId", is(category.getId().intValue())))
                .andExpect(jsonPath("$.meetingPictureUrls", is(meetingPictureUrls)))
                .andExpect(jsonPath("$.title", is(title)))
                .andExpect(jsonPath("$.description", is(description)))
                .andExpect(jsonPath("$.location", is(location)))
                .andExpect(jsonPath("$.capacity", is(capacity)))
                .andExpect(jsonPath("$.meetingDate", is(meetingDate.toString())))
        ;
    }

    @Test
    @DisplayName("모욈 정보 조회 API(GET /api/moims/{moimId}) 테스트")
    @WithCustomMockUser(username = "test")
    public void 모임정보조회API() throws Exception {
        // given
        Long moimId = 1L;

        Meeting meeting = mock(Meeting.class);
        when(meeting.getId()).thenReturn(moimId);

        Category category = mock(Category.class);
        when(meeting.getCategory()).thenReturn(category);

        Member creator = mock(Member.class);
        when(meeting.getCreator()).thenReturn(creator);

        when(meetingService.findMeeting(moimId)).thenReturn(meeting);

        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/moims/"+moimId).with(csrf()));

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("모욈 정보 수정 API(PATCH /api/moims/{moimId}) 테스트")
    @WithCustomMockUser(username = "test")
    public void 모임정보수정API() throws Exception {
        /*
        // given
        Long moimId = 1L;
        String username = "test";;

        List<String> meetingPictureUrls = Arrays.asList("testMeetingPictureUrl1.com", "testMeetingPictureUrl2.com");
        String title = "testTitle";
        String description = "testDescription";
        String location = "testLocation";
        Integer capacity = 10;
        LocalDateTime meetingDate = LocalDateTime.now();
        Integer payment = 1000;
        Boolean isSharable = false;

        // Category 객체 생성
        Category category = Category.builder()
                .id(1L)
                .name("testCategory")
                .description("testCategoryDescription")
                .imageUrl("testCategoryImageUrl")
                .build();

        MeetingDomainDTO meetingDomainDTO = MeetingDomainDTO.builder()
                .category(category.getName())
                .meetingPictureUrls(meetingPictureUrls)
                .title(title)
                .description(description)
                .location(location)
                .capacity(capacity)
                .meetingDate(meetingDate)
                .payment(payment)
                .isSharable(isSharable)
                .build();

        when(meetingService.editMoim(username, moimId, meetingDomainDTO)).thenReturn(moimId);

        // When
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/api/moims/"+moimId)
                .contentType("application/json")  // Set the content type to application/json
                .content(objectMapper.writeValueAsString(meetingDomainDTO))
                .with(csrf());

        ResultActions resultActions = mockMvc.perform(requestBuilder);

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$", is(moimId.intValue())));

         */
    }

    @Test
    @DisplayName("모욈 해산 API(DELETE /api/moims/{moimId}) 테스트")
    @WithCustomMockUser(username = "test")
    public void 모임해산API() throws Exception {
        // given
        Long moimId = 1L;
        String username = "test";

        when(meetingService.deleteMoim(username, moimId)).thenReturn(moimId);

        // When
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/moims/"+moimId)
                .with(csrf());

        ResultActions resultActions = mockMvc.perform(requestBuilder);

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$", is(moimId.intValue())));
        ;
    }

    @Test
    @DisplayName("모욈 참가자 목록 조회 API(GET /api/moims/{moimId}/users) 테스트")
    @WithCustomMockUser(username = "test")
    public void 모임참가자목록조회API() throws Exception {
        // given
        Long moimId = 1L;

        List<ParticipantDTO> participantsDto = new ArrayList<>();

        ParticipantDTO participantDTO1 = ParticipantDTO.builder()
                .userId(1L)
                .nickname("testNickname1")
                .profilePictureUrl("testProfilePictureUrl1")
                .attendance(true)
                .build();

        ParticipantDTO participantDTO2 = ParticipantDTO.builder()
                .userId(2L)
                .nickname("testNickname2")
                .profilePictureUrl("testProfilePictureUrl2")
                .attendance(false)
                .build();

        participantsDto.add(participantDTO1);
        participantsDto.add(participantDTO2);

        when(meetingService.getUsers(moimId)).thenReturn(participantsDto);

        // When
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/moims/"+moimId+"/users")
                .with(csrf());

        ResultActions resultActions = mockMvc.perform(requestBuilder);

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId", is(participantDTO1.getUserId().intValue())));
        ;
    }

    @Test
    @DisplayName("모임 강퇴 및 탈퇴 API(DELETE /api/moims/{moimId}/users/{userId}) 테스트")
    @WithCustomMockUser(username = "test")
    public void 모임강퇴및탈퇴API() throws Exception {
        // Given
        String username = "test";
        Long moimId = 1L;
        Long userId = 2L;
        String reason = "testReason";

        Member member = mock(Member.class);
        Meeting meeting = mock(Meeting.class);
        String type = "강퇴";

        MeetingOutReasonDTO meetingOutReasonDTO = new MeetingOutReasonDTO(reason);
        MeetingOut meetingOut = MeetingOut.createMeetingOutReason(member, meeting, type, reason);
        when(meetingService.deleteUserFromMoim(username, moimId, userId, reason)).thenReturn(meetingOut);

        // When
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/moims/"+moimId+"/users/"+userId)
                .contentType("application/json")  // Set the content type to application/json
                .content(objectMapper.writeValueAsString(meetingOutReasonDTO))
                .with(csrf());

        ResultActions resultActions = mockMvc.perform(requestBuilder);

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.type", is(type)))
                .andExpect(jsonPath("$.reason", is(reason)))
        ;
    }

    @Test
    @DisplayName("모임 개최자 검증 API(GET /api/moims/{moimId}/creator) 테스트")
    @WithCustomMockUser(username = "test")
    public void 모임개최자검증API() throws Exception {
        // Given
        String username = "test";
        Long moimId = 1L;

        when(meetingService.isCreator(username, moimId)).thenReturn(true);

        // When
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/moims/"+moimId+"/creator")
                .with(csrf());

        ResultActions resultActions = mockMvc.perform(requestBuilder);

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$", is(true)))
        ;
    }

    @Test
    @DisplayName("모임 참여자 여부 검증 API(GET /api/moims/{moimId}/participant")
    @WithCustomMockUser(username = "test")
    public void 모임참여자여부검증API() throws Exception {
        // Given
        String username = "test";
        Long moimId = 1L;

        Member member = mock(Member.class);

        when(memberService.findMember(username)).thenReturn(member);
        when(meetingService.isParticipant(moimId, member)).thenReturn(true);

        // When
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/moims/"+moimId+"/participant")
                .with(csrf());

        ResultActions resultActions = mockMvc.perform(requestBuilder);

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$", is(true)))
        ;
    }


}
=======
}
>>>>>>> develop
