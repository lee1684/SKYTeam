package kr.co.ssalon.domain.service;

import kr.co.ssalon.domain.dto.MemberDomainDTO;
import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.entity.Region;
import kr.co.ssalon.domain.repository.MemberRepository;
import kr.co.ssalon.web.dto.MemberDTO;
import org.apache.coyote.BadRequestException;
import kr.co.ssalon.oauth2.CustomOAuth2Member;
import kr.co.ssalon.web.controller.annotation.WithCustomMockUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.security.core.GrantedAuthority;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    String username = "";
    String email = "";
    String role = "";

    @BeforeEach
    public void getUsernameAndEmailAndRole() {
        // 소셜로부터 가져온 유저(@WithMockUser)의 username, email, role 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomOAuth2Member customOAuth2Member = (CustomOAuth2Member) authentication.getPrincipal();

        // username 추출
        username = customOAuth2Member.getUsername();

        // email 추출
        email = customOAuth2Member.getEmail();

        // role 추출
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        role = auth.getAuthority();
    }

    @Test
    @DisplayName("MemberService.signup() 테스트")
    @WithCustomMockUser(username = "username", email = "email@email.com", role = "ROLE_USER")
    public void 회원가입() throws BadRequestException {
        // given

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

        MemberDomainDTO memberDomainDTO = MemberDomainDTO.builder()
                .nickname(additionalInfo.getNickname())
                .interests(additionalInfo.getInterests())
                .address(additionalInfo.getAddress())
                .build();


        // when
        Member joinedMember = memberService.signup(username, memberDomainDTO);

        // then
        assertThat(joinedMember.getNickname()).isEqualTo("닉네임");
        assertThat(joinedMember.getInterests()).isEqualTo(new ArrayList<>(Arrays.asList("독서", "영화감상")));
        assertThat(joinedMember.getAddress()).isEqualTo(Region.GANGWONDO.getLocalName());
    }

    @Test
    @DisplayName("MemberService.register() 테스트")
    @WithCustomMockUser(username = "username", email = "email@email.com", role = "ROLE_USER")
    public void 소셜로그인등록() throws Exception {
        // given

        // MemberRepository.save() stub
        Member socialLoginMember = Member.createMember(username, email, role);
        when(memberRepository.save(any())).thenReturn(socialLoginMember);

        // when (소셜 로그인 등록)
        Member member = memberService.register(username, email, role);

        // then (등록된 소셜 확인)
        assertThat(member.getUsername()).isEqualTo(username);
        assertThat(member.getEmail()).isEqualTo(email);
        assertThat(member.getRole()).isEqualTo(role);
    }

    @Test
    @DisplayName("MemberService.oauthUpdate() 테스트")
    @WithCustomMockUser(username = "username", email = "email@email.com", role = "ROLE_USER")
    public void 소셜정보업데이트() throws Exception {
        // given

        // MemberRepository.save() stub
        Member socialLoginMember = Member.createMember(username, email, role);
        when(memberRepository.save(any())).thenReturn(socialLoginMember);

        // MemberRepository.findByUsername stub
        when(memberRepository.findByUsername(username)).thenReturn(Optional.of(socialLoginMember));

        // when (소셜 로그인 정보 업데이트)
        Member member = memberService.oauthUpdate(username, email, role);

        // then (업데이트된 소셜 정보 확인)
        assertThat(member.getUsername()).isEqualTo(username);
        assertThat(member.getEmail()).isEqualTo(email);
        assertThat(member.getRole()).isEqualTo(role);
    }
}
