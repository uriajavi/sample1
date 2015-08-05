/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package useraccess.util;

import junit.framework.Assert;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test class for TDD example
 * @author javi
 */
@RunWith(Arquillian.class)
public class CalculatorTest {
    
    @Deployment
    public static Archive<?> createDeployment(){
        return ShrinkWrap.create(JavaArchive.class, "test.jar")
                .addClass(Calculator.class);
        
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void testCalculatorAdd() {
        System.out.println("Beginning Calculator.add test");
        Calculator c=new Calculator();
        int r=c.add(7,9);
        Assert.assertEquals(16, r);
        System.out.println("Ending Calculator.add test");
    }
}
