package com.example.demo.components;

import com.almasb.fxgl.entity.component.Component;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getDialogService;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameScene;

public class NPCComponent extends Component {

    public void interact() {
        getDialogService().showMessageBox("im useless");
    }
}
