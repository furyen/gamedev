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
public class LeftWall extends AbstractGlobalForce
{
    private final G2D g2d;
    private final float springConstant;
    private final float dampingConstant;
    private final float pos;
    
    public LeftWall(G2D g2d, Iterable<? extends ForceReceiverPoint> globalList, float springConstant, float dampingConstant, float pos)
    {
        super(globalList);
        this.g2d = g2d;
        this.springConstant = springConstant;
        this.dampingConstant = dampingConstant;
        this.pos = pos;
    }

    @Override
    public void applyForce(ForceReceiverPoint point, float dt)
    {
        Point2D position = point.getPosition();
        if(position.x() > pos) return;
        Vector2D velocity = point.getVelocity();
        float depth = pos - position.x();
        float f = depth * springConstant;
        f += -velocity.x() * dampingConstant;
        float yDamp = -((depth*10.0f + 1.0f) * dampingConstant * velocity.y());
        point.addForce(g2d.newVector2D(f, yDamp));
    }
    
}
