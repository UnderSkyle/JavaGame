package com.example.demo.components;

import com.almasb.fxgl.texture.Texture;

public class DoorComponent extends RemovableObstacleComponent{

    private final Texture openTexture;


    public DoorComponent(Texture openTexture) {
        super();
        this.openTexture = openTexture;
    }

    public void openDoor(){
        super.RemoveObstacleComponent();
        entity.getViewComponent().clearChildren();
        entity.getViewComponent().addChild(openTexture);
    }

}
