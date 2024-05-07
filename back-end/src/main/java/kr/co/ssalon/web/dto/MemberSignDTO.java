package kr.co.ssalon.web.dto;

import kr.co.ssalon.domain.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberSignDTO {

    private String nickname;
    private String profilePictureUrl;
    private Character gender;
    private String address;
    private String role;
    private String introduction;
    private List<String> interests;
    private LocalDateTime joinDate;

    public MemberSignDTO(Member member) {
        this.nickname = member.getNickname();
        this.profilePictureUrl = member.getProfilePictureUrl();
        this.gender = member.getGender();
        this.address = member.getAddress();
        this.role = member.getRole();
        this.introduction = member.getIntroduction();
        this.interests = member.getInterests();
        this.joinDate = member.getMemberDates() != null ? member.getMemberDates().getJoinDate() : null;
    }
}
