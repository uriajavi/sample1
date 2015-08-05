/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package useraccess.entity;

import java.io.Serializable;
import java.util.Collection;
import static javax.persistence.CascadeType.ALL;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;


/**
 * This class represents the application's users and stores their data.
 * @author javi
 */
@Entity
@Table(name="user",schema="registerdb")
@NamedQueries({
    @NamedQuery(
            name = "findAllUsersData",
            query = "SELECT u FROM User u JOIN u.credential c"),
    @NamedQuery(
            name = "findUserByDNI",
            query = "SELECT u FROM User u JOIN u.credential c WHERE c.login= :login"),
    @NamedQuery(
            name = "findUserByEmail",
            query = "SELECT u FROM User u WHERE u.email = :email"),
    @NamedQuery(
            name = "findUserByPrivilege",
            query = "SELECT u FROM User u WHERE u.privilege = :privilege"),
    @NamedQuery(
            name = "findUsersByStatus",
            query = "SELECT u FROM User u JOIN u.credential c JOIN u.status s WHERE s.description = :description")
})
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String name;
    private String surname;
    private String email;
    private String position;
    private String title;
    private Status status;
    private Privilege privilege;
    private Credential credential;
    private Collection<SocialProfile> socialProfiles;

    public User(){}
    
    public User(String name,
               String surname,
               String email,
               String position,
               String title,
               Status status,
               Privilege privilege,
               Credential credential,
               Collection<SocialProfile> socialProfiles){
        this.name=name;
        this.surname=surname;
        this.email=email;
        this.position=position;
        this.title=title;
        this.status=status;
        this.privilege=privilege;
        this.credential=credential;
        this.socialProfiles=socialProfiles;
       
    }
    
    public User(Integer id,
               String name,
               String surname,
               String email,
               String position,
               String title,
               Status status,
               Privilege privilege,
               Credential credential,
               Collection<SocialProfile> socialProfiles){
        this.id=id;
        this.name=name;
        this.surname=surname;
        this.email=email;
        this.position=position;
        this.title=title;
        this.status=status;
        this.privilege=privilege;
        this.credential=credential;
        this.socialProfiles=socialProfiles;
       
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
    
    @ManyToOne
    @JoinColumn(name="status")
    public Status getStatus(){
        return status;
    }
    
    public void setStatus(Status status){
        this.status=status;
    }
        
    @ManyToOne
    @JoinColumn(name="privilege")
    public Privilege getPrivilege(){
        return privilege;
    }
    
    public void setPrivilege(Privilege privilege){
        this.privilege=privilege;
    }
    
    @OneToOne(cascade=ALL,fetch=FetchType.LAZY)
    @MapsId
    @JoinColumn(name="id")
    public Credential getCredential(){
        return credential;
    }
    
    public void setCredential(Credential credential){
        this.credential=credential;
    }
    
    @OneToMany(cascade=ALL,mappedBy="user")
    public Collection<SocialProfile> getSocialProfiles(){
        return this.socialProfiles;
    }
    
    public void setSocialProfiles(Collection<SocialProfile> socialProfiles){
        this.socialProfiles=socialProfiles;
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
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "useraccess.entity.User[ id=" + id + " ]";
    }
    
}
