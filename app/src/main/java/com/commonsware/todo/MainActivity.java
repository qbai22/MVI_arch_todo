package com.commonsware.todo;

import android.arch.lifecycle.Lifecycle;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class MainActivity extends AppCompatActivity
        implements
        RosterListFragment.Contract,
        DetailsFragment.Contract,
        EditFragment.Contract {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fragment listFragment = getSupportFragmentManager().findFragmentById(android.R.id.content);
        if (listFragment == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, new RosterListFragment(), "TAG")
                    .commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.actions, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.about_menu_item) {
            startActivity(new Intent(this, AboutActivity.class));
            return false;
        }

        return super.onOptionsItemSelected(item);
    }

    private void hideSoftInput() {
        View v = getCurrentFocus();
        if (v != null && v.getWindowToken() != null) {
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                    .hideSoftInputFromInputMethod(v.getWindowToken(), 0);

        }
    }

    @Override
    public void showItemDetails(ToDoModel model) {
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, DetailsFragment.newInstance(model))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void addNewItem() {
        editModel(null);
    }


    @Override
    public void editModel(ToDoModel model) {
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, EditFragment.newInstance(model))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void finishEdit() {
        hideSoftInput();
        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
            fm.popBackStack();
        }
    }
}
