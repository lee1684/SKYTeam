package kr.co.ssalon.domain.entity;

import jakarta.persistence.*;
import kr.co.ssalon.web.dto.ReportDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.apache.coyote.BadRequestException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id")
    private Member reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_member_id")
    private Member reportedMember;

    @ElementCollection
    @CollectionTable(name = "report_picture", joinColumns = @JoinColumn(name = "report_id"))
    private final List<String> reportPictureUrls = new ArrayList<>();

    private String reason;

    private Boolean isSolved;

    private LocalDateTime reportDate;
    private LocalDateTime solvedDate;

    protected Report() {}

    public void changeIsSolvedTrue() {
        this.isSolved = true;
        this.solvedDate = LocalDateTime.now();
    }
    public void changeIsSolvedFalse() {
        this.isSolved = false;
        this.solvedDate = null;
    }

    public void addReportPictureUrls(List<String> reportPictureUrls) {
        if(reportPictureUrls != null) {
            for (String reportPictureUrl : reportPictureUrls) {
                getReportPictureUrls().add(reportPictureUrl);
            }
        }
    }

    public void changeReason(String reason) {
        this.reason = reason != null ? reason : this.reason;
    }

    public void changeReportPictureUrls(List<String> reportPictureUrls) {
        if(reportPictureUrls != null) {
            this.reportPictureUrls.clear();
            this.reportPictureUrls.addAll(reportPictureUrls);
        }
    }


    public static Report createReport (Member reporter, Member reportedMember, String reason, List<String> reportPictureUrls) throws BadRequestException {
            Report report = Report.builder()
                    .reporter(reporter)
                    .reportedMember(reportedMember)
                    .reason(reason)
                    .reportDate(LocalDateTime.now())
                    .isSolved(false)
                    .build();

            report.addReportPictureUrls(reportPictureUrls);
            return report;
    }
}
