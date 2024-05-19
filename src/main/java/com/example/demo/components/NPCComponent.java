package com.example.demo.components;

import com.almasb.fxgl.entity.component.Component;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getDialogService;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameScene;

public class NPCComponent extends Component {

    private Runnable onInteract = new Runnable() {
        @Override
        public void run() {
        }
    };
    private String message;

    public NPCComponent(String message, Runnable onInteract) {
        this.onInteract = onInteract;
        this.message = message;
    }

    public NPCComponent(String message) {
        this.message = message;
    }

    public void interact(){
        System.out.println(onInteract);
        getDialogService().showMessageBox(message, onInteract);
    }

}
