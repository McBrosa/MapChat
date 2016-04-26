/*
 * Created by Alan Cai on 2016.03.23  * 
 * Copyright © 2016 Alan Cai. All rights reserved. * 
 */
package com.mapchat.managers;

import com.mapchat.chat.MessageManager;
import com.mapchat.entitypackage.Groups;
import com.mapchat.entitypackage.Message;
import com.mapchat.entitypackage.User;
import com.mapchat.entitypackage.UserGroup;
import com.mapchat.sessionbeanpackage.GroupsFacade;
import com.mapchat.sessionbeanpackage.UserFacade;
import com.mapchat.sessionbeanpackage.UserGroupFacade;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;   //TODO remove

/**
 * The Manager for the Groups object. This class handles all the groups
 * throughout the entire application. The reason that this is application scoped
 * is because there can only be one instance of each group. If this class was
 * session scoped then each session will have a different instance of the same
 * group. 
 * @author Alan Cai, Sean Arcayan
 */
@ManagedBean(name = "groupManager")
@ApplicationScoped
public class GroupManager implements Serializable {
    
    //private HashMap<String, ArrayList<String>> currentGroups; //TODO remove
    private String groupNameToCreate;
    private String groupNameToDelete;   //Not used?
    private String usernameToAdd;
    private String usernameToDelete;
    private String message;//TODO delete
    private String statusMessage;
    private Set<Groups> allGroups;
    private ArrayList<Groups> globalGroupList;
    // Groups all users have access to
    private ArrayList<String> globalgrps = new ArrayList(); // this list of global groups will be in Constants.java
     /*   
    @ManagedProperty(value="#{profileViewManager}") //TODO remove
    private ProfileViewManager profileViewManager;
    */
    @ManagedProperty(value="#{messageManager}")
    private MessageManager mm;
    
    @EJB
    private GroupsFacade groupsFacade;
  
    @EJB
    private UserGroupFacade userGroupFacade;
    
    @EJB
    private UserFacade usersFacade;
    
    /**
     * Constructor
     */
    public GroupManager() {
    }
    
    /**
     * Initializes the set of all groups
     */
    @PostConstruct
    public void init() {
        allGroups = new HashSet();
        initializeGlobalGroups();
        initializeNonGlobalGroups();
    }
/*  //TODO remove
    public ProfileViewManager getProfileViewManager() {
        return profileViewManager;
    }

    public void setProfileViewManager(ProfileViewManager profileViewManager) {
        this.profileViewManager = profileViewManager;
    }   
*/
    /**
     * Returns the list of global group names
     * @return The list of global group names
     */
    public ArrayList<String> getGlobalgrps() {
        return globalgrps;
    }
    
    /**
     * Returns the MessageManager
     * @return The MessageManager
     */
    public MessageManager getMm() {
        return mm;
    }

    /**
     * Sets the MessageManager
     * @param mm The MessageManager to set the current MessageManager to
     */
    public void setMm(MessageManager mm) {
        this.mm = mm;
    }

    /**
     * Sets the list of global group names
     * @param globalgrps The list of global group names
     */
    public void setGlobalgrps(ArrayList<String> globalgrps) {
        this.globalgrps = globalgrps;
    }

    /**
     * Returns the set of all groups users are in
     * @return The set of all groups users are in
     */
    public Set<Groups> getAllGroups() {
        return allGroups;
    }

    /**
     * Sets the set of all groups users are in
     * @param allGroups The set of all groups users are in
     */
    public void setAllGroups(Set<Groups> allGroups) {
        this.allGroups = allGroups;
    }
    
    /**
     * Returns the name of the group to create
     * @return The name of the group to create
     */
    public String getGroupNameToCreate() {
        return groupNameToCreate;
    }

    /**
     * Returns the name of the group to delete
     * @return The name of the group to delete
     */
    public String getGroupNameToDelete() {
        return groupNameToDelete;
    }

    /**
     * Returns the username to add to a group
     * @return The username to add to a group
     */
    public String getUsernameToAdd() {
        return usernameToAdd;
    }

    /**
     * Returns the username to remove from a group
     * @return The username to remove from a group
     */
    public String getUsernameToDelete() {
        return usernameToDelete;
    }

