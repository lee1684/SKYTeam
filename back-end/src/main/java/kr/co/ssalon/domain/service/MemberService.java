package kr.co.ssalon.domain.service;

import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public Long register(String username, String email, String role) throws Exception {
        validationMember(username);
        Member member = Member.builder()
                .username(username)
                .email(email)
                .role(role)
                .build();
        memberRepository.save(member);
        return member.getId();
    }
    private void validationMember(String name) throws Exception {
        Member findMember = memberRepository.findByUsername(name);
        if (findMember != null) {
            throw new Exception("이미 해당 소셜 로그인을 한 회원이 DB에 존재합니다. 업데이트를 진행합니다.");
        }
    }
    @Transactional
    public void oauthUpdate(String name, String email, String role) {
        Member findMember = memberRepository.findByUsername(name);
        findMember.changeEmail(email);
        findMember.changeRole(role);
    }
}
