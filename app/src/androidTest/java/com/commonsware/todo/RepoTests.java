package com.commonsware.todo;


import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class RepoTests {

    private ToDoRepository repo;

    @Before
    public void setUp() {
        repo = new ToDoRepository();
        repo.add(ToDoModel.creator()
           .description("Live one more day in existential hell")
           .notes("Read a book")
           .isCompleted(true)
           .build());

        repo.add(ToDoModel.creator()
           .description("Forget about your past, and open soul to your future")
           .notes("Swim")
           .build());

        repo.add(ToDoModel.creator()
                .description("Close your eyes and run")
                .notes("Take it")
                .build());
        Log.e("TEST", "setUp: invoked");
    }

    @Test
    public void getAll() {
        assertEquals(3, repo.all().size());
    }

    @Test
    public void add() {
        ToDoModel model = ToDoModel.creator()
                .isCompleted(true)
                .description("Testing example")
                .notes("foo")
                .build();

        repo.add(model);

        assertEquals(4, repo.all().size());
        assertThat(repo.all(), hasItem(model));
    }

    @Test
    public void replace() {
        ToDoModel original = repo.all().get(0);
        ToDoModel edited = original.toBuilder()
                .isCompleted(false)
                .description("Testing changes")
                .build();
        repo.placeModel(edited);
        assertEquals(3, repo.all().size());
        assertSame(edited, repo.all().get(0));

    }

    @Test
    public void delete(){
        assertEquals(3, repo.all().size());
        repo.delete(repo.all().get(0));
        assertEquals(2, repo.all().size());
        repo.delete(repo.all().get(0).toBuilder().build());
        assertEquals(1, repo.all().size());

    }


}
