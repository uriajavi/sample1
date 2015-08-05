/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package useraccess.managedbeans;

import java.io.Serializable;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import useraccess.ejb.SessionBean;
import useraccess.ejb.SessionBeanLocal;
import useraccess.exception.LoginException;
import useraccess.exception.SessionException;
import useraccess.model.UserBean;

/**
 * This class acts as a backing bean for the login form page for users.
 * @author javi
 */
@Named(value = "loginBean")
@SessionScoped
public class LoginBean implements Serializable {
    //data for login credentials
    private String login;
    private String password;
    //logger for the class
    private static final Logger logger =
            Logger.getLogger("useraccess.managedbeans.SignUpBean");
    //business EJB
    //@EJB
    @Inject 
    SessionBeanLocal sessionBean;
    //user data for the session
    private UserBean user;
    //data for passwd change
    private String newPasswd;
    private String confirmPasswd;
    /**
     * Creates a new instance of LoginBean
     */
    public LoginBean() {
    }
    /* Getters & setters*/
    public String getLogin(){
        return this.login;
    }
    
    public void setLogin(String login){
        this.login=login;
    }

    public String getPassword(){
        return this.password;
    }
    
    public void setPassword(String password){
        this.password=password;
    }
    
    public String getNewPasswd(){
        return this.newPasswd;
    }
    
    public void setNewPasswd(String newPasswd){
        this.newPasswd=newPasswd;
    }
        
    public String getConfirmPasswd(){
        return this.confirmPasswd;
    }
    
    public void setConfirmPasswd(String confirmPasswd){
        this.confirmPasswd=confirmPasswd;
    }
    
    public UserBean getUser(){
        return this.user;
    }
    /**
     * Captures and sends the login data for user validation.
     * @return The following view if there is no error: memberAreaPage
     */
    public String validateLogin(){
        try{
            logger.info("Beginning user login validation.");
            user=this.sessionBean.validateUserCredentials(this);
            logger.info("End user login validation.");    
            return "memberAreaPage";
        }catch(LoginException e){
            logger.severe("LoginException:"+e.getMessage());
            FacesContext.getCurrentInstance()
                .addMessage("loginform", new FacesMessage("Error de acceso: "+e.getMessage()));
            return "";
        }
    }
    /**
     * Resets password generating a new one and sending it to the user by email.
     */
    public void resetPassword(){
        FacesContext context=FacesContext.getCurrentInstance();
         
        if (this.getLogin()!=null){
            try{
                logger.info("Beginning password reset.");
                String email=this.sessionBean.resetPassword(this.getLogin());
                context.addMessage("loginform", 
                                    new FacesMessage("Se ha enviado su nueva contraseña a "+email ));
                logger.info("End password reset.");    

            }catch(LoginException e){
                logger.severe("LoginException:"+e.getMessage());
                context.addMessage("loginform", new FacesMessage("Error de acceso: "+e.getMessage()));
            }
        //if login is wasn't entered shows a message. This is because login is 
        //null despite having immediate attribute
        }else{
            context.addMessage("loginform", 
                    new FacesMessage("El DNI es requerido para reestablecer la contraseña"));
        }
    }
    /**
     * Cancels the user login process.
     * @return the next view: index.
     */
    public String cancel(){
        //this.getLogin().setRequired(false);
        this.login="";
        return "index";
    }
    /**
     * Invalidates current session and shows index page.
     * @return the next view: index
     */
    public String logout(){
        validateSession();
        this.login=null;
        this.password=null;
        this.user=null;
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "index.xhtml?faces-redirect=true";
        
    }
    /**
     * Changes user's password.
     * @return the next view.
     */    
    public String changePasswd(){
        String returnValue="";
        FacesContext context=FacesContext.getCurrentInstance();
        //validates new password is written in both form fields.  
        if (newPasswd.equals(confirmPasswd)){
            try{
                logger.info("Beginning password change, web tier.");
                this.sessionBean.changePassword(newPasswd);
                returnValue="memberAreaPage";
                logger.info("End password change.");    
            }catch(SessionException e){
                logger.severe(e.getMessage());
                context.addMessage("loginform", new FacesMessage("Error de acceso: "+e.getMessage()));
            }
        }else{
            context.addMessage("passwdform", 
                    new FacesMessage("Debe introducir la misma contraseña en ambos campos."));
        }
        return returnValue;
    }
    /**
     * Cancels user's password change.
     * @return the previous view: memberAreaPage.
     */
    public String cancelPasswdChange(){
        
        return "memberAreaPage";
    }
    /**
     * Gets the outcome for the last link of the navigation panel.
     * @return the outcome for the link
     */
    public String getMembersLink(){
        //if user is not logged in.
        if(user==null)
            return "index";
        //if user is logged in.
        else
            return "memberAreaPage";
    }
    /**
     * Gets the text for the last link of the navigation panel.
     * @return the text for the link
     */
    public String getMembersText(){
        //if user is not logged in.
        if(user==null)
            return "acceso miembros";
        //if user is logged in.
        else
            return "área de miembros";
    }
    /**
     * Updates user's data.
     * @return the outcome view after processing.
     */
    public String updateUserData(){
        String outcome="";
        try{
            logger.info("Begining login user's data update, web tier.");
            this.sessionBean.updateLoginUser();
            outcome="memberAreaPage";
            logger.info("End login user's data update.");
        }catch(SessionException e){
            logger.severe(e.getMessage());
            FacesContext.getCurrentInstance()
                    .addMessage("userdataform", new FacesMessage("Error al actualizar: "+e.getMessage()));
            
        }
        return outcome;
    }
    /**
     * Cheks if the session user is an admin.
     * @return true if it is an admin, otherwise false.
     */
    public boolean isUserAdmin(){
        boolean isAdmin=false;
        try{
            logger.info("Checks if the session user is an admin.");
            isAdmin= this.sessionBean.isUserAdmin();
        }catch(SessionException e){
            logger.severe(e.getMessage());
        }
        return isAdmin;
    }
    /**
     * Checks if exists the user's data in the session. If it doesn't redirects to
     * login page.
     */
    public void validateSession(){
        try{
            if(this.user==null)
                FacesContext.getCurrentInstance().getExternalContext().redirect("loginForm.xhtml");
        }catch(Exception e){
            logger.severe("Error validando sesión:"+e.getMessage());
        }
    }
}
