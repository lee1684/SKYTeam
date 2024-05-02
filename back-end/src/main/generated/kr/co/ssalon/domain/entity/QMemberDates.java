package kr.co.ssalon.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMemberDates is a Querydsl query type for MemberDates
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QMemberDates extends BeanPath<MemberDates> {

    private static final long serialVersionUID = 1735993537L;

    public static final QMemberDates memberDates = new QMemberDates("memberDates");

    public final DateTimePath<java.time.LocalDateTime> joinDate = createDateTime("joinDate", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> lastLoginDate = createDateTime("lastLoginDate", java.time.LocalDateTime.class);

    public QMemberDates(String variable) {
        super(MemberDates.class, forVariable(variable));
    }

    public QMemberDates(Path<? extends MemberDates> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMemberDates(PathMetadata metadata) {
        super(MemberDates.class, metadata);
    }

}

