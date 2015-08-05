/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package useraccess.managedbeans;

import java.io.Serializable;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import useraccess.ejb.SessionBeanLocal;
import useraccess.entity.Privilege;
import useraccess.entity.Status;
import useraccess.entity.User;
import useraccess.exception.SessionException;

/**
 * This bean provides help for UserDetailForm.
 * @author javi
 */
@Named(value = "userDetailBean")
@SessionScoped
public class UserDetailBean implements Serializable {
    //logger for the class
    private static final Logger logger =
            Logger.getLogger("useraccess.managedbeans.UserDetailBean");
    //user data
    private User user;
    //SessionBeanLocal sessionBean;
    @Inject 
    SessionBeanLocal sessionBean;
    //all statuses
    private Collection<Status> allStatuses;
    //all privileges
    private Collection<Privilege> allPrivileges;
    /**
     * Creates a new instance of UserDetailBean
     */
    public UserDetailBean() {
    }
    /**
     * Initializes bean data.
     */
    @PostConstruct
    public void init(){
        try{
            this.allStatuses=sessionBean.getAllStatuses();
            this.allPrivileges=sessionBean.getAllPrivileges();
        }catch(SessionException e){
            logger.severe(e.getMessage());
        }
    }
    
    public User getUser(){
        return this.user;
    }
    
    public void setUser(User user){
        this.user=user;
    }
    
    public Collection<Status> getAllStatuses(){
        return this.allStatuses;
    }
    
    public Collection<Privilege> getAllPrivileges(){
        return this.allPrivileges;
    }
    
    public String updateUserData(){
        String retorno="adminAreaPage";
        try{
            //deletes the user
            sessionBean.updateUser(user);
        }catch(SessionException e){
            logger.log(Level.SEVERE, "Error updating user:{0}", e.getMessage());
            FacesContext.getCurrentInstance()
                .addMessage("userstableform", new FacesMessage(e.getMessage()));
            retorno="";
        }

        return retorno;
    }
}
