/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package springphysics.general.impl;

import java.util.ArrayList;

import springphysics.general.ForceGenerator;
import springphysics.general.PhysicSimulator;
import springphysics.general.PhysicsEntity;


/**
 *
 * @author tog
 */
public class PhysicsSimulatorImpl implements PhysicSimulator
{
    private ArrayList<ForceGenerator> forces;
    private ArrayList<PhysicsEntity> entities;

    public PhysicsSimulatorImpl()
    {
        forces = new ArrayList<>();
        entities = new ArrayList<>();
    }
    
    @Override
    public void update(float dt)
    {
        for(ForceGenerator force : forces)
        {
            force.generateForce(dt);
        }
        for(PhysicsEntity entity : entities)
        {
            entity.update(dt);
        }
    }

    @Override
    public void addPhysicsEntity(PhysicsEntity entity)
    {
        entities.add(entity);
    }

    @Override
    public void removePhysicsEntity(PhysicsEntity entity)
    {
        entities.remove(entity);
    }

    @Override
    public void addForceGenerator(ForceGenerator forceGenerator)
    {
        forces.add(forceGenerator);
    }

    @Override
    public void removeForceGenerator(ForceGenerator forceGenerator)
    {
        forces.remove(forceGenerator);
    }
    
}
