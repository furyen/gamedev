/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package springphysics.testapp;

import app2dapi.App2D;
import app2dapi.Device;
import app2dapi.geometry.G2D;
import app2dapi.geometry.G2D.Point2D;
import app2dapi.geometry.G2D.Polygon;
import app2dapi.geometry.G2D.Transformation2D;
import app2dapi.graphics.Canvas;
import app2dapi.graphics.Color;
import app2dapi.graphics.ColorFactory;
import app2dapi.input.keyboard.Key;
import app2dapi.input.keyboard.KeyPressedEvent;
import app2dapi.input.keyboard.KeyReleasedEvent;
import app2dapi.input.keyboard.KeyboardListener;
import app2dapi.input.mouse.MouseButton;
import app2dapi.input.mouse.MouseEvent;
import app2dapi.input.mouse.MouseListener;
import app2dapi.input.mouse.MouseMovedEvent;
import app2dapi.input.mouse.MousePressedEvent;
import app2dapi.input.mouse.MouseReleasedEvent;
import app2dapi.input.mouse.MouseWheelEvent;
import java.util.Iterator;
import java.util.Random;
import springphysics.springsystem.MassPoint;
import springphysics.springsystem.SkyObject;
import springphysics.springsystem.Spring;
import springphysics.testworld.World;

/**
 *
 * @author tog
 */
public class SpringMassApp implements App2D, MouseListener, KeyboardListener {

    private final Random rand = new Random();
    private static final float DT = 0.001f;
    private static final float WORLD_HEIGHT = 10.0f;
    private static final float POINT_SIZE = 0.1f;
    private Polygon pointPolygon;
    private ColorFactory colorFactory;
    private double simulationTime;
    private boolean isRunning;
    private G2D g2d;
    private Transformation2D worldToScreen;
    private Transformation2D screenToWorld;
    private World world;
    private MassPoint selected;
    private MassPoint endA;
    private Point2D mousePos;
    private mouseTracker mouseTrack;
    private double lastUpdate;
    private float width;
    private double skyObjectTimer = 0;
    private SkyObject connectedObject = null;
    private MassPoint spiderman;
    private G2D.Point2D spiderman2D;
    private boolean gameOn = false;

    @Override
    public boolean showMouseCursor() {
        return true;
    }

    @Override
    public boolean initialize(Device device) {
        g2d = device.getGeometry2D();
        colorFactory = device.getScreen().getColorFactory();
        device.getMouse().addMouseListener(this);
        device.getKeyboard().addKeyboardListener(this);
        simulationTime = 0.0f;
        pointPolygon = g2d.createCircle(g2d.origo(), POINT_SIZE, 16);
        spiderman2D = g2d.newPoint2D(device.getScreen().getPixelWidth() / 2, 500);
        //Set up transformations between world and screen.
        float w = device.getScreen().getPixelWidth();
        float h = device.getScreen().getPixelHeight();
        float worldWidth = WORLD_HEIGHT * (w / h);
        width = worldWidth;
        Transformation2D worldToUnit = g2d.scale(1.0f / worldWidth, 1.0f / WORLD_HEIGHT);
        Transformation2D UnitToUpperLeftUnit = g2d.combine(g2d.translate(0, 1.0f), g2d.flipY());
        Transformation2D UnitToScreen = g2d.combine(g2d.scale(w, h), UnitToUpperLeftUnit);
        worldToScreen = g2d.combine(UnitToScreen, worldToUnit);
        screenToWorld = g2d.inverse(worldToScreen);
        //Create the world
        world = new World(g2d, 0.5f, 0.5f, worldWidth - 0.5f);
        isRunning = false;
        selected = null;
        endA = null;
        mousePos = g2d.origo();
        mouseTrack = new mouseTracker(g2d, 10);
        lastUpdate = 0.0f;
        return true;
    }

    @Override
    public boolean update(double time) {
        mouseTrack.updatePosition(mousePos);
        if (isRunning) {
            while (simulationTime + DT < time) {
                simulationTime += DT;
                world.update(DT);
            }
        } else {
            simulationTime = time;
        }
        if (selected != null) {
            float deltaTime = (float) (time - lastUpdate);
            selected.setPosition(mouseTrack.getCurrentPosition(), deltaTime);
        }
        if (skyObjectTimer + 1 < time) {
            float randomY = rand.nextFloat() * 4f + 6f;
            world.createSkyObject(g2d.newPoint2D(width, randomY));
            skyObjectTimer = time;
        }

        lastUpdate = time;
        return true;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.clear(colorFactory.getWhite());
        if (gameOn) {
            drawMassPoints(canvas);
        } else {
            canvas.drawPoint(spiderman2D, 13);
        }
        drawSprings(canvas);
      
        canvas.setColor(colorFactory.getBlack());

        drawSkyObjects(canvas);
        if (endA != null) {
            canvas.setColor(colorFactory.getBlack());
            canvas.setTransformation(worldToScreen);
            canvas.drawLine(endA.getPosition(), mousePos);
        }
        try {
//            canvas.drawLine( screenToWorld.transform(spiderman), connectedObject.getContainedPoint());
        } catch (Exception e) {

        }

    }

