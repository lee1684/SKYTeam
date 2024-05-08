package kr.co.ssalon.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class QrValidationResponseDTO {
    private String username;
    private String profilePictureUrl;

    public QrValidationResponseDTO(String username, String profilePictureUrl) {
        this.username = username;
        this.profilePictureUrl = profilePictureUrl;
    }
}
