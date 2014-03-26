 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package springphysics.testworld;

import app2dapi.geometry.G2D;
import app2dapi.geometry.G2D.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import springphysics.general.PhysicSimulator;
import springphysics.general.impl.PhysicsSimulatorImpl;
import springphysics.springsystem.Air;
import springphysics.springsystem.Gravity;
import springphysics.springsystem.Ground;
import springphysics.springsystem.LeftWall;
import springphysics.springsystem.MassPoint;
import springphysics.springsystem.RightWall;
import springphysics.springsystem.SkyObject;
import springphysics.springsystem.Spring;

/**
 *
 * @author tog
 */
public class World {

    private static final float PICK_SQR_DISTANCE = 0.01f;
    private static final float POINT_MASS = 1.0f;
    private static final float SPRING_CONSTANT = 1000.0f;
    private static final float SPRING_DAMPING = 100.0f;
    private static final float SPRING_TOLERANCE = 0.3f;
    private static final float GROUND_SPRING_CONSTANT = 1500.0f;
    private static final float GROUND_SPRING_DAMPING = 5.0f;
    private static final float GRAVITY = -9.82f;

    private final G2D g2d;
    private final PhysicSimulator sim;
    private final ArrayList<MassPoint> allPoints;
    private MassPoint connectPoint;
    private G2D.Vector2D velocity;
    private final ArrayList<SkyObject> skyObjects;
    private final ArrayList<Spring> allSprings;

    public World(G2D g2d, float ground, float left, float right) {
        this.g2d = g2d;
        allPoints = new ArrayList<>();
        allSprings = new ArrayList<>();
        skyObjects = new ArrayList<>();
        sim = new PhysicsSimulatorImpl();
        sim.addForceGenerator(new Gravity(allPoints, g2d.newVector2D(0, GRAVITY)));
        sim.addForceGenerator(new Ground(g2d, allPoints, GROUND_SPRING_CONSTANT, GROUND_SPRING_DAMPING, ground));
//        sim.addForceGenerator(new LeftWall(g2d, allPoints, GROUND_SPRING_CONSTANT, GROUND_SPRING_DAMPING, left));
//        sim.addForceGenerator(new RightWall(g2d, allPoints, GROUND_SPRING_CONSTANT, GROUND_SPRING_DAMPING, right));
        sim.addForceGenerator(new Air(g2d, allPoints));
    }

    public MassPoint createSpiderman(Point2D position) {
        MassPoint mp = new MassPoint(g2d, position, POINT_MASS);
        allPoints.add(mp);
        sim.addPhysicsEntity(mp);
        return mp;
    }

//    public MassPoint createConnectionPoint(Point2D position, G2D.Vector2D velocity) {
//        MassPoint mp = new MassPoint(g2d, position, POINT_MASS);
//        sim.addPhysicsEntity(mp);
//        connectPoint = mp;
//        this.velocity = velocity;
//        return mp;
//    }

    public SkyObject createSkyObject(G2D.Point2D position) {
        SkyObject so = new SkyObject(g2d, position);
        skyObjects.add(so);
        sim.addPhysicsEntity(so);
        return so;
    }

    public Spring createSpring(MassPoint endA, MassPoint endB) {
        float restLength = g2d.fromTo(endA.getPosition(), endB.getPosition()).length();
        float tolerance = SPRING_TOLERANCE * restLength;
        Spring spr = new Spring(g2d, endA, endB, SPRING_CONSTANT, SPRING_DAMPING, restLength, restLength - tolerance, restLength + tolerance);
        sim.addForceGenerator(spr);
        allSprings.add(spr);
        return spr;
    }

    public void clearSprings() {
        
        allSprings.stream().forEach((sp) -> {
            sim.removeForceGenerator(sp);
        });
        allSprings.clear();
    }

    public MassPoint pickPoint(Point2D mousePos) {
        MassPoint res = null;
        float closest = PICK_SQR_DISTANCE;
        for (MassPoint mp : allPoints) {
            float dist = g2d.fromTo(mousePos, mp.getPosition()).sqrLength();
            if (dist < closest) {
                res = mp;
                closest = dist;
            }
        }
        return res;
    }

    public Iterator<Spring> getSpringIterator() {
        return allSprings.iterator();
    }

    public Iterator<MassPoint> getMassPointIterator() {
        return allPoints.iterator();
    }

    public Iterator<SkyObject> getSkyObjectIterator() {
        return skyObjects.iterator();
    }

    public void update(float dt) {

        sim.update(dt);

        //TODO: remove broken springs?
    }

}
