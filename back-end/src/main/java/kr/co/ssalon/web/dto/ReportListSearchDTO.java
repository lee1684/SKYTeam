package kr.co.ssalon.web.dto;

import kr.co.ssalon.domain.entity.Meeting;
import kr.co.ssalon.domain.entity.Report;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportListSearchDTO {

    private Long reportId;
    private Boolean isSolved;
    private LocalDateTime reportDate;
    private LocalDateTime solvedDate;

    public ReportListSearchDTO(Report report) {
        this.reportId = report.getId();
        this.isSolved = report.getIsSolved();
        this.reportDate = report.getReportDate();
        this.solvedDate = report.getSolvedDate();
    }
}
