package com.edgar.module.sys.repository.querydsl;

import static com.mysema.query.types.PathMetadataFactory.*;
import com.edgar.module.sys.repository.domain.Company;


import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;




/**
 * QCompany is a Querydsl query type for Company
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QCompany extends com.mysema.query.sql.RelationalPathBase<Company> {

    private static final long serialVersionUID = 108445174;

    public static final QCompany company = new QCompany("company");

    public final StringPath companyCode = createString("companyCode");

    public final NumberPath<Integer> companyId = createNumber("companyId", Integer.class);

    public final StringPath companyName = createString("companyName");

    public final DateTimePath<java.sql.Timestamp> createdTime = createDateTime("createdTime", java.sql.Timestamp.class);

    public final BooleanPath isDel = createBoolean("isDel");

    public final DateTimePath<java.sql.Timestamp> updatedTime = createDateTime("updatedTime", java.sql.Timestamp.class);

    public final com.mysema.query.sql.PrimaryKey<Company> primary = createPrimaryKey(companyId);

    public QCompany(String variable) {
        super(Company.class, forVariable(variable), "null", "company");
        addMetadata();
    }

    public QCompany(String variable, String schema, String table) {
        super(Company.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QCompany(Path<? extends Company> path) {
        super(path.getType(), path.getMetadata(), "null", "company");
        addMetadata();
    }

    public QCompany(PathMetadata<?> metadata) {
        super(Company.class, metadata, "null", "company");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(companyCode, ColumnMetadata.named("company_code").ofType(12).withSize(8).notNull());
        addMetadata(companyId, ColumnMetadata.named("company_id").ofType(4).withSize(10).notNull());
        addMetadata(companyName, ColumnMetadata.named("company_name").ofType(12).withSize(64).notNull());
        addMetadata(createdTime, ColumnMetadata.named("created_time").ofType(93).withSize(19).notNull());
        addMetadata(isDel, ColumnMetadata.named("is_del").ofType(-7).notNull());
        addMetadata(updatedTime, ColumnMetadata.named("updated_time").ofType(93).withSize(19).notNull());
    }

}

