package com.edgar.module.sys.repository.querydsl;

import static com.mysema.query.types.PathMetadataFactory.*;
import com.edgar.module.sys.repository.domain.UserMessageSub;


import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;




/**
 * QUserMessageSub is a Querydsl query type for UserMessageSub
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QUserMessageSub extends com.mysema.query.sql.RelationalPathBase<UserMessageSub> {

    private static final long serialVersionUID = 881708875;

    public static final QUserMessageSub userMessageSub = new QUserMessageSub("user_message_sub");

    public final StringPath messageType = createString("messageType");

    public final StringPath subscriber = createString("subscriber");

    public final NumberPath<Integer> userId = createNumber("userId", Integer.class);

    public final NumberPath<Integer> userMsgSubId = createNumber("userMsgSubId", Integer.class);

    public final com.mysema.query.sql.PrimaryKey<UserMessageSub> primary = createPrimaryKey(userMsgSubId);

    public QUserMessageSub(String variable) {
        super(UserMessageSub.class, forVariable(variable), "null", "user_message_sub");
        addMetadata();
    }

    public QUserMessageSub(String variable, String schema, String table) {
        super(UserMessageSub.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QUserMessageSub(Path<? extends UserMessageSub> path) {
        super(path.getType(), path.getMetadata(), "null", "user_message_sub");
        addMetadata();
    }

    public QUserMessageSub(PathMetadata<?> metadata) {
        super(UserMessageSub.class, metadata, "null", "user_message_sub");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(messageType, ColumnMetadata.named("message_type").ofType(12).withSize(32));
        addMetadata(subscriber, ColumnMetadata.named("subscriber").ofType(12).withSize(32));
        addMetadata(userId, ColumnMetadata.named("user_id").ofType(4).withSize(10));
        addMetadata(userMsgSubId, ColumnMetadata.named("user_msg_sub_id").ofType(4).withSize(10).notNull());
    }

}

