/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package useraccess.util;

import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;

/**
 *
 * @author javi
 */
@RunWith(Arquillian.class)
public class HashEncryptTest {
    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class,"test.war")
            .addClasses(
                        HashEncrypt.class,
                        HashEncryptImplementation.class);
    }
    
    @Inject 
    private HashEncrypt instance;

    /**
     * Test of encrypt method, of class HashEncryptImplementation.
     * @throws java.lang.Exception
     */
    @Test
    public void testEncrypt() throws Exception {
        System.out.println("encrypt");
        byte[] cleartext = {'a'};
        String algorithm = "SHA-256";
        String expResult = "ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb";
        String result = instance.encrypt(cleartext, algorithm);
        assertEquals(expResult, result);
    }
    
}
