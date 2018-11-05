package com.commonsware.todo;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

/**
 * Created by Vladimir Kraev
 */

@Database(entities = ToDoEntity.class, version = 1)
@TypeConverters({TypeConverterUtil.class})
public abstract class ToDoDatabase extends RoomDatabase {

    private static volatile ToDoDatabase INSTANCE = null;
    private static final String DB_NAME = "todo_db";

    //The todoStore() method returns an instance of our @Dao-annotated interface. This,
    //coupled with the @Database annotation, tells Room’s annotation processor
    // to codegenerate an implementation of ToDoDatabase that has an implementation of
    //todoStore() that returns a code-generated implementation of ToDoEntity.Store.
    public abstract ToDoEntity.Store todoStore();

    public static synchronized ToDoDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = create(context);
        }
        return INSTANCE;
    }

    //Используем .getApplicationContext() потому что синглтон и нужно избежать утечек памяти
    private static ToDoDatabase create(Context context) {
        RoomDatabase.Builder<ToDoDatabase> builder =
                Room.databaseBuilder(context.getApplicationContext(), ToDoDatabase.class, DB_NAME);
        return builder.build();
    }

}
