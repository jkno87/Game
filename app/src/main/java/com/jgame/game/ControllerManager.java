package com.jgame.game;

import android.util.Log;

import com.jgame.definitions.GameLevels;
import com.jgame.elements.GameButton;
import com.jgame.util.Square;

import java.util.concurrent.BlockingQueue;

/**
 * Created by jose on 29/06/16.
 */
public class ControllerManager {

    public enum GameInput {
        LEFT, RIGHT, INPUT_A, INPUT_OFF,NO_INPUT
    }

    private final BlockingQueue<GameInput> inputs;
    private static final int NUMBER_OF_INPUTS = 4;
    private static final int INPUT_NONE = -1;
    public static final int INPUT_LEFT = 0;
    public static final int INPUT_RIGHT = 1;
    public static final int INPUT_A = 2;
    public static final int INPUT_B = 3;
    private final Square[] gameButtons;
    private int mainButtonPressed;
    private float inputX;
    private float inputY;

    public ControllerManager(BlockingQueue<GameInput> inputQueue){
        mainButtonPressed = INPUT_NONE;
        mainButtonPressed = INPUT_NONE;
        this.gameButtons = new Square[NUMBER_OF_INPUTS];
        this.inputs = inputQueue;
        gameButtons[INPUT_LEFT] = GameActivity.INPUT_LEFT_BOUNDS;
        gameButtons[INPUT_RIGHT] = GameActivity.INPUT_RIGHT_BOUNDS;
        gameButtons[INPUT_A] = GameActivity.INPUT_A_BOUNDS;
        gameButtons[INPUT_B] = GameActivity.INPUT_B_BOUNDS;
    }

    public void handleDown(float x, float y){
        try {
            inputX = GameLevels.FRUSTUM_WIDTH * x;
            inputY = GameLevels.FRUSTUM_HEIGHT * y;

            if (gameButtons[INPUT_LEFT].contains(inputX, inputY)) {
                inputs.put(GameInput.LEFT);
                mainButtonPressed = INPUT_LEFT;
            } else if (gameButtons[INPUT_RIGHT].contains(inputX, inputY)) {
                inputs.add(GameInput.RIGHT);
                mainButtonPressed = INPUT_RIGHT;
            } else if (gameButtons[INPUT_A].contains(inputX, inputY)) {
                inputs.add(GameInput.INPUT_A);
            }
        } catch (InterruptedException e){
            Thread.interrupted();
        }
    }

    public void handleDrag(float x, float y){
        try {
            inputX = GameLevels.FRUSTUM_WIDTH * x;
            inputY = GameLevels.FRUSTUM_HEIGHT * y;

            if(mainButtonPressed != INPUT_NONE && gameButtons[mainButtonPressed].contains(inputX, inputY))
                return;

            if (gameButtons[INPUT_LEFT].contains(inputX, inputY)) {
                inputs.put(GameInput.LEFT);
                mainButtonPressed = INPUT_LEFT;
            } else if (gameButtons[INPUT_RIGHT].contains(inputX, inputY)) {
                inputs.put(GameInput.RIGHT);
                mainButtonPressed = INPUT_RIGHT;
            } else if (gameButtons[INPUT_A].contains(inputX, inputY))
                inputs.put(GameInput.INPUT_A);
            else {
                if(mainButtonPressed != INPUT_NONE)
                    inputs.put(GameInput.INPUT_OFF);
                mainButtonPressed = INPUT_NONE;
            }
        } catch (InterruptedException e){
            Thread.interrupted();
        }

    }

    public void handleUp(float x, float y){
        inputX = x;
        inputY = y;

        if(mainButtonPressed != INPUT_NONE)
            inputs.add(GameInput.INPUT_OFF);
        mainButtonPressed = INPUT_NONE;
    }

    public void handlePointerDown(float x, float y){
        try {
            inputX = GameLevels.FRUSTUM_WIDTH * x;
            inputY = GameLevels.FRUSTUM_HEIGHT * y;

            if (gameButtons[INPUT_LEFT].contains(inputX, inputY))
                inputs.put(GameInput.LEFT);
            else if (gameButtons[INPUT_RIGHT].contains(inputX, inputY))
                inputs.put(GameInput.RIGHT);
            else if (gameButtons[INPUT_A].contains(inputX, inputY)) {
                inputs.put(GameInput.INPUT_A);
            }
        } catch (InterruptedException e){
            Thread.interrupted();
        }
    }

    public void handlePointerUp(float x, float y){
        inputX = x;
        inputY = y;

        if(mainButtonPressed != INPUT_NONE)
            inputs.add(GameInput.INPUT_OFF);
    }

    /**
     * Este metodo regresa el input disponible si no se recibio otro input adicional durante el update.
     */
    public GameInput checkPressedButtons(){
        if(mainButtonPressed == INPUT_LEFT)
            return GameInput.LEFT;
        else if (mainButtonPressed == INPUT_RIGHT)
            return GameInput.RIGHT;
        else
            return GameInput.NO_INPUT;
    }

}
