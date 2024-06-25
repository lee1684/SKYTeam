package kr.co.ssalon.web.dto;

import jakarta.validation.constraints.NotBlank;
import kr.co.ssalon.domain.entity.Ticket;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketDTO {

    @NonNull
    private Long id;
    @NotBlank
    private Long meetingId;
    @NotBlank
    private String decoration;

    public TicketDTO(Ticket ticket) {
        this.id = ticket.getId();
        this.meetingId = ticket.getMeeting().getId();
        this.decoration = ticket.getDecoration();
    }
}
