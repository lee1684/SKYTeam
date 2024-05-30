package kr.co.ssalon.domain.repository;

import kr.co.ssalon.domain.entity.Category;
import kr.co.ssalon.domain.entity.Meeting;
import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.entity.Region;
import kr.co.ssalon.domain.service.MeetingService;
import kr.co.ssalon.web.dto.MeetingSearchCondition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MeetingRepositoryTest {

    @MockBean
    private MeetingRepository meetingRepository;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private CategoryRepository categoryRepository;

/*
    @Test
    @DisplayName("MeetingRepository.searchMoims 메소드 테스트")
    @WithMockUser(username = "test")
    public void 모임목록조회() {
        // given

        // 현재 소셜 로그인한 유저(@WithMockUser) 조회
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Member currentUser = null;
        Optional<Member> optionalMember = memberRepository.findByUsername(username);
        if (optionalMember.isPresent()) {
            currentUser = optionalMember.get();
        }

        // "운동" 카테고리 객체 생성 및 테스트 DB에 저장
        Category category = Category.builder()
                .id(0L)
                .name("운동")
                .build();
        category = categoryRepository.save(category);

        // "운동", "서울특별시" 모임 객체 생성 및 테스트 DB에 저장
        Meeting meeting = Meeting.builder()
                .category(category)
                .location(Region.SEOUL.getLocalName())
                .build();
        meetingRepository.save(meeting);

        // 모임 목록 필터("운동") 객체 생성
        MeetingSearchCondition meetingSearchCondition = MeetingSearchCondition.builder()
                .category("운동")
                .build();

        // Pageable 객체 생성
        PageRequest pageable = PageRequest.of(0, 10);

        // when (모임 목록 조회)
        Page<Meeting> result = meetingRepository.searchMoims(meetingSearchCondition, currentUser.getUsername(), pageable);

        // then (조회한 모임에 대한 검증)
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getCategory().getName()).isEqualTo("운동");
    }

 */
}