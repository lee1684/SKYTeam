package kr.co.ssalon.domain.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDTO {

    private Long id;
    private String username;
    private String email;
    private String nickname;
    private String profilePictureUrl;
    private Character gender;
    private String address;
    private String role;
    private String blackReason;
    private MemberDatesDTO memberDatesDTO;
    private List<Long> joinedMeetingIds;
    private List<Long> paymentIds;
}
