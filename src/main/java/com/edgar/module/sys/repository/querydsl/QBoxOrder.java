package com.edgar.module.sys.repository.querydsl;

import static com.mysema.query.types.PathMetadataFactory.*;
import com.edgar.module.sys.repository.domain.BoxOrder;


import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;




/**
 * QBoxOrder is a Querydsl query type for BoxOrder
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QBoxOrder extends com.mysema.query.sql.RelationalPathBase<BoxOrder> {

    private static final long serialVersionUID = 1903930858;

    public static final QBoxOrder boxOrder = new QBoxOrder("box_order");

    public final StringPath address = createString("address");

    public final StringPath addTime = createString("addTime");

    public final NumberPath<Integer> companyId = createNumber("companyId", Integer.class);

    public final DateTimePath<java.sql.Timestamp> createdTime = createDateTime("createdTime", java.sql.Timestamp.class);

    public final NumberPath<Integer> customerId = createNumber("customerId", Integer.class);

    public final StringPath customerName = createString("customerName");

    public final StringPath idCardNo = createString("idCardNo");

    public final StringPath mobileNo = createString("mobileNo");

    public final NumberPath<Integer> orderId = createNumber("orderId", Integer.class);

    public final StringPath orderNo = createString("orderNo");

    public final StringPath state = createString("state");

    public final StringPath type = createString("type");

    public final DateTimePath<java.sql.Timestamp> updatedTime = createDateTime("updatedTime", java.sql.Timestamp.class);

    public final com.mysema.query.sql.PrimaryKey<BoxOrder> primary = createPrimaryKey(orderId);

    public QBoxOrder(String variable) {
        super(BoxOrder.class, forVariable(variable), "null", "box_order");
        addMetadata();
    }

    public QBoxOrder(String variable, String schema, String table) {
        super(BoxOrder.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QBoxOrder(Path<? extends BoxOrder> path) {
        super(path.getType(), path.getMetadata(), "null", "box_order");
        addMetadata();
    }

    public QBoxOrder(PathMetadata<?> metadata) {
        super(BoxOrder.class, metadata, "null", "box_order");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(address, ColumnMetadata.named("address").ofType(12).withSize(128).notNull());
        addMetadata(addTime, ColumnMetadata.named("add_time").ofType(12).withSize(14).notNull());
        addMetadata(companyId, ColumnMetadata.named("company_id").ofType(4).withSize(10));
        addMetadata(createdTime, ColumnMetadata.named("created_time").ofType(93).withSize(19).notNull());
        addMetadata(customerId, ColumnMetadata.named("customer_id").ofType(4).withSize(10));
        addMetadata(customerName, ColumnMetadata.named("customer_name").ofType(12).withSize(64).notNull());
        addMetadata(idCardNo, ColumnMetadata.named("id_card_no").ofType(12).withSize(18).notNull());
        addMetadata(mobileNo, ColumnMetadata.named("mobile_no").ofType(12).withSize(16).notNull());
        addMetadata(orderId, ColumnMetadata.named("order_id").ofType(4).withSize(10).notNull());
        addMetadata(orderNo, ColumnMetadata.named("order_no").ofType(1).withSize(10));
        addMetadata(state, ColumnMetadata.named("state").ofType(12).withSize(32).notNull());
        addMetadata(type, ColumnMetadata.named("type").ofType(12).withSize(32).notNull());
        addMetadata(updatedTime, ColumnMetadata.named("updated_time").ofType(93).withSize(19).notNull());
    }

}

