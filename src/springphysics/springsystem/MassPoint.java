
package springphysics.springsystem;

import app2dapi.geometry.G2D;
import app2dapi.geometry.G2D.Point2D;
import app2dapi.geometry.G2D.Vector2D;
import springphysics.general.PhysicsEntity;


public class MassPoint implements ForceReceiverPoint, PhysicsEntity
{
    private final G2D g2d;
    private float mass;
    private float invMass;
    //private Point2D lastPosition;
    private Point2D position;
    private Vector2D velocity;
    private Vector2D force;
    private boolean controlled;

    public MassPoint(G2D g2d, Point2D position, float mass)
    {
        this.g2d = g2d;
        this.position = position;
        this.mass = mass;
        this.invMass = 1.0f / mass;
        this.controlled = false;
        //this.lastPosition = position;
        this.force = g2d.zeroVector2D();
        this.velocity = g2d.zeroVector2D();
    }
    
    @Override
    public G2D.Point2D getPosition()
    {
        return position;
    }
    
    @Override
    public Vector2D getVelocity()
    {
        return velocity;
    }
    
    public void setPosition(Point2D pos, float dt)
    {
//        if(!controlled)
//        {
//            throw new RuntimeException("Position can only be set when masspoint is controlled!");
//        }
        velocity = g2d.fromTo(position, pos);
        velocity = g2d.times(velocity, 1.0f / dt);
        this.position = pos;
    }

    @Override
    public void addForce(G2D.Vector2D force)
    {
        this.force = g2d.add(this.force, force);
    }
    
    @Override
    public void addAcceleration(Vector2D acceleration)
    {
        this.force = g2d.add(this.force, g2d.times(acceleration, mass));
    }
    
    public void setControlled(boolean controlled)
    {
        this.controlled = controlled;
    }
    
    public boolean isControlled()
    {
        return controlled;
    }
    
    @Override
    public void update(float dt)
    {
        if(!controlled)
        {
            Vector2D acceleration = g2d.times(force, invMass);  
            velocity = g2d.add(velocity, g2d.times(acceleration, dt));
            //lastPosition = position;
            position = g2d.add(position, g2d.times(velocity, dt));
        }
        force = g2d.zeroVector2D();
    }
    
    public void setMass(float mass)
    {
        this.mass = mass;
        this.invMass = 1.0f / mass;
    }
    
    public float getMass()
    {
        return mass;
    }
}
