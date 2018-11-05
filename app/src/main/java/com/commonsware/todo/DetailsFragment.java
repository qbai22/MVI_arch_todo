package com.commonsware.todo;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.commonsware.todo.databinding.TodoDetailsBinding;

/**
 * Created by Vladimir Kraev
 */

public class DetailsFragment extends Fragment {

    private static final String ARG_ID = "arg_id";

    private TodoDetailsBinding binding;
    private RosterViewModel viewModel;
    AppCompatActivity host;

    interface Contract {
        void editModel(ToDoModel model);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        viewModel = ViewModelProviders.of(getActivity()).get(RosterViewModel.class);
        host = (AppCompatActivity) getActivity();
        super.onCreate(savedInstanceState);
    }

    static DetailsFragment newInstance(ToDoModel model) {

        DetailsFragment resultFragment = new DetailsFragment();

        if (model != null) {
            Bundle args = new Bundle();
            args.putString(ARG_ID, model.id());
            resultFragment.setArguments(args);
        }

        return resultFragment;
    }

    private String getModelId() {
        Bundle args = getArguments();
        if (args != null) {
            return getArguments().getString(ARG_ID);
        }
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        binding = TodoDetailsBinding.inflate(getLayoutInflater(), container, false);
        Toolbar tb = binding.getRoot().findViewById(R.id.toolbar);
        host.setSupportActionBar(tb);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.stateStream().observe(host, this::render);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.actions_details, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            ((Contract) getActivity()).editModel(binding.getModel());
            Log.e("EDIT", "onOptionsItemSelected: CLICKED ");
        }
        return super.onOptionsItemSelected(item);
    }

    public void render(ViewState state) {
        if (state != null && state.current() != null) {
            ToDoModel model = state.current();
            binding.setModel(model);
            binding.setCreatedOn(DateUtils.getRelativeDateTimeString(
                    getActivity(),
                    model.createdOn().getTimeInMillis(),
                    DateUtils.MINUTE_IN_MILLIS,
                    DateUtils.WEEK_IN_MILLIS, 0));
        }
    }
}