    @Override
    public void destroy() {
        //Do nothing
    }

    @Override
    public void onMouseMoved(MouseMovedEvent e) {
        mousePos = mouseToWorld(e);
    }

    @Override
    public void onMousePressed(MousePressedEvent e) {
        mousePos = mouseToWorld(e);
        world.clearSprings();
        if (e.getButton() == MouseButton.LEFT) {
            if (!gameOn) {
                spiderman = world.createSpiderman(screenToWorld.transform(spiderman2D));
                gameOn = true;
            }
            MassPoint auxPoint = new MassPoint(g2d, g2d.newPoint2D(mousePos.x(), mousePos.y()), 1f);
            Iterator<SkyObject> it = world.getSkyObjectIterator();
            while (it.hasNext()) {

                SkyObject so = it.next();
                if (so.containsPoint(auxPoint)) {
                    so.setContainedMassPoint(auxPoint);
                    connectedObject = so;
                    world.createSpring(spiderman, connectedObject.getContainedMassPoint());
                }
            }

        } else if (e.getButton() == MouseButton.RIGHT) {
           
        }
    }

    @Override
    public void onMouseReleased(MouseReleasedEvent e) {
        mousePos = mouseToWorld(e);
        if (e.getButton() == MouseButton.LEFT) {
            if (selected != null) {
                selected.setControlled(false);
                selected = null;
            }
        }
//        } else if (e.getButton() == MouseButton.RIGHT) {
//            if (endA != null) {
//                MassPoint endB = world.pickPoint(mousePos);
//                if (endB != null) {
//                    world.createSpring(endA, endB);
//                }
//            }
//            endA = null;
//        }
    }

    @Override
    public void onMouseWheel(MouseWheelEvent e) {
        //Do nothing
    }

    @Override
    public void onKeyPressed(KeyPressedEvent e) {
        if (e.getKey() == Key.VK_SPACE) {
            isRunning = !isRunning;
             if (world.getSpringIterator().hasNext()) {
            System.out.println(world.getSpringIterator().next().getB().getPosition().x());}
        }
        if (e.getKey() == Key.VK_A) {
  if (world.getSpringIterator().hasNext()) {
            System.out.println(world.getSpringIterator().next().getB().getPosition().x());
        }
        }
    }

    @Override
    public void onKeyReleased(KeyReleasedEvent e) {
        //Do nothing...
    }

    private void drawSprings(Canvas canvas) {
        canvas.setTransformation(worldToScreen);
        Iterator<Spring> it = world.getSpringIterator();
        while (it.hasNext()) {
            Spring spr = it.next();
            if (!spr.isBroken()) {
                float tension = Math.abs(spr.getTension());
                Color c = colorFactory.newColor(tension, 1.0f - tension, 0);
                canvas.setColor(c);
                canvas.drawLine(spr.getA().getPosition(), spr.getB().getPosition());
            }
        }
    }

    private void drawMassPoints(Canvas canvas) {
        Iterator<MassPoint> it = world.getMassPointIterator();
        while (it.hasNext()) {
            MassPoint mp = it.next();
            Transformation2D localToWorld = g2d.translateOrigoTo(mp.getPosition());
            Transformation2D localToScreen = g2d.combine(worldToScreen, localToWorld);
            canvas.setTransformation(localToScreen);
            canvas.drawFilledPolygon(pointPolygon);
        }
    }

    private void drawSkyObjects(Canvas canvas) {
        Iterator<SkyObject> it = world.getSkyObjectIterator();
        while (it.hasNext()) {
            SkyObject mp = it.next();

//            Transformation2D localToWorld = g2d.translateOrigoTo(mp.getPosition());
//            Transformation2D localToScreen = g2d.combine(worldToScreen, localToWorld);
//            canvas.setTransformation(localToScreen);
            canvas.drawFilledPolygon(g2d.createRectangle(mp.getPosition(), mp.getWidth(), mp.getHeight()));
        }
    }

    private Point2D mouseToWorld(MouseEvent e) {
        Point2D scrPos = g2d.newPoint2D(e.getX(), e.getY());
        return screenToWorld.transform(scrPos);
    }

}
