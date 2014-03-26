/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package springphysics.testapp;

import app2dapi.geometry.G2D;
import app2dapi.geometry.G2D.Point2D;

/**
 *
 * @author tog
 */
public class mouseTracker
{
    private final G2D g2d;
    private float[] xValues;
    private float[] yValues;
    private float xSum;
    private float ySum;
    private float modifier;
    private int curIndex;

    public mouseTracker(G2D g2d, int size)
    {
        this.g2d = g2d;
        this.xValues = new float[size];
        this.yValues = new float[size];
        curIndex = 0;
        xSum = 0;
        ySum = 0;
        this.modifier = 1.0f / size;
    }
    
    public void updatePosition(Point2D pos)
    {
        float x = pos.x();
        float y = pos.y();
        xSum -= xValues[curIndex];
        ySum -= yValues[curIndex];
        xSum += x;
        ySum += y;
        xValues[curIndex] = x;
        yValues[curIndex] = y;
        curIndex = (curIndex + 1) % xValues.length; 
    }
    
    public Point2D getCurrentPosition()
    {
        return g2d.newPoint2D(xSum * modifier, ySum * modifier);
    }
    
    
}
