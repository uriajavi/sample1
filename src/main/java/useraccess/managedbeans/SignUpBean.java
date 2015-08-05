/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package useraccess.managedbeans;

import java.io.Serializable;
import java.util.Iterator;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import useraccess.ejb.RegistrationBeanLocal;
import useraccess.exception.SignUpException;
import useraccess.model.UserBean;

/**
 * This class acts as a backing bean for the sign up form page for new users.
 * @author javi
 */
@Named(value = "signUp")
@SessionScoped
public class SignUpBean implements Serializable {
    //logger for the class
    private static final Logger logger =
            Logger.getLogger("useraccess.managedbeans.SignUpBean");
    //data for each new user
    private UserBean user;
    //EJB for business logic
    @EJB
    private RegistrationBeanLocal register;
    /**
     * Creates a new instance of SignUpBean
     */
    public SignUpBean() {
        this.user=new UserBean();
    }
    /*
     * setters and getters
     */
    public UserBean getUser(){
        return this.user;
    }
    
    public void setUser(UserBean user){
        this.user=user;
    }
    /**
     * Clears the sign up data and returns to index page.
     * @return String the following view.
     */
    public String clear(){
        logger.info("Clearing user signing up data.");
        //clears user data creating a new UserBean Object
        this.user=new UserBean();
        logger.info("Cleared user signing up data.");
        return "index";
    }
    /**
     * Process new user data and launches its creation.
     * @return The following view if there is no error: signup confirmation.
     */
    public String addNewUser(){
        //try registering new user data
        try{
            logger.info("Registering new user.");
            //registers new users data 
            register.registerNewUser(this.user);
            logger.info("New user registered.");
            return "registrationConfirmation";
        }catch(SignUpException e){
            logger.severe("SignUpException: Error during user registration. "+
                    e.getMessage());
            FacesContext.getCurrentInstance()
                    .addMessage("signupform", new FacesMessage("Error al registrar los datos: "+e.getMessage()));
            //show the actual view with the error message
            return "";
        }
    }
    /**
     * Validates the DNI introduced in the corresponding field. 
     * @param context The FacesContext.
     * @param DNIComp The component that stores the value.
     * @param value   The value itself.
     */
    public void validateDNI(FacesContext context,UIComponent DNIComp,Object value){
        //the table with the letters
        String table="TRWAGMYFPDXBNJZSQVHLCKE";
        try{
            //If the field is not required and it is empty, value is valid
            if(! ((UIInput)DNIComp).isRequired() && ((String)value).isEmpty()){
               ((UIInput)DNIComp).setValid(true);
               return;
            }
            //stores the value in a String without spaces and uppercased
            String dni=(String)value;
            dni=dni.toUpperCase().trim();
            //split number from end character
            char endChar=dni.charAt(dni.length()-1);
            Integer number=new Integer(dni.substring(0, dni.length()-1));
            //obtains remainder of number division by 23
            int remainder=number % 23;
            //obtains character corresponding to remainder indexing the table
            char endChar2=table.charAt(remainder);
            //compares the end chars
            if(endChar==endChar2) 
                ((UIInput)DNIComp).setValid(true);
            else
                throw new Exception();
        }catch(Exception e){
            ((UIInput)DNIComp).setValid(false);
            FacesMessage message=new FacesMessage("Error de validación: DNI no válido.");
            Iterator it=context.getMessages();
            boolean existsMessage=false;
            while(it.hasNext()){
                FacesMessage m=(FacesMessage)it.next();
                if(m.getDetail().equals(message.getDetail())) existsMessage=true;
            }
            if (! existsMessage)
                context.addMessage(DNIComp.getClientId(context), message);
            
        }
    }
}
