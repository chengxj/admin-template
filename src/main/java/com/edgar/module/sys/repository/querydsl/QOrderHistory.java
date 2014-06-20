package com.edgar.module.sys.repository.querydsl;

import static com.mysema.query.types.PathMetadataFactory.*;
import com.edgar.module.sys.repository.domain.OrderHistory;


import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;




/**
 * QOrderHistory is a Querydsl query type for OrderHistory
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QOrderHistory extends com.mysema.query.sql.RelationalPathBase<OrderHistory> {

    private static final long serialVersionUID = -679459955;

    public static final QOrderHistory orderHistory = new QOrderHistory("order_history");

    public final StringPath operatingTime = createString("operatingTime");

    public final StringPath operator = createString("operator");

    public final NumberPath<Integer> operatorId = createNumber("operatorId", Integer.class);

    public final NumberPath<Integer> orderHistoryId = createNumber("orderHistoryId", Integer.class);

    public final NumberPath<Integer> orderId = createNumber("orderId", Integer.class);

    public final StringPath state = createString("state");

    public final com.mysema.query.sql.PrimaryKey<OrderHistory> primary = createPrimaryKey(orderHistoryId);

    public QOrderHistory(String variable) {
        super(OrderHistory.class, forVariable(variable), "null", "order_history");
        addMetadata();
    }

    public QOrderHistory(String variable, String schema, String table) {
        super(OrderHistory.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QOrderHistory(Path<? extends OrderHistory> path) {
        super(path.getType(), path.getMetadata(), "null", "order_history");
        addMetadata();
    }

    public QOrderHistory(PathMetadata<?> metadata) {
        super(OrderHistory.class, metadata, "null", "order_history");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(operatingTime, ColumnMetadata.named("operating_time").ofType(12).withSize(14));
        addMetadata(operator, ColumnMetadata.named("operator").ofType(12).withSize(32).notNull());
        addMetadata(operatorId, ColumnMetadata.named("operator_id").ofType(4).withSize(10).notNull());
        addMetadata(orderHistoryId, ColumnMetadata.named("order_history_id").ofType(4).withSize(10).notNull());
        addMetadata(orderId, ColumnMetadata.named("order_id").ofType(4).withSize(10));
        addMetadata(state, ColumnMetadata.named("state").ofType(12).withSize(32));
    }

}

