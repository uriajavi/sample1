/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package useraccess.converter;

import java.util.Collection;
import java.util.Iterator;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import useraccess.ejb.SessionBeanLocal;
import useraccess.entity.Privilege;
import useraccess.entity.Status;
import useraccess.exception.SessionException;

/**
 * This class acts as converter for Privilege entities.
 * @author javi
 */
@FacesConverter("privilegeConverter")
public class PrivilegeConverter implements Converter{

    
    @Inject 
    private SessionBeanLocal sessionBean;

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) 
            throws ConverterException{
        Object returnObject=null;
        try{
            //gets all the privileges from database
            Collection<Privilege> privileges=sessionBean.getAllPrivileges();
            //looks for a privilege with the id passed as argument
            Iterator it=privileges.iterator();
            while(it.hasNext()){
                Privilege privilege=(Privilege)it.next();
                if (privilege.getId().equals(new Integer(value))){
                    //if it is found, the privilege object is returned
                    returnObject=privilege;
                    break;
                }
            }
            
        }catch(SessionException e){
            throw new ConverterException(e.getMessage());
        }
        
        return returnObject;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
        //returns the privilege id as String
        return ((Privilege)o).getId().toString();
    }
    
}
