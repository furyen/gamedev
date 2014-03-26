/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package springphysics.springsystem;

import app2dapi.geometry.G2D.Vector2D;

/**
 *
 * @author tog
 */
public class Gravity extends AbstractGlobalForce
{
    private final Vector2D gravityAcceleration;

    public Gravity(Iterable<? extends ForceReceiverPoint> globalList, Vector2D gravityAcceleration)
    {
        super(globalList);
        this.gravityAcceleration = gravityAcceleration;
    }

    @Override
    public void applyForce(ForceReceiverPoint point, float dt)
    {
        point.addAcceleration(gravityAcceleration);
    }    
}
