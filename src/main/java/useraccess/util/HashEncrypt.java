/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package useraccess.util;

import java.security.NoSuchAlgorithmException;

/**
 * This interface define a method for encrypting.
 * @author javi
 */
public interface HashEncrypt {
    public String encrypt(byte cleartext[],String algorithm) throws NoSuchAlgorithmException;
}
