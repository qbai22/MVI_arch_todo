package com.commonsware.todo;

import java.util.ArrayList;
import java.util.List;

public class ToDoRepository {

    private static volatile ToDoRepository INSTANCE = new ToDoRepository();

    private List<ToDoModel> items = new ArrayList<>();

    //Synchronized - значит что только один тред за раз может обращатсья к этому методу
    public synchronized static ToDoRepository get() {
        return INSTANCE;
    }

    // Возвращаем копию потому что только репозиторий должен иметь доступ к изменению
    // набора записей
    public List<ToDoModel> all() {
        return new ArrayList<>(items);
    }


    public void add(ToDoModel model) {
        items.add(model);
    }

    public void placeModel(ToDoModel model) {
        boolean doesContain = false;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).id().equals(model.id())) {
                items.set(i, model);
                doesContain = true;
            }
        }
        if (!doesContain) add(model);
    }

    public void delete(ToDoModel model) {
        for (ToDoModel original : items) {
            if (model.id().equals(original.id())) {
                items.remove(original);
                return;
            }
        }
    }

    public ToDoModel findModel(String modelId) {

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).id().equals(modelId)) {
                return items.get(i);
            }
        }

        return null;
    }


}
