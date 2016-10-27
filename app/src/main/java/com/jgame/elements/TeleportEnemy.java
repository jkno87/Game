package com.jgame.elements;

import com.jgame.game.GameActivity;
import com.jgame.game.GameFlow;
import com.jgame.util.TextureDrawer.TextureData;
import com.jgame.util.TimeCounter;
import com.jgame.util.Vector2;
import com.jgame.game.GameActivity.WorldData;

/**
 * Clase que representa un enemigo basico que se mantiene en su lugar y ataca
 * Created by jose on 24/05/16.
 */
public class TeleportEnemy extends GameCharacter {

    enum EnemyState {
        IDLE, DEAD, ATTACKING, TELEPORTING
    }

    public final static TextureData TELEPORT_TEXTURE = new TextureData(0,0.625f,0.125f,0.75f);
    public final static TextureData IDLE_TEXTURE = new TextureData(0.5f,0,0.75f,0.25f);
    public final static TextureData STARTUP_ATTACK = new TextureData(0.5f,0.25f,0.75f,0.5f);
    public final static TextureData ATTACK_TEXTURE = new TextureData(0.5f,0.5f,0.75f,0.75f);
    public final static float DISTANCE_FROM_MAIN_CHARACTER = 15;
    private CollisionObject[] startupBoxes = new CollisionObject[]{idleCollisionBoxes[0]};
    private CollisionObject[] activeBoxes = new CollisionObject[]{idleCollisionBoxes[0],
            new CollisionObject(new Vector2(60,55),0,35,20,this, CollisionObject.TYPE_ATTACK)};
    private CollisionObject[] recoveryBoxes = new CollisionObject[]{idleCollisionBoxes[0]};
    private final TimeCounter[][] attackFrames;
    protected EnemyState currentState;
    private int currentAction;
    private final EnemyAction[] actions;
    private final MainCharacter mainCharacter;
    private final TimeCounter teleportInterval;
    private final TimeCounter idleInterval;

    public TeleportEnemy(float sizeX, float sizeY, float idleSizeX, float idleSizeY, float yPosition, int id, final MainCharacter mainCharacter) {
        super(sizeX, sizeY, idleSizeX, idleSizeY, new Vector2(0, yPosition), id);
        attackFrames = new TimeCounter[][] {
                new TimeCounter[]{new TimeCounter(0.65f), new TimeCounter(0.5f), new TimeCounter(0.45f)},
                new TimeCounter[]{new TimeCounter(0.33f), new TimeCounter(0.5f), new TimeCounter(0.45f)}
        };
        currentState = EnemyState.TELEPORTING;
        this.mainCharacter = mainCharacter;
        EnemyAction move  = new EnemyAction() {
            @Override
            public void act() {
                setPosition(mainCharacter, DISTANCE_FROM_MAIN_CHARACTER);
            }
        };
        EnemyAction attack = new EnemyAction(){
            @Override
            public void act(){
                currentState = EnemyState.IDLE;
            }
        };
        actions = new EnemyAction[]{attack, move};
        currentAction = 0;
        currentDifficulty = 0;
        activeAttack = new AttackData(startupBoxes, activeBoxes, recoveryBoxes);
        //activeAttack.attackDuration = attackFrames[currentDifficulty];
        teleportInterval = new TimeCounter(0.33f);
        idleInterval = new TimeCounter(0.025f);
    }

    private void toggleCurrentAction(){
        currentAction = currentAction + 1 < actions.length ? currentAction + 1 : 0;
    }

    @Override
    public void reset(float x, float y){
        teleportInterval.reset();
        currentState = EnemyState.TELEPORTING;
        currentAction = 0;
        //activeAttack.attackDuration = attackFrames[currentDifficulty];
        idleInterval.reset();
        setPosition(mainCharacter, DISTANCE_FROM_MAIN_CHARACTER);
    }

    @Override
    public void update(GameCharacter foe, GameFlow.UpdateInterval interval, WorldData worldData) {

        adjustToFoePosition(foe);

        if(currentState == EnemyState.DEAD)
            return;

        if(currentState == EnemyState.TELEPORTING){
            teleportInterval.accum(interval);
            if(!teleportInterval.completed())
                return;
            teleportInterval.reset();
            actions[currentAction].act();
            toggleCurrentAction();

            return;
        }

        if(currentState == EnemyState.IDLE) {
            idleInterval.accum(interval);
            if (idleInterval.completed()) {
                currentState = EnemyState.ATTACKING;
                activeAttack.reset();
            }
            return;
        }

        if(currentState == EnemyState.ATTACKING) {
            activeAttack.update(interval);
            if(activeAttack.completed()){
                currentState = EnemyState.TELEPORTING;
                idleInterval.reset();
            }

            super.update(foe, interval, worldData);
        }
    }

    @Override
    public boolean hittable(){
        return currentState != EnemyState.TELEPORTING;
    }

    @Override
    public void hit(){
        currentState = EnemyState.DEAD;
    }

    //@Override
    public TextureData getCurrentTexture() {
        if(currentState == EnemyState.TELEPORTING)
            return TELEPORT_TEXTURE;

        if(currentState != EnemyState.ATTACKING)
            return IDLE_TEXTURE;

        if(activeAttack.currentState == AttackData.CollisionState.ACTIVE)
            return ATTACK_TEXTURE;
        else
            return STARTUP_ATTACK;
    }

    @Override
    public boolean attacking(){
        return currentState == EnemyState.ATTACKING;
    }

    @Override
    public boolean alive(){
        return currentState != EnemyState.DEAD;
    }


}