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
import useraccess.entity.Status;
import useraccess.exception.SessionException;

/**
 * This class acts as converter for Status entities.
 * @author javi
 */
@FacesConverter("statusConverter")
public class StatusConverter implements Converter{

    @Inject 
    private SessionBeanLocal sessionBean;
    
    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value)
            throws ConverterException{
        Object returnObject=null;
        try{
            //gets all the statuses from database
            Collection<Status> statuses=sessionBean.getAllStatuses();
            //looks for an status with the id passed as argument
            Iterator it=statuses.iterator();
            while(it.hasNext()){
                Status status=(Status)it.next();
                if (status.getId().equals(new Integer(value))){
                    //if it is found, the status object is returned
                    returnObject=status;
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
        //returns the id of the status object as String
        return ((Status)o).getId().toString();
        
    }
    
}
