package com.commonsware.todo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.commonsware.todo.databinding.TodoItemBinding;

import java.util.List;

public class RosterListAdapter extends RecyclerView.Adapter<RosterRowHolder> {

    RosterListFragment host;
    private List<ToDoModel> items;
    private static final String TAG = "ROSTER_LIST_ADAPTER";
    public RosterListAdapter(RosterListFragment host) {
        this.host = host;
    }

    @NonNull
    @Override
    public RosterRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TodoItemBinding binding = TodoItemBinding.inflate(host.getLayoutInflater(), parent, false);
        Log.e(TAG, "onCreateViewHolder: INVOKED");
        return new RosterRowHolder(binding, this);
    }

    @Override
    public void onBindViewHolder(@NonNull RosterRowHolder holder, int position) {
        holder.bind(items.get(position));

    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public void replace(ToDoModel model, boolean isChecked) {
        host.replace(model.toBuilder().isCompleted(isChecked).build());
    }

    void showItem(ToDoModel model) {
        host.showModel(model);
    }

    public void setState(ViewState state) {
        items = state.itemList();
        notifyDataSetChanged();
    }

}
