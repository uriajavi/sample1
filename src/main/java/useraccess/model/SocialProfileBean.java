/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package useraccess.model;

import java.io.Serializable;
import javax.enterprise.context.Dependent;
import javax.inject.Named;

/**
 * This class encapsulates the social profile data for a user in the web tier.
 * It is used as data transfer object between the managed beans and the EJB.
 * @author javi
 */
@Named(value = "socialProfile")
@Dependent
public class SocialProfileBean implements Serializable {
    //The social network of the profile
    private String network;
    //The account identificator for the social profile
    private String account;
    /**
     * Creates a new instance of SocialProfileBean
     */
    public SocialProfileBean() {
    }
    /**
     * Creates a new instance of SocialProfileBean
     * @param network The social network of the profile
     * @param account The account identificator for the social profile
     */
    public SocialProfileBean(String network, String account) {
        this.network=network;
        this.account=account;
    }
    
    public String getNetwork(){
        return this.network;
    }
    
    public void setNetwork(String network){
        this.network=network;
    }
    
    public String getAccount(){
        return this.account;
    }
    
    public void setAccount(String account){
        this.account=account;
    }
        
}
