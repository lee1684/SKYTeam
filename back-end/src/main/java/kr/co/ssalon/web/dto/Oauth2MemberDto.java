package kr.co.ssalon.web.dto;

import jakarta.validation.constraints.NotBlank;
import kr.co.ssalon.oauth2.OAuth2Response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Oauth2MemberDto {

    @NotBlank
    private String username;
    @NotBlank
    private String email;
    @NotBlank
    private String role;

    public Oauth2MemberDto(OAuth2Response oAuth2Response) {
        this.username =  oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
        this.email = oAuth2Response.getEmail();
        if (this.role.isEmpty()) {
            this.role = "ROLE_USER";
        }
    }
}
