package de.grau.organizer.classes;

import android.content.Intent;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Attix on 9/22/16.
 */

public class Action extends RealmObject {
    @PrimaryKey
    private long id;
    private ActionEnum type;
    private String Data;

    enum ActionEnum{
        CALL(new ActionExecutor() {
            @Override
            public void execute() {
                Intent intent = new Intent();

            }
        }),
        EMAIL(new ActionExecutor() {
            @Override
            public void execute() {

            }
        });

        private ActionExecutor executor;
        ActionEnum(ActionExecutor executor){
            this.executor = executor;
        }
    }

    private interface ActionExecutor{
        void execute();
    }

    private void whateva(){
        ActionEnum.CALL.executor.execute();
    }
}
