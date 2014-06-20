package com.edgar.module.sys.repository.querydsl;

import static com.mysema.query.types.PathMetadataFactory.*;
import com.edgar.module.sys.repository.domain.Customer;


import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;




/**
 * QCustomer is a Querydsl query type for Customer
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QCustomer extends com.mysema.query.sql.RelationalPathBase<Customer> {

    private static final long serialVersionUID = 272772485;

    public static final QCustomer customer = new QCustomer("customer");

    public final StringPath address = createString("address");

    public final NumberPath<Integer> companyId = createNumber("companyId", Integer.class);

    public final DateTimePath<java.sql.Timestamp> createdTime = createDateTime("createdTime", java.sql.Timestamp.class);

    public final NumberPath<Integer> customerId = createNumber("customerId", Integer.class);

    public final StringPath customerName = createString("customerName");

    public final StringPath dealPassword = createString("dealPassword");

    public final StringPath email = createString("email");

    public final StringPath idCardNo = createString("idCardNo");

    public final BooleanPath isDebt = createBoolean("isDebt");

    public final BooleanPath isDel = createBoolean("isDel");

    public final StringPath mobileNo = createString("mobileNo");

    public final StringPath state = createString("state");

    public final StringPath type = createString("type");

    public final DateTimePath<java.sql.Timestamp> updatedTime = createDateTime("updatedTime", java.sql.Timestamp.class);

    public final com.mysema.query.sql.PrimaryKey<Customer> primary = createPrimaryKey(customerId);

    public QCustomer(String variable) {
        super(Customer.class, forVariable(variable), "null", "customer");
        addMetadata();
    }

    public QCustomer(String variable, String schema, String table) {
        super(Customer.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QCustomer(Path<? extends Customer> path) {
        super(path.getType(), path.getMetadata(), "null", "customer");
        addMetadata();
    }

    public QCustomer(PathMetadata<?> metadata) {
        super(Customer.class, metadata, "null", "customer");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(address, ColumnMetadata.named("address").ofType(12).withSize(128).notNull());
        addMetadata(companyId, ColumnMetadata.named("company_id").ofType(4).withSize(10));
        addMetadata(createdTime, ColumnMetadata.named("created_time").ofType(93).withSize(19).notNull());
        addMetadata(customerId, ColumnMetadata.named("customer_id").ofType(4).withSize(10).notNull());
        addMetadata(customerName, ColumnMetadata.named("customer_name").ofType(12).withSize(64).notNull());
        addMetadata(dealPassword, ColumnMetadata.named("deal_password").ofType(12).withSize(512).notNull());
        addMetadata(email, ColumnMetadata.named("email").ofType(12).withSize(64));
        addMetadata(idCardNo, ColumnMetadata.named("id_card_no").ofType(12).withSize(18).notNull());
        addMetadata(isDebt, ColumnMetadata.named("is_debt").ofType(-7));
        addMetadata(isDel, ColumnMetadata.named("is_del").ofType(-7));
        addMetadata(mobileNo, ColumnMetadata.named("mobile_no").ofType(12).withSize(16).notNull());
        addMetadata(state, ColumnMetadata.named("state").ofType(12).withSize(32));
        addMetadata(type, ColumnMetadata.named("type").ofType(12).withSize(32));
        addMetadata(updatedTime, ColumnMetadata.named("updated_time").ofType(93).withSize(19).notNull());
    }

}

