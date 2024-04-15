package kr.co.ssalon.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Oauth2MemberDto {

    private String username;
    private String email;
    private String role;

    public Oauth2MemberDto(String username, String role) {
        this.username = username;
        this.role = role;
    }

}
