/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package springphysics.general;

/**
 *
 * @author tog
 */
public interface PhysicSimulator
{
    public void update(float dt);
    public void addPhysicsEntity(PhysicsEntity entity);
    public void removePhysicsEntity(PhysicsEntity entity);
    public void addForceGenerator(ForceGenerator forceGenerator);
    public void removeForceGenerator(ForceGenerator forceGenerator);
}
