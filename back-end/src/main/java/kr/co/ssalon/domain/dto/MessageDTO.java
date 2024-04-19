package kr.co.ssalon.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDTO {

    private Long id;
    private Long meetingId;
    private String message;
    private LocalDateTime sentAt;
}
