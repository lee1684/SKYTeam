package kr.co.ssalon.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QQrLink is a Querydsl query type for QrLink
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQrLink extends EntityPathBase<QrLink> {

    private static final long serialVersionUID = -1213492411L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QQrLink qrLink = new QQrLink("qrLink");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMemberMeeting memberMeeting;

    public final StringPath qrKey = createString("qrKey");

    public QQrLink(String variable) {
        this(QrLink.class, forVariable(variable), INITS);
    }

    public QQrLink(Path<? extends QrLink> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QQrLink(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QQrLink(PathMetadata metadata, PathInits inits) {
        this(QrLink.class, metadata, inits);
    }

    public QQrLink(Class<? extends QrLink> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.memberMeeting = inits.isInitialized("memberMeeting") ? new QMemberMeeting(forProperty("memberMeeting"), inits.get("memberMeeting")) : null;
    }

}