    /**
     * Sets the group name to create
     * @param groupNameToCreate The name of the group to create
     */
    public void setGroupNameToCreate(String groupNameToCreate) {
        this.groupNameToCreate = groupNameToCreate;
    }

    /**
     * Sets the group name to delete
     * @param groupNameToDelete The name of the group to delete
     */
    public void setGroupNameToDelete(String groupNameToDelete) {
        this.groupNameToDelete = groupNameToDelete;
    }

    /**
     * Sets the username to add to a group
     * @param usernameToAdd The username to add to a group
     */
    public void setUsernameToAdd(String usernameToAdd) {
        this.usernameToAdd = usernameToAdd;
    }

    /**
     * Sets the username to delete from a group
     * @param usernameToDelete The username to remove from a group
     */
    public void setUsernameToDelete(String usernameToDelete) {
        this.usernameToDelete = usernameToDelete;
    }
    
    /**
     * Returns the status message
     * @return The status message
     */
    public String getStatusMessage()
    {
        return statusMessage;
    }
    
    /**
     * Sets the status message
     * @param message The status message
     */
    public void setStatusMessage(String message)
    {
        statusMessage = message;
    }
    
    //TODO delete
    public String getMessage() {
        return message;
    }
    //TODO delete
    public void setMessage(String message) {
        this.message = message;
    }
        
    /**
     * Creates a group with a group with the name specified by groupNameToCreate.
     * Also adds the group to the allGroups set. The group name must be unique
     * from the ones in the database. Any error messages gets echoed.
     * @return The string of the page to return to
     */
    public String createGroup() {
        statusMessage = "";
        //check that the group name entered
        if(groupNameToCreate == null || groupNameToCreate.equals(""))
        {
            //Echo the error
            statusMessage += "Group name entered is empty";
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(statusMessage));
            groupNameToCreate = "";
            return "";
        }
        //Make sure the name is unique
        Groups grp = groupsFacade.findByGroupname(groupNameToCreate);
        if (grp == null) {
            //Create a new collection for messages in the MessageManager
            Collection<Message> collection = Collections.synchronizedList(new LinkedList<Message>());
            Groups g = new Groups();
            g.setGroupName(groupNameToCreate);
            g.setMessageCollection(collection);
            
            // add to the database
            groupsFacade.create(g);
            Groups created = groupsFacade.findByGroupname(groupNameToCreate);
            
            // add to the map
            mm.getGroupMessageMap().put(created.getId(), collection);
            allGroups.add(created);
            
            // create the user group to link the user to the group
            UserGroup userGroup = new UserGroup();
            userGroup.setGroupId(g.getId());
            
            // Retrieve the profile view manager
            ELContext elContext = FacesContext.getCurrentInstance().getELContext();
            ProfileViewManager profileViewManager = 
                (ProfileViewManager) FacesContext.getCurrentInstance().getApplication()
                .getELResolver().getValue(elContext, null, "profileViewManager");
            
            //Create the user to group relation
            User currentUser = profileViewManager.getLoggedInUser();
            userGroup.setUserId(currentUser.getId());
            userGroupFacade.create(userGroup);
        }
        else {
            // its already created!
            statusMessage += "Group already exists!";
        }
        //Reset the variable for future use
        groupNameToCreate = "";
        
