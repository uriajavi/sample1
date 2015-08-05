/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package useraccess.exception;

/**
 * This class represents an error during new user registration.
 * @author javi
 */
public class SignUpException extends Exception {

    /**
     * Creates a new instance of <code>SignUpException</code> without detail
     * message.
     */
    public SignUpException() {
    }

    /**
     * Constructs an instance of <code>SignUpException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public SignUpException(String msg) {
        super(msg);
    }
}
