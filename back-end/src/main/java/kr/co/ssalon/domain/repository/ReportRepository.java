package kr.co.ssalon.domain.repository;

import kr.co.ssalon.domain.entity.Report;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReportRepository extends CrudRepository<Report, Long> {
    @Query("SELECT r FROM Report r WHERE r.id = :reportId")
    List<Report> findByReportId(@Param("reportId") Long reportId);
}
