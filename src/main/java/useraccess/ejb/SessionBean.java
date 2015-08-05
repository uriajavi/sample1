/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package useraccess.ejb;

import java.io.Serializable;
import java.security.MessageDigest;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.mail.Session;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.RandomStringUtils;
import useraccess.entity.Credential;
import useraccess.entity.Privilege;
import useraccess.entity.SocialProfile;
import useraccess.entity.Status;
import useraccess.entity.User;
import useraccess.exception.LoginException;
import useraccess.exception.SessionException;
import useraccess.managedbeans.LoginBean;
import useraccess.model.SocialProfileBean;
import useraccess.model.UserBean;
import useraccess.util.HashEncrypt;
import useraccess.util.MailSender;

/**
 * This bean encapsulates methods for login and user session management.
 * @author javi
 */
//this annotation is to share the same EJB instance along all the web session.
@SessionScoped
@Stateful
public class SessionBean implements SessionBeanLocal,Serializable {
    //Persistence context for using entities.
    @PersistenceContext
    private EntityManager em;
    //Logger
    private static final Logger logger=
            Logger.getLogger("useraccess.ejb.SessionBean");
    //Java Mail session for notifications
    @Resource(name="mail/registrationSession")
    private Session session;
    //Hash encrypter for passwords
    @Inject private HashEncrypt encrypter;
    //User data for the web session
    @Inject private UserBean user;
    //Mail sender
    @Inject private MailSender mailSender;

