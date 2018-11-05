package com.commonsware.todo;

import android.support.annotation.Nullable;

import com.commonsware.todo.databinding.TodoDetailsBinding;
import com.google.auto.value.AutoValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Vladimir Kraev
 */


@AutoValue
public abstract class ViewState {

    public abstract List<ToDoModel> itemList();

    @Nullable
    public abstract ToDoModel current();

    public abstract boolean isLoaded();

    public ViewState add(ToDoModel model) {

        List<ToDoModel> models = new ArrayList<>(itemList());
        models.add(model);
        sort(models);

        return toBuilder()
                .itemList(Collections.unmodifiableList(models))
                .current(model)
                .build();
    }

    public ViewState modify(ToDoModel model) {

        List<ToDoModel> models = new ArrayList<>(itemList());
        ToDoModel original = find(models, model.id());
        if (original != null) {
            int index = models.indexOf(original);
            models.set(index, model);
        }
        return toBuilder()
                .itemList(Collections.unmodifiableList(models))
                .current(model)
                .build();
    }


    public ViewState delete(ToDoModel model) {

        List<ToDoModel> models = new ArrayList<>(itemList());
        ToDoModel original = find(models, model.id());
        if (original == null)
            throw new IllegalArgumentException("Nothing like this to delete" + model.toString());
        else models.remove(original);
        sort(models);
        return toBuilder()
                .itemList(Collections.unmodifiableList(models))
                .current(null)
                .build();
    }

    public ViewState showItem(ToDoModel model) {
        return toBuilder()
                .current(model)
                .build();
    }


    /* Pre-populating a @Builder is a common way with AutoValue to provide initial
    default values. In practice, probably the underlying boolean would be initialized to
    false, but this way, we are making that assignment explicit. */

    static Builder builder() {
        return new AutoValue_ViewState.Builder().isLoaded(false);
    }


    @AutoValue.Builder
    abstract static class Builder {
        abstract Builder itemList(List<ToDoModel> items);

        abstract Builder current(ToDoModel model);

        abstract Builder isLoaded(boolean isLoaded);

        abstract ViewState build();

    }

    static Builder empty() {
        return builder().itemList(Collections.unmodifiableList(new ArrayList<ToDoModel>()));
    }

    Builder toBuilder() {
        return builder().itemList(itemList()).current(current()).isLoaded(isLoaded());
    }

    private void sort(List<ToDoModel> models) {
        Collections.sort(models, ToDoModel.SORT_BY_TIME);
    }

    private ToDoModel find(List<ToDoModel> list, String id) {
        for (ToDoModel model : list) {
            if (model.id().equals(id)) return model;
        }
        return null;
    }

}
