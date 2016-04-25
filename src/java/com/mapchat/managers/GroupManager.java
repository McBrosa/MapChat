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
import javax.faces.bean.SessionScoped;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedProperty;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Alan
 */
@ManagedBean(name = "groupManager")
@SessionScoped
public class GroupManager implements Serializable {
    
    //private HashMap<String, ArrayList<String>> currentGroups;
    private String groupNameToCreate;
    private String groupNameToDelete;
    private String usernameToAdd;
    private String usernameToDelete;
    private User currentUser;
    private String message;
    private String statusMessage;
    private Groups currentGroup;
    private Set<Groups> allGroups;
    // Groups all users have access to
    private ArrayList<String> globalgrps = new ArrayList();
        
    @ManagedProperty(value="#{profileViewManager}")
    private ProfileViewManager profileViewManager;
    
    @ManagedProperty(value="#{messageManager}")
    private MessageManager mm;

    public MessageManager getMm() {
        return mm;
    }

    public void setMm(MessageManager mm) {
        this.mm = mm;
    }
    
    
    @EJB
    private GroupsFacade groupsFacade;
  
    @EJB
    private UserGroupFacade userGroupFacade;
    
    @EJB
    private UserFacade usersFacade;
    
    public GroupManager() {
    }
    
    @PostConstruct
    public void init() {
        allGroups = new HashSet();
        initializeGlobalGroups();
        initializeNonGlobalGroups();
    }

    public ProfileViewManager getProfileViewManager() {
        return profileViewManager;
    }

    public void setProfileViewManager(ProfileViewManager profileViewManager) {
        this.profileViewManager = profileViewManager;
    }   

    public ArrayList<String> getGlobalgrps() {
        return globalgrps;
    }

    public void setGlobalgrps(ArrayList<String> globalgrps) {
        this.globalgrps = globalgrps;
    }
    
    public Groups getCurrentGroup() {
        if (currentGroup == null) return null;
        
        return currentGroup;
    }

    public void setCurrentGroup(Groups currentGroup) {
        this.currentGroup = currentGroup;
    }

    public Set<Groups> getAllGroups() {
        return allGroups;
    }

    public void setAllGroups(Set<Groups> allGroups) {
        this.allGroups = allGroups;
    }
    
    public String getGroupNameToCreate() {
        return groupNameToCreate;
    }

    public String getGroupNameToDelete() {
        return groupNameToDelete;
    }

    public String getUsernameToAdd() {
        return usernameToAdd;
    }

    public String getUsernameToDelete() {
        return usernameToDelete;
    }

    public void setGroupNameToCreate(String groupNameToCreate) {
        this.groupNameToCreate = groupNameToCreate;
    }

    public void setGroupNameToDelete(String groupNameToDelete) {
        this.groupNameToDelete = groupNameToDelete;
    }

    public void setUsernameToAdd(String usernameToAdd) {
        this.usernameToAdd = usernameToAdd;
    }

    public void setUsernameToDelete(String usernameToDelete) {
        this.usernameToDelete = usernameToDelete;
    }
    
    public String getStatusMessage()
    {
        return statusMessage;
    }
    