        //Echo any error messages
        if(!statusMessage.equals(""))
        {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(statusMessage));
        }
        return "";
    }
    
    /**
     * Deletes a group, removes its messages from the MessageManager, and deletes
     * itself and associated rows from the database. Group with the group id must
     * exist and the group must be empty to be deleted, or else an error message gets
     * echoed.
     * @param groupId The id of the group to delete
     * @return The name of the page to return to
     */
    public String deleteGroup(Integer groupId) {
        statusMessage = "";
        try
        {
            //Check to see if the group exists
            Groups check = groupsFacade.findById(groupId);
            //Check to see if there is anyone in the group
            ArrayList<UserGroup> check2 = (ArrayList<UserGroup>)userGroupFacade.findByGroupId(groupId);
            if(check == null)
            {
                //Echo the error
                statusMessage += "The doesn't exists";
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(statusMessage));
                return "";
            }
            else if(check2 != null) {
                //Echo the error
                statusMessage += "The chat is not empty!";
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(statusMessage));
                return "";
            } else
            {
                //Delete the group from the Groups table
                Groups foundGroup = groupsFacade.findById(groupId);
                groupsFacade.deleteGroup(groupId);
                
                // create the UserGroup
                // the usergroup maps a user to a group
                UserGroup userGroup = new UserGroup();
                userGroup.setGroupId(groupId);
                
                // Retrieve the profile view manager
                ELContext elContext = FacesContext.getCurrentInstance().getELContext();
                ProfileViewManager profileViewManager = 
                    (ProfileViewManager) FacesContext.getCurrentInstance().getApplication()
                    .getELResolver().getValue(elContext, null, "profileViewManager");
                
                User currentUser = profileViewManager.getLoggedInUser();
                userGroup.setUserId(currentUser.getId());
                
                userGroupFacade.remove(userGroup);  //TODO Shouldn't be here, the group should be empty, so no relationship
                
                // Retrieve the message bean
                elContext = FacesContext.getCurrentInstance().getELContext();
                MessageBean messageBean = 
                    (MessageBean) FacesContext.getCurrentInstance().getApplication()
                    .getELResolver().getValue(elContext, null, "messageBean");
                messageBean.setCurrentGroup(null);
                
                //Remove the mapping of message to the group from the MessageManager
                mm.getGroupMessageMap().remove(check.getId());
                
                //Remove the group from the set of all groups
                allGroups.remove(foundGroup);
            }
        } catch(EJBException e)
        {
            //Echo the error
            statusMessage += "Something went wrong deleting the group";
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(statusMessage));
            return "";
        }
        if(!statusMessage.equals(""))
        {
            //Echo any errors
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(statusMessage));
        }
        return "";
    }
    
    /**
     * Adds a user with the username specified in usernameToAdd to the currently
     * selected group. The user must also not be part of the group already
     * The currently selected group must not be null and exist. Any errors are echoed.
     * @return The name of the page to return to
     */
    public String addUser() {
        statusMessage = "";
        //Make sure the usernameToAdd is not null and is not just whitespaces
        if(usernameToAdd == null || usernameToAdd.trim().equals(""))
        {
            //Echo the error message
            statusMessage += "No username entered";
            usernameToAdd = "";
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(statusMessage));
            return "";
        }
        
        // Retrieve the message bean
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        MessageBean messageBean = 
            (MessageBean) FacesContext.getCurrentInstance().getApplication()
            .getELResolver().getValue(elContext, null, "messageBean");
        
        //Check the current group to see if it is not null
        if(messageBean.getCurrentGroup() == null)
        {
            //Echo the error message
            statusMessage += "No group is selected";
            usernameToAdd = "";
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(statusMessage));
            return "";
        }
        //Make sure the fields in the current group are set
        else if(messageBean.getCurrentGroup().getId() == null || messageBean.getCurrentGroup().getGroupName() == null)
        {
            //Echo the error message
            statusMessage += "There is something wrong with the current group selected";
            usernameToAdd = "";
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(statusMessage));
            return "";   
        }
        
        //Get the current group
        Integer groupId = messageBean.getCurrentGroup().getId();
        Groups publicGroup = groupsFacade.findById(groupId);
        //Check to see if the current group is not null
        if(publicGroup == null)
        {
            //Echo the error message
            statusMessage += "The current group does not exist";
            usernameToAdd = "";
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(statusMessage));
            return "";
        }
        //Check to see if the current group is not a public group
        else if(globalgrps.contains(publicGroup.getGroupName()))
        {
            //Echo the error message
            statusMessage += "Everyone is already a part of a global group";
            usernameToAdd = "";
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(statusMessage));
            return "";
        }
        try
        {
            //Check to see if the user exists
            User check = usersFacade.findByUsername(usernameToAdd);
            if(check == null)
            {
                //Echo the error message
                statusMessage += usernameToAdd + " does not exist";
                usernameToAdd = "";
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(statusMessage));
                return "";
            }
            
            //Check to see if the user is already in the group
            if(userGroupFacade.findByIds(check.getId(), groupId) != null)
            {
                //Echo the error message
                statusMessage += usernameToAdd + " is already in the group";
                usernameToAdd = "";
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(statusMessage));
                return "";
            }
            //Add a relationship from the user to the group
            User user = usersFacade.findByUsername(usernameToAdd);
            UserGroup userGroup = new UserGroup();
            userGroup.setUserId(user.getId());
            userGroup.setGroupId(groupId);
            userGroupFacade.create(userGroup);
            usernameToAdd = "";
        } catch(EJBException e)
        {
            usernameToAdd = "";
            statusMessage += "Something went wrong adding the user to the group";
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(statusMessage));
            return "";
        }
        //Echo any error messages
        if(!statusMessage.equals(""))
        {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(statusMessage));
        }
        return "";
    }
    
    /**
     * Removes a user from a group with the username specified by usernameToDelete.
     * The current group must not be null and the usernameToDelete must not be null.
     * The User with the username specified by usernameToDelete must exist and be
     * part of the group. Any errors are echoed.
     * @return The name of the page to return to
     */
    public String removeUser() {
        statusMessage = "";
        //Check to see if the usernameToDelete field is filled and not filled with whitespaces
        if(usernameToDelete == null || usernameToDelete.trim().equals(""))
        {
            //Echo errors
            statusMessage += "No username entered";
            usernameToDelete = "";
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(statusMessage));
            return "";
        }
        
        // Retrieve the message bean
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        MessageBean messageBean = 
            (MessageBean) FacesContext.getCurrentInstance().getApplication()
            .getELResolver().getValue(elContext, null, "messageBean");
        
        
        //Check the current group to see if it exists
        if(messageBean.getCurrentGroup() == null)
        {
            //Echo errors
            statusMessage += "No group is selected";
            usernameToDelete = "";
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(statusMessage));
            return "";
        }
        //Make sure the group's fields aren't empty
        else if(messageBean.getCurrentGroup().getId() == null || messageBean.getCurrentGroup().getGroupName() == null)
        {
            //Echo errors
            statusMessage += "There is something wrong with the current group selected";
            usernameToDelete = "";
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(statusMessage));
            return "";   
        }
        
        //Check to see if the group already doesn't exist
        Integer groupId = messageBean.getCurrentGroup().getId();
        Groups publicGroup = groupsFacade.findById(groupId);
        if(publicGroup == null)
        {
            //Echo errors
            statusMessage += "The current group does not exist";
            usernameToDelete = "";
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(statusMessage));
            return "";
        }
        //Also make sure the group isn't public
        else if(globalgrps.contains(publicGroup.getGroupName()))
        {
            //Echo errors
            statusMessage += "Cannot remove someone from a global group";
            usernameToDelete = "";
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(statusMessage));
            return "";
        }
        Groups group = publicGroup;
        try
        {
            // Retrieve the profile view manager
            elContext = FacesContext.getCurrentInstance().getELContext();
            ProfileViewManager profileViewManager = 
                (ProfileViewManager) FacesContext.getCurrentInstance().getApplication()
                .getELResolver().getValue(elContext, null, "profileViewManager");
            
            //Check to see if you are deleting yourself
            boolean same = usernameToDelete.equals(profileViewManager.getLoggedInUser().getUsername());
            
            //Check to see if the user exists
            User check = usersFacade.findByUsername(usernameToDelete);
            if(check == null)
            {
                //Echo errors
                statusMessage += usernameToDelete + " does not exist";
                usernameToDelete = "";
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(statusMessage));
                return "";
            }
            
            
            //Check to see if the user is in the group
            UserGroup foundUserGroup = userGroupFacade.findByIds(check.getId(), groupId);
            if(foundUserGroup == null)
            {
                //Echo errors
                statusMessage += usernameToDelete + " is not in the group";
                usernameToDelete = "";
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(statusMessage));
                return "";
            }
            //Delete the relationship between the user and the group
            userGroupFacade.deleteUserGroup(foundUserGroup);
            
            //Check to see if the group is empty or not
            Vector<UserGroup> emptyCheck = (Vector<UserGroup>)userGroupFacade.findByGroupId(groupId);
            if(emptyCheck == null) {
                deleteGroup(groupId);
            }
            
            //Remove the user from the current group since they deleted themself
            if(same)
            {
                messageBean.setCurrentGroup(null);
                allGroups.remove(group);
            }
                
            usernameToDelete = "";
        } catch(EJBException e)
        {
            //Echo errors
            usernameToDelete = "";
            statusMessage += "Something went wrong removing the user to the group";
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(statusMessage));
            return "";
        }
        //Echo any errors
        if(!statusMessage.equals(""))
        {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(statusMessage));
        }
        return "";
    }
    
    /**
     * Returns a list of UserGroups where the group id equals the specified group id.
     * Essentially gets all the user ids that belongs to a group
     * @param groupId
     * @return 
     */
    public ArrayList<Integer> getUsers(Integer groupId) {
        ArrayList<Integer> userIds = new ArrayList<Integer>();
        Vector<UserGroup> searchResult = new Vector<UserGroup>(userGroupFacade.findByGroupId(groupId));
        for(int i = 0; i < searchResult.size(); i++)
        {
            userIds.add(searchResult.get(i).getUserId());
        }
        return userIds;
    }
    
    /**
     * Loads all the global groups, and if they do not exist yet, create them
     */
    private void initializeGlobalGroups() {
        globalGroupList = new ArrayList<>();
        if(globalgrps == null || globalgrps.isEmpty())
        {
            globalgrps.add("#Music");
            globalgrps.add("#For Sale");
            globalgrps.add("#Entertainment");
        }
        // create a message stream for each group
        for (String grpName : globalgrps) {
            
            Groups grp = groupsFacade.findByGroupname(grpName);
            
            // if the group doesnt exist
            if (grp == null) {  
                Collection<Message> collection = Collections.synchronizedList(new LinkedList<Message>());

                // create the group and set the properties
                Groups g = new Groups();
                g.setGroupName(grpName);
                g.setMessageCollection(collection);
                // g.setFileCollection(insert file collection);
                
                // add to the database
                groupsFacade.create(g);
                
                // add to the map
                Groups created = groupsFacade.findByGroupname(grpName);
                created.setMessageCollection(collection);
                mm.getGroupMessageMap().put(created.getId(), collection);
                allGroups.add(created);
                globalGroupList.add(created);
                
            }
            // if the group exists
            else {
                // reverse it from the database since we want the most recent message upt op.
                Collections.reverse((List<Message>)grp.getMessageCollection());
                mm.getGroupMessageMap().put(grp.getId(), grp.getMessageCollection());
                allGroups.add(grp);
                globalGroupList.add(grp);
            }
        }    
    }
    
    /**
     * Loads all the groups the user is in to the MessangerManager and the allGroups set
     */
    private void initializeNonGlobalGroups() {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        ProfileViewManager profileViewManager = 
            (ProfileViewManager) FacesContext.getCurrentInstance().getApplication()
            .getELResolver().getValue(elContext, null, "profileViewManager");
        
        //Get the current user
        User user = usersFacade.find(profileViewManager.getLoggedInUser().getId());
        
        //Find the list of groups the user is in
        List<UserGroup> groupsUserIsIn = userGroupFacade.findByUserId(user.getId());
        if(groupsUserIsIn == null)
        {
            return;
        }
        for(UserGroup usergroup : groupsUserIsIn)
        {
            //Get each group's name
            Groups found = groupsFacade.findById(usergroup.getGroupId());
            if (found != null) {
                mm.getGroupMessageMap().put(found.getId(), found.getMessageCollection());
                allGroups.add(found);
            }
        }
    }
    
    /**
     * Clears the user input variables and the error messages
     */
    public void closeDialog() {
        statusMessage = "";
        groupNameToCreate = "";
        groupNameToDelete = "";
        usernameToAdd = "";
        usernameToDelete = "";
    }
        
    
    /**
     * Retrieve all the current user groups that the current user is in
     * @return a list of groups that the 
     * 
     * // TODO rename to retrieveAllGroupsForCurrentUser()
     */
    public List<Groups> retrieveAllCurrentUserGroups() {
        List<Groups> grps = new LinkedList<>();
        grps.addAll(globalGroupList);
        
        // Retrieve the profile view manager
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        ProfileViewManager profileViewManager = 
            (ProfileViewManager) FacesContext.getCurrentInstance().getApplication()
            .getELResolver().getValue(elContext, null, "profileViewManager");
        
        //Get the current user
        User user = usersFacade.find(profileViewManager.getLoggedInUser().getId());
        List<UserGroup> ugList = userGroupFacade.findByUserId(user.getId());
        if (ugList != null) {
            for (UserGroup ug : ugList) {
                grps.add(groupsFacade.findById(ug.getGroupId()));
            }
        }
        return grps;
    }
}
