package com.edgar.module.sys.repository.querydsl;

import static com.mysema.query.types.PathMetadataFactory.*;
import com.edgar.module.sys.repository.domain.Test2Table;


import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;




/**
 * QTest2Table is a Querydsl query type for Test2Table
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QTest2Table extends com.mysema.query.sql.RelationalPathBase<Test2Table> {

    private static final long serialVersionUID = -1819506251;

    public static final QTest2Table test2Table = new QTest2Table("test2_table");

    public final DateTimePath<java.sql.Timestamp> createdTime = createDateTime("createdTime", java.sql.Timestamp.class);

    public final StringPath dictName = createString("dictName");

    public final StringPath parentCode = createString("parentCode");

    public final NumberPath<Integer> sorted = createNumber("sorted", Integer.class);

    public final StringPath testCode2 = createString("testCode2");

    public final StringPath testId = createString("testId");

    public final DateTimePath<java.sql.Timestamp> updatedTime = createDateTime("updatedTime", java.sql.Timestamp.class);

    public final com.mysema.query.sql.PrimaryKey<Test2Table> primary = createPrimaryKey(testCode2, testId);

    public QTest2Table(String variable) {
        super(Test2Table.class, forVariable(variable), "null", "test2_table");
        addMetadata();
    }

    public QTest2Table(String variable, String schema, String table) {
        super(Test2Table.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QTest2Table(Path<? extends Test2Table> path) {
        super(path.getType(), path.getMetadata(), "null", "test2_table");
        addMetadata();
    }

    public QTest2Table(PathMetadata<?> metadata) {
        super(Test2Table.class, metadata, "null", "test2_table");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdTime, ColumnMetadata.named("created_time").ofType(93).withSize(19).notNull());
        addMetadata(dictName, ColumnMetadata.named("dict_name").ofType(12).withSize(16).notNull());
        addMetadata(parentCode, ColumnMetadata.named("parent_code").ofType(12).withSize(32).notNull());
        addMetadata(sorted, ColumnMetadata.named("sorted").ofType(4).withSize(10).notNull());
        addMetadata(testCode2, ColumnMetadata.named("test_code2").ofType(12).withSize(32).notNull());
        addMetadata(testId, ColumnMetadata.named("test_id").ofType(12).withSize(32).notNull());
        addMetadata(updatedTime, ColumnMetadata.named("updated_time").ofType(93).withSize(19).notNull());
    }

}

