/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package useraccess.managedbeans;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import useraccess.ejb.SessionBean;
import useraccess.ejb.SessionBeanLocal;
import useraccess.entity.User;
import useraccess.exception.SessionException;
import useraccess.model.UserBean;
import useraccess.util.ListPager;

/**
 * Backing bean for the user administration page.
 * @author javi
 */
@Named(value = "adminBean")
@SessionScoped
public class AdminBean implements Serializable {
    //logger for the class
    private static final Logger logger =
            Logger.getLogger("useraccess.managedbeans.AdminBean");
    //business EJB session bean
    //@EJB
    //SessionBeanLocal sessionBean;
    @Inject 
    SessionBeanLocal sessionBean;
    //List pager controller
    @Inject
    private ListPager listPager;
    //query criteria
    private String criteria;
    //DNI for query
    private String DNI;
    //Map of selected users
    private Map<Integer,Boolean> selectedUsers;
    //select all checkbox
    private boolean selectAll;
    //shows operation done message
    private boolean showsOpDone;
    /**
     * Creates a new instance of AdminBean.
     */
    public AdminBean() {
    }
    /**
     * Initializes the data for the bean.
     */
    @PostConstruct
    private void init(){
        //sets the number of lines per page
        this.listPager.setDatosPorPagina(new Short("3"));
        //sets the default query criteria and gets data
        this.criteria="all";
        //creates the map for selected users
        this.selectedUsers=new HashMap<>();
        //gets the initial data
        this.updateData();
        //fills the selected users map
        this.fillsUsersMap();
        //unchecks select all checkbox
        this.selectAll=false;
        //hides op done message
        this.showsOpDone=false;
    }
    /**
     * Fills users maps with pairs user.id,boolean indicating if the user
     * is selected or not in the view.
     */
    private void fillsUsersMap(){
        //clears the map for not afecting other pages data
        this.selectedUsers.clear();
        //obtains data from the actual page
        Iterator it=this.listPager.getActualPage().iterator();
        //fills the map with the data obtained
        while(it.hasNext()){
            User user=(User)it.next();
            this.selectedUsers.put(user.getId(),false);
        }
    }
    
    public Collection<User> getUsers(){
        //gets the data for the actual page
        Collection<User> data= this.listPager.getActualPage();
        //creates the array of checkboxes associated to each table row
        return data;
    }
    /*
    public ListPager getListPager(){
        return this.listPager;
    }*/
    
    public int getUsersSize(){
        return this.listPager.getNumPaginas();
    }
    
    public String getCriteria(){
        return this.criteria;
    }
    
    public void setCriteria(String criteria){
        this.criteria=criteria;
    }
    
    public String getDNI(){
        return this.DNI;
    }
    
    public void setDNI(String DNI){
        this.DNI=DNI;
    }
    
    public Map<Integer,Boolean> getSelectedUsers(){
        return this.selectedUsers;
    }
    
    public void setSelectedUsers(Map<Integer,Boolean> selectedUsers){
        this.selectedUsers=selectedUsers;
    }
    
    public boolean getSelectAll(){
        return this.selectAll;
    }
    
    public void setSelectAll(boolean selectAll){
        this.selectAll=selectAll;
    }
    
