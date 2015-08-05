/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package useraccess.ejb;

import javax.ejb.Local;
import useraccess.exception.SignUpException;
import useraccess.model.UserBean;

/**
 * Local interface for RegistrationBean EJB.
 * @author javi
 */
@Local
public interface RegistrationBeanLocal {
    public void registerNewUser(UserBean user) throws SignUpException;
}
