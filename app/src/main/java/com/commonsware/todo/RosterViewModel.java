package com.commonsware.todo;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.LiveDataReactiveStreams;
import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.ReplaySubject;

public class RosterViewModel extends AndroidViewModel {

    private LiveData<ViewState> states;
    private ViewState lastState = ViewState.empty().build();
    private final ReplaySubject<ViewState> stateSubject = ReplaySubject.createWithSize(1);

    private final PublishSubject<Action> actionSubject = PublishSubject.create();

    @SuppressLint("CheckResult")
    public RosterViewModel(@NonNull Application application) {
        super(application);

        Controller controller = new Controller();
        controller.resultStream()
                .subscribe(result -> {
                    lastState = foldResultIntoState(lastState, result);
                    stateSubject.onNext(lastState);
                }, stateSubject::onError);

        controller.subscribeToActions(actionSubject);

        states = LiveDataReactiveStreams
                .fromPublisher(stateSubject.toFlowable(BackpressureStrategy.LATEST));

        process(Action.load());

    }

    public LiveData<ViewState> stateStream() {
        return states;
    }

    public void process(Action action) {
        actionSubject.onNext(action);
    }

    private ViewState foldResultIntoState(@NonNull ViewState state, @NonNull Result result) {

        if (result instanceof Result.Added)
            return state.add(((Result.Added) result).model());

        else if (result instanceof Result.Modified)
            return state.modify(((Result.Modified) result).model());

        else if (result instanceof Result.Deleted)
            return state.delete(((Result.Deleted) result).model());

        else if (result instanceof Result.Showed)
            return state.showItem(((Result.Showed)result).current());

        else if (result instanceof Result.Loaded) {
            List<ToDoModel> models = ((Result.Loaded) result).models();

            return ViewState.builder()
                    .itemList(models)
                    .current(models.isEmpty() ? null : models.get(0))
                    .build();

        } else throw new IllegalStateException("Unexpected result type" + result.toString());
    }

}
