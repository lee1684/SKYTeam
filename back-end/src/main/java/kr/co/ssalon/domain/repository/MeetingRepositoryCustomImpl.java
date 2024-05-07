package kr.co.ssalon.domain.repository;

import com.querydsl.core.types.NullExpression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import kr.co.ssalon.domain.dto.MeetingFilter;
import kr.co.ssalon.domain.entity.Meeting;
import kr.co.ssalon.web.dto.MeetingSearchCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;

import java.util.List;

import static kr.co.ssalon.domain.entity.QMeeting.meeting;

@Component
public class MeetingRepositoryCustomImpl implements MeetingRepositoryCustom {

    EntityManager em;
    JPAQueryFactory query;

    @Autowired
    public MeetingRepositoryCustomImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public Page<Meeting> searchMoims(MeetingSearchCondition meetingSearchCondition, Pageable pageable) {

        List<Meeting> content = query
                .selectFrom(meeting)
                .where(
                        categoryNameEq(meetingSearchCondition.getCategory()))
                .orderBy(orderEq(meetingSearchCondition.getMeetingFilter()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> totalCountQuery = query
                .select(meeting.count())
                .from(meeting)
                .where(
                        categoryNameEq(meetingSearchCondition.getCategory())
                );
        return PageableExecutionUtils.getPage(content, pageable, totalCountQuery::fetchOne);


    }

    // 카테고리 필터링
    private BooleanExpression categoryNameEq(String category) {
        return category != null ? meeting.category.name.eq(category) : null;
    }

    // 정렬 필터링
    private OrderSpecifier<?> orderEq(MeetingFilter meetingFilter) {
        Order desc = Order.DESC;
        Order asc = Order.ASC;
        if (meetingFilter == MeetingFilter.CAPACITY) {
            return new OrderSpecifier<>(desc, meeting.capacity);
        }
        if (meetingFilter == MeetingFilter.NUMBER) {
            return new OrderSpecifier<>(asc, meeting.id);
        }
        if (meetingFilter == MeetingFilter.RECENT) {
            return new OrderSpecifier<>(desc, meeting.meetingDate);
        }
        return new OrderSpecifier(asc, NullExpression.DEFAULT, OrderSpecifier.NullHandling.Default);
    }

}
