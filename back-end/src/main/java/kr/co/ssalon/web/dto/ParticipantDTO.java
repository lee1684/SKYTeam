package kr.co.ssalon.web.dto;

import kr.co.ssalon.domain.entity.MemberMeeting;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParticipantDTO {

    private String nickname;
    private String profilePictureUrl;

    public ParticipantDTO(MemberMeeting memberMeeting) {
        this.nickname = memberMeeting.getMember().getNickname();
        this.profilePictureUrl = memberMeeting.getMember().getProfilePictureUrl();
    }
}
