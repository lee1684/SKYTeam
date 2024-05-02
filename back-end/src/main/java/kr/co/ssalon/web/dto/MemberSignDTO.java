package kr.co.ssalon.web.dto;

import kr.co.ssalon.domain.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberSignDTO {

    private String nickname;
    private String address;
    private String role;
    private String profilePictureUrl;
    private String introduction;

    public MemberSignDTO(Member member) {
        this.nickname = member.getNickname();
        this.address = member.getAddress();
        this.role = member.getRole();
        this.profilePictureUrl = member.getProfilePictureUrl();
        this.introduction = member.getIntroduction();
    }
}
