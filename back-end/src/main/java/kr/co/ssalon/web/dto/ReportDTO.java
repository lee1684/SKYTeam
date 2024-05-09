package kr.co.ssalon.web.dto;

import kr.co.ssalon.domain.entity.Report;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportDTO {
    private Long id;
    private Long reporterId;
    private Long reportedUserId;
    private String reason;
    private Boolean isSolved;

    public ReportDTO(Report report) {
        this.id = report.getId();
        this.reporterId = report.getReporter().getId();
        this.reportedUserId = report.getReporter().getId();
        this.reason = report.getReason();
        this.isSolved = report.getIsSolved();
    }
}
