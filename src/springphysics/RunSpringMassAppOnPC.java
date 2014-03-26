/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package springphysics;

import app2dapi.App2D;
import app2dapi.Platform;
import app2dpcimpl.PCPlatformImpl;
import springphysics.testapp.SpringMassApp;



/**
 *
 * @author tog
 */
public class RunSpringMassAppOnPC
{
    public static void main(String[] args)
    {
        Platform p = new PCPlatformImpl();
        App2D app = new SpringMassApp();
        p.runApplication(app);
    }
}
