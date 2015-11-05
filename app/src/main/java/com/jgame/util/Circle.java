package com.jgame.util;

/**
 * Created by ej-jose on 11/08/15.
 */
public class Circle extends GeometricElement {

    public float radius;
    public final Vector2 position;

    public Circle(float x, float y, float radius){
        this.radius = radius;
        this.position = new Vector2(x, y);
    }

    public Circle(Vector2 position, float radius){
        this.position = position;
        this.radius = radius;
    }

    /**
     * Determina si el punto x,y se encuentra dentro del circulo
     * @param x coordenada x
     * @param y coordenada y
     * @return boolean determinando si el punto esta dentro del circulo
     */
    @Override
    public boolean contains(float x, float y){
        return position.dist(x, y) <= radius;
    }

    @Override
    public boolean intersectsX(float x) {
        return position.dist(x, position.y) <= radius;
    }

    @Override
    public boolean intersectsY(float y) {
        return position.dist(position.x, y) <= radius;
    }

    /**
     * Determina si el círculo toca a otro círculo
     * @param c Circle que se pretende comprarar
     * @return boolean que determina si el objeto toca a otro Circle
     */
    public boolean containsCircle(Circle c){
        return position.dist(c.position) <= radius + c.radius;
    }

    /**
     * Determina si el vector apunta dentro del círculo
     * @param position
     * @return boolean
     */
    public boolean contains(Vector2 position){
        return contains(position.x, position.y);
    }

}
