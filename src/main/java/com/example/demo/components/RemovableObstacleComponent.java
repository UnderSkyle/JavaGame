package com.example.demo.components;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.CollidableComponent;

public class RemovableObstacleComponent extends Component {
    public void RemoveObstacleComponent(){
        entity.removeComponent(CollidableComponent.class);
    }
    public void AddObstacleComponent(){
        entity.addComponent(new CollidableComponent(true));
    }
}
