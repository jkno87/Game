package com.jgame.characters;

import com.jgame.elements.GameElement;
import com.jgame.util.Vector2;

import java.util.List;

/**
 * Created by jose on 10/02/15.
 */
public class MainCharacter {

    public enum CharacterState {
        NORMAL, STUNNED, SPECIAL
    }

    private boolean dragging;
    public CharacterState state;
    public final int stamina;
    private MovementController movementController;

    public MainCharacter(MovementController movementController, int stamina){
        this.stamina = stamina;
        state = CharacterState.NORMAL;
        this.movementController = movementController;
    }

    private void recoverCharacter(){
        state = CharacterState.NORMAL;
    }

    public void receiveInputDown(float sourceX, float sourceY){
        if(state == CharacterState.STUNNED)
            return;

        boolean withinCharacterRadius = movementController.containsPoint(sourceX, sourceY);
        dragging = withinCharacterRadius;
        movementController.updateDirection(sourceX, sourceY);

    }

    public void receiveInputDrag(float sourceX, float sourceY){
        movementController.updateDirection(sourceX, sourceY);

        if(dragging){
            movementController.move(sourceX, sourceY);
        }
    }

    public void changeState(){
        if(state == CharacterState.NORMAL)
            this.state = CharacterState.SPECIAL;
        else if(state == CharacterState.SPECIAL)
            this.state = CharacterState.NORMAL;
    }

    public void interact(GameElement e){

    }

    public float getSize(){
        return 0;
    }

    public void update(List<GameElement> others, float timeDifference){

    }

    public boolean vivo() {
        return false;
    }

    public int getId(){
        return 0;
    }

    /**
     * Regresa un vector con la posicion del maincharacter
     * @return Vector2 con la posicion actual del personaje
     */
    public Vector2 getPosition(){
        return new Vector2(movementController.position);
    }

    /**
     * Regresa el angulo del personaje principal
     * @return float con el angulo de direccion
     */
    public float getAngle(){
        return movementController.angle;
    }

    public float getPctAlive(){
        return 0;
    }
}
