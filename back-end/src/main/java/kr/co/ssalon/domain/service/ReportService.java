package kr.co.ssalon.domain.service;

import kr.co.ssalon.domain.entity.*;
import kr.co.ssalon.domain.repository.ReportRepository;
import kr.co.ssalon.web.dto.MeetingSearchCondition;
import kr.co.ssalon.web.dto.ReportDTO;
import kr.co.ssalon.web.dto.ReportSearchCondition;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;

    @Transactional
    public Report createReport(Report report) throws BadRequestException {
        reportRepository.save(report);
        return report;
    }

    @Transactional
    public Report editReport(Long reportId, ReportDTO reportDTO) throws BadRequestException {
        Report report = findReport(reportId);
        report.changeReason(reportDTO.getReason());
        return report;
    }

    public List<Report> getMyReports(Long userId) throws BadRequestException {
        return reportRepository.findByReporterId(userId);
    }

    public Page<Report> getMyReports(Long userId, ReportSearchCondition reportSearchCondition, Pageable pageable) {
        Page<Report> reports = reportRepository.searchMyReports(userId, reportSearchCondition, pageable);
        return reports;
    }

    @Transactional
    public Boolean changeState(Long reportId) throws BadRequestException {
        Report report = findReport(reportId);

        if(report.getIsSolved()) {
            report.changeIsSolvedFalse();
            report.changeSolvedDate();
        } else {
            report.changeIsSolvedTrue();
            report.deleteSolvedDate();
        }
        return report.getIsSolved();
    }

    public Report findReport(Long reportId) throws BadRequestException {
        Optional<Report> findReport = reportRepository.findById(reportId);
        Report report = ValidationService.validationReport(findReport);
        return report;
    }

}
