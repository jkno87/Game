package com.jgame.util;

import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by jose on 3/12/15.
 */
public class TextureDrawer {

    public static class TextureData {
        public final float v1;
        public final float u1;
        public final float v2;
        public final float u2;

        public TextureData(float v1, float u1, float v2, float u2){
            this.v1 = v1;
            this.u1 = u1;
            this.v2 = v2;
            this.u2 = u2;
        }

    }


    private final static int MAX_TEXTURES = 50;
    private final static int VERTEX_PER_ELEMENT = 4;
    private final static int INDICES_PER_ELEMENT = 6;
    private float[] verticesBuffer;
    private int currentIndex;
    private int elementsAdded;
    private final int elementSize;
    private final boolean withColor;
    private final FloatBuffer vertices;
    private final ShortBuffer indices;

    public TextureDrawer(boolean withColor){
        this.withColor = withColor;
        elementSize = (4 + (withColor ? 4:0)) * VERTEX_PER_ELEMENT;
        verticesBuffer = new float[elementSize * MAX_TEXTURES];

        short[] indicesBuffer = new short[MAX_TEXTURES * INDICES_PER_ELEMENT];
        int len = indicesBuffer.length;
        short j = 0;
        for(int i = 0; i < len; i+= 6, j+= 4){
            indicesBuffer[i + 0] = (short)(j + 0);
            indicesBuffer[i + 1] = (short)(j + 1);
            indicesBuffer[i + 2] = (short)(j + 2);
            indicesBuffer[i + 3] = (short)(j + 2);
            indicesBuffer[i + 4] = (short)(j + 3);
            indicesBuffer[i + 5] = (short)(j + 0);
        }

        ByteBuffer buffer = ByteBuffer.allocateDirect(MAX_TEXTURES * elementSize * Float.SIZE);
        buffer.order(ByteOrder.nativeOrder());
        vertices = buffer.asFloatBuffer();

        buffer = ByteBuffer.allocateDirect(MAX_TEXTURES * INDICES_PER_ELEMENT * Short.SIZE / 8);
        buffer.order(ByteOrder.nativeOrder());
        indices = buffer.asShortBuffer();

        indices.clear();
        indices.put(indicesBuffer, 0, MAX_TEXTURES * INDICES_PER_ELEMENT);
        indices.flip();
    }

    /**
     * Agrega los vertices de un cuadrado con textura y color
     * @param x coordenada X del centro del cuadrado
     * @param y coordenada Y del centro de cuadrado
     * @param len longitud del cuadrado
     * @param textIndices arreglo con los indices de las texturas
     * @param colors arreglo con los indices del color
     * @return Drawer que contiene los vertices del cuadrado
     */
    public void addTexturedSquare(float x, float y, float len, TextureData tdata){
        elementsAdded++;

        float x1 = x - len;
        float x2 = x + len;
        float y1 = y - len;
        float y2 = y + len;

        verticesBuffer[currentIndex++] = x1;
        verticesBuffer[currentIndex++] = y1;
        verticesBuffer[currentIndex++] = tdata.v1;
        verticesBuffer[currentIndex++] = tdata.u2;

        verticesBuffer[currentIndex++] = x2;
        verticesBuffer[currentIndex++] = y1;
        verticesBuffer[currentIndex++] = tdata.v2;
        verticesBuffer[currentIndex++] = tdata.u2;

        verticesBuffer[currentIndex++] = x2;
        verticesBuffer[currentIndex++] = y2;
        verticesBuffer[currentIndex++] = tdata.v2;
        verticesBuffer[currentIndex++] = tdata.u1;

        verticesBuffer[currentIndex++] = x1;
        verticesBuffer[currentIndex++] = y2;
        verticesBuffer[currentIndex++] = tdata.v1;
        verticesBuffer[currentIndex++] = tdata.u1;

    }

    /**
     * Agrega un indice que representa una coordenada de un cuadrado con textura y color
     * @param x coordenada X del indice
     * @param y coordenada Y del indice
     * @param textX coordenada X de la textura del indice
     * @param textY coordenada Y de la textura del indice
     * @param color arreglo con los colores del indice
     * @return Drawer que contiene el indice
     */
    private void addSquareIndex(float x, float y, float textX, float textY, float[] color){
        verticesBuffer[currentIndex++] = x;
        verticesBuffer[currentIndex++] = y;
        verticesBuffer[currentIndex++] = textX;
        verticesBuffer[currentIndex++] = textY;
        verticesBuffer[currentIndex++] = color[0];
        verticesBuffer[currentIndex++] = color[1];
        verticesBuffer[currentIndex++] = color[2];
        verticesBuffer[currentIndex++] = color[3];
    }

    public void reset(){
        elementsAdded = 0;
        currentIndex = 0;
    }

    public void draw(GL10 gl){
        if(elementsAdded == 0)
            return;

        vertices.clear();
        vertices.put(verticesBuffer, 0, currentIndex);
        vertices.flip();

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        vertices.position(0);
        gl.glVertexPointer(2, GL10.GL_FLOAT, elementSize, vertices);

        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        vertices.position(2);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, elementSize, vertices);

        if(withColor){
            gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
            vertices.position(4);
            gl.glColorPointer(4, GL10.GL_FLOAT, elementSize, vertices);
        }

        indices.position(0);
        gl.glDrawElements(GL10.GL_TRIANGLES, elementsAdded * INDICES_PER_ELEMENT, GL10.GL_UNSIGNED_SHORT, indices);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

        if(withColor)
            gl.glDisableClientState(GL10.GL_COLOR_ARRAY);

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }
}