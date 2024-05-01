package kr.co.ssalon.domain.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import kr.co.ssalon.domain.entity.Meeting;
import kr.co.ssalon.domain.entity.Region;
import kr.co.ssalon.web.dto.MeetingSearchCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;

import java.util.List;

import static kr.co.ssalon.domain.entity.QMeeting.meeting;
import static org.springframework.util.StringUtils.hasText;

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
                        regionEq(meetingSearchCondition.getRegion()),
                        categoryNameEq(meetingSearchCondition.getCategoryName()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> totalCountQuery = query
                .select(meeting.count())
                .from(meeting)
                .where(
                        regionEq(meetingSearchCondition.getRegion()),
                        categoryNameEq(meetingSearchCondition.getCategoryName())
                );
        return PageableExecutionUtils.getPage(content, pageable, totalCountQuery::fetchOne);


    }


    // 지역 필터링
    private BooleanExpression regionEq(Region region) {
        return region != null ? meeting.location.contains(region.getLocalName()) : null;
    }

    // 카테고리 필터링
    private BooleanExpression categoryNameEq(String categoryName) {
        return hasText(categoryName) ? meeting.category.name.eq(categoryName) : null;
    }

}