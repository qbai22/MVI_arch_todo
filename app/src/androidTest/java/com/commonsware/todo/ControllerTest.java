package com.commonsware.todo;


import android.app.Application;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import io.reactivex.subjects.PublishSubject;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(AndroidJUnit4.class)

public class ControllerTest {

    private static final int POLL_DURATION = 500;

    @Before
    public void setUp(){
        ToDoDatabase toDoDatabase = ToDoDatabase.getInstance(InstrumentationRegistry.getTargetContext());
        toDoDatabase.todoStore().deleteAll();
    }

    @Test
    public void controller() throws InterruptedException {

        Controller controller = new Controller(InstrumentationRegistry.getTargetContext());
        PublishSubject<Action> actionSubject = PublishSubject.create();
        LinkedBlockingQueue<Result> receivedResults = new LinkedBlockingQueue<>();

        controller.subscribeToActions(actionSubject);
        controller.resultStream().subscribe(receivedResults::offer);

        //load test
        actionSubject.onNext(Action.load());
        Thread.sleep(500);
        assertEquals(1, receivedResults.size());
        Result.Loaded resultLoaded = (Result.Loaded) receivedResults.poll(POLL_DURATION, TimeUnit.MILLISECONDS);
        assertEquals(0, resultLoaded.models().size());

        //addition test
        ToDoModel model = ToDoModel.creator().description("Foo first").notes("Stream stock").build();
        actionSubject.onNext(Action.add(model));
        assertEquals(model, ((Result.Added) receivedResults.poll(POLL_DURATION, TimeUnit.MILLISECONDS)).model());

        ToDoModel fooModel = ToDoModel.creator().description("Hello boy").build();
        actionSubject.onNext(Action.add(fooModel));
        Result.Added resultAdded = (Result.Added) receivedResults.poll(POLL_DURATION, TimeUnit.MILLISECONDS);
        assertEquals(fooModel, resultAdded.model());

        //edition test
        ToDoModel modifiedModel = model.toBuilder().isCompleted(true).notes("bang").build();
        actionSubject.onNext(Action.edit(modifiedModel));
        Result.Modified resultModified = (Result.Modified) receivedResults.poll(POLL_DURATION, TimeUnit.MILLISECONDS);
        assertEquals(modifiedModel, resultModified.model());
        assertFalse(model.isCompleted());

        //deletion test
        actionSubject.onNext(Action.delete(model));
        Result.Deleted resultDeleted = (Result.Deleted) receivedResults.poll(POLL_DURATION, TimeUnit.MILLISECONDS);
        assertEquals(model, resultDeleted.model());
    }
}
