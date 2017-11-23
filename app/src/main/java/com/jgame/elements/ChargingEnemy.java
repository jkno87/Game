package com.jgame.elements;

import com.jgame.util.Decoration;
import com.jgame.util.Square;
import com.jgame.util.TextureDrawer;
import com.jgame.util.TextureDrawer.TextureData;
import com.jgame.game.GameData.Event;
import com.jgame.util.Vector2;

import java.util.ArrayDeque;

/**
 * Enemigo que tiene el objetivo de lanzarse hacia el personaje principal despues de realizar un periodo de carga.
 * Created by jose on 13/07/16.
 */
public class ChargingEnemy extends GameCharacter {

    enum State {
        IDLE, ATTACKING, CHARGING, DEAD
    }

    private final static Vector2 INITIAL_POSITION = new Vector2(425,0);
    public final static TextureData IDLE_TEXTURE = TextureDrawer.generarTextureData(22,0,24,2,32);
    private final static Vector2 ATTACK_SPEED = new Vector2(5f, 0);
    private final static int IDLE_FRAMES = 120;
    private final static int CHARGE_FRAMES = 20;
    private State currentState;
    private int idleFrame;
    private int chargeFrame;
    private final CollisionObject[] attackObject;

    public ChargingEnemy(float yPosition, int id){
        super(new Square(new Vector2(0, yPosition), 85,85,0), id);
        this.baseX.x = -1;
        currentState = State.IDLE;
        idleFrame = IDLE_FRAMES;
        chargeFrame = CHARGE_FRAMES;
        attackObject = new CollisionObject[]{new CollisionObject(new Vector2(), 0, 50, 50, this, CollisionObject.TYPE_ATTACK)};
    }

    @Override
    public void reset(Vector2 positionOffset) {
        activeCollisionBoxes = idleCollisionBoxes;
        idleFrame = IDLE_FRAMES;
        chargeFrame = CHARGE_FRAMES;
        currentState = State.IDLE;
        moveTo(positionOffset, INITIAL_POSITION);
        color.b = 0;
        attackObject[0].relativePosition.set(0,0);
        attackObject[0].updatePosition();
    }

    @Override
    public boolean hittable() {
        return currentState == State.ATTACKING;
    }

    @Override
    public boolean alive() {
        return currentState != State.DEAD;
    }

    @Override
    public boolean attacking() {
        return currentState == State.ATTACKING;
    }

    @Override
    public boolean completedTransition(){
        return position.x < INITIAL_POSITION.x;
    }

    @Override
    public void update(GameCharacter foe, ArrayDeque<Decoration> decorationData) {
        if(!completedTransition())
            return;

        if(currentState == State.IDLE){
            idleFrame -= 1;
            if(idleFrame <= 0) {
                currentState = State.CHARGING;
                color.b = 0.5f;
            }
        } else if(currentState == State.CHARGING){
            chargeFrame -= 1;
            if(chargeFrame == 0) {
                currentState = State.ATTACKING;
                color.b = 1;
                activeCollisionBoxes = attackObject;
            }
        } else if (currentState == State.ATTACKING){
            attackObject[0].move(ATTACK_SPEED);
            if(Event.HIT == detectCollision(foe, attackObject)){
                activeCollisionBoxes = idleCollisionBoxes;
                idleFrame = IDLE_FRAMES;
                chargeFrame = CHARGE_FRAMES;
                currentState = State.IDLE;
                color.b = 0;
                attackObject[0].relativePosition.set(0,0);
                attackObject[0].updatePosition();
            }

        }

        return;
    }

    @Override
    public TextureData getCurrentTexture() {

        return IDLE_TEXTURE;
    }

    @Override
    public void hit() {
        currentState = State.DEAD;
    }
}
