/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package useraccess.ejb;

import java.util.Collection;
import javax.ejb.Local;
import useraccess.entity.Credential;
import useraccess.entity.Privilege;
import useraccess.entity.Status;
import useraccess.entity.User;
import useraccess.exception.LoginException;
import useraccess.exception.SessionException;
import useraccess.managedbeans.LoginBean;
import useraccess.model.UserBean;

/**
 * Interface for EJB SessionBean business methods.
 * @author javi
 */
@Local
public interface SessionBeanLocal {
    public UserBean validateUserCredentials(LoginBean credentials) throws LoginException;
    public String resetPassword(String login) throws LoginException;
    public void changePassword(String passwd) throws SessionException;
    public void updateLoginUser() throws SessionException;
    public boolean isUserAdmin() throws SessionException;
    public Collection<User> getAllUsers() throws SessionException;
    public Collection<User> getUsersByStatus(String description) throws SessionException;
    public User getUserByDNI(String DNI) throws SessionException;
    public Credential findCredentialByLogin(String login) throws LoginException;
    public User updateUserStatus(User user,String description) throws SessionException;
    public void deleteUser(User user) throws SessionException;
    public void updateUser(User user) throws SessionException;
    public Collection<Status> getAllStatuses() throws SessionException;
    public Collection<Privilege> getAllPrivileges() throws SessionException;
}
