package kr.co.ssalon.domain.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketDTO {

    private Long id;
    private Long meetingId;
    private String decoration;
}
