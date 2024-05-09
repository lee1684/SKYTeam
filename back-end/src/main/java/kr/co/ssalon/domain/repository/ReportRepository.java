package kr.co.ssalon.domain.repository;

import kr.co.ssalon.domain.entity.Report;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReportRepository extends CrudRepository<Report, Long> {
    @Query("SELECT r FROM Report r WHERE r.reporter.id = :reporterId")
    List<Report> findByReporterId(@Param("reporterId") Long reporterId);
}
