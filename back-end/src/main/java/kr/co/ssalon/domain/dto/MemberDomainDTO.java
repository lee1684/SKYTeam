package kr.co.ssalon.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDomainDTO {

    private String nickname;
    private String profilePictureUrl;
    private Character gender;
    private String address;
    private String introduction;
    private List<String> interests;
    private String blackReason;
}
