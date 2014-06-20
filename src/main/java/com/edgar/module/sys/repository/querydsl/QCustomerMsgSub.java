package com.edgar.module.sys.repository.querydsl;

import static com.mysema.query.types.PathMetadataFactory.*;
import com.edgar.module.sys.repository.domain.CustomerMsgSub;


import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;




/**
 * QCustomerMsgSub is a Querydsl query type for CustomerMsgSub
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QCustomerMsgSub extends com.mysema.query.sql.RelationalPathBase<CustomerMsgSub> {

    private static final long serialVersionUID = -726465308;

    public static final QCustomerMsgSub customerMsgSub = new QCustomerMsgSub("customer_msg_sub");

    public final NumberPath<Integer> cusMsgSubId = createNumber("cusMsgSubId", Integer.class);

    public final StringPath messageType = createString("messageType");

    public final StringPath subscriber = createString("subscriber");

    public final com.mysema.query.sql.PrimaryKey<CustomerMsgSub> primary = createPrimaryKey(cusMsgSubId);

    public QCustomerMsgSub(String variable) {
        super(CustomerMsgSub.class, forVariable(variable), "null", "customer_msg_sub");
        addMetadata();
    }

    public QCustomerMsgSub(String variable, String schema, String table) {
        super(CustomerMsgSub.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QCustomerMsgSub(Path<? extends CustomerMsgSub> path) {
        super(path.getType(), path.getMetadata(), "null", "customer_msg_sub");
        addMetadata();
    }

    public QCustomerMsgSub(PathMetadata<?> metadata) {
        super(CustomerMsgSub.class, metadata, "null", "customer_msg_sub");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(cusMsgSubId, ColumnMetadata.named("cus_msg_sub_id").ofType(4).withSize(10).notNull());
        addMetadata(messageType, ColumnMetadata.named("message_type").ofType(12).withSize(32));
        addMetadata(subscriber, ColumnMetadata.named("subscriber").ofType(12).withSize(32));
    }

}