    public boolean isShowsOpDone(){
        return this.showsOpDone;
    }
    /**
     * Checks if exists the user's data in the session and if it is an admin user.
     * If it doesn't redirects to login page.
     */
    public void validateSession(){
        try{
            if( ! sessionBean.isUserAdmin())
                FacesContext.getCurrentInstance().getExternalContext().redirect("loginForm.xhtml");
            
        }catch(SessionException | IOException e){
            logger.log(Level.SEVERE, "Error validating session:{0}", e.getMessage());
        }
    }
    /**
     * Updates the data for the users table from the query criteria.
     */
    public void updateData(){
        logger.info("Updating user's data table.");
        try{
            //evaluates query criteria
            switch (criteria) {
                case "all":
                    //all users data
                    this.listPager.actualizar(this.sessionBean.getAllUsers());
                    this.fillsUsersMap();
                    break;
                case "pending":
                    //users with pending status
                    this.listPager.actualizar(this.sessionBean.getUsersByStatus(criteria));
                    this.fillsUsersMap();
                    break;
                case "dni":
                    //user with certain login
                    if(! DNI.isEmpty()){
                        sessionBean.findCredentialByLogin(DNI);
                        //because the sessionBean method returns only a single result
                        //we have to put it in a new Collection to be passed to the list pager.
                        Vector data=new Vector();
                        data.add(this.sessionBean.getUserByDNI(DNI));
                        this.listPager.actualizar(data);
                        this.fillsUsersMap();
                    }
                    break;
            }
            this.showsOpDone=false;
           logger.info("End user's data table update.");
        }catch(Exception e){
            logger.log(Level.SEVERE, "Error updating user''s data table:{0}", e.getMessage());
            FacesContext.getCurrentInstance()
                .addMessage("userstableform", new FacesMessage(e.getMessage()));

        }
        
    }
    /**
     * Gets last page of data.
     * @return the same page.
     */
    public String lastPage(){
        this.listPager.getLastPage();
        this.fillsUsersMap();
        this.selectAll=false;
        this.showsOpDone=false;
        return "";
    } 
    /**
     * Gets first page of data.
     * @return the same page.
     */
    public String firstPage(){
        this.listPager.getFirstPage();
        this.fillsUsersMap();
        this.selectAll=false;
        this.showsOpDone=false;
        return "";
    } 
    /**
     * Gets next page of data.
     * @return 
     */
    public String nextPage(){
        this.listPager.getNextPage();
        this.fillsUsersMap();
        this.selectAll=false;
        this.showsOpDone=false;
        return "";
    } 
    /**
     * Gets previous page of data.
     * @return the same page.
     */
    public String prevPage(){
        this.listPager.getPrevPage();
        this.fillsUsersMap();
        this.showsOpDone=false;
        this.selectAll=false;
        return "";
    } 
    /**
     * Checks if actual page is the last page.
     * @return true if it is.
     */
    public boolean isLastPage(){
        return this.listPager.isLastPage();
    }
    /**
     * Checks if actual page is the first page.
     * @return true if it is.
     */
    public boolean isFirstPage(){
        return this.listPager.isFirstPage();
    }
    /**
     * Gets the index/number of the actual page.
     * @return the index/number.
     */
    public int getActualPageIndex(){
        return this.listPager.getPagActual();
    }
    /**
     * Changes user status
     * @param description the new status
     */
    public void changeUsersStatus(String description){
        logger.info("Beginnig users status change.");
        boolean done=false;
        //gets the user.id,selected pairs to iterate
        Iterator it=this.selectedUsers.entrySet().iterator();
        while(it.hasNext()){
            Entry entry=(Entry)it.next();
            //if user is selected
            if((Boolean)entry.getValue()){
                //gets the user corresponding to the id
                User user=getUserById((Integer)entry.getKey());
                //validates user status
                if(user.getStatus().getDescription().equals(description)){
                   Object [] messages=new String[3];
                   messages[0]=user.getName();
                   messages[1]=user.getSurname();
                   messages[2]=user.getStatus().getDescription();
                   logger.log(Level.INFO,"User {0} {1} will not change its status = {2} .",messages);
                }else{
                    try{
                        //changes user's status
                        sessionBean.updateUserStatus(user,description);
                        done=true;
                    }catch(SessionException e){
                        logger.log(Level.SEVERE, "Error updating user's status:{0}", e.getMessage());
                        FacesContext.getCurrentInstance()
                            .addMessage("userstableform", new FacesMessage(e.getMessage()));
                    }
                }
                //deselects the user
                entry.setValue(false);
            }
        }
        //updates table data
        this.updateData();
        //shows ok message
        this.showsOpDone=done;
        //deselects all-users ck
        this.selectAll=false;
        logger.info("End users status change.");

    }
    /**
     * Gets the User object corresponding to a user from its id. Searches it
     * in the Collection data for the page dataTable.
     * @param id the user's id.
     * @return the User object with the data
     */
    private User getUserById(Integer id){
        User user=null;
        boolean found=false;
        Iterator it=this.listPager.getActualPage().iterator();
        while(it.hasNext()&& !found){
            user=(User)it.next();
            if (user.getId().equals(id)) found=true;
        }
        return user;
    }
    /**
     * Accepts or enables a set of users.
     * @return the actual page.
     */
    public String accept(){
        logger.info("Beginning users aceptance.");
        this.changeUsersStatus("normal");
        logger.info("End users aceptance.");
        return "";
    }  
    /**
     * Rejects or disables a set of users.
     * @return the actual page.
     */
    public String reject(){        
        logger.info("Beginning users rejection.");
        this.changeUsersStatus("disabled");
        logger.info("End users rejection.");
        return "";
    }

    public String delete(){

        logger.info("Beginnig users deletion.");
        boolean done=false;
        //gets the user.id,selected pairs to iterate
        Iterator it=this.selectedUsers.entrySet().iterator();
        while(it.hasNext()){
            Entry entry=(Entry)it.next();
            //if user is selected
            if((Boolean)entry.getValue()){
                //gets the user corresponding to the id
                User user=getUserById((Integer)entry.getKey());
                try{
                        //deletes the user
                        sessionBean.deleteUser(user);
                        done=true;
                }catch(SessionException e){
                        logger.log(Level.SEVERE, "Error deleting user's :{0}", e.getMessage());
                        FacesContext.getCurrentInstance()
                            .addMessage("userstableform", new FacesMessage(e.getMessage()));
                }
                //deselects the user
                entry.setValue(false);
            }
        }
        //updates table data
        this.updateData();
        //shows ok message
        this.showsOpDone=done;
        //deselects all-users ck
        this.selectAll=false;
        logger.info("End users deletion.");
        return "";
    }
}
