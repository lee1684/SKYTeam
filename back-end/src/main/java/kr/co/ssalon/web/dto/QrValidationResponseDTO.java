package kr.co.ssalon.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class QrValidationResponseDTO {
    private String nickname;
    private String profilePictureUrl;

    public QrValidationResponseDTO(String nickname, String profilePictureUrl) {
        this.nickname = nickname;
        this.profilePictureUrl = profilePictureUrl;
    }
}
