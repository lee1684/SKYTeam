package kr.co.ssalon.web.dto;

import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.entity.MemberDates;
import kr.co.ssalon.domain.entity.Report;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlackListSearchDTO {

    private Long id;

    private String blackReason;
    private LocalDateTime blackTime;

    private String username;
    private String email;
    private String nickname;
    private Character gender;
    private MemberDates memberDates;

    public BlackListSearchDTO(Member member) {

        this.id = member.getId();
        this.blackReason = member.getBlackReason();
        this.blackTime = member.getBlackTime();
        this.username = member.getUsername();
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.gender = member.getGender();
        this.memberDates = member.getMemberDates();

   }
}
