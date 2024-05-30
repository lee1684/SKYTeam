package kr.co.ssalon.web.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class ChatConnectedMemberDTO {

    private String nickname;

    private String profilePictureUrl;

    public ChatConnectedMemberDTO(String nickname, String profilePictureUrl) {
        this.nickname = nickname;
        this.profilePictureUrl = profilePictureUrl;
    }
}
