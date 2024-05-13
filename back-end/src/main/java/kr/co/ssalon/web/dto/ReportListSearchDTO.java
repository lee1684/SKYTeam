package kr.co.ssalon.web.dto;

import kr.co.ssalon.domain.entity.Meeting;
import kr.co.ssalon.domain.entity.Report;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportListSearchDTO {

    private Long id;

    private Boolean isSolved;

    private Long reporterId;
    private Long reportedId;
    private List<String> reportPictureUrls;
    private String reason;
    private LocalDateTime reportDate;
    private LocalDateTime solvedDate;


    public ReportListSearchDTO(Report report) {
        this.id = report.getId();
        this.isSolved = report.getIsSolved();
        this.reporterId = report.getReporter().getId();
        this.reportedId = report.getReporter().getId();
        this.reportPictureUrls = report.getReportPictureUrls();
        this.reason = report.getReason();
        this.reportDate = report.getReportDate();
        this.solvedDate = report.getSolvedDate();
   }
}
