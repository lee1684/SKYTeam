package kr.co.ssalon.domain.service;

import kr.co.ssalon.domain.dto.MemberDomainDTO;
import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.repository.MemberRepository;
import kr.co.ssalon.web.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public Member register(String username, String email, String role) throws Exception {
        validationUsername(username);
        Member member = Member.createMember(username, email, role);
        member = memberRepository.save(member);
        return member;
    }

    @Transactional
    public Member oauthUpdate(String username, String email, String role) throws BadRequestException {
        Optional<Member> findMember = memberRepository.findByUsername(username);
        Member member = ValidationService.validationMember(findMember);
        member.changeEmail(email);
        member.changeRole(role);
        return member;
    }


    public Member findMember(String username) throws BadRequestException {
        Optional<Member> findMember = memberRepository.findByUsername(username);
        Member member = ValidationService.validationMember(findMember);
        return member;
    }

    public Member findMember(Long id) throws BadRequestException {
        Optional<Member> findMember = memberRepository.findById(id);
        Member member = ValidationService.validationMember(findMember);
        return member;
    }


    public void validationUsername(String username) throws Exception {
        Optional<Member> findMember = memberRepository.findByUsername(username);
        if (findMember.isPresent()) {
            throw new Exception("이미 해당 소셜 로그인을 한 회원이 DB에 존재합니다. 업데이트를 진행합니다.");
        }
    }

    @Transactional
    public Member signup(String username, MemberDomainDTO additionalInfo) throws BadRequestException {
        Member currentUser = findMember(username);

        currentUser.initDetailSignInfo(
                additionalInfo.getNickname(), additionalInfo.getProfilePictureUrl(),
                additionalInfo.getGender(), additionalInfo.getAddress(), additionalInfo.getIntroduction(),
                additionalInfo.getInterests(), additionalInfo.getBlackReason()
        );
        return currentUser;
    }
}
