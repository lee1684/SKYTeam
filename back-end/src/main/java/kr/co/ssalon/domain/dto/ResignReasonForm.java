package kr.co.ssalon.domain.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResignReasonForm {
    private String type;
    private String reason;
}