package org.apache.commons.math3.userguide.genetics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;

import org.apache.commons.math3.genetics.GeneticAlgorithm;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.FastMath;

public class Polygon {

    // the polygon in packed representation:
    // index | data
    //    0  | red component
    //    1  | green component
    //    2  | blue component
    //    3  | alpha channel
    //   
    private float[] data;

    public static Polygon randomPolygon(int length) {
        final int polygonSize = 4 + 2 * length;

        final RandomGenerator random = GeneticAlgorithm.getRandomGenerator();
        
        Polygon p = new Polygon();
        p.data = new float[polygonSize];

        p.data[0] = random.nextFloat(); // r
        p.data[1] = random.nextFloat(); // g
        p.data[2] = random.nextFloat(); // b
        p.data[3] = FastMath.max(0.2f, random.nextFloat() * random.nextFloat()); // a
        
        float px = random.nextFloat();
        float py = random.nextFloat();
        
        for (int k = 0; k < length; k++) {
            p.data[4 + 2*k] = px + (random.nextFloat() - 0.5f);
            p.data[5 + 2*k] = py + (random.nextFloat() - 0.5f);
        }
        return p;
    }

    public Polygon mutate(float mutationRate, float mutationAmount) {
        Polygon mutated = new Polygon();
        int size = data.length;
        mutated.data = new float[size];
        for (int i = 0; i < size; i++) {
            float val = this.data[i];
            if (GeneticAlgorithm.getRandomGenerator().nextFloat() < mutationRate) {
                val += GeneticAlgorithm.getRandomGenerator().nextFloat() * mutationAmount * 2 - mutationAmount;
                
                if (val < 0f) {
                    val = 0f;
                } else if (val > 1f) {
                    val = 1f;
                }
            }
            mutated.data[i] = val;
        }
        return mutated;
    }    

    public void draw(Graphics2D g, int width, int height) {
        g.setColor(new Color(data[0], data[1], data[2], data[3]));

        GeneralPath path = new GeneralPath();
        path.moveTo(data[4] * width, data[5] * height);
        
        int polygonLength = (data.length - 4) / 2;
        for (int j = 1; j < polygonLength; j++) {
            path.lineTo(data[4 + j * 2] * width, data[5 + j * 2] * height);
        }
        path.closePath();

        g.fill(path);
    }
}