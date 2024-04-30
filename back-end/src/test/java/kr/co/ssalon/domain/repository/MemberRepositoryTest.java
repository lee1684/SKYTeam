package kr.co.ssalon.domain.repository;

import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.entity.Region;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;


    @Test
    @DisplayName("MemberRepository.save() 테스트")
    @WithMockUser(username = "test") // 테스트 유저 인증 정보
    public void 회원DB저장() {
        // given

        // username이 "test"인 소셜로그인 유저가 이미 저장돼 있다고 가정
        Member member = Member.createMember("test", "test@test.com", "ROLE_USER");
        memberRepository.save(member);

        // 현재 소셜 로그인한 유저(@WithMockUser) 조회
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Member currentUser = null;
        Optional<Member> optionalMember = memberRepository.findByUsername(username);
        if (optionalMember.isPresent()) {
            currentUser = optionalMember.get();
        }

        // 온보딩 페이지에서 입력된 추가정보
        currentUser.setNickname("닉네임");
        List<String> interests = new ArrayList<>(Arrays.asList("독서", "영화감상"));
        currentUser.setInterests(interests);
        currentUser.setAddress(Region.GYEONGGIDO.getLocalName());

        // when
        currentUser = memberRepository.save(currentUser);

        // then
        assertThat(currentUser.getNickname()).isEqualTo("닉네임");
        assertThat(currentUser.getInterests()).containsAll(Arrays.asList("독서", "영화감상"));
        assertThat(currentUser.getAddress()).isEqualTo(Region.GYEONGGIDO.getLocalName());
    }
}
