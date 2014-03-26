/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package springphysics.springsystem;

import app2dapi.geometry.G2D;
import app2dapi.geometry.G2D.Point2D;
import app2dapi.geometry.G2D.Vector2D;

/**
 *
 * @author tog
 */
public class Ground extends AbstractGlobalForce
{
    private final G2D g2d;
    private final float springConstant;
    private final float dampingConstant;
    private final float height;
    
    public Ground(G2D g2d, Iterable<? extends ForceReceiverPoint> globalList, float springConstant, float dampingConstant, float height)
    {
        super(globalList);
        this.g2d = g2d;
        this.springConstant = springConstant;
        this.dampingConstant = dampingConstant;
        this.height = height;
    }

    @Override
    public void applyForce(ForceReceiverPoint point, float dt)
    {
        Point2D pos = point.getPosition();
        if(pos.y() > height) return;
        Vector2D velocity = point.getVelocity();
        float depth = height - pos.y();
        float f = depth * springConstant;
        f += -velocity.y() * dampingConstant;
        float xDamp = -((depth*10.0f + 1.0f) * dampingConstant * velocity.x());
        point.addForce(g2d.newVector2D(xDamp, f));
    }
    
}
