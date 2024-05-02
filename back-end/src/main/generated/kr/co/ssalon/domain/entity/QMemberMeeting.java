package kr.co.ssalon.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberMeeting is a Querydsl query type for MemberMeeting
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberMeeting extends EntityPathBase<MemberMeeting> {

    private static final long serialVersionUID = 1341181303L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberMeeting memberMeeting = new QMemberMeeting("memberMeeting");

    public final QDiary diary;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMeeting meeting;

    public final QMember member;

    public final ListPath<Message, QMessage> messages = this.<Message, QMessage>createList("messages", Message.class, QMessage.class, PathInits.DIRECT2);

    public final QQrLink qrLink;

    public QMemberMeeting(String variable) {
        this(MemberMeeting.class, forVariable(variable), INITS);
    }

    public QMemberMeeting(Path<? extends MemberMeeting> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberMeeting(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberMeeting(PathMetadata metadata, PathInits inits) {
        this(MemberMeeting.class, metadata, inits);
    }

    public QMemberMeeting(Class<? extends MemberMeeting> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.diary = inits.isInitialized("diary") ? new QDiary(forProperty("diary"), inits.get("diary")) : null;
        this.meeting = inits.isInitialized("meeting") ? new QMeeting(forProperty("meeting"), inits.get("meeting")) : null;
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
        this.qrLink = inits.isInitialized("qrLink") ? new QQrLink(forProperty("qrLink"), inits.get("qrLink")) : null;
    }

}

