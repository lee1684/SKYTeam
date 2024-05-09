package kr.co.ssalon.domain.entity;

import jakarta.persistence.*;
import kr.co.ssalon.web.dto.ReportDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.apache.coyote.BadRequestException;

import java.time.LocalDateTime;

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

    private String reason;

    private Boolean isSolved = false;

    protected Report() {}

    public void changeIsSolvedTrue() { this.isSolved = true;}
    public void changeIsSolvedFalse() {this.isSolved = false;}
    public void changeReason(String reason) { this.reason = reason; }

    public static Report createReport (Member reporter, Member reportedMember, String reason) throws BadRequestException {
            Report report = Report.builder()
                    .reporter(reporter)
                    .reportedMember(reportedMember)
                    .reason(reason)
                    .build();
            return report;
    }
}
