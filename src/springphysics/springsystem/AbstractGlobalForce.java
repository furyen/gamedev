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

import springphysics.general.ForceGenerator;

/**
 *
 * @author tog
 */
public abstract class AbstractGlobalForce implements ForceGenerator
{
    private final Iterable<? extends ForceReceiverPoint> globalList;
    
    public AbstractGlobalForce(Iterable<? extends ForceReceiverPoint> globalList)
    {
        this.globalList = globalList;
    }

    @Override
    public void generateForce(float dt)
    {
        for(ForceReceiverPoint r : globalList)
        {
            applyForce(r, dt);
        }
    }
    
    public abstract void applyForce(ForceReceiverPoint point, float dt);
}
