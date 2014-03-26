package springphysics.springsystem;

import app2dapi.geometry.G2D.Point2D;
import app2dapi.geometry.G2D.Vector2D;

/**
 *
 * @author tog
 */
public interface ForceReceiverPoint
{
    public Point2D getPosition();
    public Vector2D getVelocity();
    public void addForce(Vector2D force);
    public void addAcceleration(Vector2D acceleration);
}
