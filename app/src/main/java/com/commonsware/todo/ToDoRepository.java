package com.commonsware.todo;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vladimir Kraev
 */

public class ToDoRepository {

    private static ToDoRepository INSTANCE = null;

    private final ToDoDatabase toDoDatabase;

    //Synchronized - значит что только один тред за раз может обращатсья к этому методу
    public synchronized static ToDoRepository get(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ToDoRepository(context.getApplicationContext());
        }

        return INSTANCE;
    }

    private ToDoRepository(Context context) {
        toDoDatabase = ToDoDatabase.getInstance(context);
    }

    public List<ToDoModel> all() {

        List<ToDoEntity> entities = toDoDatabase.todoStore().getAll();
        ArrayList<ToDoModel> result = new ArrayList<>();
        for (ToDoEntity entity : entities) {
            ToDoModel model = entity.toModel();
            result.add(model);
        }
        return result;
    }


    public void add(ToDoModel model) {
        toDoDatabase.todoStore().insert(ToDoEntity.fromModel(model));
    }

    public void placeModel(ToDoModel model) {
        toDoDatabase.todoStore().update(ToDoEntity.fromModel(model));
    }

    public void delete(ToDoModel model) {
        toDoDatabase.todoStore().delete(ToDoEntity.fromModel(model));
    }

}
