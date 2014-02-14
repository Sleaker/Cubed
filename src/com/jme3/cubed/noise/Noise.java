package com.jme3.cubed.noise;

import java.util.Random;

public abstract class Noise {
    /**
     * Random to use for generating values.
     */
    protected Random rand;
    
    /**
     * @param rand The random number generator
     */
    public Noise(int seed) {
        this.rand = new Random(seed);
    }
    
    /**
     * Get value of the noise at the given x,y,z
     * @param x
     * @param y
     * @param z
     * @return value
     */
    public abstract double getValue(double x, double y, double z);
}
