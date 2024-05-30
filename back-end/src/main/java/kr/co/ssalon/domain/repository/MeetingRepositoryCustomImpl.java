package kr.co.ssalon.domain.repository;

import com.querydsl.core.types.NullExpression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.ListPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import kr.co.ssalon.domain.dto.MeetingOrder;
import kr.co.ssalon.domain.entity.Meeting;
import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.entity.MemberMeeting;
import kr.co.ssalon.domain.entity.QMemberMeeting;
import kr.co.ssalon.domain.service.MeetingService;
import kr.co.ssalon.web.dto.MeetingSearchCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.querydsl.jpa.JPAExpressions.select;
import static kr.co.ssalon.domain.entity.QMeeting.meeting;
import static kr.co.ssalon.domain.entity.QMemberMeeting.memberMeeting;

@Component
public class MeetingRepositoryCustomImpl implements MeetingRepositoryCustom {

    MeetingService meetingService;
    EntityManager em;
    JPAQueryFactory query;

    @Autowired
    public MeetingRepositoryCustomImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public Page<Meeting> searchMoims(MeetingSearchCondition meetingSearchCondition, String username, Pageable pageable) {

        // Find member by username (assuming you have a method to do this)
        Member member = findMemberByUsername(username);




        List<Meeting> content = query
                .selectFrom(meeting)
                .where(
                        categoryNameEq(meetingSearchCondition.getCategory()),
                        isEndEq(meetingSearchCondition.getIsEnd()),
                        isParticipantEq(meetingSearchCondition.getIsParticipant(), member)
                )
                .orderBy(orderEq(meetingSearchCondition.getOrder(), member))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> totalCountQuery = query
                .select(meeting.count())
                .from(meeting)
                .where(
                        categoryNameEq(meetingSearchCondition.getCategory()),
                        isEndEq(meetingSearchCondition.getIsEnd()),
                        isParticipantEq(meetingSearchCondition.getIsParticipant(), member)

                );
        return PageableExecutionUtils.getPage(content, pageable, totalCountQuery::fetchOne);


    }

    // 카테고리 필터링
    private BooleanExpression categoryNameEq(String category) {
        return category != null ? meeting.category.name.eq(category) : null;
    }

    // 종료 여부 필터링
    private BooleanExpression isEndEq(Boolean isEnd) {
        return isEnd != null ? meeting.isFinished.eq(isEnd) : null;
    }

    private BooleanExpression isParticipantEq(Boolean isParticipant, Member member) {
        if (isParticipant == null) {
            return null;
        }
        if (isParticipant) {
            return meeting.in(
                    select(memberMeeting.meeting)
                            .from(memberMeeting)
                            .where(memberMeeting.member.eq(member))
            );
        } else {
            return meeting.notIn(
                    select(memberMeeting.meeting)
                            .from(memberMeeting)
                            .where(memberMeeting.member.eq(member))
            );
        }
    }

    // 정렬 필터링
    private OrderSpecifier<?> orderEq(MeetingOrder meetingOrder, Member member) {
        Order desc = Order.DESC;
        Order asc = Order.ASC;
        if (meetingOrder == MeetingOrder.CAPACITY) {
            return new OrderSpecifier<>(desc, meeting.capacity);
        }
        if (meetingOrder == MeetingOrder.NUMBER) {
            return new OrderSpecifier<>(asc, meeting.id);
        }
        if (meetingOrder == MeetingOrder.RECENT) {
            return new OrderSpecifier<>(desc, meeting.meetingDate);
        }
        if (meetingOrder == MeetingOrder.RECOMMEND) {
            String recommendation = member.getMeetingRecommendation();
            if (recommendation != null && !recommendation.isEmpty()) {
                List<Long> recommendedIds = Arrays.stream(recommendation
                                .replaceAll("[\\[\\]]", "")
                                .split(","))
                        .map(Long::parseLong)
                        .collect(Collectors.toList());
                return new OrderSpecifier<>(Order.ASC, Expressions.stringTemplate(
                        "FIELD({0}, {1})", meeting.id, String.join(",", recommendedIds.stream().map(String::valueOf).collect(Collectors.toList()))
                ));
            }
        }
        return new OrderSpecifier(asc, NullExpression.DEFAULT, OrderSpecifier.NullHandling.Default);
    }

    @Override
    public List<Meeting> findMeetingsByCategoryId(Long categoryId) {
        return em.createQuery(
                        "SELECT m FROM Meeting m WHERE m.category.id = :categoryId", Meeting.class)
                .setParameter("categoryId", categoryId)
                .getResultList();
    }

    private Member findMemberByUsername(String username) {
        // Implement a method to find a Member entity by username
        // Example:
        return em.createQuery("SELECT m FROM Member m WHERE m.username = :username", Member.class)
                .setParameter("username", username)
                .getSingleResult();
    }

}
