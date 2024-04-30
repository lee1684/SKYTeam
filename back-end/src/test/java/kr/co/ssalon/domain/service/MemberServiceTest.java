package kr.co.ssalon.domain.service;

import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.entity.Region;
import kr.co.ssalon.domain.repository.MemberRepository;
import kr.co.ssalon.web.dto.MemberDTO;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;


    @Test
    @DisplayName("MemberService.signup() 테스트")
    @WithMockUser(username = "test")
    public void 회원가입() throws BadRequestException {
        // given

        // 현재 소셜 로그인한 유저(@WithMockUser) 객체 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        //   MemberRepository.findByUsername() stub
        Member member = Member.createMember("test", "test@test.com", "ROLE_USER");
        Optional<Member> optionalMember = Optional.of(member);
        when(memberRepository.findByUsername(username)).thenReturn(optionalMember);

        // 온보딩 페이지에서 입력된 추가정보
        MemberDTO additionalInfo = MemberDTO.builder()
                .nickname("닉네임")
                .interests(new ArrayList<>(Arrays.asList("독서", "영화감상")))
                .address(Region.GANGWONDO.getLocalName())
                .build();

        // when
        Member joinedMember = memberService.signup(username, additionalInfo);

        // then
        assertThat(joinedMember.getNickname()).isEqualTo("닉네임");
        assertThat(joinedMember.getInterests()).isEqualTo(new ArrayList<>(Arrays.asList("독서", "영화감상")));
        assertThat(joinedMember.getAddress()).isEqualTo(Region.GANGWONDO.getLocalName());
    }
}
