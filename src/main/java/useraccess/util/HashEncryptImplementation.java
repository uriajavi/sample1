/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package useraccess.util;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.enterprise.context.Dependent;
import org.apache.commons.codec.binary.Hex;

/**
 * Implementation class for hash encryption.
 * @author javi
 */
@Dependent
public class HashEncryptImplementation implements HashEncrypt,Serializable{
    /**
     * Encrypts a text with hash algorithm.
     * @param cleartext the clear text.
     * @param algorithm the encrypting algorithm.
     * @return the encrypted text.
     * @throws NoSuchAlgorithmException if  algorithm can't be used.
     */
    @Override
    public String encrypt(byte cleartext[],String algorithm) throws NoSuchAlgorithmException{
        MessageDigest md = MessageDigest.getInstance(algorithm);
        //the digest data have to be encoded as Hex data
        return Hex.encodeHexString(md.digest(cleartext));
    }

}
