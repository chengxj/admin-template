package com.edgar.module.sys.repository.querydsl;

import static com.mysema.query.types.PathMetadataFactory.*;
import com.edgar.module.sys.repository.domain.Message;


import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;




/**
 * QMessage is a Querydsl query type for Message
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QMessage extends com.mysema.query.sql.RelationalPathBase<Message> {

    private static final long serialVersionUID = 112886144;

    public static final QMessage message = new QMessage("message");

    public final StringPath appNo = createString("appNo");

    public final BooleanPath isSuccess = createBoolean("isSuccess");

    public final StringPath messageContent = createString("messageContent");

    public final NumberPath<Integer> messageId = createNumber("messageId", Integer.class);

    public final StringPath messageType = createString("messageType");

    public final NumberPath<Integer> referenceId = createNumber("referenceId", Integer.class);

    public final StringPath referenceTable = createString("referenceTable");

    public final com.mysema.query.sql.PrimaryKey<Message> primary = createPrimaryKey(messageId);

    public QMessage(String variable) {
        super(Message.class, forVariable(variable), "null", "message");
        addMetadata();
    }

    public QMessage(String variable, String schema, String table) {
        super(Message.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QMessage(Path<? extends Message> path) {
        super(path.getType(), path.getMetadata(), "null", "message");
        addMetadata();
    }

    public QMessage(PathMetadata<?> metadata) {
        super(Message.class, metadata, "null", "message");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(appNo, ColumnMetadata.named("app_no").ofType(12).withSize(32));
        addMetadata(isSuccess, ColumnMetadata.named("is_success").ofType(-7));
        addMetadata(messageContent, ColumnMetadata.named("message_content").ofType(12).withSize(140).notNull());
        addMetadata(messageId, ColumnMetadata.named("message_id").ofType(4).withSize(10).notNull());
        addMetadata(messageType, ColumnMetadata.named("message_type").ofType(12).withSize(32).notNull());
        addMetadata(referenceId, ColumnMetadata.named("reference_id").ofType(4).withSize(10));
        addMetadata(referenceTable, ColumnMetadata.named("reference_table").ofType(12).withSize(32));
    }

}

