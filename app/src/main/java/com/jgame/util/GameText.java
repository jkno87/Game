package com.jgame.util;

import android.util.Log;

import com.jgame.elements.GameElement;

/**
 * Created by ej-jose on 17/08/15.
 */
public class GameText {

    private final byte[] texto;
    private float x;
    private float y;
    private float size;
    private static int LETTERS_COLUMN = 16;
    private static int LETTERS_ROW = 16;
    private static final SimpleTextureData LETTERS_DATA = new SimpleTextureData(LETTERS_COLUMN, LETTERS_ROW);

    public GameText(String texto, float x, float y, float size){
        this.texto = texto.getBytes();
        this.x = x;
        this.y = y;
        this.size = size;
    }

    /**
     * Recibe un byte que representa una letra del alfabeto y regresa su textura en opengl
     * @param letter byte que se desea representar con una textura
     * @return arreglo que contiene las coordenadas de textura de opengl para un byte.
     */
    private float[] getTextureData(byte letter){
        int asciiCharacter = (int) letter - 97;

        if(asciiCharacter < LETTERS_COLUMN)
            return LETTERS_DATA.getTextureCoordinates(asciiCharacter, 0);
        else
            return LETTERS_DATA.getTextureCoordinates(asciiCharacter - LETTERS_COLUMN, 1);
    }

    /**
     * Genera las coordenadas, incluyendo la textura, de las letras del texto contenido.
     * @return Arreglo que contiene las texturas de cada letra para dibujarse en opengl.
     */
    public float[][] getLettersTexture(){
        float[][] textures = new float[texto.length][];
        float currentX = x - size * (texto.length / 2);
        float offset = texto.length * 0.2f;

        for(int i = 0; i < texto.length; i++){
            Square s = new Square(currentX, y, size, size);
            textures[i] = s.getTextureCoords(getTextureData(texto[i]));
            currentX += size + offset;
        }

        return textures;
    }

    /**
     * Agrega los vertices a letterDrawer para dibujar el GameText.
     * @param letterDrawer Drawer al que se agregaran los vertices
     * @return Drawer que contiene los vertices de GameText
     */
    public void addLetterTexture(TextureDrawer letterDrawer){
        float currentX = x - size * (texto.length / 2);
        float offset = texto.length * 0.2f;

        for(int i = 0; i < texto.length; i++){
            letterDrawer.addTexturedSquare(currentX, y, size,
                    getTextureData(texto[i]), GameElement.DEFAULT_COLOR);
            currentX += size + offset;
        }
    }

    /**
     * Agrega los vertices a letterDrawer para dibujar el GameText.
     * @param letterDrawer Drawer al que se agregaran los vertices
     * @return Drawer que contiene los vertices de GameText
     */
    public void addLetterTexture(Drawer letterDrawer){
        float currentX = x - size * (texto.length / 2);
        float offset = texto.length * 0.2f;

        for(int i = 0; i < texto.length; i++){
            letterDrawer.addTexturedSquare(currentX, y, size,
                    getTextureData(texto[i]), GameElement.DEFAULT_COLOR);
            currentX += size + offset;
        }
    }

}
