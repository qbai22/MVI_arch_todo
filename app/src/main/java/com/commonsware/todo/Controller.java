package com.commonsware.todo;


import android.annotation.SuppressLint;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class Controller {

    ToDoRepository repository = ToDoRepository.get();
    PublishSubject<Result> resultSubject = PublishSubject.create();

    private void processImpl(Action action) {

        if (action instanceof Action.Add)
            add(((Action.Add) action).model());

        else if (action instanceof Action.Edit)
            modify(((Action.Edit) action).model());

        else if (action instanceof Action.Delete)
            delete(((Action.Delete) action).model());

        else if (action instanceof Action.Load)
            load();

        else if (action instanceof Action.Show)
            show(((Action.Show) action).model());

    }

    @SuppressLint("CheckResult")
    public void subscribeToActions(Observable<Action> actionStream) {
        actionStream.observeOn(Schedulers.io())
                .subscribe(this::processImpl);
    }

    public Observable<Result> resultStream() {
        return resultSubject;
    }

    private void add(ToDoModel model) {
        repository.add(model);
        resultSubject.onNext(Result.added(model));
    }

    private void modify(ToDoModel model) {
        repository.placeModel(model);
        resultSubject.onNext(Result.modified(model));
    }

    private void delete(ToDoModel toDelete) {
        repository.delete(toDelete);
        resultSubject.onNext(Result.deleted(toDelete));
    }

    private void load() {
        resultSubject.onNext(Result.loaded(repository.all()));
    }

    private void show(ToDoModel current) {
        resultSubject.onNext(Result.showed(current));
    }

}
