package kr.co.ssalon.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.entity.MemberDates;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDomainDTO {

    private String nickname;
    private String profilePictureUrl;
    private Character gender;
    private String address;
    private String introduction;
    private List<String> interests;
    @Schema(hidden = true)
    private MemberDates memberDates;

    public MemberDomainDTO(Member member) {
        this.nickname = member.getNickname();
        this.profilePictureUrl = member.getProfilePictureUrl();
        this.gender = member.getGender();
        this.address = member.getAddress();
        this.introduction = member.getIntroduction();
        this.interests = member.getInterests();
        this.memberDates = member.getMemberDates();
    }
}
