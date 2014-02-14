package com.jme3.cubed.math;


import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.math.Vector3f;
import java.io.IOException;

/**
 *
 * @author Carl
 */
public class Vector3i implements Savable, Cloneable, java.io.Serializable{
    static final long serialVersionUID = 1;
    
    public static final Vector3i ZERO = new Vector3i(0, 0, 0);
    public static final Vector3i UNIT_X = new Vector3i(1, 0, 0);
    public static final Vector3i UNIT_Y = new Vector3i(0, 1, 0);
    public static final Vector3i UNIT_Z = new Vector3i(0, 0, 1);

    public static Vector3i fromVec3f(Vector3f vec) {
        return new Vector3i((int) vec.x, (int) vec.y, (int) vec.z);
    }

    public Vector3i(int x, int y, int z){
        this();
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3i(){
        
    }
    private int x;
    private int y;
    private int z;

    public int getX(){
        return x;
    }

    public Vector3i setX(int x){
        this.x = x;
        return this;
    }

    public int getY(){
        return y;
    }

    public Vector3i setY(int y){
        this.y = y;
        return this;
    }

    public int getZ(){
        return z;
    }

    public Vector3i setZ(int z){
        this.z = z;
        return this;
    }
    
    public Vector3i set(Vector3i vector3Int){
        return set(vector3Int.getX(), vector3Int.getY(), vector3Int.getZ());
    }

    public Vector3i set(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }
    
    public Vector3i add(Vector3i vector3Int){
        return add(vector3Int.getX(), vector3Int.getY(), vector3Int.getZ());
    }
    
    public Vector3i add(int x, int y, int z){
        return new Vector3i(this.x + x, this.y + y, this.z + z);
    }
    
    public Vector3i addLocal(Vector3i vector3Int){
       return addLocal(vector3Int.getX(), vector3Int.getY(), vector3Int.getZ());
    }
    
    public Vector3i addLocal(int x, int y, int z){
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }
    
    public Vector3i subtract(Vector3i vector3Int){
        return subtract(vector3Int.getX(), vector3Int.getY(), vector3Int.getZ());
    }
    
    public Vector3i subtract(int x, int y, int z){
        return new Vector3i(this.x - x, this.y - y, this.z - z);
    }
    
    public Vector3i subtractLocal(Vector3i vector3Int){
        return subtractLocal(vector3Int.getX(), vector3Int.getY(), vector3Int.getZ());
    }
    
    public Vector3i subtractLocal(int x, int y, int z){
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }
    
    public Vector3i negate(){
        return mult(-1);
    }
    
    public Vector3i mult(int factor){
        return mult(factor, factor, factor);
    }
    
    public Vector3i mult(int x, int y, int z){
        return new Vector3i(this.x * x, this.y * y, this.z * z);
    }
    
    public Vector3i negateLocal(){
        return multLocal(-1);
    }
    
    public Vector3i multLocal(int factor){
        return multLocal(factor, factor, factor);
    }
    
    public Vector3i multLocal(int x, int y, int z){
        this.x *= x;
        this.y *= y;
        this.z *= z;
        return this;
    }
    
    public Vector3f toVector3f() {
        return new Vector3f(x, y, z);
    }
    
    @Override
    public Vector3i clone(){
        return new Vector3i(x, y, z);
    }

    @Override
    public boolean equals(Object object){
        if(object instanceof Vector3i){
            Vector3i vector3Int = (Vector3i) object;
            return ((x == vector3Int.getX()) && (y == vector3Int.getY()) && (z == vector3Int.getZ()));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (x * 211 + y) * 97 + z;
    }

    @Override
    public String toString(){
        return "[Vector3Int x=" + x + " y=" + y + " z=" + z + "]";
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(x, "x", 0);
        capsule.write(y, "y", 0);
        capsule.write(z, "z", 0);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule in = im.getCapsule(this);
        x = in.readInt("x", 0);
        y = in.readInt("y", 0);
        z = in.readInt("z", 0);
    }
}

