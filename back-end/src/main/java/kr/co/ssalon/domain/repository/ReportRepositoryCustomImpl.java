package kr.co.ssalon.domain.repository;

import com.querydsl.core.types.NullExpression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kr.co.ssalon.domain.dto.MeetingOrder;
import kr.co.ssalon.domain.dto.ReportOrder;
import kr.co.ssalon.domain.entity.Meeting;
import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.entity.Report;
import kr.co.ssalon.web.dto.MeetingSearchCondition;
import kr.co.ssalon.web.dto.ReportSearchCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import static kr.co.ssalon.domain.entity.QMeeting.meeting;
import static kr.co.ssalon.domain.entity.QReport.report;

@Component
public class ReportRepositoryCustomImpl implements ReportRepositoryCustom{

    EntityManager em;
    JPAQueryFactory query;

    @Autowired
    public ReportRepositoryCustomImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public Page<Report> searchMyReports(Long userId, ReportSearchCondition reportSearchCondition, Pageable pageable) {

        List<Report> content = query
                .selectFrom(report)
                .where(
                        reporterIdEq(userId),
                        isSolvedEq(reportSearchCondition.getIsSolved())
                )
                .orderBy(orderEq(reportSearchCondition.getOrder()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> totalCountQuery = query
                .select(report.count())
                .from(report)
                .where(
                        reporterIdEq(userId),
                        isSolvedEq(reportSearchCondition.getIsSolved())
                );
        return PageableExecutionUtils.getPage(content, pageable, totalCountQuery::fetchOne);
    }

    // 리포트 작성자 필터링
    private BooleanExpression reporterIdEq(Long userId) {
        return userId != null ? report.reporter.id.eq(userId) : null;
    }

    // isSolved 필터링
    private BooleanExpression isSolvedEq(Boolean isSolved) {
        return isSolved != null ? report.isSolved.eq(isSolved) : null;
    }

    // 정렬 필터링
    private OrderSpecifier<?> orderEq(ReportOrder reportOrder) {
        Order desc = Order.DESC;
        Order asc = Order.ASC;
        if (reportOrder == ReportOrder.NUMBER) {
            return new OrderSpecifier<>(asc, report.id);
        }
        if (reportOrder == ReportOrder.RECENT) {
            return new OrderSpecifier<>(desc, report.reportDate);
        }
        return new OrderSpecifier(asc, NullExpression.DEFAULT, OrderSpecifier.NullHandling.Default);
    }
}
