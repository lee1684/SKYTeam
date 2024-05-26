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

    @NotBlank
    private String nickname;
    @NotBlank
    private String profilePicture;
    @NotBlank
    private String message;
    @NotBlank
    private LocalDateTime date;
    @NotBlank
    private String email;

    public MessageDTO(Message messageEntity) {
        Member messageSendMember = messageEntity.getMemberMeeting().getMember();

        this.nickname = messageSendMember.getNickname();
        this.profilePicture = messageSendMember.getProfilePictureUrl();
        this.message = messageEntity.getMessage();
        this.date = messageEntity.getSentAt();
        this.email = messageSendMember.getEmail();
    }
}
