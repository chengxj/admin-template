package com.edgar.module.sys.repository.querydsl;

import static com.mysema.query.types.PathMetadataFactory.*;
import com.edgar.module.sys.repository.domain.CustomerAccount;


import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;




/**
 * QCustomerAccount is a Querydsl query type for CustomerAccount
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QCustomerAccount extends com.mysema.query.sql.RelationalPathBase<CustomerAccount> {

    private static final long serialVersionUID = 728343784;

    public static final QCustomerAccount customerAccount = new QCustomerAccount("customer_account");

    public final NumberPath<Integer> accountId = createNumber("accountId", Integer.class);

    public final BooleanPath isDel = createBoolean("isDel");

    public final StringPath password = createString("password");

    public final StringPath username = createString("username");

    public final com.mysema.query.sql.PrimaryKey<CustomerAccount> primary = createPrimaryKey(accountId);

    public QCustomerAccount(String variable) {
        super(CustomerAccount.class, forVariable(variable), "null", "customer_account");
        addMetadata();
    }

    public QCustomerAccount(String variable, String schema, String table) {
        super(CustomerAccount.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QCustomerAccount(Path<? extends CustomerAccount> path) {
        super(path.getType(), path.getMetadata(), "null", "customer_account");
        addMetadata();
    }

    public QCustomerAccount(PathMetadata<?> metadata) {
        super(CustomerAccount.class, metadata, "null", "customer_account");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(accountId, ColumnMetadata.named("account_id").ofType(4).withSize(10).notNull());
        addMetadata(isDel, ColumnMetadata.named("is_del").ofType(-7));
        addMetadata(password, ColumnMetadata.named("password").ofType(12).withSize(64));
        addMetadata(username, ColumnMetadata.named("username").ofType(12).withSize(16));
    }

}

