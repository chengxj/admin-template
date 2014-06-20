package com.edgar.module.sys.repository.querydsl;

import static com.mysema.query.types.PathMetadataFactory.*;
import com.edgar.module.sys.repository.domain.SysRoleResource;


import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;




/**
 * QSysRoleResource is a Querydsl query type for SysRoleResource
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QSysRoleResource extends com.mysema.query.sql.RelationalPathBase<SysRoleResource> {

    private static final long serialVersionUID = -420867446;

    public static final QSysRoleResource sysRoleResource = new QSysRoleResource("sys_role_resource");

    public final NumberPath<Integer> resourceId = createNumber("resourceId", Integer.class);

    public final NumberPath<Integer> roleId = createNumber("roleId", Integer.class);

    public final NumberPath<Integer> roleResourceId = createNumber("roleResourceId", Integer.class);

    public final com.mysema.query.sql.PrimaryKey<SysRoleResource> primary = createPrimaryKey(roleResourceId);

    public QSysRoleResource(String variable) {
        super(SysRoleResource.class, forVariable(variable), "null", "sys_role_resource");
        addMetadata();
    }

    public QSysRoleResource(String variable, String schema, String table) {
        super(SysRoleResource.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QSysRoleResource(Path<? extends SysRoleResource> path) {
        super(path.getType(), path.getMetadata(), "null", "sys_role_resource");
        addMetadata();
    }

    public QSysRoleResource(PathMetadata<?> metadata) {
        super(SysRoleResource.class, metadata, "null", "sys_role_resource");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(resourceId, ColumnMetadata.named("resource_id").ofType(4).withSize(10));
        addMetadata(roleId, ColumnMetadata.named("role_id").ofType(4).withSize(10));
        addMetadata(roleResourceId, ColumnMetadata.named("role_resource_id").ofType(4).withSize(10).notNull());
    }

}

