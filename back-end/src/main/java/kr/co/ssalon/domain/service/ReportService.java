package kr.co.ssalon.domain.service;

import kr.co.ssalon.domain.entity.*;
import kr.co.ssalon.domain.repository.ReportRepository;
import kr.co.ssalon.web.dto.CategoryDTO;
import kr.co.ssalon.web.dto.ReportDTO;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
        return reportRepository.findByReportId(userId);
    }

    @Transactional
    public Boolean changeState(Long reportId) throws BadRequestException {
        Report report = findReport(reportId);

        if(report.getIsSolved()) {
            report.changeIsSolvedFalse();
        } else {
            report.changeIsSolvedTrue();
        }
        return report.getIsSolved();
    }

    public Report findReport(Long reportId) throws BadRequestException {
        Optional<Report> findReport = reportRepository.findById(reportId);
        Report report = ValidationService.validationReport(findReport);
        return report;
    }
}
