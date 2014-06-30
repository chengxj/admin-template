package com.edgar.module.sys.repository.querydsl;

import static com.mysema.query.types.PathMetadataFactory.*;
import com.edgar.module.sys.repository.domain.Todo;


import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;




/**
 * QTodo is a Querydsl query type for Todo
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QTodo extends com.mysema.query.sql.RelationalPathBase<Todo> {

    private static final long serialVersionUID = -844758675;

    public static final QTodo todo = new QTodo("todo");

    public final NumberPath<Integer> todoId = createNumber("todoId", Integer.class);

    public final StringPath totoTitle = createString("totoTitle");

    public final NumberPath<Integer> userId = createNumber("userId", Integer.class);

    public final com.mysema.query.sql.PrimaryKey<Todo> primary = createPrimaryKey(todoId);

    public QTodo(String variable) {
        super(Todo.class, forVariable(variable), "null", "todo");
        addMetadata();
    }

    public QTodo(String variable, String schema, String table) {
        super(Todo.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QTodo(Path<? extends Todo> path) {
        super(path.getType(), path.getMetadata(), "null", "todo");
        addMetadata();
    }

    public QTodo(PathMetadata<?> metadata) {
        super(Todo.class, metadata, "null", "todo");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(todoId, ColumnMetadata.named("todo_id").ofType(4).withSize(10).notNull());
        addMetadata(totoTitle, ColumnMetadata.named("toto_title").ofType(12).withSize(140).notNull());
        addMetadata(userId, ColumnMetadata.named("user_id").ofType(4).withSize(10));
    }

}

