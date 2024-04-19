package kr.co.ssalon.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.entity.Message;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDTO {

    @NotNull
    private Long id;
    @NotBlank
    private Long memberMeetingId;
    @NotBlank
    private String message;
    @NotBlank
    private LocalDateTime sentAt;

    public MessageDTO(Message message) {
        this.id = message.getId();
        this.memberMeetingId = message.getMemberMeeting().getId();
        this.message = message.getMessage();
        this.sentAt = message.getSentAt();
    }
}
