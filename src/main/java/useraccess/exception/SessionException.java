/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package useraccess.exception;

/**
 * Represents an error during user's session.
 * @author javi
 */
public class SessionException extends Exception {

    /**
     * Creates a new instance of <code>SessionException</code> without detail
     * message.
     */
    public SessionException() {
    }

    /**
     * Constructs an instance of <code>SessionException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public SessionException(String msg) {
        super(msg);
    }
}
