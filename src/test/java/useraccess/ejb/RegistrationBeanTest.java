/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package useraccess.ejb;

import java.util.Date;
import java.util.Vector;
import javax.ejb.embeddable.EJBContainer;
import javax.inject.Inject;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import useraccess.entity.Credential;
import useraccess.entity.CredentialType;
import useraccess.entity.User;
import useraccess.exception.SessionException;
import useraccess.exception.SignUpException;
import useraccess.model.SocialProfileBean;
import useraccess.model.UserBean;
import useraccess.util.HashEncrypt;
import useraccess.util.HashEncryptImplementation;
import useraccess.util.MailSender;

/**
 *
 * @author javi
 */
@RunWith(Arquillian.class)
public class RegistrationBeanTest {
    
    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class,"test.war")
            .addPackage("useraccess.ejb")
            .addPackage("useraccess.entity")
            .addPackage("useraccess.model")
            .addPackage("useraccess.exception")
            .addPackage("useraccess.util")
            .addPackage("useraccess.managedbeans")
            .addPackage("useraccess.converter")
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsResource("META-INF/persistence.xml");
    }
    
    @Inject 
    RegistrationBeanLocal regBean;
    
    @Inject 
    SessionBeanLocal sessionBean;
    
    
    //@Inject
    //UserBean user;
   
    /**
     * Test a new user registration
     * @throws useraccess.exception.SignUpException
     */
    @Test
    @InSequence(0)
    public void testRegisterNewUser() throws SignUpException {
        System.out.println("Beginning testRegisterNewUser");
        
        regBean.registerNewUser(new UserBean("Pepe","Lopez Lopez","20848630G","uriajavi@gmail.com",
                                              "a","a",new Date(),new Vector()));
        Assert.assertTrue(true);
        System.out.println("Ending testRegisterNewUser");
    }

    /**
     * Test a new user registration for a existing user.
     * @throws useraccess.exception.SignUpException
     */
    @Test(expected=SignUpException.class)
    @InSequence(1)
    public void testRegisterNewUserThatExists() throws SignUpException {
        System.out.println("Beginning testRegisterNewUserThatExists");
        
        regBean.registerNewUser(new UserBean("Pepe","Lopez Lopez","20848630G","uriajavi@gmail.com",
                                              "a","a",new Date(),new Vector()));
        Assert.assertTrue(true);
        System.out.println("Ending testRegisterNewUserThatExists");
    }

    /**
     * Erase created user after alltests.
     * @throws SessionException 
     * @throws javax.naming.NamingException 
     */
    @Test
    @InSequence(99)
    public void eraseCreatedUser() throws SessionException, NamingException{
        System.out.println("Beginning eraseCreatedUser");
        sessionBean.deleteUser(sessionBean.getUserByDNI("20848630G"));
        System.out.println("Ending eraseCreatedUser");
    }
    
}
