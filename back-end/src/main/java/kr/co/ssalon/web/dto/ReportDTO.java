package kr.co.ssalon.web.dto;

import kr.co.ssalon.domain.entity.Report;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportDTO {
    private Long id;
    private Long reporterId;
    private Long reportedUserId;
    private List<String> reportPictureUrls;
    private String reason;
    private Boolean isSolved;
    private LocalDateTime reportDate;
    private LocalDateTime solvedDate;

    public ReportDTO(Report report) {
        this.id = report.getId();
        this.reporterId = report.getReporter().getId();
        this.reportedUserId = report.getReportedMember().getId();
        this.reportPictureUrls = report.getReportPictureUrls();
        this.reason = report.getReason();
        this.isSolved = report.getIsSolved();
        this.reportDate = report.getReportDate();
        this.solvedDate = report.getSolvedDate();
    }
}
