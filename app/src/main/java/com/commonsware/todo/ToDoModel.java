package com.commonsware.todo;


import java.util.Calendar;
import java.util.Comparator;
import java.util.UUID;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

// TODO: 27.08.2018 проверить работает ли автогенерация класса с произвольными названиями методов
@AutoValue
public abstract class ToDoModel {

    public abstract String id();
    public abstract boolean isCompleted();
    public abstract String description();
    @Nullable
    public abstract String notes();
    public abstract Calendar createdOn();

    public static final Comparator<ToDoModel> SORT_BY_TIME = (model1, model2) -> {
        long firstDate = model1.createdOn().getTimeInMillis();
        long secondDate = model2.createdOn().getTimeInMillis();

        return firstDate > secondDate ? 1 : -1;
    };

    static Builder builder() {
        return new AutoValue_ToDoModel.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        abstract Builder id(String id);

        public abstract Builder isCompleted(boolean isCompleted);

        public abstract Builder description(String description);

        @Nullable
        public abstract Builder notes(String note);

        abstract Builder createdOn(Calendar date);

        public abstract ToDoModel build();

    }

    public static Builder creator() {
        return builder()
                .isCompleted(false)
                .id(UUID.randomUUID().toString())
                .createdOn(Calendar.getInstance());
    }

    public Builder toBuilder() {
        return builder()
                .id(id())
                .isCompleted(isCompleted())
                .description(description())
                .notes(notes())
                .createdOn(createdOn());
    }


}
