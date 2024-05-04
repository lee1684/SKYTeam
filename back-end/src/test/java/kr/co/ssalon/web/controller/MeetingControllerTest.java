package kr.co.ssalon.web.controller;

import kr.co.ssalon.domain.entity.*;
import kr.co.ssalon.domain.service.MeetingService;
import kr.co.ssalon.web.dto.MeetingSearchCondition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MeetingController.class)
public class MeetingControllerTest {

    @MockBean
    private MeetingService meetingService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("모임 목록 조회 API(GET /api/moims) 테스트")
    @WithMockUser(username = "test")
    public void 모임목록조회API() throws Exception {
        // given

        // "운동" 카테고리 Mock 객체 생성
        Category category = mock(Category.class);
        when(category.getName()).thenReturn("운동");

        // Payment, Member(creator), Ticket Mock 객체 생성
        Payment payment = mock(Payment.class);
        Member creator = mock(Member.class);
        Ticket ticket = mock(Ticket.class);

        // "운동", "서울특별시" 모임 Mock 객체 생성
        Meeting meeting = mock(Meeting.class);
        when(meeting.getCategory()).thenReturn(category);
        when(meeting.getLocation()).thenReturn(Region.SEOUL.getLocalName());
        when(ticket.getMeeting()).thenReturn(meeting);

        // @NotNull 통과를 위해 payment, creator, ticket 빈 객체(mock) 추가
        when(meeting.getPayment()).thenReturn(payment);
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
        when(meetingService.getMoims(meetingSearchCondition, pageable)).thenReturn(meetingsPage);

        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/moims")
                .param("category", "운동")
                .param("page", "0")
                .param("size", "10"));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}
