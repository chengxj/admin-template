package com.edgar.module.sys.repository.querydsl;

import static com.mysema.query.types.PathMetadataFactory.*;
import com.edgar.module.sys.repository.domain.SysRouteRes;


import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;




/**
 * QSysRouteRes is a Querydsl query type for SysRouteRes
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QSysRouteRes extends com.mysema.query.sql.RelationalPathBase<SysRouteRes> {

    private static final long serialVersionUID = 1731043453;

    public static final QSysRouteRes sysRouteRes = new QSysRouteRes("sys_route_res");

    public final NumberPath<Integer> resourceId = createNumber("resourceId", Integer.class);

    public final NumberPath<Integer> routeId = createNumber("routeId", Integer.class);

    public final NumberPath<Integer> routeResId = createNumber("routeResId", Integer.class);

    public final com.mysema.query.sql.PrimaryKey<SysRouteRes> primary = createPrimaryKey(routeResId);

    public QSysRouteRes(String variable) {
        super(SysRouteRes.class, forVariable(variable), "null", "sys_route_res");
        addMetadata();
    }

    public QSysRouteRes(String variable, String schema, String table) {
        super(SysRouteRes.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QSysRouteRes(Path<? extends SysRouteRes> path) {
        super(path.getType(), path.getMetadata(), "null", "sys_route_res");
        addMetadata();
    }

    public QSysRouteRes(PathMetadata<?> metadata) {
        super(SysRouteRes.class, metadata, "null", "sys_route_res");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(resourceId, ColumnMetadata.named("resource_id").ofType(4).withSize(10));
        addMetadata(routeId, ColumnMetadata.named("route_id").ofType(4).withSize(10));
        addMetadata(routeResId, ColumnMetadata.named("route_res_id").ofType(4).withSize(10).notNull());
    }

}

