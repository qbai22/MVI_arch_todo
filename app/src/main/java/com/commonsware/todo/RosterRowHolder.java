package com.commonsware.todo;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.commonsware.todo.databinding.TodoItemBinding;

public class RosterRowHolder extends RecyclerView.ViewHolder {


    private final TodoItemBinding binding;
    private final RosterListAdapter adapter;

    public RosterRowHolder(TodoItemBinding binding, RosterListAdapter adapter) {
        super(binding.getRoot());
        this.binding = binding;
        this.adapter = adapter;
    }

    public void bind(ToDoModel model) {
        binding.setModel(model);
        binding.setHolder(this);
        binding.executePendingBindings();
    }

    public void completeChanged(ToDoModel model, boolean isChecked) {
        if (model.isCompleted() != isChecked) {
            adapter.replace(model, isChecked);
        }
    }

    public void onClick() {
        adapter.showItem(binding.getModel());
    }

}
