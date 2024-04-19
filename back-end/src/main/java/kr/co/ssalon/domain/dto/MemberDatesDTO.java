package kr.co.ssalon.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDatesDTO {

    private LocalDateTime joinDate;
    private LocalDateTime lastLoginDate;
}
