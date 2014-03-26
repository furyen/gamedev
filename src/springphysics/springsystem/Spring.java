package springphysics.springsystem;

import app2dapi.geometry.G2D;
import app2dapi.geometry.G2D.Vector2D;
import springphysics.general.ForceGenerator;

/**
 *
 * @author tog
 */
public class Spring implements ForceGenerator
{
    private final G2D geo;
    private final ForceReceiverPoint endA;
    private final ForceReceiverPoint endB;
    private float springConstant;
    private float dampingConstant;
    private float restLength;
    private float minLength;
    private float maxLength;
    private float lastLength;
    private float curLength;
    private boolean isBroken;

    public Spring(G2D geo, ForceReceiverPoint endA, ForceReceiverPoint endB, float springConstant, float dampingConstant, float restLength, float minLength, float maxLength)
    {
        this.geo = geo;
        this.endA = endA;
        this.endB = endB;
        this.springConstant = springConstant;
        this.dampingConstant = dampingConstant;
        this.restLength = restLength;
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.isBroken = false;
        Vector2D v = geo.fromTo(endA.getPosition(), endB.getPosition());
        this.lastLength = v.length();
        this.curLength = this.lastLength;
    }
    
    @Override
    public void generateForce(float dt)
    {
        if(isBroken) return;
        Vector2D v = geo.fromTo(endA.getPosition(), endB.getPosition());
        this.curLength = v.length();
        if(curLength < minLength || curLength > maxLength)
        {
            isBroken = true;
            return;
        }
        float expandSpeed = (curLength - lastLength) / dt;
        lastLength = curLength;
        if(curLength < 0.0001f) return;
        float damping = expandSpeed * dampingConstant;
        float stretch = curLength - restLength;
        float force = stretch * springConstant;
        Vector2D forceA = geo.times(v, (force + damping) / curLength);
        endA.addForce(forceA);
        endB.addForce(geo.inverse(forceA));
    }
    
    public float getCurrentLength()
    {
        return curLength;
    }
    
    public float getRestLength()
    {
        return restLength;
    }
    
    public void setRestLength(float restLength)
    {
        this.restLength = restLength;
    }
    
    public float getSpringConstant()
    {
        return springConstant;
    }
    
    public void setSpringConstant(float springConstant)
    {
        this.springConstant = springConstant;
    }
    
    public float getDampingConstant()
    {
        return dampingConstant;
    }
    
    public void setDampingConstant(float dampingConstant)
    {
        this.dampingConstant = dampingConstant;
    }


    public float getMinLength()
    {
        return minLength;
    }

    public void setMinLength(float minLength)
    {
        this.minLength = minLength;
    }

    public float getMaxLength()
    {
        return maxLength;
    }

    public void setMaxLength(float maxLength)
    {
        this.maxLength = maxLength;
    }
    
    public boolean isBroken()
    {
        return isBroken;
    }

    public ForceReceiverPoint getA()
    {
        return endA;
    }

    public ForceReceiverPoint getB()
    {
        return endB;
    }

    //0 is no tension, -1 is compressed to break, 1 is stretched to break;
    public float getTension()
    {
        if(curLength == restLength) return 0.0f;
        if(curLength < restLength)
        {
            return ((curLength - minLength) / (restLength - minLength)) - 1.0f;
        }
        else
        {
            return (curLength - restLength) / (maxLength - restLength);
        }
    }
}
