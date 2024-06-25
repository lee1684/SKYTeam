package kr.co.ssalon.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NicknameDTO {

    private String nickname;

    public String getNickname() {
        return nickname;
    }

    public void NicknameDTO(String nickname) {
        this.nickname = nickname;
    }
}
