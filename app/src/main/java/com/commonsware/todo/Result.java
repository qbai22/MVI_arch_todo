package com.commonsware.todo;

import com.google.auto.value.AutoValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Vladimir Kraev
 */

public abstract class Result {

    @AutoValue
    public static abstract class Added extends Result {
        public abstract ToDoModel model();
    }

    @AutoValue
    public static abstract class Modified extends Result {
        public abstract ToDoModel model();
    }

    @AutoValue
    public static abstract class Deleted extends Result {
        public abstract ToDoModel model();
    }

    @AutoValue
    public static abstract class Showed extends Result {
        public abstract ToDoModel current();
    }

    @AutoValue
    public static abstract class Loaded extends Result {
        public abstract List<ToDoModel> models();
    }

    public static Result added(ToDoModel model){
        return new AutoValue_Result_Added(model);
    }

    public static Result modified(ToDoModel model){
        return new AutoValue_Result_Modified(model);
    }

    public static Result deleted(ToDoModel model){
        return new AutoValue_Result_Deleted(model);
    }

    public static Result showed(ToDoModel current){
        return new AutoValue_Result_Showed(current);
    }

    public static Result loaded(List<ToDoModel> models){
        return new AutoValue_Result_Loaded(Collections.unmodifiableList(models));
    }


}
