package com.jme3.cubed.math;


import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.math.Vector2f;
import java.io.IOException;

public class Vector2i implements Savable, Cloneable, java.io.Serializable {
    static final long serialVersionUID = 1;
    
    public static final Vector2i ZERO = new Vector2i(0, 0);
    public static final Vector2i UNIT_X = new Vector2i(1, 0);
    public static final Vector2i UNIT_Y = new Vector2i(0, 1);

    public static Vector2i fromVec2f(Vector2f vec) {
        return new Vector2i((int) vec.x, (int) vec.y);
    }

    private int x;
    private int y;

    public Vector2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2i() {
        
    }


    public int getX() {
        return x;
    }

    public Vector2i setX(int x) {
        this.x = x;
        return this;
    }

    public int getY() {
        return y;
    }

    public Vector2i setY(int y) {
        this.y = y;
        return this;
    }
    
    public Vector2i set(Vector2i vector2Int) {
        return set(vector2Int.getX(), vector2Int.getY());
    }

    public Vector2i set(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }
    
    public Vector2i add(Vector2i vector2Int) {
        return add(vector2Int.getX(), vector2Int.getY());
    }
    
    public Vector2i add(int x, int y) {
        return new Vector2i(this.x + x, this.y + y);
    }
    
    public Vector2i addLocal(Vector2i vector2Int) {
       return addLocal(vector2Int.getX(), vector2Int.getY());
    }
    
    public Vector2i addLocal(int x, int y) {
        this.x += x;
        this.y += y;
        return this;
    }
    
    public Vector2i subtract(Vector2i vector2Int) {
        return subtract(vector2Int.getX(), vector2Int.getY());
    }
    
    public Vector2i subtract(int x, int y) {
        return new Vector2i(this.x - x, this.y - y);
    }
    
    public Vector2i subtractLocal(Vector2i vector2Int) {
        return subtractLocal(vector2Int.getX(), vector2Int.getY());
    }
    
    public Vector2i subtractLocal(int x, int y) {
        this.x -= x;
        this.y -= y;
        return this;
    }
    
    public Vector2i negate() {
        return mult(-1);
    }
    
    public Vector2i mult(float factor) {
        return mult(factor, factor);
    }
    
    public Vector2i mult(float x, float y) {
        return new Vector2i((int) (this.x * x),(int) (this.y * y));
    }
    
    public Vector2i negateLocal(){
        return multLocal(-1);
    }
    
    public Vector2i multLocal(float factor) {
        return multLocal(factor, factor);
    }
    
    public Vector2i multLocal(float x, float y) {
        this.x *= x;
        this.y *= y;
        return this;
    }
    
    public Vector2f toVector2f() {
        return new Vector2f(x, y);
    }
    
    @Override
    public Vector2i clone(){
        return new Vector2i(x, y);
    }

    @Override
    public boolean equals(Object object){
        if(object instanceof Vector2i){
            Vector2i vector3Int = (Vector2i) object;
            return ((x == vector3Int.getX()) && (y == vector3Int.getY()));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (x * 31 + y);
    }

    @Override
    public String toString(){
        return "[Vector3Int x=" + x + " y=" + y + "]";
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(x, "x", 0);
        capsule.write(y, "y", 0);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule in = im.getCapsule(this);
        x = in.readInt("x", 0);
        y = in.readInt("y", 0);
    }
}