    public SessionBean(){
        
    }
    /**
     * Finds a user's credentials using the login (DNI) to locate them.
     * @param login the user's login
     * @return A Credential object 
     * @throws LoginException If login doesn't exist.
     */
    @Override
    public Credential findCredentialByLogin(String login) throws LoginException{
        try{
            Credential cred=(Credential)em.createNamedQuery("findCredentialByLogin")
                    .setParameter("login",login).getSingleResult();
            return cred;
        }catch(NoResultException e){
            //If there is no result, login is incorrect
            throw new LoginException("El DNI no está registrado como usuario.");
        }
    }
    /**
     * Validates user credentials for login into the application
     * @param credentials the login credentials
     * @return the user data for the session if login is sucessful.
     * @throws LoginException if there is any error in the credentials or user status
     * is different from normal
     */
    @Override
    public UserBean validateUserCredentials(LoginBean credentials) throws LoginException{
        try{
            logger.info("Beginning of user login validation, business tier.");
            //Validates login existence
            Credential cred=this.findCredentialByLogin(credentials.getLogin());
            //Validates password
            //String passwdUTF=new String (cred.getPassword().getBytes(), Charset.forName("UTF-8"));
            if(! MessageDigest.isEqual(this.encrypter.encrypt(credentials.getPassword().getBytes(), "SHA-256").getBytes(),
                                       cred.getPassword().getBytes()))
                throw new LoginException("Contraseña incorrecta.");    
            //Checks the user status
            User user=(User)em.find(User.class,cred.getId());
            Status status=(Status)em.createNamedQuery("findStatusByDescription")
                    .setParameter("description", "normal").getSingleResult();
            if(! user.getStatus().equals(status))
                throw new LoginException("El estado del usuario es incorrecto:"+user.getStatus().getDescription());    
            //Updates user last access date
            cred.setLastAccess(new Date());
            //sets user data for the session and returns
            this.user.setId(user.getId());
            this.user.setName(user.getName());
            this.user.setSurname(user.getSurname());
            this.user.setDni(cred.getLogin());
            this.user.setEmail(user.getEmail());
            this.user.setPosition(user.getPosition());
            this.user.setTitle(user.getTitle());
            this.user.setLastAccess(cred.getLastAccess());
            Iterator it=user.getSocialProfiles().iterator();
            Vector<SocialProfileBean> userProfiles=new Vector<SocialProfileBean>();
            while (it.hasNext()){
                SocialProfile profile=(SocialProfile)it.next();
                SocialProfileBean newProfile= new SocialProfileBean(profile.getNetwork(),
                                                                    profile.getAccount());
                userProfiles.add(newProfile);
            }
            this.user.setSocialProfiles(userProfiles);
            logger.info("End of user login validation, business tier.");
            return this.user;
        }catch(Exception e){
            throw new LoginException(e.getMessage());
        }
        
    }
    /**
     * Resets the password for a user identified by his login.
     * @param login
     * @return A message containing the email of the user which password has been
     *         reset.
     * @throws LoginException If 
     */
    @Override
    public String resetPassword(String login) throws LoginException{
        try{
            logger.info("Beginning of user password reset, business tier.");
            //Validates login existence
            Credential cred=this.findCredentialByLogin(login);
            //Generates a new password
            byte passwd[] =RandomStringUtils.randomAlphanumeric(8).getBytes();
            //Stores new password 
            cred.setPassword(this.encrypter.encrypt(passwd, "SHA-256"));
            cred.setLastPasswdChange(new Date());
            //em.persist(cred);
            //Notify by email the new password
            String email=em.find(User.class, cred.getId()).getEmail();
            String subject="Cambio de contraseña en ECUSA";
            String messageBody = "Se ha modificado su contraseña en ECUSA.\n"+
                    "Su nueva contraseña es: "+new String(passwd)+"\n"+
                    "\n Saludos.";
            this.mailSender.sendMail(email, subject, messageBody);
            
            return email;
        }catch(Exception e){
            throw new LoginException(e.getMessage());
        }
        
    }
    /**
     * Changes user password.
     * @param passwd the new password
     * @throws SessionException if there is any error during processing.
     */
    @Override
    public void changePassword(String passwd) throws SessionException{
        try{
            logger.info("Beginning of user password change, business tier.");
            //gets user credentials
            Credential cred=this.findCredentialByLogin(user.getDni());
            //Stores new password 
            cred.setPassword(this.encrypter.encrypt(passwd.getBytes(), "SHA-256"));
            cred.setLastPasswdChange(new Date());
            //em.persist(cred);
        }catch(Exception e){
            throw new SessionException(e.getMessage());
        }
    }
    /**
     * Updates session user's data.
     * @throws SessionException if there was any error.
     */
    @Override
    public void updateLoginUser() throws SessionException{
        try{
            logger.info("Beginning user's data update, business tier.");
            //finds user by id
            User userToUpdate=em.find(User.class, user.getId());
            //updates its data
            userToUpdate.setEmail(user.getEmail());
            userToUpdate.setPosition(user.getPosition());
            userToUpdate.setTitle(user.getTitle());
            //updates social profiles, deleting previous profiles and 
            //adding the new ones.
            Iterator it=userToUpdate.getSocialProfiles().iterator();
            while(it.hasNext()){
                SocialProfile sp=(SocialProfile)it.next();
                em.remove(sp);
            }
            userToUpdate.getSocialProfiles().clear();
            it=user.getSocialProfiles().iterator();
            while(it.hasNext()){
                SocialProfileBean sp=(SocialProfileBean)it.next();
                SocialProfile newProfile=new SocialProfile(sp.getNetwork(),
                                                            sp.getAccount(),
                                                            userToUpdate);
                userToUpdate.getSocialProfiles().add(newProfile);
            }
            //em.persist(userToUpdate);
        }catch(Exception e){
            throw new SessionException(e.getMessage());
        }
    }
    /**
     * Checks if the session user is an admin.
     * @return true if it is an admin.
     * @throws SessionException if there was any error.
     */
    @Override
    public boolean isUserAdmin() throws SessionException{
        boolean isAdmin=false;
        try{
            //finds user by id
            if(user.getId()!=null){
                User userAux=em.find(User.class, this.user.getId());        
                if (userAux.getPrivilege().getDescription().equals("admin"))
                    isAdmin=true;
            }
        }catch(Exception e){
            throw new SessionException(e.getMessage());
        }
        return isAdmin;
    }
    /**
     * Obtains all data from users
     * @return a Collection of User objects with users data
     * @throws SessionException if there was any error
     */
    @Override
    public Collection<User> getAllUsers() throws SessionException{
        try{
            logger.info("Getting all users data from BD.");
            List data=em.createNamedQuery("findAllUsersData").getResultList();
            return data;
        }catch(Exception e){
            throw new SessionException(e.getMessage());
        }
        
    }
    /**
     * Obtains the users that have a concrete status.
     * @param description the description of the status
     * @return a Collection of User objects
     * @throws SessionException if there was any error
     */
    @Override
    public Collection<User> getUsersByStatus(String description) throws SessionException{
        try{
            logger.info("Getting users by status from BD.");
            return em.createNamedQuery("findUsersByStatus")
                    .setParameter("description", description).getResultList();
        }catch(Exception e){
            throw new SessionException(e.getMessage());
        }
    }
    /**
     * Obtains the user data finding it by login (DNI).
     * @param DNI the login value
     * @return A User object with the data.
     * @throws SessionException if there was any error.
     */
    @Override
    public User getUserByDNI(String DNI) throws SessionException{
        try{
            logger.info("Getting user by DNI from BD.");
            return (User)em.createNamedQuery("findUserByDNI")
                    .setParameter("login", DNI).getSingleResult();
        }catch(Exception e){
            throw new SessionException(e.getMessage());
        }
    }
    /**
     * Updates user's status.
     * @param user the user to udpate
     * @param description the description of the status
     * @return the User object updated.
     * @throws SessionException if there was any error
     */
    @Override
    public User updateUserStatus(User user,String description) throws SessionException{
        try{
            logger.info("Beginning user acceptance, business tier.");
            //finds status
            Status normal=(Status)em.createNamedQuery("findStatusByDescription")
                    .setParameter("description", description).getSingleResult();
            //sets user status 
            user.setStatus(normal);
            //merges the user received as argument to the entity manager because
            //it can be detached from it.
            if(! em.contains(user))user=em.merge(user);
            return user;
        }catch(Exception e){
            throw new SessionException(e.getMessage());
        }
    }
    /**
     * Deletes the user from the underlying database.
     * @param user the user to be deleted
     * @throws SessionException if there is any error
     */
    @Override
    public void deleteUser(User user) throws SessionException{
        try{
            logger.info("Beginnig user deletion, business tier.");
            //merges the user and deletes it
            user=em.merge(user);
            em.remove(user);
        }catch(Exception e){
            throw new SessionException(e.getMessage());
        }
    }
    /**
     * Updates the user.
     * @param user the user to be updted
     * @throws SessionException if there is any error
     */
    @Override
    public void updateUser(User user) throws SessionException{
        try{
            logger.info("Beginnig user update, business tier.");
            //merges the user and updates it
            if(!em.contains(user)) user=em.merge(user);
        }catch(Exception e){
            throw new SessionException(e.getMessage());
        }
        
    }
    /**
     * Gets all the statuses from the DB.
     * @return A Collecion of Status objects with the data.
     * @throws SessionException If there is any error.
     */
    @Override
    public Collection<Status> getAllStatuses() throws SessionException{
        try{
            logger.info("Getting all status from BD.");
            List data=em.createNamedQuery("findAllStatus").getResultList();
            return data;
        }catch(Exception e){
            throw new SessionException(e.getMessage());
        }
        
    }
    /**
     * Gets all privileges from DB.
     * @return A Collection of Privilege objects with the data.
     * @throws SessionException If there is any error.
     */
    @Override
    public Collection<Privilege> getAllPrivileges() throws SessionException{
        try{
            logger.info("Getting all privileges from BD.");
            List data=em.createNamedQuery("findAllPrivileges").getResultList();
            return data;
        }catch(Exception e){
            throw new SessionException(e.getMessage());
        }
        
    }
    

}
