package kr.co.ssalon.web.dto;

import kr.co.ssalon.domain.entity.QrLink;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class MeetingOutReasonDTO {

    private String reason;

    public MeetingOutReasonDTO(String reason) {
        this.reason = reason;
    }
}
