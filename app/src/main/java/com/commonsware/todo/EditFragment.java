package com.commonsware.todo;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.commonsware.todo.databinding.TodoEditBinding;

public class EditFragment extends Fragment {

    private static final String ARGS_KEY = "args_key";

    private TodoEditBinding binding;
    private FloatingActionButton deleteFab;
    private RosterViewModel viewModel;

    interface Contract {
        void finishEdit();
    }

    public static EditFragment newInstance(ToDoModel model) {

        EditFragment result = new EditFragment();

        if (model != null) {
            Bundle args = new Bundle();
            args.putString(ARGS_KEY, model.id());
            result.setArguments(args);
        }

        return result;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        viewModel = ViewModelProviders.of(getActivity()).get(RosterViewModel.class);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = TodoEditBinding.inflate(inflater, container, false);
        Toolbar tb = binding.getRoot().findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(tb);

        deleteFab = binding.getRoot().findViewById(R.id.delete_fab);
        deleteFab.setOnClickListener(v -> deleteItem());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel.stateStream().observe(getActivity(), this::render);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.actions_edit, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            saveItem();
            ((Contract) getActivity()).finishEdit();
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private String getModelId() {
        Bundle args = getArguments();
        if (args != null) {
            return getArguments().getString(ARGS_KEY);
        }
        return null;
    }

    private void saveItem() {

        ToDoModel.Builder builder;

        if (binding.getModel() == null) {
            builder = ToDoModel.creator();
        } else {
            builder = binding.getModel().toBuilder();
        }

        ToDoModel modelChanged = builder
                .description(binding.descEditText.getText().toString())
                .notes(binding.notesEditText.getText().toString())
                .isCompleted(binding.editCompletedCheckbox.isChecked())
                .build();

        if (binding.getModel() == null) viewModel.process(Action.add(modelChanged));
        else viewModel.process(Action.edit(modelChanged));

        //ToDoRepository.get().placeModel(modelChanged);
    }

    private void deleteItem() {
        viewModel.process(Action.delete(binding.getModel()));
        ((Contract) getActivity()).finishEdit();
    }

    private void render(ViewState state) {
        if (state != null && getModelId() == null && deleteFab != null) {
            deleteFab.setVisibility(View.INVISIBLE);
            deleteFab.setActivated(false);
        } else {
            ToDoModel model = state.current();
            binding.setModel(model);
        }
    }

}
