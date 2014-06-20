package com.edgar.module.sys.repository.querydsl;

import static com.mysema.query.types.PathMetadataFactory.*;
import com.edgar.module.sys.repository.domain.SafeBox;


import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;




/**
 * QSafeBox is a Querydsl query type for SafeBox
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QSafeBox extends com.mysema.query.sql.RelationalPathBase<SafeBox> {

    private static final long serialVersionUID = 1015971959;

    public static final QSafeBox safeBox = new QSafeBox("safe_box");

    public final StringPath boxCode = createString("boxCode");

    public final NumberPath<Integer> boxId = createNumber("boxId", Integer.class);

    public final NumberPath<Integer> companyId = createNumber("companyId", Integer.class);

    public final NumberPath<Integer> customerId = createNumber("customerId", Integer.class);

    public final BooleanPath enabled = createBoolean("enabled");

    public final com.mysema.query.sql.PrimaryKey<SafeBox> primary = createPrimaryKey(boxId);

    public QSafeBox(String variable) {
        super(SafeBox.class, forVariable(variable), "null", "safe_box");
        addMetadata();
    }

    public QSafeBox(String variable, String schema, String table) {
        super(SafeBox.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QSafeBox(Path<? extends SafeBox> path) {
        super(path.getType(), path.getMetadata(), "null", "safe_box");
        addMetadata();
    }

    public QSafeBox(PathMetadata<?> metadata) {
        super(SafeBox.class, metadata, "null", "safe_box");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(boxCode, ColumnMetadata.named("box_code").ofType(12).withSize(16).notNull());
        addMetadata(boxId, ColumnMetadata.named("box_id").ofType(4).withSize(10).notNull());
        addMetadata(companyId, ColumnMetadata.named("company_id").ofType(4).withSize(10));
        addMetadata(customerId, ColumnMetadata.named("customer_id").ofType(4).withSize(10));
        addMetadata(enabled, ColumnMetadata.named("enabled").ofType(-7));
    }

}

