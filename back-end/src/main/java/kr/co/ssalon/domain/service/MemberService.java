package kr.co.ssalon.domain.service;

import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.repository.MemberRepository;
import kr.co.ssalon.web.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public Long register(String username, String email, String role) throws Exception {
        validationUsername(username);
        Member member = Member.createMember(username, email, role);
        memberRepository.save(member);
        return member.getId();
    }
    @Transactional
    public void oauthUpdate(String username, String email, String role) throws BadRequestException {
        Optional<Member> findMember = memberRepository.findByUsername(username);
        Member member = validationMember(findMember);
        member.changeEmail(email);
        member.changeRole(role);
    }


    public Member findMember(String username) throws BadRequestException {
        Optional<Member> findMember = memberRepository.findByUsername(username);
        Member member = validationMember(findMember);
        return member;
    }

    public Member findMember(Long id) throws BadRequestException {
        Optional<Member> findMember = memberRepository.findById(id);
        Member member = validationMember(findMember);
        return member;
    }

    private Member validationMember(Optional<Member> member) throws BadRequestException {
        if (member.isPresent()) {
            return member.get();
        }else
            throw new BadRequestException("해당 회원을 찾을 수 없습니다");
    }

    private void validationUsername(String username) throws Exception {
        Optional<Member> findMember = memberRepository.findByUsername(username);
        if (findMember.isPresent()) {
            throw new Exception("이미 해당 소셜 로그인을 한 회원이 DB에 존재합니다. 업데이트를 진행합니다.");
        }
    }

    @Transactional
    public Member signup(String username, MemberDTO additionalInfo) throws BadRequestException {
        Member currentUser = this.findMember(username);

        currentUser.setNickname(additionalInfo.getNickname());
        currentUser.setProfilePictureUrl(additionalInfo.getProfilePictureUrl());
        currentUser.setGender(additionalInfo.getGender());
        currentUser.setAddress(additionalInfo.getAddress());
        currentUser.setIntroduction(additionalInfo.getIntroduction());
        currentUser.setInterests(additionalInfo.getInterests());
        currentUser.setBlackReason(additionalInfo.getBlackReason());

        memberRepository.save(currentUser);

        return currentUser;
    }
}
