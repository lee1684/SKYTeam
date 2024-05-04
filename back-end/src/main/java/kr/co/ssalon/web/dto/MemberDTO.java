package kr.co.ssalon.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.entity.MemberMeeting;
import kr.co.ssalon.domain.entity.Payment;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDTO {
    @NotNull
    private Long id;
    @NotBlank
    private String username;
    @NotBlank
    private String email;
    @NotNull
    private String nickname;
    @NotNull
    private String profilePictureUrl;
    @NotNull
    private Character gender;
    @NotNull
    private String address;
    @NotBlank
    private String role;
    private String introduction;
    private List<String> interests;
    private String blackReason;
    @NotBlank
    private LocalDateTime joinDate;
    @NotBlank
    private LocalDateTime lastLoginDate;
    private List<Long> joinedMeetingIds;
    private List<Long> paymentIds;

    public MemberDTO(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.profilePictureUrl = member.getProfilePictureUrl();
        this.gender = member.getGender();
        this.address = member.getAddress();
        this.role = member.getRole();
        this.introduction = member.getIntroduction();
        this.interests = member.getInterests();
        this.blackReason = member.getBlackReason();
        this.joinedMeetingIds = member.getJoinedMeetings().stream().map(MemberMeeting::getId).collect(Collectors.toList());
        this.paymentIds = member.getPayments().stream().map(Payment::getId).collect(Collectors.toList());
    }
}
