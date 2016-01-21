package com.jgame.game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.jgame.definitions.GameLevels;
import com.jgame.util.GameButton;
import com.jgame.util.Square;

/**
 * Created by jose on 27/01/15.
 */
public class GameActivity extends Activity {

    private GLSurfaceView gameSurfaceView;
    private SoundManager soundManager;
    private GameFlow gameFlow;
    private Boolean paused;
    public final GameButton continueButton = new GameButton(new Square(GameLevels.FRUSTUM_WIDTH / 2, GameLevels.FRUSTUM_HEIGHT/2, 150, 40), "continue");
    public final GameButton quitButton = new GameButton(new Square(GameLevels.FRUSTUM_WIDTH / 2, GameLevels.FRUSTUM_HEIGHT/2 - 100, 150, 40), "quit");

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        soundManager = GameResources.soundManager;
        gameFlow = new LevelSelectFlow(this);
        gameSurfaceView = new GameSurfaceView(this);
        setContentView(gameSurfaceView);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        paused = false;
    }

    /**
     * Regresa el GameFlow actual de la actividad.
     * @return
     */
    public GameFlow getGameFlow(){
        return gameFlow;
    }

    /**
     * Asigna un nuevo GameFlow a la actividad
     * @param gameFlow
     */
    public void setGameFlow(GameFlow gameFlow){
        this.gameFlow = gameFlow;
    }

    /**
     * Regresa un boolean con el estado de pausa.
     * @return boolean con el estado de pause
     */
    public boolean isPaused(){
        synchronized (paused){
            return paused;
        }
    }

    /**
     * Cambia el estado de pausa.
     */
    public void togglePause(){
        synchronized (paused){
            paused = !paused;
        }
    }

    /**
     * Regresa a la actividad de LoadActivity y le manda la senal de terminar la aplicacion
     */
    private void quitGame(){
        Intent intent = new Intent(this, LoadActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e){
        if(!isPaused())
            return false;

        float x = (e.getX() / (float) gameSurfaceView.getWidth()) * GameLevels.FRUSTUM_WIDTH;
        float y = (((float) gameSurfaceView.getHeight() - e.getY()) / (float) gameSurfaceView.getHeight()) * GameLevels.FRUSTUM_HEIGHT;

        if(continueButton.bounds.contains(x, y))
            togglePause();
        else if(quitButton.bounds.contains(x, y))
            quitGame();

        return true;
    }

    @Override
    public void onResume(){
        super.onResume();
        soundManager.iniciar();
        new Thread(soundManager).start();
        gameSurfaceView.onResume();
        gameFlow.resume();
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d("Game", "onPause");
        soundManager.terminar();
        gameSurfaceView.onPause();
        gameFlow.pause();
    }

    @Override
    public boolean onKeyDown(int keycode, KeyEvent event){
        if(KeyEvent.KEYCODE_BACK == keycode){
            gameFlow.pause();
            togglePause();
            return true;
        }

        return false;
    }

}
