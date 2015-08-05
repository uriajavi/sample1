/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package useraccess.ejb;

import java.util.Collection;
import javax.ejb.embeddable.EJBContainer;
import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import useraccess.entity.Credential;
import useraccess.entity.Privilege;
import useraccess.entity.Status;
import useraccess.entity.User;
import useraccess.exception.LoginException;
import useraccess.exception.SessionException;
import useraccess.managedbeans.LoginBean;
import useraccess.model.UserBean;

/**
 *
 * @author javi
 */
@RunWith(Arquillian.class)
public class SessionBeanTest {
    
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
    SessionBeanLocal sessionBean;
   
    /**
     * Test of getAllStatuses method, of class UserDetailBean. Test the size 
     * of the collection returned.
     * @throws useraccess.exception.SessionException
     */
    @Test
    public void testAllStatusesSize() throws SessionException {
        System.out.println("getAllStatuses");
        Collection statuses=sessionBean.getAllStatuses();
        Assert.assertEquals("Statuses size is wrong",3,statuses.size());
    }

    /**
     * Test of findCredentialByLogin method, of class SessionBean for a login 
     * that exists.
     * @throws java.lang.Exception
     */
    @Test
    public void testFindCredentialByLoginThatExists() throws Exception {
        System.out.println("Beginning SessionBean.findCredentialByLogin test");
        String login = "23T";
        Credential result = sessionBean.findCredentialByLogin(login);
        Assert.assertEquals(login, result.getLogin());
        System.out.println("Ending SessionBean.findCredentialByLogin test");
    }
    /**
     * Test of findCredentialByLogin for a login that doesn't exist.
     * @throws LoginException 
     */
    @Test(expected=LoginException.class)
    public void testFindCredentialByLoginThatNotExists() throws LoginException{
        System.out.println("SessionBean.findCredentialByLogin test: throwing LoginException");
        String login = "33757362D";
        Credential result = sessionBean.findCredentialByLogin(login);
    }

}
