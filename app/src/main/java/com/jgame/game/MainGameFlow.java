package com.jgame.game;

import com.jgame.definitions.CharacterInformation;
import com.jgame.elements.ElementCreator;
import com.jgame.elements.GameElement;
import com.jgame.elements.Organism;
import com.jgame.util.TimeCounter;
import com.jgame.util.Vector2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ej-jose on 12/08/15.
 */
public class MainGameFlow extends GameFlow {

    public enum GameState {
        PLAYING, FINISHED
    }

    private final TimeCounter GAME_OVER_UPDATE_INTERVAL = new TimeCounter(0.02f);
    public final static float FRUSTUM_HEIGHT = 480f;
    public final static float FRUSTUM_WIDTH = 320f;
    public final static float BAIT_TIME = 0.5f;
    public final CharacterInformation characterInfo;
    public final ElementCreator elementCreator;
    public final List<GameElement> levelElements;
    public final float timeLimit;
    public float timeElapsed;
    public GameState currentState;

    public MainGameFlow(CharacterInformation characterInfo, ElementCreator elementCreator, float timeLimit){
        this.characterInfo = characterInfo;
        this.elementCreator = elementCreator;
        this.timeLimit = timeLimit;
        levelElements = new ArrayList<GameElement>();
        currentState = GameState.PLAYING;
        elementCreator.start();
    }

    @Override
    public void handleDrag(float x, float y){
    }

    @Override
    public void handleUp(float x, float y){
        if(currentState != GameState.PLAYING)
            return;

        float gameX = FRUSTUM_WIDTH * x;
        float gameY = FRUSTUM_HEIGHT * y;

        levelElements.add(new Organism(BAIT_TIME, new Vector2(gameX, gameY)));
    }

    @Override
    public void handleDown(float x, float y){
    }

    @Override
    public void update(float interval){
        if(currentState == GameState.PLAYING) {
            timeElapsed += interval;
            levelElements.addAll(elementCreator.createElements(interval));
            Iterator<GameElement> itElements = levelElements.iterator();
            while (itElements.hasNext()) {
                GameElement e = itElements.next();
                e.update(levelElements, interval);
                if (!e.vivo())
                    itElements.remove();
            }

            if(timeElapsed > timeLimit)
                currentState = GameState.FINISHED;
        }
    }


    /**
     * Funcion que se llamara cuando el juego se encuentre en el estado game over.
     * Reduce el tiempo mostrado en pantalla para simular que se estan contando los segundos que restaron cuando termina el juego.
     * @param interval diferencia de tiempo que ha transcurrido desde el ultimo update.
     */
    private void updateTerminado(float interval){
        GAME_OVER_UPDATE_INTERVAL.accum(interval);
        if(!GAME_OVER_UPDATE_INTERVAL.completed())
            return;

        //TODO: Variables que deben agregarse a la clase para que se muestren al usuario. Ahorita solo estan como variables locales
        float timeShown = 0;
        float speciesSaved = 0;
        float timeBonus = 0;
        float speciesPoints = 0;

        timeShown--;
        speciesSaved--;
        GAME_OVER_UPDATE_INTERVAL.reset();
    }

    @Override
    public void pause(){

    }

    @Override
    public void resume(){

    }

}
