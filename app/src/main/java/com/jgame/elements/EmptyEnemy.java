package com.jgame.elements;

import com.jgame.game.GameActivity;
import com.jgame.game.GameFlow;
import com.jgame.util.TextureDrawer;
import com.jgame.util.TimeCounter;
import com.jgame.game.GameActivity.WorldData;
import com.jgame.util.Vector2;

/**
 * Enemigo que sirve nada mas para representar el intervalo de tiempo que existe en el juego cuando se elimina al enemigo principal.
 * Tambien se usa para evitar estar usando el horror de null
 * Created by jose on 24/05/16.
 */
public class EmptyEnemy extends GameCharacter {

    private final int framesToLive;
    private int currentFrame;

    public EmptyEnemy(int id, float time) {
        super(0, 0, 0, 0, new Vector2(), id);
        framesToLive = (int)(time * GameActivity.FRAMES_PER_SECOND);
    }

    @Override
    public void reset(float x, float y){
        currentFrame = 0;
    }

    @Override
    public boolean hittable() {
        return false;
    }

    @Override
    public boolean alive() {
        return currentFrame < framesToLive;
    }

    @Override
    public boolean attacking() {
        return false;
    }

    @Override
    public void update(GameCharacter foe, WorldData wData) {
        currentFrame += 1;
    }

    //@Override
    public TextureDrawer.TextureData getCurrentTexture() {
        return MainCharacter.IDLE_TEXTURE;
    }

    @Override
    public void hit() {

    }
}
