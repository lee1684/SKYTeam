package kr.co.ssalon.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class QrValidationResponseDTO {
    private Long userId;
    private String nickname;
    private String profilePictureUrl;
    private Boolean attendance;

    public QrValidationResponseDTO(Long userId, String nickname, String profilePictureUrl, Boolean attendance) {
        this.userId = userId;
        this.nickname = nickname;
        this.profilePictureUrl = profilePictureUrl;
        this.attendance = attendance;
    }
}
