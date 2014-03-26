/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package springphysics.springsystem;

import app2dapi.geometry.G2D;

/**
 *
 * @author tog
 */
public class Air extends AbstractGlobalForce
{
    private static final float frictionConstant = -0.01f;
    private final G2D g2d;
    
    public Air(G2D g2d, Iterable<? extends ForceReceiverPoint> globalList)
    {
        super(globalList);
        this.g2d = g2d;
    }

    @Override
    public void applyForce(ForceReceiverPoint point, float dt)
    {
        point.addForce(g2d.times(point.getVelocity(), frictionConstant * point.getVelocity().length()));
    }

}
