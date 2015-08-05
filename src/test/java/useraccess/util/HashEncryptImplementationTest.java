/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package useraccess.util;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author javi
 */
public class HashEncryptImplementationTest {
    
    public HashEncryptImplementationTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of encrypt method, of class HashEncryptImplementation.
     */
    @Test
    public void testEncrypt() throws Exception {
        System.out.println("encrypt");
        byte[] cleartext = {'a'};
        String algorithm = "SHA-256";
        HashEncryptImplementation instance = new HashEncryptImplementation();
        String expResult = "ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb";
        String result = instance.encrypt(cleartext, algorithm);
        assertEquals(expResult, result);
    }
    
}
