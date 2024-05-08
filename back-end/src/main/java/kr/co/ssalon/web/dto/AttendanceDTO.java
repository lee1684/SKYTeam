package kr.co.ssalon.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class AttendanceDTO {

    private String nickname;
    private String profilePictureUrl;
    private Boolean attendance;

    public AttendanceDTO(String nickname, String profilePictureUrl, Boolean attendance) {
        this.nickname = nickname;
        this.profilePictureUrl = profilePictureUrl;
        this.attendance = attendance;
    }
}
