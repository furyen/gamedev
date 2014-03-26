/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package springphysics.springsystem;

import app2dapi.geometry.G2D;
import springphysics.general.PhysicsEntity;

/**
 *
 * @author Pege
 */
public class SkyObject implements PhysicsEntity {

    private G2D.Vector2D velocity;
    private final G2D g2d;
    private G2D.Point2D position;
    float width = 1.0f;
    float height = 0.1f;
    private MassPoint containedPoint;
    private G2D.Point2D contPoint2d;
    private float difx;
    private float dify;
    
    public SkyObject(G2D g2d, G2D.Point2D position) {
        this.g2d = g2d;
        this.position = position;

    }

    @Override 
    public void update(float dt) {

        velocity = g2d.fromTo(position, g2d.newPoint2D(position.x() - 1f, position.y()));
        position = g2d.add(position, g2d.times(velocity, dt));
        if (containedPoint != null) {
            contPoint2d = g2d.newPoint2D(position.x()-difx, position.y()-dify);
            containedPoint.setPosition( contPoint2d, dt);
        }
    }

    public float getLeft() {
        return position.x() - width / 2.0f;
    }

    public float getRight() {
        return position.x() + width / 2.0f;
    }

    public G2D.Vector2D getVelocity() {
        return velocity;
    }

    public void setVelocity(G2D.Vector2D velocity) {
        this.velocity = velocity;
    }

    public G2D.Point2D getPosition() {
        return position;
    }

    public void setPosition(G2D.Point2D position) {
        this.position = position;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getUp() {
        return position.y() + height / 2.0f;
    }

    public float getDown() {
        return position.y() - height / 2.0f;
    }

    public boolean containsPoint(MassPoint point) {
        boolean check = false;
        if (point.getPosition().x() > getLeft() && point.getPosition().x() < getRight() && point.getPosition().y() > getDown() && point.getPosition().y() < getUp()) {
            check = true;
        }
        return check;
    }

    public MassPoint getContainedMassPoint() {
        return containedPoint;
    }

    public void setContainedMassPoint(MassPoint containedPoint) {
        this.containedPoint = containedPoint;
//        this.contPoint2d = containedPoint.getPosition();
        difx = position.x() - containedPoint.getPosition().x();
        dify = position.y() - containedPoint.getPosition().y();
        
        
    }
}
