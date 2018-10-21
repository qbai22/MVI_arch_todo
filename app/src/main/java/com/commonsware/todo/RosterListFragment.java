package com.commonsware.todo;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Objects;


public class RosterListFragment extends Fragment {

    interface Contract {
        void showItemDetails(ToDoModel model);
        void addNewItem();
    }

    private RecyclerView tasksRecyclerView;
    private TextView placeholderText;
    private FloatingActionButton addItemFab;

    private RosterViewModel viewModel;
    private RosterListAdapter adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(getActivity()).get(RosterViewModel.class);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.todo_roster, container, false);
        Toolbar tb = v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(tb);
        tasksRecyclerView = v.findViewById(R.id.tasks_recyclerview);
        placeholderText = v.findViewById(R.id.placeholder_textview);
        addItemFab = v.findViewById(R.id.fab);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration decoration = new DividerItemDecoration(Objects.requireNonNull(getActivity()), DividerItemDecoration.VERTICAL);
        tasksRecyclerView.addItemDecoration(decoration);
        adapter = new RosterListAdapter(this);
        tasksRecyclerView.setAdapter(adapter);
        addItemFab.setOnClickListener(v -> ((Contract) getActivity()).addNewItem());

        viewModel.stateStream().observe(this, viewState -> render(viewState));
    }

    public void render(ViewState state) {
        adapter.setState(state);
        if (tasksRecyclerView.getAdapter().getItemCount() > 0)
            placeholderText.setVisibility(View.GONE);
        else placeholderText.setVisibility(View.VISIBLE);
    }

    public void replace(ToDoModel model) {
        viewModel.process(Action.edit(model));
    }

    void showModel(ToDoModel model) {
        ((RosterListFragment.Contract) getActivity()).showItemDetails(model);
        viewModel.process(Action.show(model));
    }

}
