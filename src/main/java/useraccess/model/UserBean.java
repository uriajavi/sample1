/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package useraccess.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;
import javax.enterprise.context.Dependent;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * This class encapsulates the user data for representing it in data tier and for 
 * data transfering between web and business tier.
 * @author javi
 */
@Dependent
@Named(value = "user")
public class UserBean implements Serializable {
    private Integer id;
    private String name;
    private String surname;
    private String dni;
    private String email;
    private String position;
    private String title;
    private Date   lastAccess;
    private Collection<SocialProfileBean> socialProfiles;

    public UserBean(){
        this.socialProfiles=new Vector();
    }
    
    public UserBean(String name,
                    String surname,
                    String dni,
                    String email,
                    String position,
                    String title,
                    Date   lastAccess,
                    Collection<SocialProfileBean> socialProfiles){
        this.name=name;
        this.surname=surname;
        this.dni=dni;
        this.email=email;
        this.position=position;
        this.title=title;
        this.lastAccess=lastAccess;
        this.socialProfiles=socialProfiles;
    }
    
    public Integer getId(){
        return id;
    }

    public void setId(Integer id){
        this.id=id;
    }
    
    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name=name;
    }
    
    public String getSurname(){
        return surname;
    }

    public void setSurname(String surname){
        this.surname=surname;
    }
    
    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email=email;
    }
    
    public String getPosition(){
        return position;
    }

    public void setPosition(String position){
        this.position=position;
    }
    
    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title=title;
    }
    public Collection<SocialProfileBean> getSocialProfiles(){
        return this.socialProfiles;
    }
    
    public void setSocialProfiles(Collection<SocialProfileBean> socialProfiles){
        this.socialProfiles=socialProfiles;
    }

    public String getDni(){
        return this.dni;
    }

    public void setDni(String dni){
        this.dni=dni;
    }
    
    public Date getLastAccess(){
        return this.lastAccess;
    }
    
    public void setLastAccess(Date lastAccess){
        this.lastAccess=lastAccess;
    }
    /**
     * Adds a social profile for the user.
     */
    public void addSocialProfile(){
        SocialProfileBean socialProfile=new SocialProfileBean();
        this.socialProfiles.add(socialProfile);
    }
    /**
     * Deletes the actual SocialProfile.
     */
    public void deleteSocialProfile(){
        //recovers actual profile from the associated request attribute and
        //removes it from the socialprofiles list.
        SocialProfileBean socialProfile=(SocialProfileBean)FacesContext.getCurrentInstance()
                .getExternalContext().getRequestMap().get("profile");
        this.socialProfiles.remove(socialProfile);
    }
    /**
     * Returns the number of social profiles added for a  user.
     * @return int the number of social profiles.
     */
    public int getSocialProfilesSize(){
        return this.socialProfiles.size();
    }

}
