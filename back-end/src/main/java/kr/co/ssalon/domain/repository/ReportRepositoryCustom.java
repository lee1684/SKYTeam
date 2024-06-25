package kr.co.ssalon.domain.repository;

import kr.co.ssalon.domain.entity.Meeting;
import kr.co.ssalon.domain.entity.Report;
import kr.co.ssalon.web.dto.MeetingSearchCondition;
import kr.co.ssalon.web.dto.ReportSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReportRepositoryCustom {

    Page<Report> searchMyReports(Long userId, ReportSearchCondition reportSearchCondition, Pageable pageable);
}
