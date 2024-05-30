package kr.co.ssalon.domain.repository;

import com.querydsl.core.types.NullExpression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import kr.co.ssalon.domain.dto.BlackListOrder;
import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.web.dto.BlackListSearchCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;

import java.util.List;

import static kr.co.ssalon.domain.entity.QMember.member;

@Component
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom{

    EntityManager em;
    JPAQueryFactory query;

    @Autowired
    public MemberRepositoryCustomImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public Page<Member> getBlackList(BlackListSearchCondition blackListSearchCondition, Pageable pageable) {

        List<Member> content = query
                .selectFrom(member)
                .where(
                        blackReasonIsNotNull()
                )
                .orderBy(orderEq(blackListSearchCondition.getOrder()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> totalCountQuery = query
                .select(member.count())
                .from(member)
                .where(

                );
        return PageableExecutionUtils.getPage(content, pageable, totalCountQuery::fetchOne);
    }

    private BooleanExpression blackReasonIsNotNull() {
        return member.blackReason.isNotNull();
    }

    // 정렬 필터링
    private OrderSpecifier<?> orderEq(BlackListOrder blackListOrder) {
        Order desc = Order.DESC;
        Order asc = Order.ASC;
        if (blackListOrder == BlackListOrder.NUMBER) {
            return new OrderSpecifier<>(asc, member.id);
        }
        if (blackListOrder == BlackListOrder.RECENT) {
            return new OrderSpecifier<>(desc, member.blackTime);
        }
        if (blackListOrder == BlackListOrder.NAME) {
            return new OrderSpecifier<>(desc, member.nickname);
        }
        return new OrderSpecifier(asc, NullExpression.DEFAULT, OrderSpecifier.NullHandling.Default);
    }
}