    public void setStatusMessage(String message)
    {
        statusMessage = message;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public void setCurrentUser(User user) {
        currentUser = user;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
        
    public String createGroup() {
        statusMessage = "";
        //check the group name entered
        if(groupNameToCreate == null || groupNameToCreate.equals(""))
        {
            statusMessage += "Group name entered is empty";
            groupNameToCreate = "";
            return "";
        }
        Groups grp = groupsFacade.findByGroupname(groupNameToCreate);
        if (grp == null) {
            // the group doesnt exist, so create it
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
            currentUser = profileViewManager.getLoggedInUser();
            userGroup.setUserId(currentUser.getId());
            userGroupFacade.create(userGroup);
            
            RequestContext.getCurrentInstance().closeDialog("User Added");
        }
        else {
            // its already created!
            statusMessage += "Group already exists!";
        }
        groupNameToCreate = "";
        return "";
    }
    
    public String deleteGroup(Integer groupId) {
        statusMessage = "";
        try
        {
            //Check to see if the group already exists
            Groups check = groupsFacade.findById(groupId);
            ArrayList<UserGroup> check2 = (ArrayList<UserGroup>)userGroupFacade.findByGroupId(groupId);
            if(check == null)
            {
                statusMessage += "The doesn't exists";
                return "";
            }
            else if(check2 != null) {
                statusMessage += "The chat is not empty!";
                return "";
            } else
            {
                Groups foundGroup = groupsFacade.findById(groupId);
                groupsFacade.deleteGroup(groupId);
                
                UserGroup userGroup = new UserGroup();
                userGroup.setGroupId(groupId);
                currentUser = profileViewManager.getLoggedInUser();
                userGroup.setUserId(currentUser.getId());
                userGroupFacade.remove(userGroup);
                
                currentGroup = null;
                mm.getGroupMessageMap().remove(check.getId());
                allGroups.remove(foundGroup);
            }
        } catch(EJBException e)
        {
            statusMessage += "Something went wrong deleting the group";
            return "";
        }
        return "";
    }
    
    public String addUser() {
        statusMessage = "";
        if(usernameToAdd == null || usernameToAdd.trim().equals(""))
        {
            statusMessage += "No username entered";
            usernameToAdd = "";
            return "";
        }
        //Check the current group, if there is one
        if(currentGroup == null)
        {
            statusMessage += "No group is selected";
            usernameToAdd = "";
            return "";
        }
        else if(currentGroup.getId() == null || currentGroup.getGroupName() == null)
        {
            statusMessage += "There is something wrong with the current group selected";
            usernameToAdd = "";
            return "";   
        }
        
        Integer groupId = currentGroup.getId();
        Groups publicGroup = groupsFacade.findById(groupId);
        if(publicGroup == null)
        {
            statusMessage += "The current group does not exist";
            usernameToAdd = "";
            return "";
        }
        else if(globalgrps.contains(publicGroup.getGroupName()))
        {
            statusMessage += "Everyone is already a part of a global group";
            usernameToAdd = "";
            return "";
        }
        try
        {
            //Check to see if the user exists
            User check = usersFacade.findByUsername(usernameToAdd);
            if(check == null)
            {
                statusMessage += usernameToAdd + " does not exist";
                usernameToAdd = "";
                return "";
            }
            
            //Check to see if the user is already in the group
            if(userGroupFacade.findByIds(check.getId(), groupId) != null)
            {
                statusMessage += usernameToAdd + " is already in the group";
                usernameToAdd = "";
                return "";
            }
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
            return "";
        }
        return "";
    }
    
    public String removeUser() {
        statusMessage = "";
        if(usernameToDelete == null || usernameToDelete.trim().equals(""))
        {
            statusMessage += "No username entered";
            usernameToDelete = "";
            return "";
        }
        //Check the current group, if there is one
        if(currentGroup == null)
        {
            statusMessage += "No group is selected";
            usernameToDelete = "";
            return "";
        }
        else if(currentGroup.getId() == null || currentGroup.getGroupName() == null)
        {
            statusMessage += "There is something wrong with the current group selected";
            usernameToDelete = "";
            return "";   
        }
        Integer groupId = currentGroup.getId();
        Groups publicGroup = groupsFacade.findById(groupId);
        if(publicGroup == null)
        {
            statusMessage += "The current group does not exist";
            usernameToDelete = "";
            return "";
        }
        else if(globalgrps.contains(publicGroup.getGroupName()))
        {
            statusMessage += "Cannot remove someone from a global group";
            usernameToDelete = "";
            return "";
        }
        Groups group = publicGroup;
        try
        {
            //Check to see if you are deleting yourself
            boolean same = usernameToDelete.equals(profileViewManager.getLoggedInUser().getUsername());
            //statusMessage += same;
            //Check to see if the user exists
            User check = usersFacade.findByUsername(usernameToDelete);
            if(check == null)
            {
                statusMessage += usernameToDelete + " does not exist";
                usernameToDelete = "";
                return "";
            }
            
            
            //Check to see if the user is in the group
            UserGroup foundUserGroup = userGroupFacade.findByIds(check.getId(), groupId);
            if(foundUserGroup == null)
            {
                statusMessage += usernameToDelete + " is not in the group";
                usernameToDelete = "";
                return "";
            }
            statusMessage = "UserGroup " + foundUserGroup.getId() + " deleted";
            userGroupFacade.deleteUserGroup(foundUserGroup);
            
            ArrayList<UserGroup> emptyCheck = (ArrayList<UserGroup>)userGroupFacade.findByGroupId(groupId);
            if(emptyCheck == null) {
                deleteGroup(groupId);
            }
            if(same)
            {
                currentGroup = null;
                boolean removed = allGroups.remove(group);
            }
                
            
            usernameToDelete = "";
            
            
        } catch(EJBException e)
        {
            usernameToDelete = "";
            statusMessage += "Something went wrong removing the user to the group";
            return "";
        }
        return "";
    }
    
    public ArrayList<Integer> getUsers(Integer groupId) {
        ArrayList<Integer> userIds = new ArrayList<Integer>();
        ArrayList<UserGroup> searchResult = new ArrayList<UserGroup>(userGroupFacade.findByGroupId(groupId));
        for(int i = 0; i < searchResult.size(); i++)
        {
            userIds.add(searchResult.get(i).getUserId());
        }
        return userIds;
    }
    
    public String showAllGroups()
    {
        String groupsString = "";
        
            currentUser = profileViewManager.getLoggedInUser();
            ArrayList<UserGroup> searchResult = new ArrayList<UserGroup>(userGroupFacade.findAll());

            for(int i = 0; i < searchResult.size(); i++)
            {
                //groupsString += searchResult.size() + "<<>>";
                groupsString += "Group ID: " + (searchResult.get(i).getGroupId()) + " User ID: " + searchResult.get(i).getUserId() + " Id: " + searchResult.get(i).getId() + "/";
            }
            
            UserGroup test = userGroupFacade.test();
            if(test != null)
            groupsString += ">>" + test.getId() + "<<";
        
        return groupsString;
    }
    
    /**
     * Initialize global groups
     */
    private void initializeGlobalGroups() {
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
                mm.getGroupMessageMap().put(created.getId(), collection);
                allGroups.add(created);
                
            }
            // if the group exists
            else {
                mm.getGroupMessageMap().put(grp.getId(), grp.getMessageCollection());
                allGroups.add(grp);
            }
        }    
    }
    
    /**
     * Initialize nonglobal groups
     */
    private void initializeNonGlobalGroups() {
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
    
    public void closeDialog() {
        statusMessage = "";
        groupNameToCreate = "";
        groupNameToDelete = "";
        usernameToAdd = "";
        usernameToDelete = "";
    }
        
}
