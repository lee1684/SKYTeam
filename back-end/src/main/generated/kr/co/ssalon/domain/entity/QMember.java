package kr.co.ssalon.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -1339038684L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMember member = new QMember("member1");

    public final StringPath address = createString("address");

    public final StringPath blackReason = createString("blackReason");

    public final StringPath email = createString("email");

    public final ComparablePath<Character> gender = createComparable("gender", Character.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<String, StringPath> interests = this.<String, StringPath>createList("interests", String.class, StringPath.class, PathInits.DIRECT2);

    public final StringPath introduction = createString("introduction");

    public final ListPath<MemberMeeting, QMemberMeeting> joinedMeetings = this.<MemberMeeting, QMemberMeeting>createList("joinedMeetings", MemberMeeting.class, QMemberMeeting.class, PathInits.DIRECT2);

    public final QMemberDates memberDates;

    public final StringPath nickname = createString("nickname");

    public final ListPath<Payment, QPayment> payments = this.<Payment, QPayment>createList("payments", Payment.class, QPayment.class, PathInits.DIRECT2);

    public final StringPath profilePictureUrl = createString("profilePictureUrl");

    public final StringPath role = createString("role");

    public final StringPath username = createString("username");

    public QMember(String variable) {
        this(Member.class, forVariable(variable), INITS);
    }

    public QMember(Path<? extends Member> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMember(PathMetadata metadata, PathInits inits) {
        this(Member.class, metadata, inits);
    }

    public QMember(Class<? extends Member> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.memberDates = inits.isInitialized("memberDates") ? new QMemberDates(forProperty("memberDates")) : null;
    }

}

