/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package useraccess.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * This class represents the status of a user in the application.
 * @author Javi
 */
@Entity
@Table(name="status",schema="registerdb")
@NamedQueries({
    @NamedQuery(
            name="findStatusByDescription",
            query="SELECT s FROM Status s WHERE s.description = :description"
    ),
    @NamedQuery(
            name = "findAllStatus",
            query = "SELECT s FROM Status s ORDER BY s.description")
})

public class Status implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Integer id;
    private String description;

    public Status(){}
    
    public Status(String description){
        this.description=description;        
    }

    public Status(Integer id,String description){
        this.id=id;
        this.description=description;        
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription(){
        return this.description;
    }
    
    public void setDescription(String description){
        this.description=description;
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
        if (!(object instanceof Status)) {
            return false;
        }
        Status other = (Status) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "useraccess.entity.Status[ id=" + id + " ]";
        //return id.toString();
    }
    
}
