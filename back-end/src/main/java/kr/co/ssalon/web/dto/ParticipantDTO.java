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

    private Long userId;
    private String nickname;
    private String profilePictureUrl;
    private Boolean attendance;

    public ParticipantDTO(MemberMeeting memberMeeting) {
        this.userId = memberMeeting.getMember().getId();
        this.nickname = memberMeeting.getMember().getNickname();
        this.profilePictureUrl = memberMeeting.getMember().getProfilePictureUrl();
        this.attendance = memberMeeting.isAttendance();
    }
}
