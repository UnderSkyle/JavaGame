package com.example.demo.components;

import com.almasb.fxgl.texture.Texture;

public class DoorComponent extends RemovableObstacleComponent{

    private Texture closedTexture;
    private Texture openTexture;


    public DoorComponent(Texture closedTexture, Texture openTexture) {
        super();
        this.closedTexture = closedTexture;
        this.openTexture = openTexture;
    }

    public void openDoor(){
        super.RemoveObstacleComponent();
        entity.getViewComponent().clearChildren();
        entity.getViewComponent().addChild(openTexture);
    }

}
