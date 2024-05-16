package kr.co.ssalon.web.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class MemberSignupVerificationDTO {

    private Boolean isRegistered;

    public MemberSignupVerificationDTO(Boolean isRegistered) {
        this.isRegistered = isRegistered;
    }
}
