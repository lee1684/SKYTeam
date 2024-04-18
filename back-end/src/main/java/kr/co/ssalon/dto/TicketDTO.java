package kr.co.ssalon.dto;

import kr.co.ssalon.domain.entity.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketDTO {
    private Long id;
    private Meeting meeting;
    private String decoration;
}
