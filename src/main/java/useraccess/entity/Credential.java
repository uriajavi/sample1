/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package useraccess.entity;

import java.io.Serializable;
import java.util.Date;
import static javax.persistence.CascadeType.ALL;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import static javax.persistence.TemporalType.TIMESTAMP;



/**
 * This class represents the user's credential for login into 
 * the application.
 * @author javi
 */
@Entity
@Table(name="credential",schema="registerdb")
@NamedQuery(
        name = "findCredentialByLogin",
        query = "SELECT c FROM Credential c WHERE c.login = :login")
public class Credential implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer id;
    private String login;
    private String password;
    private CredentialType type;
    private java.util.Date lastAccess;
    private java.util.Date lastPasswdChange;
    //private User user;
    
    public Credential(){}
    
    public Credential(
            Integer id,
            String login, 
            String password,
            CredentialType type){
        this.id=id;
        this.login=login;        
        this.password=password;
        this.type=type;
        lastAccess=new java.util.Date();
        lastPasswdChange=new java.util.Date();
      //  this.user=user;
    }

    public Credential(
            String login, 
            String password,
            CredentialType type){
        this.login=login;        
        this.password=password;
        this.type=type;
        lastAccess=new java.util.Date();
        lastPasswdChange=new java.util.Date();
        //this.user=user;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
/*
    @OneToOne
    //@MapsId
    //@OneToOne(cascade=ALL)
    //@JoinColumn(name="user",insertable=false,updatable=false)  
    @PrimaryKeyJoinColumn(name="id",referencedColumnName="id")
    //@JoinColumn(name="id")
    //@MapsId
    public User getUser(){
        return user;
    }
    
    public void setUser(User user){
        this.user=user;
    }
/**/
    public String getLogin(){
        return login;
    }

    public void setLogin (String login){
        this.login=login;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword (String password){
        this.password=password;
    }
    
    @ManyToOne
    @JoinColumn(name="type")
    public CredentialType getType(){
        return type;
    }
    
    public void setType(CredentialType type){
        this.type=type;
    }
    
    @Temporal(TIMESTAMP)
    @Column(name="last_access")
    public java.util.Date getLastAccess(){
        return lastAccess;
    }
    
    public void setLastAccess(java.util.Date lastAccess){
        this.lastAccess=lastAccess;
    }
    
    @Temporal(TIMESTAMP)
    @Column(name="last_passwd_change")
    public java.util.Date getLastPasswdChange(){
        return lastPasswdChange;
    }
    
    public void setLastPasswdChange(java.util.Date lastPasswdChange){
        this.lastPasswdChange=lastPasswdChange;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Credential)) {
            return false;
        }
        Credential other = (Credential) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "useraccess.entity.Credential[ id=" + id + " ]";
    }
    
}
