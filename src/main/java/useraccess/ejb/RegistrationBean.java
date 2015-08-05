/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package useraccess.ejb;

import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.Session;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.RandomStringUtils;
import useraccess.entity.Credential;
import useraccess.entity.CredentialType;
import useraccess.entity.Privilege;
import useraccess.entity.SocialProfile;
import useraccess.entity.Status;
import useraccess.entity.User;
import useraccess.exception.SignUpException;
import useraccess.model.SocialProfileBean;
import useraccess.model.UserBean;
import useraccess.util.HashEncrypt;
import useraccess.util.MailSender;

/**
 * This class encapsulates all the bussines methods for the user signUp proccess.
 * @author javi
 */
@Stateless
public class RegistrationBean implements RegistrationBeanLocal{
    //Persistence context for using entities.
    @PersistenceContext
    private EntityManager em;
    //Logger
    private static final Logger logger=
            Logger.getLogger("useraccess.ejb.RegistrationBean");
    //Java Mail session for notifications
    @Resource(name="mail/registrationSession")
    private Session session;
    //Hash encrypter for passwords
    @Inject private HashEncrypt encrypter;
    //Mail sender
    @Inject private MailSender mailSender;
    public RegistrationBean() throws Exception{
    
    }
    /**
     * Bussines method to register a new user
     * @param user a SignUpBean object containig user data
     * @throws SignUpException a Exception indicating error during registration 
     * processing.
     */
    @Override
    public void registerNewUser(UserBean user) throws SignUpException{
        try{
            logger.info("Beginning of user registration, business tier.");
            //validates user login (DNI)
            if(! this.isLoginValid(user.getDni()))
                throw new SignUpException("El DNI que ha introducido ya está registrado como miembro.");
            //validates user email
            //if(! this.isEmailValid(user.getEmail()))
            //    throw new SignUpException("El Email que ha introducido ya está registrado, introduzca otro.");
            //Constructs a user object with the new data
            User newuser=new User();
            newuser.setName(user.getName());
            newuser.setSurname(user.getSurname());
            newuser.setEmail(user.getEmail());
            newuser.setPosition(user.getPosition());
            newuser.setTitle(user.getTitle());
            //sets users network profiles: we convert the list of SocialProfilesBeans
            //to a list of SocialProfiles (entities) associated to the newuser object:
            newuser.setSocialProfiles(new Vector<SocialProfile>());
            Iterator it=user.getSocialProfiles().iterator();
            while (it.hasNext()){
                SocialProfileBean profile=(SocialProfileBean)it.next();
                SocialProfile newProfile= new SocialProfile(profile.getNetwork(),
                                                            profile.getAccount(),
                                                            newuser);
                newuser.getSocialProfiles().add(newProfile);
            }
            //setss user privile(2=user) and status(0=pending)
            newuser.setPrivilege(em.find(Privilege.class, 2));
            newuser.setStatus(em.find(Status.class, 0));
            //creates user credentials using email as login and a temporal generated
            //password encrypted with a hash algorithm SHA-256.
            byte passwd[] =RandomStringUtils.randomAlphanumeric(8).getBytes();
            Credential cred=new Credential(user.getDni().toUpperCase(),
                                            encrypter.encrypt(passwd,"SHA-256"),
                                            em.find(CredentialType.class, 1));
            newuser.setCredential(cred);
            //persists newuser.
            em.persist(newuser);
            //Notify the registration to the new user and the validator users
            notifyNewRegistration(newuser.getEmail(), cred.getLogin(), new String(passwd));
            notifyNewValidationNeeded();
            logger.info("End of user registration, business tier.");
        }catch(Exception e){
            logger.severe("SignUpException registering new user, business tier.");
            throw new SignUpException("Error en el servidor intentando registrar los datos. "
                    +e.getMessage());
        }
    }
    /**
     * Checks if a login is valid for a new user.
     * @param login the login to be tested.
     * @return true if login is valid, false if it is not.
     */
    public boolean isLoginValid(String login){
        Credential credential=null;
        try{    
            credential=(Credential)em.createNamedQuery("findCredentialByLogin")
                    .setParameter("login", login).getSingleResult();
        }catch(NoResultException e){
              //if there is not a result, true is returned  
              return true;  
        }
        //if there is a result, we find user status and see if it is pending
        User user=(User)em.find(User.class,credential.getId());
        if(user.getStatus().equals("0"))
            //if status is pending the login is valid
            return true;
        else
            //if status is not pending login is invalid
            return false;
    }
    /**
     * Checks if a email is valid for a new user, that is, if it already exists
     * in the database.
     * @param email the email to be tested.
     * @return true if email is valid, false if it is not.
     */
    public boolean isEmailValid(String email){
        User user=null;
        try{    
            user=(User)em.createNamedQuery("findUserByEmail")
                    .setParameter("email", email).getSingleResult();
        }catch(NoResultException e){
              //if there is not a result, email is valid
              return true;  
        }
        //if there is a result, email is invalid
        return false;
    }
    /**
     * Notifies by email to the user the new registration.
     * @param email user's email
     * @param login future login for user
     * @param passwd future passwd for user
     */
    public void notifyNewRegistration(String email,String login, String passwd) {
            String subject="Registro en ECUSA";
            DateFormat dateFormatter = DateFormat
                    .getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT);
            Date timeStamp = new Date();
            String messageBody = "Se ha recibido su registro en ECUSA el "
                    + dateFormatter.format(timeStamp) + ".\n" +
                    "Su petición será validada y se le notificará el resultado.\n"+
                    "Anote las siguientes credenciales para su futuro acceso a nuestra web:\n"+
                    "Usuario (su DNI): "+login+"\n"+
                    "Contraseña: "+passwd+"\n"+
                    "\n Saludos.";
            this.mailSender.sendMail(email,subject,messageBody);
    }
    /**
     * Notifies by email to the validator users the new registration.
     * 
     */
    public void notifyNewValidationNeeded() {
            //Gets the email addresses for admin users
            String emailList=getAdminsEmails();
            if(emailList!=null){
                String subject="Nuevos registros en ECUSA";
                DateFormat dateFormatter = DateFormat
                        .getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT);
                Date timeStamp = new Date();
                String messageBody = "Se han producido nuevos registros en ECUSA el "
                        + dateFormatter.format(timeStamp) + ".\n" +
                        "Dichos registros están disponibles para su validación.\n"+
                        "\n Saludos.";
                this.mailSender.sendMail(emailList, subject, messageBody);
            }
    }
    /**
     * This method obtains the emails of the users that have the admin privilege.
     * @return A space separated string with the admins emails.
     */
    private String getAdminsEmails(){
        StringBuffer emailList=null;
        try{
            //gets the privilege  for admins
            Privilege adminPrivilege= (Privilege)em.createNamedQuery("findPrivilegeByDescription")
                    .setParameter("description", "admin").getSingleResult();
            //gets the users with admin privilege
            List adminusers=em.createNamedQuery("findUserByPrivilege")
                    .setParameter("privilege",adminPrivilege).getResultList();
            //creates and fills an array of Address with users emails
            emailList=new StringBuffer();
            Iterator it=adminusers.iterator();
            for(int i=0;it.hasNext();i++){
                String email=((User)it.next()).getEmail();
                emailList.append(email+" ");
            }
        }catch(Exception e){
            logger.severe("Exception getting admins emails"+e.getMessage());
        }
        return emailList.toString();
    }

}
