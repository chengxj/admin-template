package com.edgar.module.sys.repository.querydsl;

import static com.mysema.query.types.PathMetadataFactory.*;
import com.edgar.module.sys.repository.domain.Contact;


import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;




/**
 * QContact is a Querydsl query type for Contact
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QContact extends com.mysema.query.sql.RelationalPathBase<Contact> {

    private static final long serialVersionUID = 109487513;

    public static final QContact contact = new QContact("contact");

    public final NumberPath<Integer> contactId = createNumber("contactId", Integer.class);

    public final DateTimePath<java.sql.Timestamp> createdTime = createDateTime("createdTime", java.sql.Timestamp.class);

    public final NumberPath<Integer> customerId = createNumber("customerId", Integer.class);

    public final StringPath description = createString("description");

    public final StringPath fullName = createString("fullName");

    public final StringPath mobileNo = createString("mobileNo");

    public final DateTimePath<java.sql.Timestamp> updatedTime = createDateTime("updatedTime", java.sql.Timestamp.class);

    public final com.mysema.query.sql.PrimaryKey<Contact> primary = createPrimaryKey(contactId);

    public QContact(String variable) {
        super(Contact.class, forVariable(variable), "null", "contact");
        addMetadata();
    }

    public QContact(String variable, String schema, String table) {
        super(Contact.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QContact(Path<? extends Contact> path) {
        super(path.getType(), path.getMetadata(), "null", "contact");
        addMetadata();
    }

    public QContact(PathMetadata<?> metadata) {
        super(Contact.class, metadata, "null", "contact");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(contactId, ColumnMetadata.named("contact_id").ofType(4).withSize(10).notNull());
        addMetadata(createdTime, ColumnMetadata.named("created_time").ofType(93).withSize(19).notNull());
        addMetadata(customerId, ColumnMetadata.named("customer_id").ofType(4).withSize(10));
        addMetadata(description, ColumnMetadata.named("description").ofType(12).withSize(128));
        addMetadata(fullName, ColumnMetadata.named("full_name").ofType(12).withSize(32));
        addMetadata(mobileNo, ColumnMetadata.named("mobile_no").ofType(12).withSize(16));
        addMetadata(updatedTime, ColumnMetadata.named("updated_time").ofType(93).withSize(19).notNull());
    }

}

