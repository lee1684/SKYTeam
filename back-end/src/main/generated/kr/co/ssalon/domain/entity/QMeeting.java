package kr.co.ssalon.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMeeting is a Querydsl query type for Meeting
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMeeting extends EntityPathBase<Meeting> {

    private static final long serialVersionUID = 1432625649L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMeeting meeting = new QMeeting("meeting");

    public final NumberPath<Integer> capacity = createNumber("capacity", Integer.class);

    public final QCategory category;

    public final QMember creator;

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath location = createString("location");

    public final DateTimePath<java.time.LocalDateTime> meetingDate = createDateTime("meetingDate", java.time.LocalDateTime.class);

    public final ListPath<String, StringPath> meetingPictureUrls = this.<String, StringPath>createList("meetingPictureUrls", String.class, StringPath.class, PathInits.DIRECT2);

    public final ListPath<MemberMeeting, QMemberMeeting> participants = this.<MemberMeeting, QMemberMeeting>createList("participants", MemberMeeting.class, QMemberMeeting.class, PathInits.DIRECT2);

    public final QPayment payment;

    public final QTicket ticket;

    public final StringPath title = createString("title");

    public QMeeting(String variable) {
        this(Meeting.class, forVariable(variable), INITS);
    }

    public QMeeting(Path<? extends Meeting> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMeeting(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMeeting(PathMetadata metadata, PathInits inits) {
        this(Meeting.class, metadata, inits);
    }

    public QMeeting(Class<? extends Meeting> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.category = inits.isInitialized("category") ? new QCategory(forProperty("category")) : null;
        this.creator = inits.isInitialized("creator") ? new QMember(forProperty("creator"), inits.get("creator")) : null;
        this.payment = inits.isInitialized("payment") ? new QPayment(forProperty("payment"), inits.get("payment")) : null;
        this.ticket = inits.isInitialized("ticket") ? new QTicket(forProperty("ticket"), inits.get("ticket")) : null;
    }

}

