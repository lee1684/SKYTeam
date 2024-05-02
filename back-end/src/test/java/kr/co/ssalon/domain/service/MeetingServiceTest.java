package kr.co.ssalon.domain.service;

import kr.co.ssalon.domain.entity.*;
import kr.co.ssalon.domain.repository.MeetingRepository;
import kr.co.ssalon.domain.repository.MemberMeetingRepository;
import kr.co.ssalon.oauth2.CustomOAuth2Member;
import kr.co.ssalon.web.controller.annotation.WithCustomMockUser;
import kr.co.ssalon.web.dto.MeetingDTO;
import kr.co.ssalon.web.dto.MeetingSearchCondition;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class MeetingServiceTest {

    @Mock
    private MeetingRepository meetingRepository;

    @Mock
    private MemberMeetingRepository memberMeetingRepository;

    @Mock
    private MemberService memberService;

    @InjectMocks
    private MeetingService meetingService;

    @Test
    @DisplayName("MeetingService.getMoims 메소드 테스트")
    public void 모임목록조회() {
        // given

        // "운동" 카테고리 Mock 객체 생성
        Category category = mock(Category.class);
        when(category.getName()).thenReturn("운동");

        // "운동", "서울특별시" 모임 Mock 객체 생성
        Meeting meeting = mock(Meeting.class);
        when(meeting.getCategory()).thenReturn(category);
        when(meeting.getLocation()).thenReturn(Region.SEOUL.getLocalName());

        // 모임 목록 필터("운동", "서울특별시") 객체 생성
        MeetingSearchCondition meetingSearchCondition = MeetingSearchCondition.builder()
                .categoryName("운동")
                .region(Region.SEOUL)
                .build();

        // Pageable 객체 생성
        PageRequest pageable = PageRequest.of(0, 10);

        // "운동", "서울특별시" 모임 Mock 객체에 대한 Page 객체 생성
        Page<Meeting> meetingsPage = new PageImpl<>(Collections.singletonList(meeting));
        when(meetingRepository.searchMoims(meetingSearchCondition, pageable)).thenReturn(meetingsPage);

        // when (모임 목록 조회)
        Page<Meeting> result = meetingService.getMoims(meetingSearchCondition, pageable);

        // then (조회한 모임에 대한 검증)
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getLocation()).isEqualTo("서울특별시");
        assertThat(result.getContent().get(0).getCategory().getName()).isEqualTo("운동");
    }

    @Test
    @DisplayName("MeetingService.join() 테스트")
    @WithCustomMockUser
    public void 모임참가() throws BadRequestException {
        // given
        Long moimId = 11111L;

        // 현재 로그인된 사용자의 username, email, role 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomOAuth2Member customOAuth2Member = (CustomOAuth2Member) authentication.getPrincipal();

        // Meeting Mock 객체 생성
        Meeting meeting = mock(Meeting.class);
        Category category = mock(Category.class);
        Payment payment = mock(Payment.class);
        Member creator = mock(Member.class);
        Ticket ticket = mock(Ticket.class);
        when(meeting.getCategory()).thenReturn(category);
        when(meeting.getPayment()).thenReturn(payment);
        when(meeting.getCreator()).thenReturn(creator);
        when(meeting.getTicket()).thenReturn(ticket);
        when(meeting.getDescription()).thenReturn("독서 모임입니다.");

        // MemberService stub
        Member currentUser = mock(Member.class);
        when(memberService.findMember(anyString())).thenReturn(currentUser);

        // MeetingRepository stub
        when(meetingRepository.findById(anyLong())).thenReturn(Optional.ofNullable(meeting));

        // MemberMeetingRepository stub
        when(memberMeetingRepository.save(any())).thenReturn(any());

        // when
        MeetingDTO joinedMeeting = meetingService.join(customOAuth2Member, moimId);

        // then
        assertThat(joinedMeeting.getDescription()).isEqualTo("독서 모임입니다.");
    }
}
